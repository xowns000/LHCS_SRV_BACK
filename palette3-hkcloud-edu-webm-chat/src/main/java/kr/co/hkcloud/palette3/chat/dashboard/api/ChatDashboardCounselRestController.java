package kr.co.hkcloud.palette3.chat.dashboard.api;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.chat.dashboard.app.ChatDashboardCounselService;
import kr.co.hkcloud.palette3.chat.dashboard.util.ChatDashboardCounselValidator;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.NoBizLog;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbEnvironment;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "ChatDashboardCounselRestController",
     description = "채팅대시보드상담(구 채팅상담모니터링) REST 컨트롤러")
public class ChatDashboardCounselRestController
{
    private final ChatDashboardCounselService   chatDashboardCounselService;
    private final ChatDashboardCounselValidator chatDashboardCounselValidator;


    /**
     * 상담모니터링 조회
     * 
     * @param  mjsonParams
     * @param  result
     * @return                     TelewebJSON 형식의 처리 결과 데이터
     * @throws TelewebApiException
     */
    @NoBizLog
    @ApiOperation(value = "채팅대시보드상담-조회(구 채팅상담모니터링)",
                  notes = "채팅대시보드상담-조회(구 채팅상담모니터링)")
    @PostMapping("/chat-api/dashboard/counsel/inqire")
    public Object selectRtnMonitoring(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        //필수객체정의
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);    //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.

        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        //Validation 체크 
        chatDashboardCounselValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        TelewebJSON objTempParams = chatDashboardCounselService.selectRtnInTalk(jsonParams);
        objRetParams.setString("IN_CNT", objTempParams.getString("CNT"));

        objTempParams = chatDashboardCounselService.selectRtnTalkConsulting(jsonParams);
        objRetParams.setString("CONSULTING_CNT", objTempParams.getString("CNT"));

        objTempParams = chatDashboardCounselService.selectRtnTalkReady(jsonParams);
        objRetParams.setString("READY_CNT", objTempParams.getString("CNT"));

        objTempParams = chatDashboardCounselService.selectRtnTalkConsultingUser(jsonParams);
        objRetParams.setString("USER_CNT", objTempParams.getString("CNT"));

        objTempParams = chatDashboardCounselService.selectRtnTalkTotData(jsonParams);
        objRetParams.setString("TOT_CNT", objTempParams.getString("CNT"));

        objTempParams = chatDashboardCounselService.selectRtnTalkConsultComplete(jsonParams);
        objRetParams.setString("COMPLETE_CNT", objTempParams.getString("CNT"));

        objTempParams = chatDashboardCounselService.selectRtnTalkConsultCompleteRate(jsonParams);
        if(objTempParams.getHeaderInt("TOT_COUNT") > 0) {
            objRetParams.setString("COMPLETE_RATE", objTempParams.getString("RATIO"));
        }
        else {
            objRetParams.setString("COMPLETE_RATE", "0");
        }

        //상담원 현황 그리드 
        objTempParams = chatDashboardCounselService.selectRtnTalkConsultUserList(jsonParams);

        objRetParams.setDataObject("LIST", objTempParams.getDataObject());

        //상담원별 주간 랭킹
        objTempParams = chatDashboardCounselService.selectRtnWeekUserRank(jsonParams);
        objRetParams.setDataObject("RANK", objTempParams.getDataObject());

        objRetParams.setHeader("ERROR_FLAG", false);

