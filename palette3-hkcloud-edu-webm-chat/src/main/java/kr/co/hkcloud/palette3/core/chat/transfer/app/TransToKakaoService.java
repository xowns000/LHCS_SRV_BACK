package kr.co.hkcloud.palette3.core.chat.transfer.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.chat.transfer.domain.TransToEndTalkEvent;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import net.sf.json.JSONObject;


/**
 * 
 * @author Orange
 *
 */
public interface TransToKakaoService
{
    JSONObject transToKakao(String active, JSONObject writeData, String callTypCd) throws TelewebAppException;

    JSONObject transToEndTalkEvent(final TransToEndTalkEvent endTalkEvent) throws TelewebAppException;

    void sendSystemMsg(String userKey, String senderKey, String message, String strSystemMsgId, String callTypCd, String custcoId) throws TelewebAppException;

    void noSendSystemMsg(String userKey, String senderKey, String message, String callTypCd) throws TelewebAppException;

    void sendCustNoResponseSystemMsg(String userKey, String senderKey, TelewebJSON messageJson, String strSystemMsgId, String callTypCd, String custcoId) throws TelewebAppException;

    void sendSystemMsg(String userKey, String senderKey, TelewebJSON messageJson, String callTypCd) throws TelewebAppException;

    void sendSystemButtonMsgReadyDetail(String userKey, String senderKey, String message, String strlinks, String strSystemMsgId, String callTypCd, String custcoId) throws TelewebAppException;

    void sendSystemButtonLinkMsgReadyDetail(String userKey, String senderKey, String message, String strlinks, String strSystemMsgId, String callTypCd, String custcoId, JSONObject param) throws TelewebAppException;

    void sendSystemButtonMsgContactDetail(String userKey, String senderKey, TelewebJSON messageJson, String strlinks, String strSystemMsgId, String callTypCd, String custcoId) throws TelewebAppException;

    void sendAutoSystemButtonMsgReadyDetail(String userKey, String senderKey, String message, String strSystemMsgId, String callTypCd, String custcoId) throws TelewebAppException;

    void sendInqryLevelTypeBtnMsg(String userKey, String senderKey, String message, JSONObject rcvJson, String callTypCd) throws TelewebAppException;

    void endWithBot(String userKey, String senderKey, String bot_event) throws TelewebAppException;

    void sendWorktimeMsg(String userKey, String senderKey, TelewebJSON messageJson, String callTypCd) throws TelewebAppException;

    void sendCallbackYnMsg(String userKey, String senderKey, String callbackYn, String callTypCd) throws TelewebAppException;

    boolean chatbotYn(String senderKey) throws TelewebAppException;

    boolean custChtIng(String userKey, String senderKey) throws TelewebAppException;

    boolean custChtbot(String userKey, String senderKey) throws TelewebAppException;

    void insertKakaoChatbotConents(JSONObject contents) throws TelewebAppException;
    
    int chbtCnt(String userKey, String senderKey) throws TelewebAppException;

    void deleteChbtDupl(String userKey, String senderKey) throws TelewebAppException;
    
    

}
