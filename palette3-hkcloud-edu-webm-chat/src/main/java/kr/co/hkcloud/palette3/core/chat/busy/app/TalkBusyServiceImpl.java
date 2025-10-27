package kr.co.hkcloud.palette3.core.chat.busy.app;


import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbEnvironment;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbSystemMessage;
import kr.co.hkcloud.palette3.core.chat.busy.dao.TalkBusyDAO;
import kr.co.hkcloud.palette3.core.chat.busy.domain.TalkBusyVO;
import kr.co.hkcloud.palette3.core.chat.busy.util.TalkBusyUtils;
import kr.co.hkcloud.palette3.core.chat.transfer.app.TransToKakaoService;
import kr.co.hkcloud.palette3.core.chat.transfer.domain.TransToEndTalkEvent;
import kr.co.hkcloud.palette3.core.chat.router.dao.RoutingToAgentReadyDAO;
import kr.co.hkcloud.palette3.core.chat.router.app.TalkDataProcessService;
import kr.co.hkcloud.palette3.core.palette.asp.dao.TalkAspCustJpaRepository;
import kr.co.hkcloud.palette3.core.palette.asp.domain.TwbAspCust;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * 텔레톡 BUSY(상담불가) 서비스 구현체
 * 
 * @author User
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("talkBusyService")
public class TalkBusyServiceImpl implements TalkBusyService
{
    private final InnbCreatCmmnService      innbCreatCmmnService;
    private final TwbComDAO                 mobjDao;
    private final TalkBusyDAO               busyDAO;
    private final TransToKakaoService       transToKakaoService;
    private final TalkBusyUtils             talkBusyUtils;
    private final TalkAspCustJpaRepository  aspCustJpaRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final RoutingToAgentReadyDAO    routingToAgentReadyDAO;
    private final TalkDataProcessService    talkDataProcessService;
    String logDevider = "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";


