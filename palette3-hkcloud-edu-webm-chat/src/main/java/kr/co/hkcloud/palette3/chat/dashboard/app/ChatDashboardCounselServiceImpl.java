package kr.co.hkcloud.palette3.chat.dashboard.app;


import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("chatDashboardCounselService")
public class ChatDashboardCounselServiceImpl implements ChatDashboardCounselService
{
    private final TwbComDAO mobjDao;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnInTalk(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectRtnInTalk", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnTalkConsulting(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectRtnTalkConsulting", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnTalkReady(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectRtnTalkReady", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnTalkConsultingUser(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectRtnTalkConsultingUser", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnTalkTotData(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectRtnTalkTotData", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnTalkConsultComplete(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectRtnTalkConsultComplete", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnTalkConsultCompleteRate(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectRtnTalkConsultCompleteRate", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnTalkConsultUserList(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectRtnTalkConsultUserList", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnWeekUserRank(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectRtnWeekUserRank", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectContactStatus(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectContactStatus", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectAvgReadyTimes(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectAvgReadyTimes", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectStackAvgReadyTimes(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectStackAvgReadyTimes", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectAvgChatTimes(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectAvgChatTimes", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectStackAvgChatTimes(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectStackAvgChatTimes", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectReadyCounselorTotalCnt(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectReadyCounselorTotalCnt", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectReadyCounselorCnt(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectReadyCounselorCnt", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectCounselCounselorCnt(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectCounselCounselorCnt", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectCounselorStatByOnOff(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectCounselorStatByOnOff", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectCounselStatus(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectCounselStatus", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectInqryTypeStatus(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectInqryTypeStatus", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectUserReadyStatus(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectUserReadyStatus", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public Map<String, String> getWorkTime(TelewebJSON jsonParams, String strNeedYesterdayYN) throws TelewebAppException
    {
        //반환 값 생성
        Map<String, String> mapReturn = new HashMap<String, String>();

        //조회
        TelewebJSON jsonSelect = new TelewebJSON(jsonParams);
        jsonSelect.setDataObject(jsonParams.getDataObject(TwbCmmnConst.G_DATA));
        jsonSelect.setString("NEED_YESTERDAY_YN", strNeedYesterdayYN);
        TelewebJSON jsonWorkTime = mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectRtnWorkTime", jsonSelect);

        //반환 값 설정
        mapReturn.put("TODAY_WORK_TIME_START", jsonWorkTime.getString("TODAY_WORK_TIME_START"));
        mapReturn.put("TODAY_WORK_TIME_END", jsonWorkTime.getString("TODAY_WORK_TIME_END"));
        if(StringUtils.equals(strNeedYesterdayYN, "Y")) {
            mapReturn.put("YESTERDAY_WORK_TIME_START", jsonWorkTime.getString("YESTERDAY_WORK_TIME_START"));
        }

        return mapReturn;
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectAcceptList_new(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectAcceptList_new", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectCountByAcceptStatus(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectCountByAcceptStatus", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectCountByAcceptInqryStatus(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectCountByAcceptInqryStatus", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectAcceptInqryList_new(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectAcceptInqryList_new", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectReadyList_new(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectReadyList_new", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectCounselList_new(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectCounselList_new", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectAfterList_new(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectAfterList_new", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectTimeStatsByCounselStatus(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectTimeStatsByCounselStatus", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectTimeStatsByCounselDoneStatus(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectTimeStatsByCounselDoneStatus", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectAspSenderKeyStatus(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectAspSenderKeyStatus", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnTalkCnslTypeCahrt(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectRtnTalkCnslTypeCahrt", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnMonthResultChart(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectRtnMonthResultChart", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectCountselorList(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectCountselorList", telewebJSON);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectATTRCustUser(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectATTRCustUser", telewebJSON);
    }


    
    
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectCuttTotStat(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectCuttTotStat", telewebJSON);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectChnCuttTotStat(TelewebJSON telewebJSON) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectChnCuttTotStat", telewebJSON);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectTimeCuttStat(TelewebJSON telewebJSON) throws TelewebAppException
    {
    	TelewebJSON selectData = new TelewebJSON(telewebJSON);
    	selectData.setDataObject("selectAltmntTimeCuttStat", mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectAltmntTimeCuttStat", telewebJSON));
    	selectData.setDataObject("selectEndTimeCuttStat", mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectEndTimeCuttStat", telewebJSON));
        return selectData;
    }
    
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRightCuttStat(TelewebJSON telewebJSON) throws TelewebAppException
    {
    	TelewebJSON selectData = new TelewebJSON(telewebJSON);
    	//배정가능건수
    	selectData.setDataObject("selectAltmntCnt", mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectAltmntCnt", telewebJSON));
    	//현재 배정/총요청/총인입 건수
    	selectData.setDataObject("selectStatCnt", mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectStatCnt", telewebJSON));
    	//상담완료현황
    	selectData.setDataObject("selectEndCnt", mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectEndCnt", telewebJSON));
    	//평균 대기/상담처리 시간
    	//편균건수는 건수가 없으면(NULL) 오류 발생
    	if(telewebJSON.getString("TOT_CNT") != null && !telewebJSON.getString("TOT_CNT").equals("") && !telewebJSON.getString("TOT_CNT").equals("0")) {
    		selectData.setDataObject("selectAvgTime", mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectAvgTime", telewebJSON));
    	}
    	//고객포기건수
    	selectData.setDataObject("selectGiveUpCnt", mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectGiveUpCnt", telewebJSON));
    	//busy(요청불가)건수
    	selectData.setDataObject("selectBusyCnt", mobjDao.select("kr.co.hkcloud.palette3.chat.dashboard.dao.ChatDashboardCounselMapper", "selectBusyCnt", telewebJSON));
    	
    	
    	return selectData;
    }
    
}
