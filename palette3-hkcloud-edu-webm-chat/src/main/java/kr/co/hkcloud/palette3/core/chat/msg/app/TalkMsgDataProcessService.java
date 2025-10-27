package kr.co.hkcloud.palette3.core.chat.msg.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 
 * @author jangh
 *
 */
public interface TalkMsgDataProcessService
{
//    void processExpiredSession(TelewebJSON objParams, String calledApi, JSONObject rcvJson) throws TelewebAppException;
//    TelewebJSON insertCntRcvMsg(TelewebJSON mjsonParams) throws TelewebAppException;
    TelewebJSON insertSndMsg(TelewebJSON mjsonParams) throws TelewebAppException;
    TelewebJSON updateCustNoRespIs0(TelewebJSON mjsonParams) throws TelewebAppException;
//    TelewebJSON selectTalkContactId(TelewebJSON mjsonParams) throws TelewebAppException;
//    TelewebJSON selectTalkUserReady(TelewebJSON mjsonParams) throws TelewebAppException;
//    TelewebJSON updateTalkContactPost(TelewebJSON mjsonParams) throws TelewebAppException;
    TelewebJSON updateTalkContactStatusCd(TelewebJSON mjsonParams) throws TelewebAppException;
    TelewebJSON selectAgentNoResponseTalkContactId(TelewebJSON mjsonParams) throws TelewebAppException;
}
