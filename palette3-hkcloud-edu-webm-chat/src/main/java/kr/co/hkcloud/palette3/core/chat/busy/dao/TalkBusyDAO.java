package kr.co.hkcloud.palette3.core.chat.busy.dao;


import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Repository
public class TalkBusyDAO
{
    private final TwbComDAO mobjDao;


    /**
     * 고객의 접수제한 상태 조회 (통과PASS / 접수제한NO_ACPT)
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectAcptLimitByUserKey(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectAcptLimitByUserKey", mjsonParams);

        return objRetParams;
    }


    /**
     * 휴일 체크
     * 
     * @param  workStartTime 상담가능시간(from) - 업무시작시간
     * @param  workEndTime   상담가능시간(to) - 업무종료시간
     * @return               휴일 여부('Y': 휴일, 'N': 비휴일)
     */
    @Transactional(readOnly = true)
    public String selectTalkHoliday(String workStartTime, String workEndTime, String custcoId) throws TelewebDaoException
    {
        TelewebJSON jsonParams = new TelewebJSON();

        //파라메터 설정
        jsonParams.setString("WORK_START_TIME", workStartTime);
        jsonParams.setString("WORK_END_TIME", workEndTime);
        jsonParams.setString("CUSTCO_ID", custcoId);

        //DAO검색 메서드 호출
        TelewebJSON objRetParams = mobjDao.select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectTalkHoliday", jsonParams);

        if(objRetParams.getString("HOLIDAY_YN") == "") {
            String returnN = "N";
            return returnN;
        }

        return objRetParams.getString("HOLIDAY_YN");
    }


    /**
     * 점심시간 체크
     * 
     * @param  workStartTime 상담가능시간(from) - 업무시작시간
     * @param  workEndTime   상담가능시간(to) - 업무종료시간
     * @return               휴일 여부('Y': 휴일, 'N': 비휴일)
     */
    @Transactional(readOnly = true)
    public String selectLunchTime(String workStartTime, String workEndTime, String custcoId) throws TelewebDaoException
    {
        TelewebJSON jsonParams = new TelewebJSON();

        //파라메터 설정
        jsonParams.setString("WORK_START_TIME", workStartTime);
        jsonParams.setString("WORK_END_TIME", workEndTime);
        jsonParams.setString("CUSTCO_ID", custcoId);

        //DAO검색 메서드 호출
        TelewebJSON objParams = mobjDao.select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectLunchTime", jsonParams);

        return objParams.getString("LUNCH_YN");
    }


    /**
     * 고객의 대기상태를 조회한다. (접수RCEPT, 대기READY, 배정ASIGN, 전달DLIV, 그외ELSE)
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectCustomerReadyStat(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectCustomerReadyStat", mjsonParams);

        return objRetParams;
    }


    /**
     * 채팅방의 최근 대화이력 조회
     * 최근 대화이력이 보내려는 시스템 메시지라면 시스템 메시지를 보내지 않도록 함
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRecentMsg(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectRecentMsg", mjsonParams);

        JSONArray recentData = objRetParams.getDataObject();
        if(recentData.size() == 0) {
        	objRetParams.setString("SYS_MSG_ID", null);
        }
        
        return objRetParams;
    }


    /**
     * 동시에 들어온 채팅 조회
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRecentCutt(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectRecentCutt", mjsonParams);

        return objRetParams;
    }
    
}
