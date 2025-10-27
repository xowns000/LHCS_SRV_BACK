package kr.co.hkcloud.palette3.chat.main.app;


import java.io.IOException;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public interface ChatMainService
{
    TelewebJSON processRtnTalkHistInfo(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON updateRtnTalkHistTemp(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON updateRtnTalkHist(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON updateRtnTalkHistByTrans(TelewebJSON jsonParams) throws TelewebAppException;

    //TelewebJSON processRtnTwbTalkReasonTyp(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON getAllText(TelewebJSON jsonParams) throws IOException;

    TelewebJSON getAllTextByDB(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON insertUserInformation(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnChatContent(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON updateRemoveConent(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON parseProhibiteByMessage(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON updateRtnMemoAndAtentCustomer(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON updateRtnChatCustomerInfo(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON chkUserInCustomer(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnGiveupUserReady(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnTalkList(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnTalkSearch(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnTalkSearch4(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnTalkSearch5(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnTalkHistInfo(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnTalkCustInfo(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectBbsParams(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertBbsAnswer(TelewebJSON jsonParams) throws TelewebAppException;

//    TelewebJSON syncChatStatus(TelewebJSON jsonParams) throws TelewebAppException;

//    TelewebJSON checkReadyTimeout(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectPrivateHist(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON getGreetingMessage(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectTalkStateProcessByUser(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON updateSystemMsgSkip(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON getChatInfo(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON searchScripts(TelewebJSON jsonParams) throws TelewebAppException;

    //TelewebJSON searchUntacts(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnTalkInqryInfo(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON insertTalkReadyOff(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON existScriptCommand(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON insertRtnScriptMng(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON updateRtnScriptMng(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON deleteRtnScriptMng(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON insertSystemMsgSkip(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON checkReadyTimeout(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON isReadyUser(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON getScript(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON insertUserScriptShortKey(TelewebJSON jsonParams, JSONArray commands) throws TelewebAppException;

    TelewebJSON deleteUserScriptShortKey(TelewebJSON jsonParams, JSONArray commands) throws TelewebAppException;

    TelewebJSON updateIsReadTalkAll(TelewebJSON jsonParams) throws TelewebAppException;	//20201028 추가

    TelewebJSON updateIsReadTalk(TelewebJSON jsonParams) throws TelewebAppException;	//20201028 추가

    TelewebJSON selectTalkStateDashboard(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON updateRtnCustInfoEai(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectUntactUrlSendCnt(TelewebJSON jsonParams) throws TelewebAppException;

    JSONObject getLastCustAtent(JSONArray jsonArray) throws TelewebAppException;

    TelewebJSON selectUserSta(TelewebJSON jsonParams) throws TelewebAppException;	//20210119 추가

    TelewebJSON selectRtnUserNm(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON getImgSrc(TelewebJSON jsonParams) throws IOException;               //20211202추가

    TelewebJSON getSenderKey(TelewebJSON jsonParams) throws IOException;               //20211202추가

    TelewebJSON regiMarkUp(TelewebJSON jsonParams) throws IOException;               //20220621추가

    TelewebJSON getChatYN(TelewebJSON jsonParams) throws IOException;               //20220702추가

    TelewebJSON getSender(TelewebJSON jsonParams) throws IOException;               //20220830추가

    TelewebJSON getUuid(TelewebJSON jsonParams) throws IOException;               //20220901추가

    TelewebJSON chatCnslForceRegist(TelewebJSON jsonParams) throws IOException;   //20230120추가

    TelewebJSON chatCnslCount(TelewebJSON jsonParams) throws IOException;   //20230120추가

    TelewebJSON selectNowCutt(TelewebJSON jsonParams) throws IOException;   //20230120추가

    TelewebJSON selectCuttType(TelewebJSON jsonParams) throws IOException;   //20230120추가

    TelewebJSON insertClbkRdy(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON getCuttDetailList(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectPstQstn(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON getChtStng(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON getTripleCutt(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON getChbtData(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON getChbtUserData(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON getChbtUser(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON getChbtUserHsty(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON chkInputPsblty(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON cuttExpsnAttrReg(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON cuttExpsnAttrMerge(TelewebJSON jsonParams) throws TelewebAppException;
}