    /**
     * BUSY(상담불가) 체크
     */
    @Override
    public boolean checkBusy() throws TelewebAppException
    {
        //채팅이 가능하지 않은 상태인 지 체크한다
        // 1. 업무 시간 체크
        // (제거, 실시간 인입시에만 체크) 2. 신규 고객접수 여부 체크, 2019-03-22 현재 접수건이 모두 이관 되는 문제 
        // (제거, 실시간 인입시에만 체크) 3. 고객접수 제한 건 체크, 2019-03-22 현재 접수건이 모두 이관 되는 문제
        // 4. 휴일 체크
        //-----------------------------------------------
        //라우터에서의 접근인 경우 상담(채팅)불가 상태면
        //해당 조건의 불가 메시지를 고객에게 보내고
        //user_ready의 09,10을 contact에 저장한다

        //ASP고객사 리스트를 가져와 BUSY처리함 (서비스사용여부 Y)
        List<TwbAspCust> twbAspCustList = aspCustJpaRepository.findAllBySrvcMaintYn("Y");

        for(TwbAspCust twbAspCust : twbAspCustList) {

            if(Integer.toString(twbAspCust.getCustcoId()) == null || "".equals(twbAspCust.getCustcoId())) {
                log.error("checkBusy getCustcoId faild.");
                continue;
            }

            String custcoId = Integer.toString(twbAspCust.getCustcoId());

            // 2018.10.19 lsm자동응답메시지 체크 : y인 경우 사용
            String autoMeesgeYn = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "AUTO_MESSAGE_YN");
            String workStartTime = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "WORK_START_TIME");
            String workEndTime = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "WORK_END_TIME");

            if(autoMeesgeYn == null || "".equals(autoMeesgeYn)) {
                log.error("checkBusy autoMeesgeYn is null or empty. CustcoId ::: {}", custcoId);
                continue;
            }

            if(workStartTime == null || "".equals(workStartTime)) {
                log.error("checkBusy workStartTime is null or empty. CustcoId ::: {}", custcoId);
                continue;
            }

            if(workEndTime == null || "".equals(workEndTime)) {
                log.error("checkBusy workEndTime is null or empty. CustcoId ::: {}", custcoId);
                continue;
            }

            // 4. 휴일 체크
            String holidayYn = busyDAO.selectTalkHoliday(workStartTime, workEndTime, custcoId);
            if("Y".equals(holidayYn)) {
                String systemMsgId = "";
                systemMsgId = "Y".equals(autoMeesgeYn) ? "13" : "11";

                TalkBusyVO talkBusyVO = new TalkBusyVO();
                talkBusyVO.setSystemMsgId(systemMsgId);
                processBusyUserReadyToContact(talkBusyVO, custcoId);

                //ASP는 고객사별 설정이 틀리기 때문에, 모든 BUSY 상태체크가 필요함
                //            return true;
            }

            // 1. 업무종료 시간 도래시 바로 불가 처리 ( = ) 포함.
            log.trace("checkBusy isAvailableWorkTime custcoId ::: {}, {}", custcoId, talkBusyUtils.isAvailableWorkTime(custcoId));
            if(talkBusyUtils.isAvailableWorkTime(custcoId) == false) {
                String systemMsgId = "";
                systemMsgId = "Y".equals(autoMeesgeYn) ? "11" : "11";

                TalkBusyVO talkBusyVO = new TalkBusyVO();
                talkBusyVO.setSystemMsgId(systemMsgId);
                processBusyUserReadyToContact(talkBusyVO, custcoId);

                //ASP는 고객사별 설정이 틀리기 때문에, 모든 BUSY 상태체크가 필요함
//                return true;
            }

            // 5. 점심시간 체크
            String lunchYn = busyDAO.selectLunchTime(workStartTime, workEndTime, custcoId);
            if("Y".equals(lunchYn)) {
                String systemMsgId = "";
                systemMsgId = "Y".equals(autoMeesgeYn) ? "12" : "11";

                TalkBusyVO talkBusyVO = new TalkBusyVO();
                talkBusyVO.setSystemMsgId(systemMsgId);
                processBusyUserReadyToContact(talkBusyVO, custcoId);

                //ASP는 고객사별 설정이 틀리기 때문에, 모든 BUSY 상태체크가 필요함
                //            return true;
            }
        }
        log.info("############here1#############");
        return false;
    }


    /**
     * 라우터 : BUSY(상담불가) 시 고객접수테이블에서 상담이력 테이블로 이관
     * 
     * @param objParams
     */
    @Override
    public void processBusyUserReadyToContact(TalkBusyVO talkBusyVO, String custcoId) throws TelewebAppException
    {
        if(talkBusyVO != null) {
            TelewebJSON inJson = null;
            String telIdentifier = null;

            inJson = new TelewebJSON();
            inJson.setString("CUSTCO_ID", custcoId);
            
            //이메일, 게시판은 채팅이 아니므로 제외한다.
            TelewebJSON talkBusyUserReady = mobjDao.select("kr.co.hkcloud.palette3.core.chat.busy.dao.TalkBusyMapper", "selectTalkBusyUserReady0910", inJson);
            JSONArray talkBusyUserReadyList = talkBusyUserReady.getDataObject();
            if(talkBusyUserReadyList.size() > 0) {
                //시스템 메시지
                TelewebJSON sysMsg = HcTeletalkDbSystemMessage.getInstance().getTelewebJsonBySystemMsgId(custcoId, talkBusyVO.getSystemMsgId());
                for(int i = 0; i < talkBusyUserReadyList.size(); i++) {
                    // 20190409 lgc 상담이관처리 겹치는 로직 함수로 묶어서 처리
                    transUserReadyToContact(sysMsg, talkBusyUserReadyList.getJSONObject(i), true);
                }
            }
        }
        else {
            log.debug("processBusyUserReadyToContact ::: talkBusyVO is null");
        }
    }


    /**
     * 메시지인입 : BUSY(상담불가) 시 해당 고객만 고객접수테이블에서 상담이력 테이블로 이관
     * 
     * @param objParams
     */
    @Override
    public void processBusyUserReadyToContactByTalkUserKey(String talkUserKey, TalkBusyVO talkBusyVO, String custcoId, boolean isShowMsg) throws TelewebAppException
    {
        if(talkBusyVO != null && talkUserKey != null) {
            TelewebJSON inJson = null;
            String strTelIdentifier = null;
            inJson = new TelewebJSON();
            inJson.setString("TALK_USER_KEY", talkUserKey);
            inJson.setString("CHT_USER_KEY", talkUserKey);
            //TODO 확인 필요 - inJson 에서 SNDR_KEY 가 없는데 selectTalkBusyUserReady0910ByTalkUserKey 쿼리 조건에 SNDR_KEY가 있음.
            TelewebJSON talkBusyUserReady = mobjDao.select("kr.co.hkcloud.palette3.core.chat.busy.dao.TalkBusyMapper", "selectTalkBusyUserReady0910ByTalkUserKey", inJson);
            JSONArray talkBusyUserReadyList = talkBusyUserReady.getDataObject();
            if(talkBusyUserReadyList.size() > 0) {
                //시스템 메시지
                TelewebJSON sysMsg = HcTeletalkDbSystemMessage.getInstance().getTelewebJsonBySystemMsgId(custcoId, talkBusyVO.getSystemMsgId());
                transUserReadyToContact(sysMsg, talkBusyUserReadyList.getJSONObject(0), isShowMsg);
            }
        }
        else {}
    }


    /**
     * BUSY(상담불가) 시 고객접수테이블에서 상담이력 테이블로 이관 , SJH 2018/11/08
     * 
     * @param objParams
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   isolation = Isolation.READ_COMMITTED,
                   rollbackFor = {SQLException.class},
                   readOnly = false)
    public void processData(TelewebJSON inJson) throws TelewebAppException
    {
        //상담대기(PLT_CHT_RDY) -> 상담대기이력(PLT_CHT_USER_RDY_HSTRY)
        inJson.setString("CHT_USER_HSTRY_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_USER_HSTRY_ID")));
        mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertRtnTalkHistDetailInfo", inJson);
        
        //상담이력(PLT_CHT_CUTT) 상태 상담 진행중으로 변경
        mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateChtCuttCant", inJson);
        
        //상담 대기 테이블에서 해당 데이터 삭제
        //챗봇상담 상담 대기id기준으로 대기삭제하면 오류 발생
        inJson.setString("CHT_RDY_ID", "");
        mobjDao.delete("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "deleteRtnTalkReadyInfo", inJson);
    }


    /**
     * BUSY(상담불가) 시 메시지 전송으로 ready상세만 데이타 존재..history 이관처리함. , SJH 2018/11/08
     * 
     * @param objParams
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   isolation = Isolation.READ_COMMITTED,
                   rollbackFor = {SQLException.class},
                   readOnly = false)
    public void processDataByreceiver(TelewebJSON inJson) throws TelewebAppException
    {
        log.info("route" + inJson);
//        String strBizCase = "TALK";
        routingToAgentReadyDAO.insertTalkUserReady(inJson);

        //고객대기에서 대기이력테이블에 데이터 저장
//        mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertRtnTalkReadyDetHist", inJson);
//
//        //고객대기상세에서 해당 데이터 삭제
//        mobjDao.delete("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "deleteRtnTalkReadyDetailInfo", inJson);
    }


    /**
     * 상담(채팅) 불가 여부 - 상담(채팅) 불가한 상태(업무시간외, 점심시간 등)일 시 처리
     * 
     * @param  dataProcess
     * @param  userKey
     * @param  senderKey
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   isolation = Isolation.READ_COMMITTED,
                   rollbackFor = {SQLException.class},
                   readOnly = false)
    public boolean isChatDisable(String userKey, String senderKey, String callTypCd, String custcoId, boolean isShowMsg) throws TelewebAppException
    {
        String messageType = "onMessage";
        if(("KAKAO".equals(callTypCd) || "TTALK".equals(callTypCd)) && isShowMsg) {
            messageType = "onReference";
        }
        String logPrefix = logDevider + ".isChatDisable" + "___" + messageType + "___" + userKey + "___isShowMsg:" + isShowMsg + "___";
        int logNum = 1;

        log.info(logPrefix + (logNum++) + " ::: 상담(채팅) 불가 여부 - 상담(채팅) 불가한 상태(업무시간외, 점심시간 등)일 시 처리 start");

        // 2019.04.05 lgc BUSY일 경우 처리 수정됨 (BUSY 각각 if문 안의 내용)
        // 2018.10.15 lsm자동응답메시지 체크 : y인 경우 사용
        // 2020.06.15 라인은 자동응답모드가 없음.
        String autoMeesgeYn = "N";
        if(!callTypCd.equals("LINE")) {
            autoMeesgeYn = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "AUTO_MESSAGE_YN");
        }

        // 업무 시간 체크
//        // [2019.04.21 KMG] 서버의 시간이 KR로 지정되있지 않을 경우를 대비하여 timeZone을 설정한다.
//        TimeZone time = TimeZone.getTimeZone("Asia/Seoul");
//        SimpleDateFormat formatter = new SimpleDateFormat("HHmm");
//        formatter.setTimeZone(time);

//        String timeNow = formatter.format(new Date());
        String workStartTime = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "WORK_START_TIME");
        String workEndTime = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "WORK_END_TIME");

        log.info(logPrefix + (logNum++) + " ::: workStartTime : " + workStartTime);
        log.info(logPrefix + (logNum++) + " ::: workEndTime : " + workEndTime);

//        int intTimeNow = Integer.valueOf(timeNow);
//        int intWorkStartTime = Integer.valueOf(workStartTime);
//        int intWorkEndTime = Integer.valueOf(workEndTime);

        // Busy Check 규칙 변경
        TelewebJSON inJson = new TelewebJSON();
        inJson.setString("TALK_USER_KEY", userKey); //컬럼명 변경 TALK_USER_KEY => CHT_USER_KEY
        inJson.setString("CHT_USER_KEY", userKey);
        inJson.setString("SNDR_KEY", senderKey);
        inJson.setString("CALL_TYP_CD", callTypCd); //컬럼명 변경 CALL_TYP_CD => CHN_CLSF_CD
        inJson.setString("CHN_CLSF_CD", callTypCd);
        inJson.setString("CUSTCO_ID", custcoId);

        log.info(logPrefix + (logNum++) + " ::: autoMeesgeYn : " + autoMeesgeYn);
        log.info(logPrefix + (logNum++) + " ::: inJson : ");
        log.info(logPrefix + (logNum++) + inJson.toString() );

//        // 업무종료 시간 도래시 바로 불가 처리 ( = ) 포함.
//        // if (timeNow.compareTo(workStartTime) < 0 || timeNow.compareTo(workEndTime) >= 0) {
//        if (intWorkStartTime > intTimeNow)
//        {
//            if (intWorkEndTime <= intTimeNow)
//            {
//                // 전달이나 무응답건에 대해서는 예외처리 없이 하도록함. SJH 20181024
//                String strMsg = "";
//
//                // 자동응답메시지 설정값에 따른 메시지
//                if ("Y".equals(autoMeesgeYn))
//                {
//                    strMsg = "11";
//                }
//                else
//                {
//                    strMsg = "11";
//                }
//
//                // 고객 상태에 따른 처리
//                return checkBusyByCustomerReadyStat(autoMeesgeYn, strMsg, inJson);
//            }
//        }

        log.info(logPrefix + (logNum++) + " ::: 1. 업무시간 체크 하는 로직");
        if(talkBusyUtils.isAvailableWorkTime(custcoId) == false) {
            log.info(logPrefix + (logNum++) + " ::: talkBusyUtils.isAvailableWorkTime(custcoId) == false ::: true");

            String strMsg = "";

            // 자동응답메시지 설정값에 따른 메시지
            if("Y".equals(autoMeesgeYn)) {
                strMsg = "11";  //업무시간 아닐때 시스템 메시지
            }
            else {
                strMsg = "11";  //기존에도 MSG_ID는 다르지만 동일한 내용의 메시지가 할당됨 => 일단 동일 메시지 가져오기로 변경
            }

            // 고객 상태에 따른 처리
            log.info(logPrefix + (logNum++) + " ::: 고객 상태에 따른 처리 ::: ");
            log.info(logPrefix + (logNum++) + " ::: call before checkBusyByCustomerReadyStat(autoMeesgeYn, strMsg, inJson, isShowMsg) ::: "
                + "\nautoMeesgeYn ::: " + autoMeesgeYn
                + "\nstrMsg ::: " + strMsg
                + "\ninJson ::: " + inJson
                + "\nisShowMsg ::: " + isShowMsg
            );
            return checkBusyByCustomerReadyStat(autoMeesgeYn, strMsg, inJson, isShowMsg);
        }

        // 2. 신규 고객접수 여부 체크
        log.info(logPrefix + (logNum++) + " ::: 2. 신규 고객접수 여부 체크");
        String routeCd = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "ROUTE_CD");
        log.info(logPrefix + (logNum++) + " ::: TalkBusyServiceImpl isChatDisable routeCd ::: " + routeCd);
        if(!"Y".equals(routeCd)) {
            log.info(logPrefix + (logNum++) + " ::: !\"Y\".equals(routeCd) ::: true");
            
            String strMsg = "";

            // 자동응답메시지 설정값에 따른 메시지
            if("Y".equals(autoMeesgeYn)) {
                strMsg = "7";   //응대지연 메시지
            }
            else {
                strMsg = "7";   //기존에도 MSG_ID는 다르지만 동일한 내용의 메시지가 할당됨 => 일단 동일 메시지 가져오기로 변경
            }

            routingToAgentReadyDAO.insertTalkUserReady(inJson);
            
            // 고객 상태에 따른 처리
            log.info(logPrefix + (logNum++) + " ::: 고객 상태에 따른 처리 ::: ");
            log.info(logPrefix + (logNum++) + " ::: call before checkBusyByCustomerReadyStat(autoMeesgeYn, strMsg, inJson, isShowMsg) ::: "
                + "\nautoMeesgeYn ::: " + autoMeesgeYn
                + "\nstrMsg ::: " + strMsg
                + "\ninJson ::: " + inJson
                + "\nisShowMsg ::: " + isShowMsg
            );
            return checkBusyByCustomerReadyStat(autoMeesgeYn, strMsg, inJson, isShowMsg);
        }

        // 3. 고객접수 가능 건 수
        log.info(logPrefix + (logNum++) + " ::: 3. 고객접수 가능 건 수");
        int routeWaitingCnt = Integer.valueOf(HcTeletalkDbEnvironment.getInstance().getString(custcoId, "ROUTE_WAITING_CNT"));
        log.info(logPrefix + (logNum++) + " ::: TalkBusyServiceImpl isChatDisable routeWaitingCnt ::: " + routeWaitingCnt);


        TelewebJSON jsonParam = new TelewebJSON();
        jsonParam.setString("CUSTCO_ID", custcoId);
        jsonParam.setInt("ACPT_LIMIT_CNT", routeWaitingCnt);
        jsonParam.setString("TALK_USER_KEY", userKey);
        jsonParam.setString("CHT_USER_KEY", userKey);
        TelewebJSON retJson = busyDAO.selectAcptLimitByUserKey(jsonParam);
        String strAcptCd = ((JSONObject) (retJson.getDataObject().get(0))).getString("ACPT_LIMIT_STAT");
        log.info("===>" + userKey + ":TalkBusyServiceImpl isChatDisable retJson : ");
        log.info("===>" + userKey + ":{} ", retJson.toString() );
        log.info(logPrefix + (logNum++) + " ::: strAcptCd ::: " + strAcptCd);
        // PASS : 계속 진행
        if("PASS".equals(strAcptCd)) {
            log.info("===>" + userKey + ":{} ", "ACPT_LIMIT_STAT -> PASS :::");
        }
        // NO_ACPT : 신규고객 메시지 접수제한, 메시지만 이관 처리
        else if("NO_ACPT".equals(strAcptCd)) {
            log.info("===>" + userKey + ":{} ", "ACPT_LIMIT_STAT -> NO_ACPT :::");
            String strMsg = "";

            // 자동응답메시지 설정값에 따른 메시지
            if("Y".equals(autoMeesgeYn)) {
                strMsg = "7";   //응대지연 자동 메시지
            }
            else {
                strMsg = "7";   //기존에도 MSG_ID는 다르지만 동일한 내용의 메시지가 할당됨 => 일단 동일 메시지 가져오기로 변경
            }

            // 메시지는 항상 보여줄 필요없음( message / reference 동시 이벤트 발생 시 문제가 되어 메시지는 reference 만 보여줌)
            if(isShowMsg) {
                log.info(logPrefix + (logNum++) + " ::: 고객에게 시스템메시지 전송 isShowMsg true ::: ");
                transToKakaoService.sendSystemMsg(userKey, senderKey, HcTeletalkDbSystemMessage.getInstance().getTelewebJsonBySystemMsgId(custcoId, strMsg), callTypCd);    //고객에게 시스템메시지 전송
            } else {
                log.info(logPrefix + (logNum++) + " ::: isShowMsg false ::: ");
            }
            
            log.info(logPrefix + (logNum++) + " ::: call before routingToAgentReadyDAO.insertTalkUserReady(inJson) ::: " + inJson);
            routingToAgentReadyDAO.insertTalkUserReady(inJson);
            log.info(logPrefix + (logNum++) + " ::: call after routingToAgentReadyDAO.insertTalkUserReady(inJson) ::: ");
//            processDataByreceiver(inJson);    // 메시지 DB 이관 처리

            // Session End
            JSONObject obj = new JSONObject();
            obj.put("user_key", userKey);
            obj.put("sndrKey", senderKey);
            obj.put("CHT_CUTT_DTL_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID")));

            // 스프링 이벤트로 처리 (transToKakaoService.transToEndTalkEvent)
            log.info(logPrefix + (logNum++) + " ::: 스프링 이벤트로 처리 (transToKakaoService.transToEndTalkEvent) obj ::: " + obj);
            log.info(logPrefix + (logNum++) + " ::: callTypCd ::: " + callTypCd);
            eventPublisher.publishEvent(TransToEndTalkEvent.builder().active("endtalk").writeData(obj).callTypCd(callTypCd).build());

            return true;
        }
        else {
            log.info("===>" + userKey + ":ACPT_LIMIT_STAT -> else? ::: {}", strAcptCd);
        }

        // 4. 휴일 체크
        log.info(logPrefix + (logNum++) + " ::: 5. 휴일 체크 ::: ");
        String holidayYn = busyDAO.selectTalkHoliday(workStartTime, workEndTime, custcoId);
        if("Y".equals(holidayYn)) {
            String strMsg = "";

            // 자동응답메시지 설정값에 따른 메시지
            if("Y".equals(autoMeesgeYn)) {
                strMsg = "13";  //휴일 메시지
            }
            else {
                strMsg = "11";  //업무시간 메시지
            }

            // 고객 상태에 따른 처리
            log.info(logPrefix + (logNum++) + " ::: 고객 상태에 따른 처리 ::: ");
            log.info(logPrefix + (logNum++) + " ::: call before checkBusyByCustomerReadyStat(autoMeesgeYn, strMsg, inJson, isShowMsg) ::: "
                + "\nautoMeesgeYn ::: " + autoMeesgeYn
                + "\nstrMsg ::: " + strMsg
                + "\ninJson ::: " + inJson
                + "\nisShowMsg ::: " + isShowMsg
            );
            return checkBusyByCustomerReadyStat(autoMeesgeYn, strMsg, inJson, isShowMsg);
        }

        // 5. 점심시간 체크
        log.info(logPrefix + (logNum++) + " ::: 4. 점심시간 체크 ::: ");
        String lunchYn = busyDAO.selectLunchTime(workStartTime, workEndTime, custcoId);
        if("Y".equals(lunchYn)) {
            String strMsg = "";
            strMsg = "Y".equals(autoMeesgeYn) ? "12" : "11";    //업무시간 메시지 , 점심시간 메시지

            // 고객 상태에 따른 처리
            log.info(logPrefix + (logNum++) + " ::: 고객 상태에 따른 처리 ::: ");
            log.info(logPrefix + (logNum++) + " ::: call before checkBusyByCustomerReadyStat(autoMeesgeYn, strMsg, inJson, isShowMsg) ::: "
                + "\nautoMeesgeYn ::: " + autoMeesgeYn
                + "\nstrMsg ::: " + strMsg
                + "\ninJson ::: " + inJson
                + "\nisShowMsg ::: " + isShowMsg
            );
            return checkBusyByCustomerReadyStat(autoMeesgeYn, strMsg, inJson, isShowMsg);

            //ASP는 고객사별 설정이 틀리기 때문에, 모든 BUSY 상태체크가 필요함
            //            return true;
        }
        log.info(logPrefix + (logNum++) + " ::: return false ::: ");
        return false;
    }


    /**
     * BUSY일 때 신규, 접수(09)/대기(10), 배정(11)/전달(13)에 따른 처리
     * 
     * @param  dataProcess
     * @param  userKey
     * @param  senderKey
     * @param  autoMeesgeYn
     * @return              true : 종료 처리됨, false : PASS 이후 로직 처리
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   isolation = Isolation.READ_COMMITTED,
                   rollbackFor = {SQLException.class},
                   readOnly = false)
    private boolean checkBusyByCustomerReadyStat(String autoMeesgeYn, String strMsgId, TelewebJSON jsonParam, boolean isShowMsg) throws TelewebAppException
    {
        String messageType = "onMessage";
        if(("KAKAO".equals(jsonParam.getString("CALL_TYP_CD")) || "TTALK".equals(jsonParam.getString("CALL_TYP_CD"))) && isShowMsg) {
            messageType = "onReference";
        }
        String logPrefix = logDevider + ".checkBusyByCustomerReadyStat" + "___" + messageType + "___" + jsonParam.getString("TALK_USER_KEY") + "___isShowMsg:" + isShowMsg + "___";
        int logNum = 1;
        log.info(logPrefix + (logNum++) + " ::: checkBusyByCustomerReadyStat start ::: "
            + "\nautoMeesgeYn ::: " + autoMeesgeYn
            + "\nstrMsgId ::: " + strMsgId
            + "\njsonParam ::: " + jsonParam
            + "\nisShowMsg ::: " + isShowMsg
        );
        String talkUserKey = jsonParam.getString("TALK_USER_KEY");
        String senderKey = jsonParam.getString("SNDR_KEY");
        String callTypCd = jsonParam.getString("CALL_TYP_CD");
        String custcoId = jsonParam.getString("CUSTCO_ID");

        //고객 대기 상태 조회
        log.info(logPrefix + (logNum++) + " ::: 고객 대기 상태 조회 - call before busyDAO.selectCustomerReadyStat(jsonParam) ::: " + jsonParam);
        TelewebJSON retJson = busyDAO.selectCustomerReadyStat(jsonParam);
        JSONArray customerReadyStatList = retJson.getDataObject();

        // 신규
        if(customerReadyStatList.size() < 1) {
            //메시지는 항상 보여줄 필요없음( message / reference 동시 이벤트 발생 시 문제가 되어 메시지는 reference 만 보여줌)
            if(isShowMsg) {
                log.info(logPrefix + (logNum++) + " ::: 신규 ::: ");
                TelewebJSON inJson = new TelewebJSON();
                inJson.setString("TALK_USER_KEY", talkUserKey);
                inJson.setString("CHT_USER_KEY", talkUserKey);
                inJson.setString("TYPE", "system");
                inJson.setString("CUSTCO_ID", custcoId);
                inJson.setString("SNDR_KEY", senderKey);
                // ALTMNT_WAIT - 배분대기
                inJson.setString("TALK_READY_CD", "ALTMNT_WAIT");
                inJson.setString("ALTMNT_STTS_CD", "ALTMNT_WAIT"); 
                inJson.setString("CUTT_STTS_CD", "ALTMNT_WAIT");
                inJson.setString("CHATBOT_YN", "N");
                inJson.setString("CALL_TYP_CD", callTypCd);
                //상담대기 등록
                log.info(logPrefix + (logNum++) + " ::: 상담대기 등록 - call before talkDataProcessService.processInsertTalkReady(inJson) ::: " + inJson);
                talkDataProcessService.processInsertTalkReady(inJson);
                
                
                TelewebJSON messageJson = HcTeletalkDbSystemMessage.getInstance().getTelewebJsonBySystemMsgId(custcoId, strMsgId);
                log.info(logPrefix + (logNum++) + " ::: strMsgId" + strMsgId);
                log.info(logPrefix + (logNum++) + " ::: systemMessage ::: " + messageJson);
                
                log.info(logPrefix + (logNum++) + " ::: 고객에게 시스템메시지 전송 - call before transToKakaoService.sendSystemMsg(talkUserKey, senderKey, HcTeletalkDbSystemMessage.getInstance().getTelewebJsonBySystemMsgId(custcoId, strMsgId), callTypCd) ::: ");
                //최근 전송 메시지가 해당 시스템 메시지였다면 중복으로 전송이므로 보내지 않는다
                //시간별 전송 시스템 메시지는 중복으로 나가도 됨 (1차/2차3차 무응답,접수지연,응대지연 등)
                //일단은 시스템 메시지 발송 후 바로 종료시키는 건(업무시간 외/휴일/점심시간)들만 걸러내기 ==> 테스트 후 나머지 시스템 메시지도 포함시키기
                if(strMsgId.equals("11") || strMsgId.equals("12") || strMsgId.equals("13")) {
                    TelewebJSON recentMsgJson = busyDAO.selectRecentMsg(jsonParam);     //최근 채팅방에 발송한 메시지
                    log.info(logPrefix + (logNum++) + " ::: 최근 전송한 메시지 확인 ::: " + recentMsgJson);
                    if(recentMsgJson.getString("SYS_MSG_ID") == null) {
                        log.info(logPrefix + (logNum++) + " ::: 고객에게 전송한 시스템 메시지 없음 ::: ");
                        transToKakaoService.sendSystemMsg(talkUserKey, senderKey, messageJson, callTypCd);  //고객에게 시스템메시지 전송
                    } else if(!recentMsgJson.getString("SYS_MSG_ID").equals(strMsgId)) {
                        log.info(logPrefix + (logNum++) + " ::: 고객에게 시스템메시지를 전송했지만 다른 시스템 메시지임(중복이 아님) ::: ");
                        transToKakaoService.sendSystemMsg(talkUserKey, senderKey, messageJson, callTypCd);  //고객에게 시스템메시지 전송
                    } else {
                        log.info(logPrefix + (logNum++) + " ::: 고객에게 시스템메시지 이미 전송완료 ::: ");
                    }
                } else {
                    transToKakaoService.sendSystemMsg(talkUserKey, senderKey, messageJson, callTypCd);  //고객에게 시스템메시지 전송
                }
//                transToKakaoService.sendSystemMsg(talkUserKey, senderKey, messageJson, callTypCd);    //고객에게 시스템메시지 전송
                //processDataByreceiver(jsonParam); // 메시지 DB 이관 처리
    
                // kakao Session End
                JSONObject obj = new JSONObject();
                obj.put("user_key", talkUserKey);
                obj.put("sndrKey", senderKey);
                obj.put("CHT_CUTT_DTL_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID")));
    
    //            new Thread(()->{
    //              try {
    //                  transToKakaoService.transToKakao("endtalk", obj, callTypCd);
    //              } catch(Exception e) {
    //                  e.printStackTrace();
    //              }
    //            }).start();
    
                // 스프링 이벤트로 처리 (transToKakaoService.transToEndTalkEvent)
                log.info(logPrefix + (logNum++) + " ::: 스프링 이벤트로 처리 (transToKakaoService.transToEndTalkEvent) obj ::: " + obj);
                log.info(logPrefix + (logNum++) + " ::: callTypCd ::: " + callTypCd);
                
                //네이버톡톡인 경우 채팅 불가일때 endtalk이벤트를 발생시키면 채팅상태가 상담불가상태가 아닌 포기상태로 변경됨 (session_id가 없기 때문에)
                if(!callTypCd.equals("NTT")) {
                    eventPublisher.publishEvent(TransToEndTalkEvent.builder().active("endtalk").writeData(obj).callTypCd(callTypCd).build());
                }
                
                //상담 종료 / 상담 대기 삭제 / 상담 이력 업데이트
                log.info(logPrefix + (logNum++) + " ::: 상담 종료 / 상담 대기 삭제 / 상담 이력 업데이트 ::: " + inJson);
                processData(inJson);
            } else {
                log.info(logPrefix + (logNum++) + " ::: isShowMsg ::: false");
            }

            return true;
        }
        // 접수/대기/배정/전달
        else if(customerReadyStatList.size() > 0) {
            log.info(logPrefix + (logNum++) + " ::: 접수/대기/배정/전달 customerReadyStatList.size() ::: " + customerReadyStatList.size());
            String strCustReadyStat = customerReadyStatList.getJSONObject(0).getString("CUSTOMER_READY_STAT");
            log.info(logPrefix + (logNum++) + " ::: strCustReadyStat ::: " + strCustReadyStat);
            switch(strCustReadyStat)
            {
                // 접수/대기 - 메시지 발송 후 상담이관
                case "RCEPT":
                case "READY":
                {
                    log.info("2@2@2@");
                    //메시지 발송 후 상담이관
                    TalkBusyVO talkBusyVO = new TalkBusyVO();
                    talkBusyVO.setSystemMsgId(strMsgId);
                    
                    log.info(logPrefix + (logNum++) + " ::: 메시지인입 : BUSY(상담불가) 시 해당 고객만 고객접수테이블에서 상담이력 테이블로 이관 - call before processBusyUserReadyToContactByTalkUserKey(talkUserKey, talkBusyVO, custcoId, isShowMsg) ::: "
                        + "\ntalkUserKey ::: " + talkUserKey
                        + "\ntalkBusyVO ::: " + talkBusyVO
                        + "\ncustcoId ::: " + custcoId
                        + "\nisShowMsg ::: " + isShowMsg
                    );
                    processBusyUserReadyToContactByTalkUserKey(talkUserKey, talkBusyVO, custcoId, isShowMsg);
                    
                    if(isShowMsg) {
                        log.info(logPrefix + (logNum++) + " ::: 고객에게 시스템메시지 전송 - call before transToKakaoService.sendSystemMsg(talkUserKey, senderKey, HcTeletalkDbSystemMessage.getInstance().getTelewebJsonBySystemMsgId(custcoId, strMsgId), callTypCd) ::: ");
                        transToKakaoService.sendSystemMsg(talkUserKey, senderKey, HcTeletalkDbSystemMessage.getInstance().getTelewebJsonBySystemMsgId(custcoId, strMsgId), callTypCd);  //고객에게 시스템메시지 전송
                    } else {
                        log.info(logPrefix + (logNum++) + " ::: isShowMsg ::: false");
                    }
                    //processDataByreceiver(jsonParam); // 메시지 DB 이관 처리

                    // kakao Session End
                    JSONObject obj = new JSONObject();
                    obj.put("user_key", talkUserKey);
                    obj.put("sndrKey", senderKey);
                    obj.put("CHT_CUTT_DTL_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID")));

//                    // endtalk ( 별도 쓰레드 처리 ) 
//                    new Thread(()->{
//                      try {
//                          transToKakaoService.transToKakao("endtalk", obj, callTypCd);
//                      } catch(Exception e) {
//                          e.printStackTrace();
//                      }
//                    }).start();

                    // 스프링 이벤트로 처리 (transToKakaoService.transToEndTalkEvent)
                    // 접수제한상담 응대지연멘트 후 상담 종료 => 종료하지 않음
                    // N차 응대지연멘트 후 종료
                    //eventPublisher.publishEvent(TransToEndTalkEvent.builder().active("endtalk").writeData(obj).callTypCd(callTypCd).build());

                    return true;
                }
                // 배정/전달 - PASS
                case "ASIGN":
                case "DLIV":
                {
                    return false;
                }
            }
        }
        // 의도치 않은 케이스
        else {
            log.error(logPrefix + (logNum++) + " ::: 의도치 않은 케이스 ::: customerReadyStatList.size() ::: " + customerReadyStatList.size());
        }
        return false;
    }


    /**
     * 상담이력을 이관한다.(USER_READY -> CONTACT)
     */
    @Override
    public void transUserReadyToContact(TelewebJSON sysMsg, JSONObject talkBusyUserReadyList, boolean isShowMsg) throws TelewebAppException
    {

        // 배분 처리함.  
        TelewebJSON inJson = new TelewebJSON();

        String talkUserKey = talkBusyUserReadyList.getString("CHT_USER_KEY");
        String sndrKey = talkBusyUserReadyList.getString("SNDR_KEY");
        String custcoId = talkBusyUserReadyList.getString("CUSTCO_ID");
        String callTypCd = talkBusyUserReadyList.getString("CHN_TYPE_CD");
        
        inJson.setString("TALK_USER_KEY", talkUserKey);
        inJson.setString("CHT_USER_KEY", talkUserKey);
        inJson.setString("TYPE", "system");
        inJson.setString("CUSTCO_ID", custcoId);
        inJson.setString("SNDR_KEY", sndrKey);
        inJson.setString("TALK_READY_CD", "ALTMNT_WAIT");
        inJson.setString("ALTMNT_STTS_CD", "ALTMNT_WAIT");
        inJson.setString("CUTT_STTS_CD", "ALTMNT_WAIT");

        inJson.setString("CHATBOT_YN", "N");
        inJson.setString("CALL_TYP_CD", callTypCd);
        inJson.setString("CHN_TYPE_CD", callTypCd);
        talkDataProcessService.processInsertTalkReady(inJson);

        //고객에게 시스템메시지 전송
        if(isShowMsg)
            transToKakaoService.sendSystemMsg(talkUserKey, sndrKey, sysMsg, callTypCd);

        //상담 종료 / 상담 대기 삭제 / 상담 이력 업데이트
        processData(inJson);
    }


    /**
     * 배치실행을 위한 채팅고객사 테넌트 체크 - (서비스_상태_코드:ON, 이전_시스템_여부:N)
     * 
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   isolation = Isolation.READ_COMMITTED,
                   rollbackFor = {SQLException.class},
                   readOnly = true)
    public TelewebJSON selectBatchTenantList() throws TelewebAppException
    {
        TelewebJSON mjsonParams = new TelewebJSON();
        TelewebJSON tenantObj = new TelewebJSON(mjsonParams);

        tenantObj = mobjDao.select("kr.co.hkcloud.palette3.core.chat.busy.dao.TalkBusyMapper", "selectBatchTenantList", mjsonParams);
        
        return tenantObj;
    }

}
