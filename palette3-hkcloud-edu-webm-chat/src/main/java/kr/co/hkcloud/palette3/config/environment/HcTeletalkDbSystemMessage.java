package kr.co.hkcloud.palette3.config.environment;


import java.util.concurrent.ConcurrentHashMap;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.cache.app.TalkRedisCacheServiceImpl;
import kr.co.hkcloud.palette3.core.cache.enumer.TalkCacheManager;
import kr.co.hkcloud.palette3.core.chat.router.app.RoutingToAgentManagerServiceImpl;
import kr.co.hkcloud.palette3.core.chat.router.domain.CustcoId;
import kr.co.hkcloud.palette3.core.util.PaletteBeanUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

/**
 * 텔레톡 DB 시스템메시지 속성
 * <p>
 * initialization on demand holder idiom
 * <p>
 * JVM의 class loader의 매커니즘과 class load 시점을 이용해 내부 class를 생성시켜 thread간 동기화 문제를 해결한다.
 *
 * @author Orange
 */
@Slf4j
public class HcTeletalkDbSystemMessage
{
    private static final TalkCacheManager                             NOW_CACHE       = TalkCacheManager.REDIS;
    private static volatile ConcurrentHashMap<CustcoId, JSONObject> dbSystemMessage = new ConcurrentHashMap<>();


    private HcTeletalkDbSystemMessage() {
        log.info("=================================");
        log.info("HcTeletalkDbSystemMessage loaded ::: {}", NOW_CACHE.toString());
        log.info("=================================");
    }


    private static class Singleton
    {
        private static final HcTeletalkDbSystemMessage INSTANCE = new HcTeletalkDbSystemMessage();
    }


    public static HcTeletalkDbSystemMessage getInstance()
    {
        return Singleton.INSTANCE;
    }


    /**
     * 시스템메시지 setter
     *
     * @param sysMsgCustcoId
     * @param sysMsgId
     * @param sysMsgJSON
     */
    public void setSystemMessage(TelewebJSON inJson)
    {
        JSONArray dbSystemMessageLoop = inJson.getDataObject();
        if(dbSystemMessageLoop.size() > 0) {
            switch(NOW_CACHE)
            {
                case REDIS:
                {
                    TalkRedisCacheServiceImpl talkRedisCacheService = PaletteBeanUtils
                        .getBean(TalkRedisCacheServiceImpl.class);
                    String sysMsgCustcoId = null, sysMsgId = null;
                    JSONObject sysMsgJSON = null;
                    for(int i = 0; i < dbSystemMessageLoop.size(); i++) {
                        sysMsgCustcoId = ((JSONObject) dbSystemMessageLoop.get(i)).getString("CUSTCO_ID");
                        sysMsgId = ((JSONObject) dbSystemMessageLoop.get(i)).getString("SYS_MSG_ID");
                        sysMsgJSON = (JSONObject) dbSystemMessageLoop.get(i);
                        talkRedisCacheService
                            .setRedisSystemMessage(new CustcoId(sysMsgCustcoId, sysMsgId), sysMsgJSON);
                    }

                    if(log.isTraceEnabled()) {
                        StringBuffer sbLog = new StringBuffer("\n");
                        sbLog.append("---------------------------------------------\n");
                        sbLog.append("REDIS SYSTEM MESSAGE INFORMATION\n");
                        sbLog.append("---------------------------------------------\n");

                        String prettyPrintJSONString = "", print = "";
                        JSONObject json;
                        for(CustcoId custcoId : talkRedisCacheService.keysSystemMessage()) {
                            json = talkRedisCacheService.getRedisJsonBySystemMsgId(custcoId);
                            try {
                                ObjectMapper objectMapper = new ObjectMapper();
                                Object object = objectMapper.readValue(json.toString(), Object.class);
                                prettyPrintJSONString = objectMapper.writerWithDefaultPrettyPrinter()
                                    .writeValueAsString(object);
                                print = String.format("%-70s :%-20s :%-30s :%s", custcoId, custcoId
                                    .getCustKey(), custcoId.getKey(), prettyPrintJSONString);
                            }
                            catch(JsonProcessingException e) {
                                log.warn("JsonProcessingException ::: {}", json);
                            }
                            sbLog.append("").append(print).append("\n");
                        }
                        log.trace("{}", sbLog.toString());
                    }
                    break;
                }
                default:
                {
                    String sysMsgCustcoId = null, sysMsgId = null;
                    JSONObject sysMsgJSON = null;
                    for(int i = 0; i < dbSystemMessageLoop.size(); i++) {
                        sysMsgCustcoId = ((JSONObject) dbSystemMessageLoop.get(i)).getString("CUSTCO_ID");
                        sysMsgId = ((JSONObject) dbSystemMessageLoop.get(i)).getString("SYS_MSG_ID");
                        sysMsgJSON = (JSONObject) dbSystemMessageLoop.get(i);
                        dbSystemMessage.put(new CustcoId(sysMsgCustcoId, sysMsgId), sysMsgJSON);
                    }

                    if(log.isTraceEnabled()) {
                        StringBuffer sbLog = new StringBuffer("\n");
                        sbLog.append("---------------------------------------------\n");
                        sbLog.append("DB SYSTEM MESSAGE INFORMATION\n");
                        sbLog.append("---------------------------------------------\n");

                        String prettyPrintJSONString = "", print = "";
                        JSONObject json;
                        for(CustcoId custcoId : dbSystemMessage.keySet()) {
                            json = dbSystemMessage.get(custcoId);
                            try {
                                ObjectMapper objectMapper = new ObjectMapper();
                                Object object = objectMapper.readValue(json.toString(), Object.class);
                                prettyPrintJSONString = objectMapper.writerWithDefaultPrettyPrinter()
                                    .writeValueAsString(object);
                                print = String.format("%-70s :%-20s :%-30s :%s", custcoId, custcoId
                                    .getCustKey(), custcoId.getKey(), prettyPrintJSONString);
                            }
                            catch(JsonProcessingException e) {
                                log.warn("JsonProcessingException ::: {}", json);
                            }
                            sbLog.append("").append(print).append("\n");
                        }
                        log.trace("{}", sbLog.toString());
                    }
                }
            }
        }
        else {
            log.error("systemMessageConfigs size 0? dbSystemMessageLoop.size()={}", dbSystemMessageLoop.size());
        }
    }


