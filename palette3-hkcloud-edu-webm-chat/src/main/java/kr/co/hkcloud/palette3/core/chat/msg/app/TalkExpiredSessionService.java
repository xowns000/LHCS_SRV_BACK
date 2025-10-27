package kr.co.hkcloud.palette3.core.chat.msg.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import net.sf.json.JSONObject;


/**
 * 세션 만료 서비스스
 * 
 * @author jangh
 *
 */
public interface TalkExpiredSessionService
{
    void updadateExpiredSessionReady(TelewebJSON objParams) throws TelewebAppException;
    void processExpiredSessionReady(TelewebJSON objParams, String calledApi, JSONObject rcvJson) throws TelewebAppException;
    void processExpiredSessionContract(TelewebJSON objParams, String calledApi, JSONObject rcvJson) throws TelewebAppException;
}
