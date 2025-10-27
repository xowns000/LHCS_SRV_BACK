package kr.co.hkcloud.palette3.config.datasources.datasource;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.BaseExecutor;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.HashMap;
import java.util.Properties;

/**
 *  페이징관련 Interceptor (Mssql)
 */
@Slf4j
@Intercepts(
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
)
public class PagingInterceptorMssql implements Interceptor {

    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            Executor executor = (Executor)invocation.getTarget();
            Object[] args = invocation.getArgs();
            MappedStatement ms = (MappedStatement)args[0];
            Object parameterObject = args[1];

            int rowCnt = 0;
            int pagesCnt = 0;
            if( ((HashMap)parameterObject).get("ROW_CNT") != null ) rowCnt = (Integer)((HashMap)parameterObject).get("ROW_CNT");
            if( ((HashMap)parameterObject).get("PAGES_CNT") != null ) pagesCnt = (Integer)((HashMap)parameterObject).get("PAGES_CNT");

            if( rowCnt > 0 && pagesCnt > 0 )
            {
                RowBounds rowBounds = (RowBounds) args[2];
                ResultHandler resultHandler = (ResultHandler) args[3];
                BoundSql boundSql = ms.getBoundSql(parameterObject);
                CacheKey pageKey = null, countKey = null;
                if(executor instanceof CachingExecutor)
                {
                    pageKey = ((CachingExecutor) executor).createCacheKey(ms, parameterObject, rowBounds, boundSql);
                    countKey = ((CachingExecutor) executor).createCacheKey(ms, parameterObject, rowBounds, boundSql);
                } else
                {
                    pageKey = ((BaseExecutor) executor).createCacheKey(ms, parameterObject, rowBounds, boundSql);
                    countKey = ((BaseExecutor) executor).createCacheKey(ms, parameterObject, rowBounds, boundSql);
                }

                countKey.update("Count");
                BoundSql countSql = new BoundSql(ms.getConfiguration(), "SELECT COUNT(1) FROM (" + boundSql.getSql() + ") it_count ", boundSql.getParameterMappings(), parameterObject);
                Object countObj = executor.query(ms, parameterObject, rowBounds, resultHandler, countKey, countSql).get(0);

                BoundSql listSql = new BoundSql(ms.getConfiguration(), "SELECT PAGING.* FROM ( SELECT "+ ((HashMap)countObj).get("count") +" as TWB_PAGING_TOT_COUNT ,* FROM (" + boundSql.getSql() + ") it_list ) PAGING LIMIT "+ rowCnt +" OFFSET (("+ pagesCnt  +" - 1) * " + rowCnt + ") ", boundSql.getParameterMappings(), parameterObject);

                return executor.query(ms, parameterObject, rowBounds, resultHandler, pageKey, listSql);

            }else {
                return invocation.proceed();
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
