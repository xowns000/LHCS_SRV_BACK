package kr.co.hkcloud.palette3.core.chat.router.app;


import kr.co.hkcloud.palette3.common.chat.domain.OrgContentVO;
import kr.co.hkcloud.palette3.common.chat.domain.OrgFileVO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public interface TalkDataProcessService
{
//    void deleteTalkInit() throws TelewebAppException;

//    void processInfoMsg(JSONObject talkInfoMsg) throws TelewebAppException;

//    void processCustNoResponseMsg(JSONObject talkInfoMsg) throws TelewebAppException;

//    void processInqryTypeNoContact(JSONObject talkInfoMsg) throws TelewebAppException;

//    TelewebJSON selectInqryTypeNoContact() throws TelewebAppException;

    TelewebJSON insertTalkContactDetail(TelewebJSON mjsonParams) throws TelewebAppException;

//    void processTalkIngLast(TelewebJSON inJson) throws TelewebAppException;

    void processTalkIng(TelewebJSON inJson) throws TelewebAppException;

//    void processTalkStackIng(TelewebJSON inJson) throws TelewebAppException;

//    void processTalkStackIng(TalkWebsocketChatConsultServerEndpoint talkWebsocketChatConsultServerEndpoint, TelewebJSON inJson) throws TelewebAppException;

    String processTransTalkReady(TelewebJSON requestJson) throws TelewebAppException;
    
//    TelewebJSON selectRtnTalkHistInfo(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON processInsertTalkReady(TelewebJSON inJson) throws TelewebAppException;

    TelewebJSON processInsertChatbotReady(TelewebJSON inJson) throws TelewebAppException;

    TelewebJSON processInqryType(TelewebJSON msgInfoJson, TelewebJSON inJson, JSONObject rcvJson) throws TelewebAppException;

    void processTalkReady(TelewebJSON inJson) throws TelewebAppException;

    void processDeleteTalkReady(TelewebJSON inJson) throws TelewebAppException;

//    TelewebJSON insertTalkReady(TelewebJSON mjsonParams) throws TelewebAppException;

//    TelewebJSON deleteTalkReady(TelewebJSON mjsonParams) throws TelewebAppException;

//    TelewebJSON deleteAllTalkReady(TelewebJSON mjsonParams) throws TelewebAppException;

//    TelewebJSON insertTalkIng(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON deleteTalkIng(TelewebJSON mjsonParams) throws TelewebAppException;

//    TelewebJSON deleteAllTalkIng(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON mergeTalkLast(TelewebJSON mjsonParams) throws TelewebAppException;

//    TelewebJSON selectCntTalkUserReady(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON selectCntTalkUserReadyKey(TelewebJSON mjsonParams) throws TelewebAppException;

//    TelewebJSON selectAcptLimitByUserKey(TelewebJSON mjsonParams) throws TelewebAppException;

//    TelewebJSON selectCustomerReadyStat(TelewebJSON mjsonParams) throws TelewebAppException;

//    TelewebJSON insertTalkUserReady(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON updateTalkUserReadyRouting(TelewebJSON mjsonParams) throws TelewebAppException;

//    TelewebJSON insertTalkUserReadyDetail(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON mergeTalkStack(TelewebJSON mjsonParams) throws TelewebAppException;

//    TelewebJSON deleteTalkStack(TelewebJSON mjsonParams) throws TelewebAppException;

//    TelewebJSON selectTalkUserReadyAgent(TelewebJSON mjsonParams) throws TelewebAppException;

//    TelewebJSON selectTalkUserReadyAgentNot11(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON selectTalkUserReadyInfoByWcnct(TelewebJSON mjsonParams) throws TelewebAppException;

//    TelewebJSON selectTalkUserReadyInfo(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON selectTalkUserInfo(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON selectTalkUserCustSeq(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON selectTalkRouteToAgent(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON selectTalkRouteToSpecAgent(TelewebJSON mjsonParams) throws TelewebAppException;

//    TelewebJSON selectTalkReadyUserId(TelewebJSON mjsonParams) throws TelewebAppException;

//    TelewebJSON selectTalkUserReadyCode(TelewebJSON mjsonParams) throws TelewebAppException;

//    TelewebJSON selectTalkUserReady(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON updateTalkUserInqryCd(TelewebJSON mjsonParams) throws TelewebAppException;

//    TelewebJSON updateTalkUserReadyInqryCd(TelewebJSON mjsonParams) throws TelewebAppException;

//    TelewebJSON updateTalkUserReady10(TelewebJSON mjsonParams) throws TelewebAppException;

//    TelewebJSON updateTalkUserReady(TelewebJSON mjsonParams) throws TelewebAppException;

//    String getSeqNo(String strBizCase) throws TelewebAppException;

//    String selectTalkHoliday(String workStartTime, String workEndTime) throws TelewebAppException;

//    TelewebJSON selectTalkEnv() throws TelewebAppException;

//    TelewebJSON selectProhibiteWords() throws TelewebAppException;

//    TelewebJSON selectSystemMessage() throws TelewebAppException;

//    TelewebJSON updateTalkContactStat(TelewebJSON mjsonParams) throws TelewebAppException;

//    TelewebJSON selectInfoMsg() throws TelewebAppException;

//    TelewebJSON updateTalkSetTime(TelewebJSON mjsonParams) throws TelewebAppException;

//    TelewebJSON selectCustNoResponseMsg() throws TelewebAppException;

//    TelewebJSON updateCustNoResponeTime(TelewebJSON mjsonParams) throws TelewebAppException;

//    void chatEndAfterLastCustNoResponse(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON selectInqryLevelType(JSONObject rcvJson) throws TelewebAppException;

    TelewebJSON selectInqryCode(JSONObject rcvJson, String InqryCode) throws TelewebAppException;

    TelewebJSON selectInqryExistAlone(String InqryCode, JSONObject rcvJson) throws TelewebAppException;

    TelewebJSON selectInqryTypeIsAgentOrMessage(String InqryCode, JSONObject rcvJson) throws TelewebAppException;

    TelewebJSON selectLinksButtonList(String strSystemMsgId, String strCustcoId) throws TelewebAppException;

    boolean isExistInqryType(JSONObject rcvJson) throws TelewebAppException;

    boolean isInqryFinalLevel(JSONObject rcvJson) throws TelewebAppException;

    JSONArray selectInqryTypeIsMessage(JSONObject rcvJson) throws TelewebAppException;

    boolean isInqryBeforeBtn(JSONObject rcvJson) throws TelewebAppException;

    TelewebJSON selectTalkContactAutoGreeting(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON updateTalkContactAutoGreetingY(TelewebJSON mjsonParams) throws TelewebAppException;

//    TelewebJSON processMergeTalkReady(TelewebJSON inJson) throws TelewebAppException;

//    TelewebJSON mergeTalkUserReadyDetail(TelewebJSON mjsonParams) throws TelewebAppException;

//    void talkReadyTimeUpdate(TelewebJSON msgJson) throws TelewebAppException;

//    void inqryTypeNoContactByClose(JSONObject jsonParams) throws TelewebAppException;

//    void delayByClose(JSONObject jsonParams) throws TelewebAppException;

//    void registerReadyOffHist(TelewebJSON mjsonParams) throws TelewebAppException;

    void updateTalkUserReadyInqryCdIsThisCode(TelewebJSON mjsonParams) throws TelewebAppException;

    void processTalkReadyToTalkContact(TelewebJSON mjsonParams) throws TelewebAppException;

//    TelewebJSON readyTalkRouteToAgent(TelewebJSON mjsonParams) throws TelewebAppException;

//    TelewebJSON readyTalkRouteToSpecAgent(TelewebJSON mjsonParams) throws TelewebAppException;

//    TelewebJSON processRoutingToAgent(JSONObject talkInfoMsg) throws TelewebAppException;

//    void processTransToAgent(String chkUserId) throws TelewebAppException;

    void insertOrgContent(OrgContentVO orgContentVO) throws TelewebAppException;

    void insertOrgFile(OrgFileVO orgFileVO) throws TelewebAppException;

    TelewebJSON selectNowCutt(TelewebJSON mjsonParams) throws TelewebAppException;
    
    TelewebJSON processInputInfoMsg(TelewebJSON mjsonParams) throws TelewebAppException;
    
    TelewebJSON processRsvt(TelewebJSON mjsonParams) throws TelewebAppException;
    
    TelewebJSON processGetRsvtPsbltyYn(TelewebJSON mjsonParams) throws TelewebAppException;
    
    TelewebJSON processGetRsvtPsbltyArr(TelewebJSON mjsonParams) throws TelewebAppException;
    
    String getSndrKey(String uuid) throws TelewebAppException;

    TelewebJSON processGetRsvtPsbltyDate(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON postExtApi(String param) throws TelewebAppException;
}
