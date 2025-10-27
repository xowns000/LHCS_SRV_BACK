package kr.co.hkcloud.palette3.config.datasources.datasource.palette;


import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import kr.co.hkcloud.palette3.config.datasources.datasource.PagingInterceptorMysql;
import kr.co.hkcloud.palette3.config.datasources.datasource.PagingInterceptorOracle;
import kr.co.hkcloud.palette3.config.datasources.datasource.PagingInterceptorPostgreSQL;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import kr.co.hkcloud.palette3.config.datasources.enumer.DatasourceDbVendor;
import kr.co.hkcloud.palette3.config.datasources.properties.VendorDatabaseIdProperies;
import kr.co.hkcloud.palette3.config.multitenancy.TenantConnectionProvider;
import kr.co.hkcloud.palette3.config.multitenancy.TenantIdentifierResolver;
import kr.co.hkcloud.palette3.config.properties.datasources.DatasourcesProperties;
import kr.co.hkcloud.palette3.core.profile.enumer.PaletteProfiles;
import kr.co.hkcloud.palette3.core.profile.util.PaletteProfileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 팔레트 라우팅 데이터소스 설정 - JNDI 지원
 * 
 * @author Orange
 *
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@MapperScan(value = "kr.co.hkcloud.palette3",
            annotationClass = PaletteConnMapper.class,
            sqlSessionFactoryRef = "paletteRoutingSqlSessionFactory")
@EnableTransactionManagement
public class PaletteRoutingDataSourceConfig
{
    private final DatasourcesProperties     datasourceProperties;
    private final VendorDatabaseIdProperies vendorDatabaseIdProperies;

    private JndiDataSourceLookup lookup = new JndiDataSourceLookup();

    private final PaletteProfileUtils paletteProfileUtils;
    
    @Autowired
    private JpaProperties jpaProperties;

    /**
     * Master 데이터소스 -JNDI를 사용하고자 하면 해당 개발단계 profile을 수정하여 적용할 것
     * 
     * @return DataSource
     */
    @Primary
    @Bean(destroyMethod = "close")
    @ConfigurationProperties(prefix = "datasources.datasource.palette.master")
    public DataSource paletteRoutingMasterDataSource()
    {
        PaletteProfiles profile = paletteProfileUtils.getActiveProfile();
        log.trace("*palette master datasource profile={}", profile);
        switch(profile)
        {
            case local:
            case dev:
            case production:
            case productionlhcs:
            case productioncloud:
            case productioncloudgwm:
            {
                return DataSourceBuilder.create().type(HikariDataSource.class).build();
            }
            case uat:
            {
                return lookup.getDataSource(datasourceProperties.getDatasource().getPalette().getMaster().getJndiName());
            }
            default:
            {
                return null;
            }
        }
    }


    /**
     * Slave 데이터소스 -JNDI를 사용하고자 하면 해당 개발단계 profile을 수정하여 적용할 것
     * 
     * @return DataSource
     */
    @Bean(destroyMethod = "close")
    @ConfigurationProperties(prefix = "datasources.datasource.palette.slave")
    public DataSource paletteRoutingSlaveDataSource()
    {
        PaletteProfiles profile = paletteProfileUtils.getActiveProfile();
        log.trace("*palette slave datasource profile={}", profile);
        switch(profile)
        {
            case local:
            case dev:
            case production:
            case productionlhcs:
            case productioncloud:
            case productioncloudgwm:
            {
                return DataSourceBuilder.create().type(HikariDataSource.class).build();
            }
            case uat:
            {
                return lookup.getDataSource(datasourceProperties.getDatasource().getPalette().getSlave().getJndiName());
            }
            default:
            {
                return null;
            }
        }
    }


    /**
     * @param  routingMasterDataSource
     * @param  routingSlaveDataSource
     * @return
     */
    @Bean
    public DataSource paletteRoutingDataSource()
    {
        Map<Object, Object> targetDataSourceMap = new LinkedHashMap<Object, Object>();
        targetDataSourceMap.put("master", paletteRoutingMasterDataSource());
        targetDataSourceMap.put("slave_1", paletteRoutingSlaveDataSource());
//        targetDataSourceMap.put("slave_2", routingSlaveDataSource());
        PaletteReplicationRoutingDataSource routingDataSource = new PaletteReplicationRoutingDataSource();
        routingDataSource.setTargetDataSources(targetDataSourceMap);
        routingDataSource.setDefaultTargetDataSource(paletteRoutingMasterDataSource());
        return routingDataSource;
    }


    /**
     * @param  routingDataSource
     * @return
     */
    @Bean
    public DataSource paletteRoutingLazyDataSource()
    {
        return new LazyConnectionDataSourceProxy(paletteRoutingDataSource());
    }


