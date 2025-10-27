package kr.co.hkcloud.palette3.common.twb.dao;


import com.google.gson.Gson;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.datasources.enumer.DatasourcePoolName;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkEnvironment;
import kr.co.hkcloud.palette3.core.util.PaletteUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Orange
 */
@Slf4j
@Repository
@Transactional
public class TwbExternalDAO
{
    private SqlSession externalRoutingSqlSessionTemplate = null;

    @Autowired
    private HcTeletalkEnvironment env;

    @Autowired
    private PaletteUtils telewebCommUtil;


    @Autowired
    public TwbExternalDAO(SqlSession externalSqlSession)
    {
        this.externalRoutingSqlSessionTemplate = externalSqlSession;
    }


//    @Autowired
//    public void setExternalRoutingSqlSessionTemplate(SqlSession externalRoutingSqlSessionTemplate)
//    {
//        this.externalRoutingSqlSessionTemplate = externalRoutingSqlSessionTemplate;
//    }


    /**
     * 리스트 조회
     *
     * @param id : 실행 쿼리아이디
     * @param :
     * @return :
     * @throws :Exception
     */
    private List<HashMap<String, Object>> select(String id, HashMap<String, Object> map, HttpServletRequest objRequest, DatasourcePoolName datasourcePoolName) throws TelewebDaoException
    {
        return getSqlSession(objRequest, datasourcePoolName).selectList(id, map);
    }


    /**
     * INSERT
     *
     * @param id : 실행 쿼리아이디
     * @param :
     * @return : Insert된 행의 개수 반환 (없다면 0)
     * @throws :Exception
     */
    private int insert(String id, HashMap<String, String> map, HttpServletRequest objRequest, DatasourcePoolName datasourcePoolName) throws TelewebDaoException
    {
        return getSqlSession(objRequest, datasourcePoolName).insert(id, map);
    }


    /**
     * INSERT
     *
     * @param id : 실행 쿼리아이디
     * @param :
     * @return : Update된 행의 개수 반환 (없다면 0)
     * @throws :Exception
     */
    private int update(String id, HashMap<String, String> map, HttpServletRequest objRequest, DatasourcePoolName datasourcePoolName) throws TelewebDaoException
    {
        return getSqlSession(objRequest, datasourcePoolName).update(id, map);
    }


    /**
     * INSERT
     *
     * @param id : 실행 쿼리아이디
     * @param :
     * @return : Delete된 행의개수 (없다면 0)
     * @throws :Exception
     */
    private int delete(String id, HashMap<String, String> map, HttpServletRequest objRequest, DatasourcePoolName datasourcePoolName) throws TelewebDaoException
    {
        return externalRoutingSqlSessionTemplate.delete(id, map);
    }



    /*------------------------------------------------------------------------------------------------------------------------------------------------------*/
    /*------------------------------------------------------------------------------------------------------------------------------------------------------*/
    /*------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public TelewebJSON select(String sqlNameSpace, String sqlNm, TelewebJSON acJson) throws TelewebDaoException
    {

        JSONObject obj = JSONObject.fromObject(acJson.getHeaderObject());

        if (obj.containsKey("POOL_NM"))
        {
            DatasourcePoolName datasourcePoolName = DatasourcePoolName.valueOf(obj.getString("POOL_NM"));
            return this.select(sqlNameSpace, sqlNm, acJson, null, datasourcePoolName);
        } else
        {
            return this.select(sqlNameSpace, sqlNm, acJson, null, DatasourcePoolName.palette);
        }
    }


    public TelewebJSON select(String sqlNameSpace, String sqlNm, TelewebJSON acJson, HttpServletRequest objRequest) throws TelewebDaoException
    {
        return this.select(sqlNameSpace, sqlNm, acJson, objRequest, DatasourcePoolName.palette);
    }


    @SuppressWarnings("unchecked")
    public TelewebJSON select(String sqlNameSpace, String sqlNm, TelewebJSON acJson, HttpServletRequest objRequest, DatasourcePoolName datasourcePoolName) throws TelewebDaoException
    {
        TelewebJSON objRetJson = new TelewebJSON(acJson);
        objRetJson.setHeader("ERROR_FLAG", true);
        List<HashMap<String, Object>> retMap = null;
        HashMap<String, Object> map = new HashMap<>();

        int intPagesCnt = acJson.getHeaderInt("PAGES_CNT");
        int intRowCnt = acJson.getHeaderInt("ROW_CNT");
        int intStartRow = (intRowCnt * intPagesCnt) - intRowCnt;

        if (intPagesCnt != 0)
        {
            acJson.setInt("PAGES_CNT", intPagesCnt);
        }
        if (intRowCnt != 0)
        {
            acJson.setInt("ROW_CNT", intRowCnt);
        }
        Gson gson = new Gson();
        String json = acJson.getDataObject().get(0).toString();
        json = StringUtils.replace(json, "&#40;", "(");
        json = StringUtils.replace(json, "&#41;", ")");
        json = StringUtils.replace(json, "&#91;", "[");
        json = StringUtils.replace(json, "&#93;", "]");

        map = gson.fromJson(json, map.getClass());
        /*
         * MCRM 로직 추가 - 암호화 대상 컬럼이 발생하는 경우 참조하여 처리 - 20190308 liy map = getJsonFromMapObject(acJson);
         */

