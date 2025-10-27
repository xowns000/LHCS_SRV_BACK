package kr.co.hkcloud.palette3.chat.dashboard.app;


import java.util.Map;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface ChatDashboardCounselService
{
    TelewebJSON selectRtnInTalk(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectRtnTalkConsulting(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectRtnTalkReady(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectRtnTalkConsultingUser(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectRtnTalkTotData(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectRtnTalkConsultComplete(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectRtnTalkConsultCompleteRate(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectRtnTalkConsultUserList(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectRtnWeekUserRank(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectContactStatus(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectAvgReadyTimes(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectStackAvgReadyTimes(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectAvgChatTimes(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectStackAvgChatTimes(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectReadyCounselorTotalCnt(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectReadyCounselorCnt(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectCounselCounselorCnt(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectCounselorStatByOnOff(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectCounselStatus(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectInqryTypeStatus(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectUserReadyStatus(TelewebJSON telewebJSON) throws TelewebAppException;
    Map<String, String> getWorkTime(TelewebJSON jsonParams, String strNeedYesterdayYN) throws TelewebAppException;
    TelewebJSON selectAcceptList_new(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectCountByAcceptStatus(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectCountByAcceptInqryStatus(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectAcceptInqryList_new(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectReadyList_new(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectCounselList_new(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectAfterList_new(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectTimeStatsByCounselStatus(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectTimeStatsByCounselDoneStatus(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectAspSenderKeyStatus(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectRtnTalkCnslTypeCahrt(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectRtnMonthResultChart(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectCountselorList(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectATTRCustUser(TelewebJSON telewebJSON) throws TelewebAppException;
    
    TelewebJSON selectCuttTotStat(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectChnCuttTotStat(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectTimeCuttStat(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectRightCuttStat(TelewebJSON telewebJSON) throws TelewebAppException;
    
}
