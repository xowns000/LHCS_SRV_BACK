package kr.co.hkcloud.palette3.core.chat.busy.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.chat.busy.domain.TalkBusyVO;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import net.sf.json.JSONObject;


/**
 * 텔레톡 BUSY(상담불가) 서비스
 * 
 * @author User
 */
public interface TalkBusyService
{
    boolean checkBusy() throws TelewebAppException;
    boolean isChatDisable(String userKey, String senderKey, String callTypCd, String custcoId, boolean isShowMsg) throws TelewebAppException;
    void processBusyUserReadyToContact(TalkBusyVO talkBusyVO, String custcoId) throws TelewebAppException;
    void processBusyUserReadyToContactByTalkUserKey(String talkUserkey, TalkBusyVO talkBusyVO, String custcoId, boolean isShowMsg) throws TelewebAppException;
    void processData(TelewebJSON inJson) throws TelewebAppException;
    void processDataByreceiver(TelewebJSON inJson) throws TelewebAppException;
    void transUserReadyToContact(TelewebJSON sysMsg, JSONObject talkBusyUserReadyList, boolean isShowMsg) throws TelewebAppException;
    TelewebJSON selectBatchTenantList() throws TelewebAppException;
}