        if (intRowCnt != 0)
        {
            map.put("PAGES_CNT", new Integer(intPagesCnt));
            map.put("ROW_CNT", new Integer(intRowCnt));
            map.put("START_ROW", new Integer(intStartRow));
        } else
        {
            map.put("START_ROW", new Integer(0));        // 값이 없는 경우 초기화값을 넘김.
            map.put("PAGES_CNT", new Integer(intPagesCnt));
            map.put("ROW_CNT", new Integer(intRowCnt));
        }
        //20190508 ojw added for MySQL Paging
        map.put("LIMIT_FROM", ((intPagesCnt - 1) * intRowCnt));
        map.put("LIMIT_COUNT", intRowCnt);

        objRetJson.setHeader("TWB_SQL_NAME_SPACE", sqlNameSpace);
        objRetJson.setHeader("TWB_SQL_ID", sqlNm);

        if (sqlNameSpace != null && !sqlNameSpace.equals(""))
        {
            if (sqlNameSpace.substring(0, 1).equals("."))
            {
                sqlNameSpace = sqlNameSpace.substring(1);
            }
            if (!sqlNameSpace.substring(sqlNameSpace.length() - 1).equals("."))
            {
                sqlNameSpace = sqlNameSpace + ".";
            }
        }
        String strQry = sqlNameSpace + sqlNm;
//        long s = System.currentTimeMillis();

        retMap = this.select(strQry, map, objRequest, datasourcePoolName);

//        long e = System.currentTimeMillis();

//        String strLog = "\n";
//        strLog += "IDENTIFIER     : " + objRetJson.getHeaderString("TELEWEB_IDENTIFIER") + "\n";
//        strLog += "SQL_NAME_SPACE : " + sqlNameSpace + "\n";
//        strLog += "SQL_ID         : " + sqlNm + "\n";
//        strLog += "DURATION       : " + (e - s) + "\n";
//        strLog += "QRY            : \n" + getSqlPrepared(sqlNameSpace, sqlNm, acJson) + "\n";
//        strLog += "PARAMS(acJson) : \n" + acJson.getDataObject().toString() + "\n";
//        strLog += "PARAMS(map)    : \n" + map.toString() + "\n";
//        log.debug("\n{}", strLog);

        objRetJson.setDataObject(getJsonArrayFromList(retMap));
//        log.debug("objRetJson.getDataObject()=>\n{}", objRetJson.getDataObject().toString());

        int index = 0;
        if (retMap.isEmpty())
        {
            index = 0;
        } else
        {
            index = retMap.size();
        }