    /**
     * SYS_MSG_ID로 메시지 JSON 객체를 가져온다.
     *
     * @param  custcoId
     * @param  sysMsgId   입력한 메시지 아이디
     * @return            retSystemMessage 메시지 아이디에 해당하는 TelewebJSON 객체
     */
    public TelewebJSON getTelewebJsonBySystemMsgId(String custcoId, String sysMsgId)
    {
        TelewebJSON retTelewebJson = new TelewebJSON();
        switch(NOW_CACHE)
        {
            case REDIS:
            {
                TalkRedisCacheServiceImpl talkRedisCacheService = PaletteBeanUtils
                    .getBean(TalkRedisCacheServiceImpl.class);
                Long size = talkRedisCacheService.getSizeSystemMessage();
                if(size > 0) {
                    JSONObject sysMsgJSON = talkRedisCacheService
                        .getRedisJsonBySystemMsgId(new CustcoId(custcoId, sysMsgId));
                    retTelewebJson.setDataObject("DATA", sysMsgJSON);
                }
                else {
                    log.error("size is 0, confirm your REDIS DB");
                }
                break;
            }
            default:
            {
                int intMsgSize = dbSystemMessage.size();
                if(intMsgSize > 0) {
                    JSONObject sysMsgJSON = dbSystemMessage.get(new CustcoId(custcoId, sysMsgId));
                    retTelewebJson.setDataObject("DATA", sysMsgJSON);
                }
                else {
                    log.error("dbSystemMessage size is 0, confirm your DB (TWB_TALK_SYSTEM_MSG)");
                }
            }
        }
        return retTelewebJson;
    }


    /**
     * SYS_MSG_ID로 메시지를 가져온다.
     *
     * @param  custcoId
     * @param  sysMsgId   입력한 메시지 아이디
     * @return            retSystemMessage 메시지 아이디에 해당하는 문자열
     */
    public String getStringBySystemMsgId(String custcoId, String sysMsgId)
    {
        String retString = "";
        switch(NOW_CACHE)
        {
            case REDIS:
            {
                TalkRedisCacheServiceImpl talkRedisCacheService = PaletteBeanUtils.getBean(TalkRedisCacheServiceImpl.class);
                Long size = talkRedisCacheService.getSizeSystemMessage();
                if(size > 0) {
                    JSONObject sysMsgJSON = talkRedisCacheService.getRedisJsonBySystemMsgId(new CustcoId(custcoId, sysMsgId));
                    if(sysMsgJSON != null) {
                        retString = sysMsgJSON.getString("MSG_CN");
                    }
                }
                else {
                    // DbEnv size 없으면 새로 텔레톡 시스템메시지 설정
                    RoutingToAgentManagerServiceImpl routingToAgentManagerService = PaletteBeanUtils.getBean(RoutingToAgentManagerServiceImpl.class);
                    TelewebJSON sysMsgJson = routingToAgentManagerService.selectSystemMessage();
                    HcTeletalkDbSystemMessage.getInstance().setSystemMessage(sysMsgJson);

                    size = talkRedisCacheService.getSizeSystemMessage();
                    if(size > 0) {
                        JSONObject sysMsgJSON = talkRedisCacheService.getRedisJsonBySystemMsgId(new CustcoId(custcoId, sysMsgId));
                        if(sysMsgJSON != null) {
                            retString = sysMsgJSON.getString("MSG_CN");
                        }
                    }else
                    {
                        log.error("dbSystemMessage size is 0, confirm your DB (TWB_TALK_SYSTEM_MSG)");
                    }

                }
                break;
            }
            default:
            {
                int intMsgSize = dbSystemMessage.size();
                if(intMsgSize > 0) {
                    JSONObject sysMsgJSON = dbSystemMessage.get(new CustcoId(custcoId, sysMsgId));
                    if(sysMsgJSON != null) {
                        retString = sysMsgJSON.getString("MSG_CN");
                    }
                }
                else {
                    log.error("dbSystemMessage size is 0, confirm your DB (TWB_TALK_SYSTEM_MSG)");
                }
            }
        }
        return retString;
    }


    public void clear()
    {
        switch(NOW_CACHE)
        {
            case REDIS:
            {
                TalkRedisCacheServiceImpl talkRedisCacheService = PaletteBeanUtils
                    .getBean(TalkRedisCacheServiceImpl.class);
                talkRedisCacheService.removeAllSystemMessage();
                break;
            }
            default:
            {
                dbSystemMessage.clear();
            }
        }
    }


    public void clear(@NotNull CustcoId custcoId)
    {
        switch(NOW_CACHE)
        {
            case REDIS:
            {
                TalkRedisCacheServiceImpl talkRedisCacheService = PaletteBeanUtils
                    .getBean(TalkRedisCacheServiceImpl.class);
                talkRedisCacheService.removeSystemMessage(custcoId);
                break;
            }
            default:
            {
                dbSystemMessage.clear();
            }
        }
    }
}