    /**
     * @param  routingLazyDataSource
     * @param  applicationContext
     * @return
     * @throws Throwable
     */
    @Bean
    public SqlSessionFactory paletteRoutingSqlSessionFactory(ApplicationContext applicationContext) throws Throwable
    {
        String locationPattern = "";

        //Mybatis가 읽도록 변경
//        switch(datasourcePaletteProperties.getDbVendor())

        DatabaseIdProvider vendorDatabaseIdProvider = new VendorDatabaseIdProvider();
        vendorDatabaseIdProvider.setProperties(vendorDatabaseIdProperies.getDbVendorProperties());

        DatasourceDbVendor dbVendor = DatasourceDbVendor.valueOf(vendorDatabaseIdProvider.getDatabaseId(paletteRoutingLazyDataSource()));

        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(paletteRoutingLazyDataSource());
        sessionFactory.setTypeHandlersPackage("kr.co.hkcloud.palette3");  // 데이터소스 TypeHandlers 테스트중 20210610
        sessionFactory.setTypeAliasesPackage("kr.co.hkcloud.palette3");
        sessionFactory.setDatabaseIdProvider(vendorDatabaseIdProvider);

        switch(dbVendor)
        {
            case Oracle:
            {
                sessionFactory.setPlugins( new PagingInterceptorOracle() );
                locationPattern = "classpath*:kr/co/hkcloud/palette3/**/dao/xml/*Mapper_Oracle.xml";
                break;
            }
            case Mysql:
            {
                sessionFactory.setPlugins( new PagingInterceptorMysql() );
                locationPattern = "classpath*:kr/co/hkcloud/palette3/**/dao/xml/*Mapper_Mysql.xml";
                break;
            }
            case PostgreSQL:
            {
                sessionFactory.setPlugins( new PagingInterceptorPostgreSQL() );
            	locationPattern = "classpath*:kr/co/hkcloud/palette3/**/dao/xml/*Mapper_Postgresql.xml";
            	break;
            }
            default:
            {
                sessionFactory.setPlugins( new PagingInterceptorMysql() );
                locationPattern = "classpath*:kr/co/hkcloud/palette3/**/dao/xml/*Mapper_Mysql.xml";
                break;
            }
        }


        sessionFactory.setMapperLocations(applicationContext.getResources(locationPattern));

        return sessionFactory.getObject();
    }


