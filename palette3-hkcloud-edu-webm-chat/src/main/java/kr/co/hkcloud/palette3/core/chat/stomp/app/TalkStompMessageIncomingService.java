package kr.co.hkcloud.palette3.core.chat.stomp.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 
 * @author Orange
 *
 */
public interface TalkStompMessageIncomingService
{
    void messageIncoming(String userKey, String calledApi, String request, TelewebJSON objParams) throws TelewebAppException;
    int chkValidationData(TelewebJSON jsonParams) throws TelewebAppException;
    void sendToAgentMessageError(String userKey, int rtnCode, String userId, String custcoId) throws TelewebAppException;
}