        return objRetParams;    //최종결과값 반환
    }


    /**
     * 
     * @return TelewebJSON 형식의 처리 결과 데이터
     * @since  2018.05.09
     * @author kang myoung gu
     * 
     */
    @ApiOperation(value = "채팅대시보드상담-대쉬보드데이터조회(구 채팅상담모니터링)",
                  notes = "채팅대시보드상담-대쉬보드데이터조회(구 채팅상담모니터링)")
    @PostMapping("/chat-api/dashboard/counsel/dashboard-data/inqire")
    public Object selectMonitoringData(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        //        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");    //client에서 전송받은 파라메터 생성
        //필수객체정의
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);    //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        //Validation 체크 
        chatDashboardCounselValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        jsonParams.setDataObject("DATA", mjsonParams.getDataObject("DATA"));

        //근무 시작/종료 시간을 반환한다.
        Map<String, String> mapTwbTalkStat = chatDashboardCounselService.getWorkTime(mjsonParams, "N");
        jsonParams.setString("TODAY_WORK_TIME_START", mapTwbTalkStat.get("TODAY_WORK_TIME_START"));
        jsonParams.setString("TODAY_WORK_TIME_END", mapTwbTalkStat.get("TODAY_WORK_TIME_END"));

        //상담상태 조회
        objRetParams.setDataObject("CONTACT_STATUS", chatDashboardCounselService.selectContactStatus(jsonParams));

        TelewebJSON objTmpData = null;

        // 평균 대기 시간
        //      TelewebJSON objTmpData = chatDashboardCounselService.selectAvgReadyTimes(jsonParams);
        //      objRetParams.setString("AVG_READYTIMES", objTmpData.getString("AVGDELAYTIME"));

        // 평균 누적 대기 시간
        //      objTmpData = chatDashboardCounselService.selectStackAvgReadyTimes(jsonParams);
        //      objRetParams.setString("STACKAVG_READYTIMES", objTmpData.getString("STACKAVGDELAYTIME"));

        // 평균 채팅 시간
        //20190520 ojw: not used
        //      objTmpData = chatDashboardCounselService.selectAvgChatTimes(jsonParams);
        //      objRetParams.setString("AVG_CHATTIMES", objTmpData.getString("CHATTIMES"));

        // 평균 누적 채팅 시간
        //20190520 ojw: not used
        //      objTmpData = chatDashboardCounselService.selectStackAvgChatTimes(jsonParams);
        //      objRetParams.setString("STACKAVG_CHATTIMES", objTmpData.getString("STACKCHATTIMES"));

        //채팅 ON인 상담사 건수
        //      READY_COUNSELOR_TOTALCNT
        objTmpData = chatDashboardCounselService.selectReadyCounselorTotalCnt(jsonParams);
        objRetParams.setString("READY_COUNSELOR_TOTALCNT", objTmpData.getString("READY_COUNSELOR_TOTALCNT"));

        //대기중 상담사 건수
        //      READY_COUNSELOR_CNT
        objTmpData = chatDashboardCounselService.selectReadyCounselorCnt(jsonParams);
        objRetParams.setString("READY_COUNSELOR_CNT", objTmpData.getString("READY_COUNSELOR_CNT"));

        //상담중인 상담사 건수
        //COUNSEL_COUNSELOR_CNT
        objTmpData = chatDashboardCounselService.selectCounselCounselorCnt(jsonParams);
        objRetParams.setString("COUNSEL_COUNSELOR_CNT", objTmpData.getString("COUNSEL_COUNSELOR_CNT"));

        //상담사 상태
        //      objTmpData = chatDashboardCounselService.selectCounselorStatByOnOff(jsonParams);
        //      objRetParams.setDataObject("COUNSELOR_STATUS_LIST", objTmpData.getDataObject());

        // 전체 상담현황 
        //20190520 ojw: not used
        //      objTmpData = chatDashboardCounselService.selectCounselStatus(jsonParams);
        //      objRetParams.setDataObject("COUNSEL_STATUS_LIST", objTmpData.getDataObject());

        // 문의 유형 현황
        //20190520 ojw: not used
        //      objTmpData = chatDashboardCounselService.selectAvgReadyTimes(jsonParams);
        //      objRetParams.setDataObject("INQRY_STATUS_LIST", objTmpData.getDataObject());

        /*
         * 20190520 ojw: not used //접수건_문의유형 선택 전 objTmpData = mobjDao.select("com.hcteletalk.teletalk.monitoring.dao.TalkMonitoringMapper", "selectCountByAcceptStatus", jsonParams); objRetParams.setString("ACCEPT_CNT",
         * objTmpData.getString("CNT")); //접수건_문의유형 선택 후 objTmpData = mobjDao.select("com.hcteletalk.teletalk.monitoring.dao.TalkMonitoringMapper", "selectCountByAcceptInqryStatus", jsonParams);
         * objRetParams.setString("ACCEPTINQRY_CNT", objTmpData.getString("CNT")); //대기건 objTmpData = mobjDao.select("com.hcteletalk.teletalk.monitoring.dao.TalkMonitoringMapper", "selectCountByReadyStatus", jsonParams);
         * objRetParams.setString("READY_CNT", objTmpData.getString("CNT")); //상담건 objTmpData = mobjDao.select("com.hcteletalk.teletalk.monitoring.dao.TalkMonitoringMapper", "selectCountByCounselStatus", jsonParams);
         * objRetParams.setString("COUNSEL_CNT", objTmpData.getString("CNT")); //후처리건 objTmpData = mobjDao.select("com.hcteletalk.teletalk.monitoring.dao.TalkMonitoringMapper", "selectInqryAfterProcStatus", jsonParams);
         * objRetParams.setString("AFTER_PROC_CNT", objTmpData.getString("CNT"));
         */
        //고객대기상태 조회
        objRetParams.setDataObject("USER_READY_STATUS", chatDashboardCounselService.selectUserReadyStatus(jsonParams));

        objRetParams.setHeader("ERROR_FLAG", false);

        return objRetParams;
    }


    /**
     * 
     * @return TelewebJSON 형식의 처리 결과 데이터
     * @since  2018.05.09
     * @author kang myoung gu
     * 
     */
    @ApiOperation(value = "채팅대시보드상담-대쉬보드테이블조회(구 채팅상담모니터링)",
                  notes = "DashBoard Table Data (고객접수, 고객대기, 고객 상담중, 후처리 상세 리스트)")
    @PostMapping("/chat-api/dashboard/counsel/dashboard-table-data/inqire")
    public Object selectMonitoringTableData(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        //        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");    //client에서 전송받은 파라메터 생성
        //필수객체정의
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);    //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        //Validation 체크 
        chatDashboardCounselValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        jsonParams.setDataObject("DATA", mjsonParams.getDataObject("DATA"));

        //근무 시작/종료 시간을 반환한다.
        Map<String, String> mapTwbTalkStat = chatDashboardCounselService.getWorkTime(mjsonParams, "N");
        jsonParams.setString("TODAY_WORK_TIME_START", mapTwbTalkStat.get("TODAY_WORK_TIME_START"));
        jsonParams.setString("TODAY_WORK_TIME_END", mapTwbTalkStat.get("TODAY_WORK_TIME_END"));

        TelewebJSON telewebJson = new TelewebJSON();

        // client에서 넘어온 타입에 따라 쿼리가 틀려진다.
        switch(jsonParams.getString("TYPE"))
        {
            case "accept":
                //              telewebJson = mobjDao.select("com.hcteletalk.teletalk.monitoring.dao.TalkMonitoringMapper", "selectAcceptList", jsonParams);
                telewebJson = chatDashboardCounselService.selectAcceptList_new(jsonParams);
                //JSONObject obj = new JSONObject();

                // 접수 문의유형 선택 전
                //TelewebJSON objTmpData = chatDashboardCounselService.selectCountByAcceptStatus(jsonParams);
                //obj.put("ACCEPT_CNT", objTmpData.getString("CNT"));

                // 접수 문의유형 선택 후
                //objTmpData = chatDashboardCounselService.selectCountByAcceptInqryStatus(jsonParams);
                //obj.put("ACCEPTINQRY_CNT", objTmpData.getString("CNT"));

                //telewebJson.setDataObject("ACCEPT_CNTS", obj);
                break;
            case "acceptinqry":
                telewebJson = chatDashboardCounselService.selectAcceptInqryList_new(jsonParams);
                break;
            case "ready":
                telewebJson = chatDashboardCounselService.selectReadyList_new(jsonParams);
                break;
            case "counsel":
                telewebJson = chatDashboardCounselService.selectCounselList_new(jsonParams);
                break;
            case "after":
                telewebJson = chatDashboardCounselService.selectAfterList_new(jsonParams);
                break;
        }

        objRetParams.setDataObject("DATA", telewebJson.getDataObject("DATA"));
        objRetParams.setDataObject("CNT_DATA", telewebJson.getDataObject("ACCEPT_CNTS"));

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @param  result
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     * 
     */
    @NoBizLog
    @ApiOperation(value = "채팅대시보드상담-시간대별접수현황조회(구 채팅상담모니터링)",
                  notes = "시간대별접수현황 차트 데이터 조회")
    @PostMapping("/chat-api/dashboard/counsel/rcept-sttus-by-time/inqire")
    public Object selectTimeStatsByCounselStatus(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //Validation 체크 
        chatDashboardCounselValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        //근무 시작/종료 시간을 반환한다.
        Map<String, String> mapTwbTalkStat = chatDashboardCounselService.getWorkTime(mjsonParams, "N");
        mjsonParams.setString("TODAY_WORK_TIME_START", mapTwbTalkStat.get("TODAY_WORK_TIME_START"));
        mjsonParams.setString("TODAY_WORK_TIME_END", mapTwbTalkStat.get("TODAY_WORK_TIME_END"));

        //HYG 20190903 :: 상담가능시간 가져옴
        //HYG :: 상담모니터링 > 시간대별 접수현황 시간대가 상담가능시간대를 바라보게끔 수정
        String workStartTime = HcTeletalkDbEnvironment.getInstance().getString(((JSONObject) (mjsonParams.getDataObject().get(0))).getString("ASP_NEWCUST_KEY"), "WORK_START_TIME");
        String workEndTime = HcTeletalkDbEnvironment.getInstance().getString(((JSONObject) (mjsonParams.getDataObject().get(0))).getString("ASP_NEWCUST_KEY"), "WORK_END_TIME");
        mjsonParams.setString("WORK_START_TIME", workStartTime);
        mjsonParams.setString("WORK_END_TIME", workEndTime);

        //플러스친구 체크박스 데이터 배열로 설정
        List<String> arrCheckAspSenderKey = new LinkedList<String>();
        JSONObject objData = mjsonParams.getDataObject(TwbCmmnConst.G_DATA).getJSONObject(0);
        @SuppressWarnings("rawtypes")
        Iterator it = objData.keys();
        log.debug("*********11111111111******************" + mjsonParams);
        log.debug("*********22222222222******************" + objData);
        while(it.hasNext()) {
            String strKey = (String) it.next();
            String strValue = objData.getString(strKey);
            log.debug("********3333333333333*******************" + strKey);

            if(strKey.indexOf("CHECK_SNDR_KEY_") > -1 && StringUtils.isNotEmpty(strValue)) {
                arrCheckAspSenderKey.add(strValue);
                log.debug("*****44444444444444**********************" + arrCheckAspSenderKey);
            }
        }
        if(arrCheckAspSenderKey.size() != 0) {
            mjsonParams.setObject("arrCheckAspSenderKey", 0, arrCheckAspSenderKey);
        }

        System.out.println("@!@#!@#!@#!#!@#!@#" + mjsonParams);
        log.debug("***************************" + mjsonParams);

        objRetParams = chatDashboardCounselService.selectTimeStatsByCounselStatus(mjsonParams);

        return objRetParams;
    }


    /**
     * 시간대별 완료 현황 차트 조회
     * 
     * @param  mjsonParams
     * @param  result
     * @return
     * @throws TelewebApiException
     */
    @NoBizLog
    @ApiOperation(value = "'시간대별 완료 현황' 차트 데이터 조회",
                  notes = "'시간대별 완료 현황' 차트 데이터 조회")
    @PostMapping("/chat-api/dashboard/counsel/compt-sttus-by-time/inqire")
    public Object selectTimeStatsByCounselDoneStatus(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        //        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //Validation 체크 
        chatDashboardCounselValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        //근무 시작/종료 시간을 반환한다.
        Map<String, String> mapTwbTalkStat = chatDashboardCounselService.getWorkTime(mjsonParams, "N");
        mjsonParams.setString("TODAY_WORK_TIME_START", mapTwbTalkStat.get("TODAY_WORK_TIME_START"));
        mjsonParams.setString("TODAY_WORK_TIME_END", mapTwbTalkStat.get("TODAY_WORK_TIME_END"));

        //상담모니터링 > 시간대별 완료현황 시간대가 상담가능시간대를 바라보게끔 수정
        String workStartTime = HcTeletalkDbEnvironment.getInstance().getString(((JSONObject) (mjsonParams.getDataObject().get(0))).getString("ASP_NEWCUST_KEY"), "WORK_START_TIME");
        String workEndTime = HcTeletalkDbEnvironment.getInstance().getString(((JSONObject) (mjsonParams.getDataObject().get(0))).getString("ASP_NEWCUST_KEY"), "WORK_END_TIME");
        mjsonParams.setString("WORK_START_TIME", workStartTime);
        mjsonParams.setString("WORK_END_TIME", workEndTime);

        //플러스친구 체크박스 데이터 배열로 설정
        List<String> arrCheckAspSenderKey = new LinkedList<String>();
        JSONObject objData = mjsonParams.getDataObject(TwbCmmnConst.G_DATA).getJSONObject(0);
        @SuppressWarnings("rawtypes")
        Iterator it = objData.keys();
        while(it.hasNext()) {
            String strKey = (String) it.next();
            String strValue = objData.getString(strKey);

            if(strKey.indexOf("CHECK_SECOND_SNDR_KEY_") > -1 && StringUtils.isNotEmpty(strValue)) {
                arrCheckAspSenderKey.add(strValue);
            }
        }
        if(arrCheckAspSenderKey.size() != 0) {
            mjsonParams.setObject("arrCheckAspSenderKey", 0, arrCheckAspSenderKey);
        }

        objRetParams = chatDashboardCounselService.selectTimeStatsByCounselDoneStatus(mjsonParams);

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @param  result
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     * 
     */
    @NoBizLog
    @ApiOperation(value = "채팅대시보드상담/문의유형별상담요청현황/조회",
                  notes = "채팅대시보드상담/문의유형별상담요청현황/조회")
    @PostMapping("/chat-api/dashboard/counsel/cnslt-requst-sttus-by-svc/inqire")
    public Object selectInqryTypeStatus(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //Validation 체크 
        chatDashboardCounselValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        //근무 시작/종료 시간을 반환한다.
        Map<String, String> mapTwbTalkStat = chatDashboardCounselService.getWorkTime(mjsonParams, "N");
        mjsonParams.setString("TODAY_WORK_TIME_START", mapTwbTalkStat.get("TODAY_WORK_TIME_START"));
        mjsonParams.setString("TODAY_WORK_TIME_END", mapTwbTalkStat.get("TODAY_WORK_TIME_END"));

        objRetParams = chatDashboardCounselService.selectInqryTypeStatus(mjsonParams);

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @param  result
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     * 
     */
    @NoBizLog
    @ApiOperation(value = "채팅대시보드상담-플친별상담요청현황조회(구 채팅상담모니터링)",
                  notes = "'플친별 상담요청 현황' 차트 데이터 조회")
    @PostMapping("/chat-api/dashboard/counsel/cnslt-requst-sttus-by-plus-frnd/inqire")
    public Object selectAspSenderKeyStatus(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //Validation 체크 
        chatDashboardCounselValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        //근무 시작/종료 시간을 반환한다.
        Map<String, String> mapTwbTalkStat = chatDashboardCounselService.getWorkTime(mjsonParams, "N");
        mjsonParams.setString("TODAY_WORK_TIME_START", mapTwbTalkStat.get("TODAY_WORK_TIME_START"));
        mjsonParams.setString("TODAY_WORK_TIME_END", mapTwbTalkStat.get("TODAY_WORK_TIME_END"));

        objRetParams = chatDashboardCounselService.selectAspSenderKeyStatus(mjsonParams);

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     * 
     */
    @ApiOperation(value = "채팅대시보드상담-상담유형별현황조회(구 채팅상담모니터링)",
                  notes = "상담유형별 현황차트 데이터 조회.")
    @PostMapping("/chat-api/dashboard/counsel/sttus-by-cnslt-ty/inqire")
    public Object selectRtnTalkCnslTypeCahrt(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatDashboardCounselService.selectRtnTalkCnslTypeCahrt(jsonParams);

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     * 
     */
    @ApiOperation(value = "채팅대시보드상담-월별실적조회(구 채팅상담모니터링)",
                  notes = "채팅대시보드상담-월별실적조회(구 채팅상담모니터링)")
    @PostMapping("/chat-api/dashboard/counsel/mnby-acmslt/inqire")
    public Object selectRtnMonthResultChart(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatDashboardCounselService.selectRtnMonthResultChart(jsonParams);

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     * 
     */
    @ApiOperation(value = "채팅대시보드상담-상담사현황목록(구 채팅상담모니터링)",
                  notes = "상담사현황 그리드 데이터 조회")
    @PostMapping("/chat-api/dashboard/counsel/cnsltnt-sttus/inqire")
    public Object selectRtnTalkConsultUserList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatDashboardCounselService.selectRtnTalkConsultUserList(jsonParams);

        return objRetParams;
    }


    /**
     * @param  mjsonParams
     * @return
     */
    @NoBizLog
    @ApiOperation(value = "채팅대시보드상담-상담사상세목록(구 채팅상담모니터링)",
                  notes = "상담사 상태 클릭 시, 레이어 팝업으로 상세 리스트 활성화.")
    @PostMapping("/chat-api/dashboard/counsel/cnsltnt/detail")
    public Object selectCountselorList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatDashboardCounselService.selectCountselorList(jsonParams);

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     * 
     */
    @ApiOperation(value = "회사별 사용자 소속 조회",
                  notes = "회사별 사용자 소속 조회")
    @PostMapping("/chat-api/dashboard/ATTR/inqire")
    public Object selectATTRCustUser(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatDashboardCounselService.selectATTRCustUser(jsonParams);

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     * 
     */
    @ApiOperation(value = "상담 총합 건수 조회",
                  notes = "상담 총합 건수 조회")
    @PostMapping("/chat-api/main/dash/totalStat")
    public Object selectCuttTotStat(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatDashboardCounselService.selectCuttTotStat(jsonParams);

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     * 
     */
    @ApiOperation(value = "채널별 상담요청건수 조회",
                  notes = "채널별 상담요청건수 조회")
    @PostMapping("/chat-api/main/dash/chnStat")
    public Object selectChnCuttTotStat(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatDashboardCounselService.selectChnCuttTotStat(jsonParams);

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     * 
     */
    @ApiOperation(value = "시간대별 상담건수 조회",
                  notes = "시간대별 상담건수 조회")
    @PostMapping("/chat-api/main/dash/timeStat")
    public Object selectTimeCuttStat(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatDashboardCounselService.selectTimeCuttStat(jsonParams);

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     * 
     */
    @ApiOperation(value = "채팅 모니터링 우측 건수 조회",
                  notes = "채팅 모니터링 우측 건수 조회")
    @PostMapping("/chat-api/main/dash/rightTotalStat")
    public Object selectRightCuttStat(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatDashboardCounselService.selectRightCuttStat(jsonParams);

        return objRetParams;
    }
}
