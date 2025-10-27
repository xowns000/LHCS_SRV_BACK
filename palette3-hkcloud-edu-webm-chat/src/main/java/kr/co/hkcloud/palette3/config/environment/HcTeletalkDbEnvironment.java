package kr.co.hkcloud.palette3.config.environment;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.core.cache.app.TalkRedisCacheServiceImpl;
import kr.co.hkcloud.palette3.core.cache.enumer.TalkCacheManager;
import kr.co.hkcloud.palette3.core.chat.router.app.RoutingToAgentManagerServiceImpl;
import kr.co.hkcloud.palette3.core.chat.router.domain.CustcoId;
import kr.co.hkcloud.palette3.core.util.PaletteBeanUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.util.concurrent.ConcurrentHashMap;


/**
 * 텔레톡 DB 환경설정
 * <p>
 * initialization on demand holder idiom
 * <p>
 * JVM의 class loader의 매커니즘과 class load 시점을 이용해 내부 class를 생성시켜 thread간 동기화 문제를 해결한다.
 *
 * @author Orange
 */
@Slf4j
public class HcTeletalkDbEnvironment
{
    private static final TalkCacheManager NOW_CACHE = TalkCacheManager.REDIS;
    private static volatile ConcurrentHashMap<CustcoId, String> dbEnvironment = new ConcurrentHashMap<>();


    private HcTeletalkDbEnvironment()
    {
        log.info("=================================");
        log.info("HcTeletalkDbEnvironment loaded ::: {}", NOW_CACHE.toString());
        log.info("=================================");
    }


    private static class Singleton
    {
        private static final HcTeletalkDbEnvironment INSTANCE = new HcTeletalkDbEnvironment();
    }


    public static HcTeletalkDbEnvironment getInstance()
    {
        return Singleton.INSTANCE;
    }


    /**
     * 텔레톡 DB 환경설정 setter
     *
     * @param inJson
     */
    public void setDbEnvironment(TelewebJSON inJson)
    {
        JSONArray dbEnvLoop = inJson.getDataObject();
        if (dbEnvLoop.size() > 0)
        {
            switch (NOW_CACHE)
            {
                case REDIS:
                {
                    log.info("********************************************************** setDbEnvironment ::: " + dbEnvLoop.size());
                    TalkRedisCacheServiceImpl talkRedisCacheService = PaletteBeanUtils.getBean(TalkRedisCacheServiceImpl.class);
                    String envCustcoId = null, envKey = null, envValue = null;
                    for (int i = 0; i < dbEnvLoop.size(); i++)
                    {
                        envCustcoId = ((JSONObject) dbEnvLoop.get(i)).getString("CUSTCO_ID");
                        envKey = ((JSONObject) dbEnvLoop.get(i)).getString("STNG_CD");
                        envValue = ((JSONObject) dbEnvLoop.get(i)).getString("STNG_VL");

                        TenantContext.setCurrentCustco( envCustcoId );
                        talkRedisCacheService.setRedisDbEnvironment(new CustcoId(envCustcoId, envKey), envValue);
                    }

                    if (log.isTraceEnabled())
                    {
                        StringBuffer sbLog = new StringBuffer("\n");
                        sbLog.append("---------------------------------------------\n");
                        sbLog.append("REDIS ENVIRONMENT INFORMATION\n");
                        sbLog.append("---------------------------------------------\n");

                        String strValue, print;
                        for (CustcoId custcoId : talkRedisCacheService.keysDbEnv())
                        {
                            TenantContext.setCurrentCustco( custcoId.getCustKey() );
                            strValue = talkRedisCacheService.getRedisStringByDbEnv(custcoId);
                            print = String.format("%-70s :%-20s :%-30s :%s", custcoId, custcoId.getCustKey(), custcoId.getKey(), strValue);
                            sbLog.append("").append(print).append("\n");
                        }
                        log.trace("{}", sbLog.toString());
                    }
                    break;
                }
                default:
                {
                    String envCustcoId = null, envKey = null, envValue = null;
                    for (int i = 0; i < dbEnvLoop.size(); i++)
                    {
                        envCustcoId = ((JSONObject) dbEnvLoop.get(i)).getString("CUSTCO_ID");
                        envKey = ((JSONObject) dbEnvLoop.get(i)).getString("STNG_CD");
                        envValue = ((JSONObject) dbEnvLoop.get(i)).getString("STNG_VL");

                        TenantContext.setCurrentCustco( envCustcoId );
                        dbEnvironment.put(new CustcoId(envCustcoId, envKey), envValue);
                    }

                    if (log.isTraceEnabled())
                    {
                        StringBuffer sbLog = new StringBuffer("\n");
                        sbLog.append("---------------------------------------------\n");
                        sbLog.append("DB ENVIRONMENT INFORMATION\n");
                        sbLog.append("---------------------------------------------\n");

                        String strValue, print;
                        for (CustcoId custcoId : dbEnvironment.keySet())
                        {
                            TenantContext.setCurrentCustco( custcoId.getCustKey() );
                            strValue = dbEnvironment.get(custcoId);
                            print = String.format("%-70s :%-20s :%-30s :%s", custcoId, custcoId.getCustKey(), custcoId.getKey(), strValue);
                            sbLog.append("").append(print).append("\n");
                        }
                        log.trace("{}", sbLog.toString());
                    }
                }
            }
        } else
        {
            log.error("teletalkConfigs env size 0? dbEnvLoop.size()={}", dbEnvLoop.size());
        }
    }


