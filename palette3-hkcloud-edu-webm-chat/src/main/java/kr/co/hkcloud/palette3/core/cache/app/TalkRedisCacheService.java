package kr.co.hkcloud.palette3.core.cache.app;


import java.util.Set;

import javax.validation.constraints.NotNull;

import kr.co.hkcloud.palette3.core.chat.router.domain.CustcoId;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import net.sf.json.JSONObject;


/**
 * 
 * @author Orange
 *
 */
public interface TalkRedisCacheService
{
    Set<CustcoId> keysDbEnv() throws TelewebAppException;
    Long getSizeDbEnv() throws TelewebAppException;
    void setRedisDbEnvironment(CustcoId envCustcoId, String envValue) throws TelewebAppException;
    String getRedisStringByDbEnv(CustcoId envCustcoId) throws TelewebAppException;

    Set<CustcoId> keysSystemMessage() throws TelewebAppException;
    Long getSizeSystemMessage() throws TelewebAppException;
    void setRedisSystemMessage(CustcoId custcoId, JSONObject sysMsgJSON) throws TelewebAppException;
    JSONObject getRedisJsonBySystemMsgId(CustcoId custcoId) throws TelewebAppException;

    void removeAllSystemMessage() throws TelewebAppException;
    void removeSystemMessage(@NotNull CustcoId custcoId) throws TelewebAppException;
}