        objRetJson.setHeader("TOT_COUNT", index);
        objRetJson.setHeader("COUNT", index);
        objRetJson.setHeader("ERROR_FLAG", false);
        if (index > 0 && objRetJson.containsKey("TWB_PAGING_TOT_COUNT"))
        {
            objRetJson.setHeader("PAGES_CNT", acJson.getHeaderInt("PAGES_CNT"));
            objRetJson.setHeader("ROW_CNT", acJson.getHeaderInt("ROW_CNT"));
            objRetJson.setHeader("TOT_COUNT", objRetJson.getInt("TWB_PAGING_TOT_COUNT"));
        }

        if (index > 0)
        {
            objRetJson.setHeader("ERROR_MSG", "정상 조회 되었습니다.");
        } else
        {
            objRetJson.setHeader("ERROR_MSG", "조회 데이터가 존재 하지 않습니다.");
        }
//        log.debug("objRetJson=>\n{}", objRetJson.toString());
        return objRetJson;
    }


    public TelewebJSON insert(String sqlNameSpace, String sqlNm, TelewebJSON acJson) throws TelewebDaoException
    {
        return this.insert(sqlNameSpace, sqlNm, acJson, null, DatasourcePoolName.palette);
    }


    public Object insert(String sqlNameSpace, String sqlNm, Object params) throws TelewebDaoException
    {
        return this.insert(sqlNameSpace, sqlNm, params, null, DatasourcePoolName.palette);
    }


    public TelewebJSON insert(String sqlNameSpace, String sqlNm, TelewebJSON acJson, HttpServletRequest objRequest) throws TelewebDaoException
    {
        return this.insert(sqlNameSpace, sqlNm, acJson, objRequest, DatasourcePoolName.palette);
    }


    @SuppressWarnings("unchecked")
    public TelewebJSON insert(String sqlNameSpace, String sqlNm, TelewebJSON acJson, HttpServletRequest objRequest, DatasourcePoolName datasourcePoolName) throws TelewebDaoException
    {
        TelewebJSON objRetJson = new TelewebJSON(acJson);
        objRetJson.setHeader("ERROR_FLAG", true);
        int result = 0;
        HashMap<String, String> map = new HashMap<>();

        Gson gson = new Gson();
        String json = acJson.getDataObject().get(0).toString();
        json = StringUtils.replace(json, "&#40;", "(");
        json = StringUtils.replace(json, "&#41;", ")");
        json = StringUtils.replace(json, "&#91;", "[");
        json = StringUtils.replace(json, "&#93;", "]");

        map = gson.fromJson(json, map.getClass());

        if (sqlNameSpace != null && !sqlNameSpace.equals(""))
        {
            if (sqlNameSpace.substring(0, 1).equals("."))
            {
                sqlNameSpace = sqlNameSpace.substring(1);
            }
            if (!sqlNameSpace.substring(sqlNameSpace.length() - 1).equals("."))
            {
                sqlNameSpace = sqlNameSpace + ".";
            }
        }
        String strQry = sqlNameSpace + sqlNm;

//        long s = System.currentTimeMillis();

        result = this.insert(strQry, map, objRequest, datasourcePoolName);

//        long e = System.currentTimeMillis();

//        String strLog = "\n";
//        strLog += "IDENTIFIER     : " + objRetJson.getHeaderString("TELEWEB_IDENTIFIER") + "\n";
//        strLog += "SQL_NAME_SPACE : " + sqlNameSpace + "\n";
//        strLog += "SQL_ID         : " + sqlNm + "\n";
//        strLog += "DURATION       : " + (e - s) + "\n";
//        strLog += "QRY            : \n" + getSqlPrepared(sqlNameSpace, sqlNm, acJson) + "\n";
//        strLog += "PARAMS         : \n" + acJson.getDataObject().toString() + "\n";

        int index = result;
        if (result == 0)
        {
            index = 0;
        } else
        {
            index = result;
        }

        objRetJson.setHeader("TOT_COUNT", index);
        objRetJson.setHeader("COUNT", index);
        objRetJson.setHeader("ERROR_FLAG", false);
        if (index > 0)
        {
            objRetJson.setHeader("ERROR_MSG", "정상 입력 되었습니다.");
        } else
        {
            objRetJson.setHeader("ERROR_MSG", "입력 데이터가 존재 하지 않습니다.");
        }

        return objRetJson;
    }


    public Object insert(String sqlNameSpace, String sqlNm, Object parameters, HttpServletRequest objRequest, DatasourcePoolName datasourcePoolName) throws TelewebDaoException
    {
        int result = 0;
        HashMap<String, String> map = new HashMap<>();

        if (sqlNameSpace != null && !sqlNameSpace.equals(""))
        {
            if (sqlNameSpace.substring(0, 1).equals("."))
            {
                sqlNameSpace = sqlNameSpace.substring(1);
            }
            if (!sqlNameSpace.substring(sqlNameSpace.length() - 1).equals("."))
            {
                sqlNameSpace = sqlNameSpace + ".";
            }
        }
        String strQry = sqlNameSpace + sqlNm;

        long s = System.currentTimeMillis();
        result = getSqlSession(objRequest, datasourcePoolName).insert(strQry, parameters);
        long e = System.currentTimeMillis();
        /*
         * String strLog = "\n"; strLog += "IDENTIFIER     : " + objRetJson.getHeaderString("TELEWEB_IDENTIFIER") + "\n"; strLog += "SQL_NAME_SPACE : " + sqlNameSpace + "\n"; strLog += "SQL_ID         : " + sqlNm + "\n";
         * strLog += "DURATION       : " + (e-s) + "\n"; strLog += "QRY            : \n" + getSqlPrepared(sqlNameSpace, sqlNm, acJson) + "\n"; strLog += "PARAMS         : \n" + acJson.getDataObject().toString() + "\n";
         */
        int index = result;
        if (result == 0)
        {
            index = 0;
        } else
        {
            index = result;
        }
        return parameters;
    }


    public TelewebJSON update(String sqlNameSpace, String sqlNm, TelewebJSON acJson) throws TelewebDaoException
    {
        return this.update(sqlNameSpace, sqlNm, acJson, null, DatasourcePoolName.palette);
    }


    public TelewebJSON update(String sqlNameSpace, String sqlNm, TelewebJSON acJson, HttpServletRequest objRequest) throws TelewebDaoException
    {
        return this.update(sqlNameSpace, sqlNm, acJson, objRequest, DatasourcePoolName.palette);
    }


    @SuppressWarnings("unchecked")
    public TelewebJSON update(String sqlNameSpace, String sqlNm, TelewebJSON acJson, HttpServletRequest objRequest, DatasourcePoolName datasourcePoolName) throws TelewebDaoException
    {
        TelewebJSON objRetJson = new TelewebJSON(acJson);
        objRetJson.setHeader("ERROR_FLAG", true);
        int result = 0;
        HashMap<String, String> map = new HashMap<>();

        Gson gson = new Gson();
        String json = acJson.getDataObject().get(0).toString();
        json = StringUtils.replace(json, "&#40;", "(");
        json = StringUtils.replace(json, "&#41;", ")");
        json = StringUtils.replace(json, "&#91;", "[");
        json = StringUtils.replace(json, "&#93;", "]");

        map = gson.fromJson(json, map.getClass());

        if (sqlNameSpace != null && !sqlNameSpace.equals(""))
        {
            if (sqlNameSpace.substring(0, 1).equals("."))
            {
                sqlNameSpace = sqlNameSpace.substring(1);
            }
            if (!sqlNameSpace.substring(sqlNameSpace.length() - 1).equals("."))
            {
                sqlNameSpace = sqlNameSpace + ".";
            }
        }
        String strQry = sqlNameSpace + sqlNm;

//        long s = System.currentTimeMillis();

        result = this.update(strQry, map, objRequest, datasourcePoolName);

//        long e = System.currentTimeMillis();

//        String strLog = "\n";
//        strLog += "IDENTIFIER     : " + objRetJson.getHeaderString("TELEWEB_IDENTIFIER") + "\n";
//        strLog += "SQL_NAME_SPACE : " + sqlNameSpace + "\n";
//        strLog += "SQL_ID         : " + sqlNm + "\n";
//        strLog += "DURATION       : " + (e - s) + "\n";
//        strLog += "QRY            : \n" + getSqlPrepared(sqlNameSpace, sqlNm, acJson) + "\n";
//        strLog += "PARAMS         : \n" + acJson.getDataObject().toString() + "\n";

        int index = result;
        if (result == 0)
        {
            index = 0;
        } else
        {
            index = result;
        }

        objRetJson.setHeader("TOT_COUNT", index);
        objRetJson.setHeader("COUNT", index);
        objRetJson.setHeader("ERROR_FLAG", false);
        if (index > 0)
        {
            objRetJson.setHeader("ERROR_MSG", "정상 수정 되었습니다.");
        } else
        {
            objRetJson.setHeader("ERROR_MSG", "수정 데이터가 존재 하지 않습니다.");
        }

        return objRetJson;
    }


    public TelewebJSON delete(String sqlNameSpace, String sqlNm, TelewebJSON acJson) throws TelewebDaoException
    {
        return this.delete(sqlNameSpace, sqlNm, acJson, null, DatasourcePoolName.palette);
    }


    public TelewebJSON delete(String sqlNameSpace, String sqlNm, TelewebJSON acJson, HttpServletRequest objRequest) throws TelewebDaoException
    {
        return this.delete(sqlNameSpace, sqlNm, acJson, objRequest, DatasourcePoolName.palette);
    }


    @SuppressWarnings("unchecked")
    public TelewebJSON delete(String sqlNameSpace, String sqlNm, TelewebJSON acJson, HttpServletRequest objRequest, DatasourcePoolName datasourcePoolName) throws TelewebDaoException
    {
        TelewebJSON objRetJson = new TelewebJSON(acJson);
        objRetJson.setHeader("ERROR_FLAG", true);
        int result = 0;
        HashMap<String, String> map = new HashMap<>();

        Gson gson = new Gson();
        String json = acJson.getDataObject().get(0).toString();
        map = gson.fromJson(json, map.getClass());

        if (sqlNameSpace != null && !sqlNameSpace.equals(""))
        {
            if (sqlNameSpace.substring(0, 1).equals("."))
            {
                sqlNameSpace = sqlNameSpace.substring(1);
            }
            if (!sqlNameSpace.substring(sqlNameSpace.length() - 1).equals("."))
            {
                sqlNameSpace = sqlNameSpace + ".";
            }
        }
        String strQry = sqlNameSpace + sqlNm;

//        long s = System.currentTimeMillis();

        result = this.delete(strQry, map, objRequest, datasourcePoolName);

//        long e = System.currentTimeMillis();

//        String strLog = "\n";
//        strLog += "IDENTIFIER     : " + objRetJson.getHeaderString("TELEWEB_IDENTIFIER") + "\n";
//        strLog += "SQL_NAME_SPACE : " + sqlNameSpace + "\n";
//        strLog += "SQL_ID         : " + sqlNm + "\n";
//        strLog += "DURATION       : " + (e - s) + "\n";
//        strLog += "QRY            : \n" + getSqlPrepared(sqlNameSpace, sqlNm, acJson) + "\n";
//        strLog += "PARAMS         : \n" + acJson.getDataObject().toString() + "\n";

        int index = result;
        if (result == 0)
        {
            index = 0;
        } else
        {
            index = result;
        }

        objRetJson.setHeader("TOT_COUNT", index);
        objRetJson.setHeader("COUNT", index);
        objRetJson.setHeader("ERROR_FLAG", false);
        if (index > 0)
        {
            objRetJson.setHeader("ERROR_MSG", "정상 삭제 되었습니다.");
        } else
        {
            objRetJson.setHeader("ERROR_MSG", "삭제 데이터가 존재 하지 않습니다.");
        }

        return objRetJson;
    }


    /**
     * List<Map>을 json으로 변환한다.
     *
     * @param list List<Map<String, Object>>.
     * @return JSONArray.
     * @throws TelewebDaoException
     */
    private static JSONArray getJsonArrayFromList(List<HashMap<String, Object>> list) throws TelewebDaoException
    {

        JSONArray jsonArray = new JSONArray();
        for (HashMap<String, Object> map : list)
        {
            jsonArray.add(getJsonStringFromMap(map));
        }

        return jsonArray;
    }


    /**
     * Map을 jsonString으로 변환한다.
     *
     * @param map Map<String, Object>.
     * @return String.
     * @throws TelewebDaoException
     */
    private static JSONObject getJsonStringFromMap(HashMap<String, Object> map) throws TelewebDaoException
    {
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, Object> entry : map.entrySet())
        {
            String key = entry.getKey();
            Object value = entry.getValue();
            jsonObject.put(key, nvl(value));
        }
        return jsonObject;
    }


    /**
     * 내장 nvl
     *
     * @param str
     * @return
     */
    private static String nvl(Object str)
    {
        if (str == null)
        {
            return "";
        } else
        {
            return str.toString();
        }
    }


    /**
     * sqlSession을 변환한다.
     *
     * @param map Map<String, Object>.
     * @return String.
     * @throws TelewebDaoException
     */
    private SqlSession getSqlSession(HttpServletRequest objRequest, DatasourcePoolName datasourcePoolName) throws TelewebDaoException
    {
        SqlSession sqlSession = null;
        switch (datasourcePoolName)
        {
            case palette:
                sqlSession = this.externalRoutingSqlSessionTemplate;
                break;
        }
//        {
//            WebApplicationContext webApplicationContext = RequestContextUtils.findWebApplicationContext(objRequest);
//            sqlSession = (SqlSession) webApplicationContext.getBean(revPoolNm);
//        }
        return sqlSession;
    }


    public String getSqlPrepared(String sqlNameSpace, String sqlNm, TelewebJSON acJson) throws TelewebDaoException
    {
        return getSqlPrepared(sqlNameSpace, sqlNm, acJson, null, DatasourcePoolName.palette);
    }


    public String getSqlPrepared(String sqlNameSpace, String sqlNm, TelewebJSON acJson, HttpServletRequest objRequest, DatasourcePoolName datasourcePoolName) throws TelewebDaoException
    {

        HashMap<String, String> map = new HashMap<>();

        int intPagesCnt = acJson.getHeaderInt("PAGES_CNT");
        int intRowCnt = acJson.getHeaderInt("ROW_CNT");
        if (intPagesCnt != 0)
        {
            acJson.setInt("PAGES_CNT", intPagesCnt);
        }
        if (intRowCnt != 0)
        {
            acJson.setInt("ROW_CNT", intRowCnt);
        }

        Gson gson = new Gson();
        String json = acJson.getDataObject().get(0).toString();
        map = gson.fromJson(json, map.getClass());

        if (sqlNameSpace != null && !sqlNameSpace.equals(""))
        {
            if (sqlNameSpace.substring(0, 1).equals("."))
            {
                sqlNameSpace = sqlNameSpace.substring(1);
            }
            if (!sqlNameSpace.substring(sqlNameSpace.length() - 1).equals("."))
            {
                sqlNameSpace = sqlNameSpace + ".";
            }
        }

        SqlSession sqlSession = getSqlSession(objRequest, datasourcePoolName);
        String strQryNm = sqlNameSpace + sqlNm;

        BoundSql boundSql = sqlSession.getConfiguration().getMappedStatement(strQryNm).getSqlSource().getBoundSql(map);

        return boundSql.getSql();
    }


    /**
     * xml파일의 sql을 읽어 파라메터와 조합된 쿼리문을 리턴한다.
     *
     * @return
     * @throws TelewebDaoException
     */
    public String getSqlStatement(String sqlNameSpace, String sqlNm, TelewebJSON acJson) throws TelewebDaoException
    {
        return getSqlStatement(sqlNameSpace, sqlNm, acJson, null, DatasourcePoolName.palette);
    }


    public String getSqlStatement(String sqlNameSpace, String sqlNm, TelewebJSON acJson, HttpServletRequest objRequest, DatasourcePoolName datasourcePoolName) throws TelewebDaoException
    {

        HashMap<String, String> map = new HashMap<>();

        int intPagesCnt = acJson.getHeaderInt("PAGES_CNT");
        int intRowCnt = acJson.getHeaderInt("ROW_CNT");
        if (intPagesCnt != 0)
        {
            acJson.setInt("PAGES_CNT", intPagesCnt);
        }
        if (intRowCnt != 0)
        {
            acJson.setInt("ROW_CNT", intRowCnt);
        }

        Gson gson = new Gson();
        String json = acJson.getDataObject().get(0).toString();
        map = gson.fromJson(json, map.getClass());

        if (sqlNameSpace != null && !sqlNameSpace.equals(""))
        {
            if (sqlNameSpace.substring(0, 1).equals("."))
            {
                sqlNameSpace = sqlNameSpace.substring(1);
            }
            if (!sqlNameSpace.substring(sqlNameSpace.length() - 1).equals("."))
            {
                sqlNameSpace = sqlNameSpace + ".";
            }
        }

        SqlSession sqlSession = getSqlSession(objRequest, datasourcePoolName);
        String strQryNm = sqlNameSpace + sqlNm;

        BoundSql boundSql = sqlSession.getConfiguration().getMappedStatement(strQryNm).getSqlSource().getBoundSql(map);
        String strSqlQry = boundSql.getSql();
        List<ParameterMapping> paramsList = boundSql.getParameterMappings();
        Object paramsObj = boundSql.getParameterObject();

        JSONObject objJsonParams = JSONObject.fromObject(paramsObj);
        if (!paramsList.isEmpty())
        {
            String strKey = "";
            JdbcType paramJdbcType = null;
            for (int i = 0; i < paramsList.size(); i++)
            {
                strKey = paramsList.get(i).getProperty();
                paramJdbcType = paramsList.get(i).getJdbcType();
                if (JdbcType.CHAR.equals(paramJdbcType) || JdbcType.VARCHAR.equals(paramJdbcType) || JdbcType.LONGVARCHAR.equals(paramJdbcType) || JdbcType.NVARCHAR.equals(paramJdbcType) || JdbcType.NCHAR.equals(paramJdbcType) || paramJdbcType == null)
                {
                    strSqlQry = strSqlQry.replaceFirst("\\?", "'" + objJsonParams.getString(strKey) + "'");
                } else
                {
                    strSqlQry = strSqlQry.replaceFirst("\\?", objJsonParams.getString(strKey));
                }
            }
        }

        return strSqlQry;
    }


    @SuppressWarnings("unchecked")
    public HashMap<String, Object> getJsonFromMapObject(TelewebJSON acJson) throws TelewebDaoException
    {
        HashMap<String, Object> map = new HashMap<>();
        Gson gson = new Gson();
        String json = acJson.getDataObject().get(0).toString();
        map = gson.fromJson(json, map.getClass());

        String[] cryptColumns = telewebCommUtil.getCryptColumns(env.getString("CRYPT_COLUMNS"));

        //암호화 대상 컬럼을 찾아 암호화 시킨다.
        for (String cKey : cryptColumns)
        {
            if (map.containsKey(cKey))
            {
                map.put(cKey, telewebCommUtil.getEncryptString((String) map.get(cKey), env.getString("CRYPT_KEY")));
            }
        }

        return map;
    }


    @SuppressWarnings("unchecked")
    public HashMap<String, String> getJsonFromMapString(TelewebJSON acJson) throws TelewebDaoException
    {
        HashMap<String, String> map = new HashMap<>();
        Gson gson = new Gson();
        String json = acJson.getDataObject().get(0).toString();
        map = gson.fromJson(json, map.getClass());
        /*
         * TelewebCommUtil telewebCommUtil = new TelewebCommUtil(); String[] cryptColumns = telewebCommUtil.getCryptColumns(telewebProperty.getString("CRYPT_COLUMNS"));
         */
        //암호화 대상 컬럼을 찾아 암호화 시킨다.
        /*
         * for( String cKey : cryptColumns ) { if( map.containsKey(cKey) ) { map.put(cKey, telewebCommUtil.getEncryptString( (String) map.get(cKey), telewebProperty.getString("CRYPT_KEY"))); } }
         */
        return map;
    }
}