    /**
     * @param  paletteRoutingSqlSessionFactory
     * @return
     */
    @Primary
    @Bean
    public SqlSessionTemplate paletteRoutingSqlSessionTemplate(@Qualifier("paletteRoutingSqlSessionFactory") SqlSessionFactory paletteRoutingSqlSessionFactory)
    {
        org.apache.ibatis.session.Configuration mybatisConfig = paletteRoutingSqlSessionFactory.getConfiguration();

        //조회결과를 가져올때 가져올 데이터 크기를 제어하는 용도로 드라이버에 힌트를 설정
        //이 파라미터값은 쿼리 설정으로 변경할 수 있다.
        mybatisConfig.setDefaultFetchSize(50);

        //가져온 값이 null일때 setter나 맵의 put 메소드를 호출할지를 명시 Map.keySet() 이나 null값을 초기화할때 유용하다.
        //int, boolean 등과 같은 원시타입은 null을 설정할 수 없다는 점은 알아두면 좋다.
        mybatisConfig.setCallSettersOnNulls(true);

        //마이바티스가 사용할 로깅 구현체를 명시 이 설정을 사용하지 않으면
        //마이바티스가 사용할 로깅 구현체를 자동으로 찾는다.
        mybatisConfig.setLogImpl((Class<? extends Log>) Slf4jImpl.class);

        //설정에서 각 매퍼에 설정된 캐시를 전역적으로 사용할지 말지에 대한 여부
        mybatisConfig.setCacheEnabled(true);

        //지연로딩을 사용할지에 대한 여부.
        //사용하지 않는다면 모두 즉시 로딩할 것이다.
        //이 값은 fetchType 속성을 사용해서 대체할 수 있다.
        mybatisConfig.setLazyLoadingEnabled(true);

        //한개의 구문에서 여러개의 ResultSet을 허용할지의 여부
        //(드라이버가 해당 기능을 지원해야 함)
        mybatisConfig.setMultipleResultSetsEnabled(true);

        //칼럼명 대신에 칼럼라벨을 사용.
        //드라이버마다 조금 다르게 작동한다.
        //문서와 간단한 테스트를 통해 실제 기대하는 것처럼 작동하는지 확인해야 한다.
        mybatisConfig.setUseColumnLabel(true);

        //생성키에 대한 JDBC 지원을 허용.
        //지원하는 드라이버가 필요하다.
        //true로 설정하면 생성키를 강제로 생성한다.
        //일부 드라이버(예를들면, Derby)에서는 이 설정을 무시한다.
        mybatisConfig.setUseGeneratedKeys(false);

        //디폴트 실행자(executor) 설정.
        //SIMPLE 실행자는 특별히 하는 것이 없다.
        //REUSE 실행자는 PreparedStatement를 재사용한다.
        //BATCH 실행자는 구문을 재사용하고 수정을 배치처리한다.
        mybatisConfig.setDefaultExecutorType(ExecutorType.SIMPLE);

        //데이터베이스로의 응답을 얼마나 오래 기다릴지를 판단하는 타임아웃을 설정
        mybatisConfig.setDefaultStatementTimeout(5000);

        //중첩구문내 RowBound사용을 허용
        //허용한다면 false로 설정
        mybatisConfig.setSafeRowBoundsEnabled(false);

        //전통적인 데이터베이스 칼럼명 형태인 A_COLUMN을 CamelCase형태의
        //자바 프로퍼티명 형태인 aColumn으로 자동으로 매핑하도록 함
        mybatisConfig.setMapUnderscoreToCamelCase(true);

        //마이바티스는 순환참조를 막거나 반복된 쿼리의 속도를 높히기 위해 로컬캐시를 사용한다.
        //디폴트 설정인 SESSION을 사용해서 동일 세션의 모든 쿼리를 캐시한다.
        //localCacheScope=STATEMENT 로 설정하면 로컬 세션은 구문 실행할때만 사용하고 같은
        //SqlSession에서 두개의 다른 호출사이에는 데이터를 공유하지 않는다.
        mybatisConfig.setLocalCacheScope(LocalCacheScope.SESSION);

        //JDBC타입을 파라미터에 제공하지 않을때 null값을 처리한 JDBC타입을 명시한다.
        //일부 드라이버는 칼럼의 JDBC타입을 정의하도록 요구하지만
        //대부분은 NULL, VARCHAR 나 OTHER 처럼 일반적인 값을 사용해서 동작한다.
        mybatisConfig.setJdbcTypeForNull(JdbcType.NULL);

        //지연로딩을 야기하는 객체의 메소드를 명시
        Set<String> lazyLoadTriggerMethods = new HashSet<String>();
        lazyLoadTriggerMethods.add("equals");
        lazyLoadTriggerMethods.add("clone");
        lazyLoadTriggerMethods.add("hashCode");
        lazyLoadTriggerMethods.add("toString");
        mybatisConfig.setLazyLoadTriggerMethods(lazyLoadTriggerMethods);

        return new SqlSessionTemplate(paletteRoutingSqlSessionFactory);
    }


//    @Bean
//    public PlatformTransactionManager transactionManager()
//    {
//        return new DataSourceTransactionManager(paletteRoutingLazyDataSource());
//    }
    
    //멀티테넌시(multitenancy) 처리 : transactionManager 를 DataSourceTransactionManager에서 JpaTransactionManager으로 변경.
    //hibernate 의 멀티테넌트 관련 설정 및 provider 사용을 위함.(하단 entityManagerFactoryBean 참조)
    //!!!transactionManager로 이용하려면 service 구현체에 @Transactional 어노테이션을 반드시 사용해야 함. 미사용 시, set schema 타지 않음!!!
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory)
    {
    	JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        transactionManager.setDataSource(paletteRoutingLazyDataSource());
    	
        return transactionManager;
    }
    
    
    /**
	 * 멀티테넌시(multitenancy) 처리 : entityManagerFactory 를 config에 추가해야 SchemaMultiTenantConnectionProvider에서 entityManagerFactory 객체를 가져옴.
	 * @param multiTenantConnectionProvider
	 * @param currentTenantIdentifierResolver
	 * @return
	 */
    @Bean(name="entityManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(
    		TenantConnectionProvider multiTenantConnectionProvider,
    		TenantIdentifierResolver currentTenantIdentifierResolver) {

        Map<String, Object> hibernateProps = new LinkedHashMap<>();
        hibernateProps.putAll(this.jpaProperties.getProperties());
        
        hibernateProps.put(Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
        hibernateProps.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
        hibernateProps.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);
        
        System.out.println("JpaConfig. LocalContainerEntityManagerFactoryBean ::: " + hibernateProps.toString());
        
        LocalContainerEntityManagerFactoryBean result = new LocalContainerEntityManagerFactoryBean();
        result.setPackagesToScan("kr.co.hkcloud.palette3");
        result.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        result.setDataSource(paletteRoutingLazyDataSource());
        result.setJpaPropertyMap(hibernateProps);

        return result;
    }
	
	@Bean
    @Primary
    public EntityManagerFactory entityManagerFactory(LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        return entityManagerFactoryBean.getObject();
    }
}