    /**
     * DbEnvironment getter
     *
     * @param custcoId
     * @param key
     * @return
     */
    public String getString(String custcoId, String key)
    {
        String result = "";
        switch (NOW_CACHE)
        {
            case REDIS:
            {
                TalkRedisCacheServiceImpl talkRedisCacheService = PaletteBeanUtils.getBean(TalkRedisCacheServiceImpl.class);
                Long size = talkRedisCacheService.getSizeDbEnv();
                if (size > 0)
                {
                    result = talkRedisCacheService.getRedisStringByDbEnv(new CustcoId(custcoId, key));
                } else
                {
                    log.info("getString ::: " + size);
                    // DbEnv size 없으면 새로 텔레톡 상담 설정
                    RoutingToAgentManagerServiceImpl routingToAgentManagerService = PaletteBeanUtils.getBean(RoutingToAgentManagerServiceImpl.class);
                    TelewebJSON outJson = routingToAgentManagerService.selectTalkEnv();
                    HcTeletalkDbEnvironment.getInstance().setDbEnvironment(outJson);

                    result = talkRedisCacheService.getRedisStringByDbEnv(new CustcoId(custcoId, key));
                    if(StringUtils.isEmpty( result ))
                    {
                        log.error("size is 0, confirm your REDIS DB");
                    }
                }
                break;
            }
            default:
            {
                int intSize = dbEnvironment.size();
                if (intSize > 0)
                {
                    result = dbEnvironment.get(new CustcoId(custcoId, key)) != null ? dbEnvironment.get(new CustcoId(custcoId, key)) : "";
                } else
                {
                    log.error("dbEnvironment size is 0, confirm your DB (TWB_TALK_ENV)");
                }
            }
        }

//        if (null == result)
//        {
//            switch (key)
//            {
//                //START,STOP : 디폴트 값은 라우트 임	
//                case "ROUTE_CD":
//                {
//                    result = "START";
//                    break;
//                }
//                //10 : 디폴트 값은 10	
//                case "ROUTE_WAITING_CNT":
//                {
//                    result = "10";
//                    break;
//                }
//                //배분시작시간 : 디폴트 값은 0900
//                case "WORK_START_TIME":
//                {
//                    result = "0900";
//                    break;
//                }
//                //배분종료시간 : 디폴트 값은 1800
//                case "WORK_END_TIME":
//                {
//                    result = "1800";
//                    break;
//                }
//                //챗봇사용여부 : 디폴트 값은 Y
//                case "CHATBOT_YN":
//                {
//                    result = "Y";
//                    break;
//                }
//                //고객문의유형 사용여부 : 디폴트 값은 N
//                case "INQRY_TYPE_YN":
//                {
//                    result = "N";
//                    break;
//                }
//                //전문상담만배분 : 디폴트 값은 NORMAL
//                case "SPEC_CNSL_ROUTE":
//                {
//                    result = "NORMAL";
//                    break;
//                }
//                // 금칙어사용여부 : 디폴트 값은 APPLY
//                case "PROHIBITE_APPLY_YN":
//                {
//                    result = "APPLY";
//                    break;
//                }
//                // 상담사자동인사사용여부 : 디폴트 값은 N
//                case "AUTO_GREETING_YN":
//                {
//                    result = "N";
//                    break;
//                }
//                // 고객무응답메시지 사용여부 : 디폴트 값은 N
//                case "CUST_NORESP_USE_YN":
//                {
//                    result = "N";
//                    break;
//                }
//                // 고객무응답메시지 전송 후 자동종료 사용여부 : 디폴트 값은 N
//                case "CUST_NORESP_CHATEND":
//                {
//                    result = "N";
//                    break;
//                }
//                // 자동응답메시지 사용여부 : 디폴트 값은 N
//                case "AUTO_MESSAGE_YN":
//                {
//                    result = "N";
//                    break;
//                }
//                case "REQ_USER_INFO_YN":
//                {
//                    result = "N";
//                    break;
//                }
//                case "CONSULT_ALRAM_YN":
//                {
//                    result = "Y";
//                    break;
//                }
//                default:
//                {
//                    result = null;
//                }
//            }
//        }
        return result;
    }
}
