package kr.co.hkcloud.palette3.chat.dashboard.dao;


import java.util.HashMap;
import java.util.List;

import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;


public interface ChatDashboardCounselMapper
{

    List<HashMap<String, Object>> selectRtnWorkTime(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectContactStatus(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectContactStatusInTalkMain(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectRtnInTalk(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectRtnTalkConsulting(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectRtnTalkReady(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectRtnTalkConsultingUser(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectRtnTalkTotData(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectRtnTalkConsultComplete(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectRtnTalkConsultCompleteRate(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectRtnTalkConsultUserList(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectRtnTalkCnslTypeCahrt(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectRtnMonthResultChart(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectRtnWeekUserRank(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectTimeStatsByCounselStatus(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectTimeStatsByCounselDoneStatus(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectUserReadyStatusInTalkMain(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectAvgReadyTimes(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectStackAvgReadyTimes(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectAvgChatTimes(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectStackAvgChatTimes(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectReadyCounselorTotalCnt(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectReadyCounselorCnt(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectCounselCounselorCnt(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectCounselorStatByOnOff(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectCounselStatus(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectInqryTypeStatus(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectAspSenderKeyStatus(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectUserReadyStatus(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectCountByAcceptStatus(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectCountByAcceptInqryStatus(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectCountByReadyStatus(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectCountByCounselStatus(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectInqryAfterProcStatus(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectAcceptList_new(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectAcceptInqryList_new(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectReadyList_new(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectCounselList_new(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectAfterList_new(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectAcceptList(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectAcceptInqryList(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectReadyList(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectCounselList(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectAfterList(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectCountselorList(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> getLastUserChatInfo(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> getUserCustseq(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectATTRCustUser(HashMap<String, Object> inHashMap) throws TelewebDaoException;
}
