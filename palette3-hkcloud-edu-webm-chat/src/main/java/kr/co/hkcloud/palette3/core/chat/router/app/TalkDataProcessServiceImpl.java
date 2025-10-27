package kr.co.hkcloud.palette3.core.chat.router.app;


import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import kr.co.hkcloud.palette3.common.chat.domain.OrgContentVO;
import kr.co.hkcloud.palette3.common.chat.domain.OrgFileVO;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.setting.customer.app.SettingCustomerInformationListService;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbEnvironment;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbSystemMessage;
import kr.co.hkcloud.palette3.core.chat.redis.dao.TalkRedisChatInoutRepository;
import kr.co.hkcloud.palette3.core.chat.redis.dao.TalkRedisChatReadyRepository;
import kr.co.hkcloud.palette3.core.chat.router.dao.RoutingToAgentReadyDAO;
import kr.co.hkcloud.palette3.core.chat.stomp.app.TalkStompInoutService;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatStompVO;
import kr.co.hkcloud.palette3.core.chat.transfer.app.TransToKakaoService;
import kr.co.hkcloud.palette3.core.chat.transfer.domain.TransToEndTalkEvent;
import kr.co.hkcloud.palette3.core.util.PaletteFilterUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.enumer.RepositoryPathTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTaskTypeCd;
import kr.co.hkcloud.palette3.file.util.FileRulePropertiesUtils;
import kr.co.hkcloud.palette3.chat.setting.util.ChatSettingBannedWordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.util.Iterator;

import java.util.regex.*;

import kr.co.hkcloud.palette3.core.util.PaletteFilterUtils;
import kr.co.hkcloud.palette3.file.util.FileRulePropertiesUtils;


@Slf4j
@RequiredArgsConstructor
@Service("talkDataProcessService")
public class TalkDataProcessServiceImpl implements TalkDataProcessService
{
    private final InnbCreatCmmnService         innbCreatCmmnService;
    private final TwbComDAO                    mobjDao;
    private final TalkRedisChatInoutRepository chatInoutRepository;
    private final TalkRedisChatReadyRepository chatReadyRepository;
    private final TalkStompInoutService        stompInoutService;
    private final RoutingToAgentReadyDAO       routingToAgentReadyDAO;
    private final TransToKakaoService          transToKakaoService;
    private final TalkRouteMapper talkRouteMapper;
    private final ApplicationEventPublisher    eventPublisher;
    private final ChatSettingBannedWordUtils chatSettingBannedWordUtils;
    
    private final SettingCustomerInformationListService custInfoService;
    
    private final PaletteFilterUtils paletteFilterUtils;
    private final FileRulePropertiesUtils fileRulePropertiesUtils;
    
    String logDevider = "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";
	
//	private String extUrl = "http://localhost:8445/v1/sw/";
//	private String token = "bearer FHeyMNl6jNknvgTGfT3RS5a3w1EQd1fqLVriEH3F/Jql7rNDbu5mqRyVwPbLH0NEGdsGt2eS+wAz0Dx+3iM2H5YEEpDzjEushrjU4f/B5Mp0onp0O4s2l3AA96sucDzCaQQw1qrPcSIKBrHkbSbEfUNNDwwZGobPkHiZyqQCfBAqLNRqMlBvDOEFnewI1iQJJ7NL7QPZP375Ys2QcFqUQQ==";

    @Value("${palette.external-api.sw.url}")
    private String extUrl;
    
    @Value("${palette.external-api.sw.token}")
    private String token;


    /**
     * 상담이력 상세 삽입
     * 
     * @Transactional             Auto Commit
     * @param         TelewebJSON
     * @return                    TelewebJSON 형식의 처리 결과 데이터
     */
    public TelewebJSON insertTalkContactDetail(TelewebJSON mjsonParams) throws TelewebAppException
    {
        String logPrefix = logDevider + ".insertTalkContactDetail___" + mjsonParams.getString("TALK_USER_KEY") + "___";
        int logNum = 1;
        log.info(logPrefix + (logNum++) + " ::: 상담이력 상세 삽입 - insertTalkContactDetail start ::: "
            + "\nmjsonParams ::: " + mjsonParams
        );
        
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //금칙어 변환
        String msg = chatSettingBannedWordUtils.parseContent_3(mjsonParams.getString("CONTENT"), mjsonParams.getString("CUSTCO_ID"));
        mjsonParams.setString("CHG_CONTENT",msg);
        log.info(logPrefix + (logNum++) + " ::: 금칙어 변환 - msg ::: " + msg);
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgMapper", "INSERT_PLT_CHT_CUTT_DTL", mjsonParams);

        return objRetParams;
    }

    /**
     * 상담예약문의유형을 선택 했을 때 상담 상태를 상담 예약상태로 만들어준다
     * 
     * @Transactional             Auto Commit
     * @param         TelewebJSON
     * @return                    TelewebJSON 형식의 처리 결과 데이터
     */
    public TelewebJSON updateChtCuttRsvt(TelewebJSON mjsonParams) throws TelewebAppException
    {
        String logPrefix = logDevider + ".insertTalkContactDetail___" + mjsonParams.getString("TALK_USER_KEY") + "___";
        int logNum = 1;
        log.info(logPrefix + (logNum++) + " ::: 상담예약문의유형을 선택 했을 때 상담 상태를 상담 예약상태로 만들어준다 - insertTalkContactDetail start ::: "
            + "\nmjsonParams ::: " + mjsonParams
        );
        
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgMapper", "updateChtCuttRsvt", mjsonParams);
        //상담 대기에서 데이터 삭제
        objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgMapper", "deleteChtRdyRsvt", mjsonParams);

        return objRetParams;
    }


    /**
     * 상담시작(중) 삽입
     * 
     * @param TelewebJSON inJson
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   isolation = Isolation.READ_COMMITTED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public void processTalkIng(TelewebJSON inJson) throws TelewebAppException
    {
        //CONSULT:CONSULT_CONN - 지원연결완료 (상담지원 시작) 
        //stompInoutService.insertTalkIng(inJson);

    	log.info("11!!11!!11!!"+inJson);
        //SET - INOUT:USER_KEY - 상당지원시작 시 커넥션 정보 추가
        String userKey = inJson.getString("TALK_USER_KEY");
        String userId = "";
        if(inJson.getString("USER_ID") != null || inJson.getString("USER_ID") != "") {
            userId = inJson.getString("USER_ID");
        }
        else {
            userId = "NO_USER";
        }

        chatInoutRepository.setStompVO(ChatStompVO.builder().agentId(userId).userKey(userKey).build());
    }


    /**
     * 전달대기 처리
     * 
     * @param  JSONObject requestJson
     * @return            String cnt
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   isolation = Isolation.READ_COMMITTED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public String processTransTalkReady(TelewebJSON requestJson) throws TelewebAppException
    {
        // 전달 대기 등록.
        String transId = requestJson.getHeaderString("agent_id");
        String userId = requestJson.getString("USER_ID");
        String userKey = requestJson.getHeaderString("CUST_ID");
        String sendeyKey = requestJson.getHeaderString("SNDR_KEY");
        String custcoId = requestJson.getString("CUSTCO_ID");
        String talkSerialNumber = requestJson.getHeaderString("CHT_CUTT_DTL_ID");
        String cuttSttsCd = requestJson.getString("CUTT_STTS_CD");
        String talkContactId = requestJson.getHeaderString("CHT_CUTT_ID");
        String talkInqryCd = requestJson.getHeaderString("CUTT_TYPE_ID");
        String cuttCn = requestJson.getString("CUTT_CN");
        String execRstCd = requestJson.getHeaderString("PRCS_RSLT_CD");      // 콜백 추가 (  SJH )

        String recipientCounselerNickName = requestJson.getHeaderString("agent_nick");

        TelewebJSON inJson = new TelewebJSON();
        inJson.setString("CUST_ID", userKey);
        inJson.setString("SNDR_KEY", sendeyKey);
        inJson.setString("CUSTCO_ID", custcoId);
        inJson.setString("CHT_CUTT_DTL_ID", talkSerialNumber);
        inJson.setString("TALK_API_CD", "/trans");
        inJson.setString("SESSION_ID", "");
        inJson.setString("TYPE", "text");
        inJson.setString("CONTENT", recipientCounselerNickName + " 에이전트님께서 상담을 전달하였습니다.");
        inJson.setString("CUTT_STTS_CD", cuttSttsCd);
        inJson.setString("USER_ID", userId);
        inJson.setString("AGENT_ID", transId);
        inJson.setString("CHT_CUTT_ID", talkContactId);

        inJson.setString("PRCS_RSLT_CD", execRstCd);

        inJson.setString("CUTT_TYPE_ID", talkInqryCd);
        inJson.setString("CUTT_CN", cuttCn);

        //inJson.setString("EXEC_RST_CD", execRstCd != null && execRstCd.equals("4") ? "4" : ""); // 처리결과 (콜백) ,값이 있다면..

        // 콜백 부가정보를 위하여 db 조회 함.(중복코드 제거 )
//        TelewebJSON objRetParams = routingToAgentReadyDAO.selectRtnTalkHistInfo(inJson);

        // 콜백 정보 셋팅 
        inJson.setString("CLBK_YN", requestJson.getString("CLBK_YN"));
        inJson.setString("CLBK_YMD", requestJson.getString("CLBK_YMD"));
        inJson.setString("CLBK_BGNG_DT", requestJson.getString("CLBK_BGNG_DT"));

        // talk_ready_dt 정보 업데이트
        inJson.setString("CUTT_STTS_CD", requestJson.getString("CUTT_STTS_CD"));

        // 카카오톡 / Ttalk 코드값 인입채널 이전 , SJH , 2019.11.20
        inJson.setString("CHN_CLSF_CD", requestJson.getString("CHN_CLSF_CD"));

        routingToAgentReadyDAO.insertTalkUserReady(inJson);
        //routingToAgentReadyDAO.insertTalkUserReadyDetail(inJson);

        // 기존 상담중 데이터는 전달대기완료 값으로 업데이트 함
        inJson.setString("PRCS_RSLT_CD", execRstCd);   // 처리결과 (전달)

        // 상담 대기 건수 조회
        TelewebJSON outJson = routingToAgentReadyDAO.selectTalkUserReadyAgent(inJson);
        String cnt = ((JSONObject) (outJson.getDataObject().get(0))).getString("CNT");
        return cnt;
    }


    /**
     * 배분 대기 및 상세 등록
     * 
     * @param  TelewebJSON inJson
     * @return             String cnt
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   isolation = Isolation.READ_COMMITTED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON processInsertTalkReady(TelewebJSON inJson) throws TelewebAppException
    {
        TelewebJSON outJson = new TelewebJSON();

        // 배분 대기 병합
        log.info("배분 대기 및 상세 등록");
        outJson = routingToAgentReadyDAO.insertTalkUserReady(inJson);
        //routingToAgentReadyDAO.insertTalkUserReadyDetail(inJson);
        
        processTalkIng(inJson);

        return outJson;
    }


    /**
     * 챗봇상담 상담 및 대기 등록
     * 
     * @param  TelewebJSON inJson
     * @return             String cnt
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   isolation = Isolation.READ_COMMITTED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON processInsertChatbotReady(TelewebJSON inJson) throws TelewebAppException
    {
        TelewebJSON outJson = new TelewebJSON();

        // 배분 대기 병합
        log.info("챗봇상담 및 대기 등록");
        outJson = routingToAgentReadyDAO.insertChbtCutt(inJson);

        return outJson;
    }


        /**
	     * 고객문의유형 처리
	     * 
	     * @param  TelewebJSON inJson
	     * @return             String outJson
	     */
	    @Transactional(propagation = Propagation.REQUIRED,
	                   isolation = Isolation.READ_COMMITTED,
	                   rollbackFor = {Exception.class, SQLException.class},
	                   readOnly = false)
	    public TelewebJSON processInqryType(TelewebJSON msgInfoJson, TelewebJSON inJson, JSONObject rcvJson) throws TelewebAppException
	    {
	        String telIdentifier = DateFormatUtils.format((new Date()), "HH:mm:ss") + "-" + UUID.randomUUID().toString().substring(0, 8);
	
	        TelewebJSON outJson = new TelewebJSON();
	        TelewebJSON selectReadyJson = new TelewebJSON();
	        log.info("===>"+ inJson.getString("TALK_USER_KEY") +":inq here####@@@@!!!!" + msgInfoJson);
	        log.info("===>"+ inJson.getString("TALK_USER_KEY") +":inq here####@@@@!!!!" + inJson);
	        log.info("===>"+ inJson.getString("TALK_USER_KEY") +":inq here####@@@@!!!!" + rcvJson);
	
	        String userKey = inJson.getString("TALK_USER_KEY");
	        String senderKey = inJson.getString("SNDR_KEY");
	        String custcoId = inJson.getString("CUSTCO_ID");
	        String readyToTalk = msgInfoJson.getString("MSG_READY_TO_TALK");
	        String readyToTalkId = msgInfoJson.getString("MSG_READY_TO_TALK_ID");
	        String inqryTypeMsg = msgInfoJson.getString("MSG_INQRY_TYPE_MSG");
	        String callTypCd = inJson.getString("CALL_TYP_CD");
	        String chnClsfCd = inJson.getString("CALL_TYP_CD");
	        String strReadyCode = "";
	        int intSelectingCnt = 0;
	
	        selectReadyJson = routingToAgentReadyDAO.selectTalkUserReadyCode(inJson);
	        intSelectingCnt = selectReadyJson.getHeaderInt("COUNT");
	        log.info("===>"+ inJson.getString("TALK_USER_KEY") +":selectReadyJson ::: {}", selectReadyJson);
	        log.info("===>"+ inJson.getString("TALK_USER_KEY") +":intSelectingCnt ::: {}", intSelectingCnt);
	        //outJson = routingToAgentReadyDAO.mergeTalkUserReadyDetail(inJson);
	
	        //인입된 채팅이 상담예약확인 문의유형인지 확인
	        //상담예약확인 프로세스 진행
	        TelewebJSON rsvtJson = new TelewebJSON();
	        rsvtJson = routingToAgentReadyDAO.selectRsvtCutt(inJson);
	        int intRsvtCnt = rsvtJson.getHeaderInt("COUNT");
	        log.info("===>"+ inJson.getString("TALK_USER_KEY") +":selectReadyJson ::: {}", rsvtJson);
	        log.info("===>"+ inJson.getString("TALK_USER_KEY") +":intRsvtCnt ::: {}", intRsvtCnt);
	        //outJson = routingToAgentReadyDAO.mergeTalkUserReadyDetail(inJson);
	        
	        if(intRsvtCnt > 0) {
	        	String rsvtMsg = rsvtProcess(inJson);
            	//시스템 메시지 발송
        		transToKakaoService.sendSystemMsg(userKey, senderKey, rsvtMsg, readyToTalkId, callTypCd, custcoId);
	        }
	        
	        // 이미 등록되어 있다면
	        if(intSelectingCnt > 0) {
	//            strReadyCode = selectReadyJson.getString("TALK_READY_CD");
	            strReadyCode = selectReadyJson.getString("ALTMNT_STTS_CD");
	
	            // 고객이 버튼을 클릭했다면
	            rcvJson.put("chnClsfCd",chnClsfCd);
	            if(isExistInqryType(rcvJson)) {
	            	if(chnClsfCd.equals("NTT")) {
	            		log.info("processInqryType 네이버톡톡 고객버튼클릭 >>"+rcvJson.getString("textContent"));
	                	JSONObject jsonObject = JSONObject.fromObject(rcvJson.getString("textContent"));
	                	TelewebJSON codeContentParam = new TelewebJSON();
	        			//mts 발송결과값 세팅
	        			for (Object key : jsonObject.keySet()) {
	        				log.info(key + " : " + jsonObject.get(key));
	        				codeContentParam.setString(key.toString(), jsonObject.get(key).toString());
	        	        }
	                    
		                inJson.setString("TALK_INQRY_CD", codeContentParam.getString("code"));
		                inJson.setString("QSTN_TYPE_ID", codeContentParam.getString("code"));
		                rcvJson.put("extra",codeContentParam.getString("code"));
	            	} else {
	            		if(rcvJson.getString("extra") == null) {
			                inJson.setString("TALK_INQRY_CD", "");
			                inJson.setString("QSTN_TYPE_ID", "");
	            		} else {
			                inJson.setString("TALK_INQRY_CD", rcvJson.getString("extra"));
			                inJson.setString("QSTN_TYPE_ID", rcvJson.getString("extra"));
	            		}
	            	}
	            	//rcvJson["reference"]:{"extra":"고객사에서 관리되는 커스텀한 메타 정보가 전달", "text": "","lastText":"채팅방에서 상담톡으로 전환 시 버튼으로부터 전달된 메타정보", "lastTextDate":"메타정보가 생성된 최신 시각"}}
	//            	  JSONObject qstnData = rcvJson.getJSONObject("reference");
	//            	  inJson.setString("TALK_INQRY_CD", qstnData.getString("extra"));
	//                inJson.setString("QSTN_TYPE_ID", qstnData.getString("extra"));
	
	                // 클릭한 버튼이 이전 버튼일 경우
	                if(isInqryBeforeBtn(rcvJson)) {
	                	String parentInqryCd = "";
	                    log.info("===>"+ inJson.getString("TALK_USER_KEY") +":클릭한 버튼이 이전 버튼일 경우 ::: strReadyCode {}", strReadyCode);
	                    if("QSTN_CHCING".equals(strReadyCode)) {
	                    	if(chnClsfCd.equals("NTT")) {
	                    		log.info("processInqryType 네이버톡톡 이전버튼 클릭 >>"+rcvJson.getString("textContent"));
	                        	JSONObject jsonObject = JSONObject.fromObject(rcvJson.getString("textContent"));
	                        	TelewebJSON codeContentParam = new TelewebJSON();
	                			//mts 발송결과값 세팅
	                			for (Object key : jsonObject.keySet()) {
	                				log.info(key + " : " + jsonObject.get(key));
	                				codeContentParam.setString(key.toString(), jsonObject.get(key).toString());
	                	        }
	        	                parentInqryCd = codeContentParam.getString("code").replaceAll("back_", "").toString();
	        	                JSONObject beforeInqryJson = new JSONObject();
		                        beforeInqryJson.put("code", parentInqryCd);
		                        rcvJson.put("extra",parentInqryCd);
	                    	} else {
		                        // get parentCd
		                        parentInqryCd = rcvJson.getString("extra").replaceAll("back_", "").toString();
		                        JSONObject beforeInqryJson = new JSONObject();
		                        beforeInqryJson.put("extra", parentInqryCd);
		                        inJson.setString("back", "Y");
	                    	}
	                        rcvJson.put("INQRY_STATUS", "BEFORE");  // 이전버튼 FLAG 
	                        transToKakaoService.sendInqryLevelTypeBtnMsg(userKey, senderKey, inqryTypeMsg, rcvJson, callTypCd);
	                        inJson.setString("TALK_INQRY_CD", parentInqryCd);
	                        inJson.setString("QSTN_TYPE_ID", parentInqryCd);
	                        //2. 문의유형 코드 업데이트
	                        routingToAgentReadyDAO.updateTalkUserReadyInqryCd(inJson);
	                    }
	                }
	                // 클릭한 버튼이 문의유형에 해당되는 버튼일 경우
	                else {
	                	//티톡일 때 안내메시지가 있는 문의유형일 경우 안내메시지 발송
	                	if(chnClsfCd.equals("TTALK")) {
	                        TelewebJSON selectInfoMsgJson = new TelewebJSON();
	                        selectInfoMsgJson = routingToAgentReadyDAO.selectInfoMsgJson(inJson);
	                        log.info("selectRsvtJson >>> " + selectInfoMsgJson);
	                        int intInfoMsgCnt = selectInfoMsgJson.getHeaderInt("TOT_COUNT");
	                        log.info("intInfoMsgCnt >>> " + intInfoMsgCnt);
	                        if(intInfoMsgCnt>0) {
	                        	//고객이 인입한 문의유형이 안내메시지가 있을 때
	                        	String InfoMsg = selectInfoMsgJson.getString("QSTN_TYPE_EXPLN").replaceAll("\"", "&quot;");
	                        	//시스템 메시지 발송
	                    		transToKakaoService.sendSystemMsg(userKey, senderKey, InfoMsg, readyToTalkId, callTypCd, custcoId);
	                        }
	                	}
	                    // 고객이 클릭한 버튼의 값이 마지막 레벨의 문의유형이면 상담사배분 대기 상태로 업데이트
	                    // 상담사연결 버튼일 경우 대기 상태로 업데이트 (경희사이버대학교)
	                    if(isInqryFinalLevel(rcvJson)) {
	                        log.info("===>"+ inJson.getString("TALK_USER_KEY") +":고객이 클릭한 버튼의 값이 마지막 레벨의 문의유형이면 상담사배분 대기 상태로 업데이트 strReadyCode {} ", strReadyCode);
	                        if("QSTN_CHCING".equals(strReadyCode)) {
	
	                            JSONArray inqryTypeArr = selectInqryTypeIsMessage(rcvJson);
	
	                            log.info("===>"+ inJson.getString("TALK_USER_KEY") +"고객이 클릭한 버튼의 문의유형 타입 ::: {}", inqryTypeArr.getJSONObject(0).getString("INQRY_TYPE"));
	                            if("MESSAG".equals(inqryTypeArr.getJSONObject(0).getString("INQRY_TYPE"))) {
	                                log.info("===>"+ inJson.getString("TALK_USER_KEY") +"고객이 클릭한 버튼의 문의유형 정보 ::: {}", inqryTypeArr.getJSONObject(0));
	                                //문의유형 신규화면 TOBE 로직  시작 - 백업 2020-09-24(lgc)
	                                JSONObject param = new JSONObject();
	                                String image = inqryTypeArr.getJSONObject(0).getString("IMAGE");
	                                String fileKey = inqryTypeArr.getJSONObject(0).getString("FILE_KEY");
	                                String fileGroupKey = inqryTypeArr.getJSONObject(0).getString("FILE_GROUP_KEY");
	                                readyToTalk = inqryTypeArr.getJSONObject(0).getString("INQRY_DESC");
	                                if(image != null && !image.isEmpty()) {
	                                    param.put("IMAGE", image);
	                                } else {
	                                	//값이 없으면 오류발생 - 빈값 세팅
	                                    param.put("IMAGE", "");
	                                }
	                                if(fileKey != null && !fileKey.isEmpty()) {
	                                    param.put("FILE_KEY", fileKey);
	                                } else {
	                                	//값이 없으면 오류발생 - 빈값 세팅
	                                    param.put("FILE_KEY", "");
	                                }
	                                if(fileGroupKey != null && !fileGroupKey.isEmpty()) {
	                                    param.put("FILE_GROUP_KEY", fileGroupKey);
	                                } else {
	                                	//값이 없으면 오류발생 - 빈값 세팅
	                                    param.put("FILE_GROUP_KEY", "");
	                                }
	                                log.info("===>"+ inJson.getString("TALK_USER_KEY") +"고객이 클릭한 버튼의 문의유형 정보 파라메터 세팅 ::: {}", param);
	                                // inqryType이 MESSAGE 일 경우 TWB_TALK_CONTACT으로 이관 및 메시지 전송 후 종료처리.
	                                updateTalkUserReadyInqryCdIsThisCode(inJson);
	                                processTalkReadyToTalkContact(inJson);
	                                transToKakaoService.sendSystemButtonLinkMsgReadyDetail(userKey, senderKey, readyToTalk, "", "", callTypCd, custcoId, param);
	                                //문의유형 신규화면 TOBE 로직  종료
	
	                                //20200528 LYJ :: 카카오톡 메시지형 문의유형 종료처리.
	                                JSONObject obj = new JSONObject();
	                                obj.put("user_key", userKey);
	                                obj.put("sndrKey", senderKey);
	                                obj.put("CHT_CUTT_DTL_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID")));
	
	                                // 스프링 이벤트로 처리 (transToKakaoService.transToEndTalkEvent)
	                                eventPublisher.publishEvent(TransToEndTalkEvent.builder().active("endtalk").writeData(obj).callTypCd(chnClsfCd).build());
	                            } //상담 예약 확인일 경우
	                            else if("RSVT".equals(inqryTypeArr.getJSONObject(0).getString("INQRY_TYPE"))) {
	                            	log.info("문의유형 - 예약확인버튼 클릭");
	                                //문의유형 신규화면 TOBE 로직  시작 - 백업 2020-09-24(lgc)
	                                JSONObject param = new JSONObject();
	                                String image = inqryTypeArr.getJSONObject(0).getString("IMAGE");
	                                String fileKey = inqryTypeArr.getJSONObject(0).getString("FILE_KEY");
	                                String fileGroupKey = inqryTypeArr.getJSONObject(0).getString("FILE_GROUP_KEY");
	                                readyToTalk = inqryTypeArr.getJSONObject(0).getString("INQRY_DESC");
	                                if(image != null && !image.isEmpty()) {
	                                    param.put("IMAGE", image);
	                                } else {
	                                	//값이 없으면 오류발생 - 빈값 세팅
	                                    param.put("IMAGE", "");
	                                }
	                                if(fileKey != null && !fileKey.isEmpty()) {
	                                    param.put("FILE_KEY", fileKey);
	                                } else {
	                                	//값이 없으면 오류발생 - 빈값 세팅
	                                    param.put("FILE_KEY", "");
	                                }
	                                if(fileGroupKey != null && !fileGroupKey.isEmpty()) {
	                                    param.put("FILE_GROUP_KEY", fileGroupKey);
	                                } else {
	                                	//값이 없으면 오류발생 - 빈값 세팅
	                                    param.put("FILE_GROUP_KEY", "");
	                                }
	                                log.info("===>"+ inJson.getString("TALK_USER_KEY") +"고객이 클릭한 버튼의 문의유형 정보 파라메터 세팅 ::: {}", param);
	                                // inqryType이 RSVT 일 경우 메시지 전송 후 상담 상태 예약확인상태로 변경
	                                updateTalkUserReadyInqryCdIsThisCode(inJson);
	                                String link = "";
	                                TelewebJSON RsvtBtn = getRsvtBtn(inJson);
	                                JSONArray getRsvtBtnList = RsvtBtn.getDataObject();
	                                if(getRsvtBtnList.size() > 0) {
	                                    for(int i = 0; i < getRsvtBtnList.size(); i++) {
	                                    	JSONObject obj = getRsvtBtnList.getJSONObject(i);
	                                    	obj.getString("HR");
	                                    	if(i == 0) {
	                                    		link = "{\"name\":\""+obj.getString("HR")+"\",\"type\":\"BK\",\"extra\":\""+obj.getString("HR")+"\"}";
	                                    	} else {
	                                    		link = link+",{\"name\":\""+obj.getString("HR")+"\",\"type\":\"BK\",\"extra\":\""+obj.getString("HR")+"\"}";
	                                    	}
	                                    }
	                                }
                                	log.info("link ::: " + link);
                                	
                                    JSONObject sendJson = null;
                                    String serial = Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID"));
                                    String filter2Msg = paletteFilterUtils.filter4(readyToTalk);
                                    String sendString = "";
                                    if(!StringUtils.isEmpty(param.getString("FILE_GROUP_KEY")) && !StringUtils.isEmpty(param.getString("FILE_KEY"))) {
                                        sendString = String.format(
                                            "{\"user_key\":\"%s\",\"sndrKey\":\"%s\",\"CHT_CUTT_DTL_ID\":\"%s\",\"message_type\":\"RSVT\",\"message\":\"%s\",\"links\":[%s],\"image\":\"%s\"}",
                                            userKey, senderKey, serial, filter2Msg, link, param.getString("IMAGE"));
                                        sendJson = JSONObject.fromObject(sendString);

                                        // [파일o] 카카오톡 전송: 채팅-대기중 링크버튼 메시지를 전송
                                        final RepositoryTaskTypeCd taskTypeCd = RepositoryTaskTypeCd.chat;    //채팅
                                        final RepositoryPathTypeCd pathTypeCd = RepositoryPathTypeCd.images;  //문의유형 이미지
                                        final FilePropertiesResponse fileProperties = fileRulePropertiesUtils.getProperties(taskTypeCd, pathTypeCd);
                                        log.debug("fileProperties>>>{}", fileProperties);

                                        //이미지 처리
                                        sendJson.put("IMAGE_TYPE", param.getString("IMAGE_TYPE"));
                                        sendJson.put("FILE_GROUP_KEY", param.getString("FILE_GROUP_KEY"));
                                        sendJson.put("FILE_KEY", param.getString("FILE_KEY"));
                                        sendJson.put("USER_ID", "system");
                                    } else {
                                    	sendString = String.format(
                        	                "{\"user_key\":\"%s\",\"sndrKey\":\"%s\",\"CHT_CUTT_DTL_ID\":\"%s\",\"message_type\":\"RSVT\",\"message\":\"%s\",\"links\":[%s]}",
                        	                userKey, senderKey, serial, filter2Msg, link);
                        	            sendJson = JSONObject.fromObject(sendString);
                                    }

                                    JSONObject ret = transToKakaoService.transToKakao("write", sendJson, callTypCd);

                                    if (readyToTalk != null && !"".equals(readyToTalk)) {
                                        inJson.setString("TALK_USER_KEY", userKey);
                                        inJson.setString("TALK_SERIAL_NUMBER", serial);
                                        inJson.setString("CHT_CUTT_DTL_ID", serial);
                                        inJson.setString("SNDR_KEY", senderKey);
                                        inJson.setString("CUSTCO_ID", custcoId);
                                        inJson.setString("TALK_API_CD", "/message");
                                        inJson.setString("TYPE", "system");
                                        inJson.setString("CONTENT", filter2Msg+"||"+link);
                                        inJson.setString("SYS_MSG_ID", "");
                                        inJson.setString("IMAGE_URL", image);
                                        inJson.setString("IMAGE_TALK_PATH", image);
                                        inJson.setString("MSG_TYPE_CD", "RSVT");
                                        inJson.setString("RCPTN_DSPTCH_CD", "SND");

                            	        insertTalkContactDetail(inJson);
                                        //상담 예약 폼을 보낸 뒤 종료처리
                                        //종료처리가 아닌 상담 상태 예약상태로 변경처리
                                        //상담예약은 상담원을 연결하지 않는다
                                        updateChtCuttRsvt(inJson);
                                    }
	                            } //문의유형 - API 일때
	                            else if("API".equals(inqryTypeArr.getJSONObject(0).getString("INQRY_TYPE"))) {
	                                //문의유형 신규화면 TOBE 로직  시작 - 백업 2020-09-24(lgc)
	                            	log.info("문의유형 - API");
	                                JSONObject param = new JSONObject();
	                                String image = inqryTypeArr.getJSONObject(0).getString("IMAGE");
	                                String fileKey = inqryTypeArr.getJSONObject(0).getString("FILE_KEY");
	                                String fileGroupKey = inqryTypeArr.getJSONObject(0).getString("FILE_GROUP_KEY");
	                                readyToTalk = inqryTypeArr.getJSONObject(0).getString("INQRY_DESC");
	                                if(image != null && !image.isEmpty()) {
	                                    param.put("IMAGE", image);
	                                } else {
	                                	//값이 없으면 오류발생 - 빈값 세팅
	                                    param.put("IMAGE", "");
	                                }
	                                if(fileKey != null && !fileKey.isEmpty()) {
	                                    param.put("FILE_KEY", fileKey);
	                                } else {
	                                	//값이 없으면 오류발생 - 빈값 세팅
	                                    param.put("FILE_KEY", "");
	                                }
	                                if(fileGroupKey != null && !fileGroupKey.isEmpty()) {
	                                    param.put("FILE_GROUP_KEY", fileGroupKey);
	                                } else {
	                                	//값이 없으면 오류발생 - 빈값 세팅
	                                    param.put("FILE_GROUP_KEY", "");
	                                }
	                                log.info("===>"+ inJson.getString("TALK_USER_KEY") +"고객이 클릭한 버튼의 문의유형 정보 파라메터 세팅 ::: {}", param);
	                                // inqryType이 RSVT 일 경우 메시지 전송 후 상담 상태 예약확인상태로 변경
	                                updateTalkUserReadyInqryCdIsThisCode(inJson);
	                                transToKakaoService.sendSystemButtonLinkMsgReadyDetail(userKey, senderKey, readyToTalk, "", "", callTypCd, custcoId, param);
	                            }
	                            else {
	                                outJson = routingToAgentReadyDAO.updateTalkUserReady10(inJson);
	                                transToKakaoService.sendSystemMsg(userKey, senderKey, readyToTalk, readyToTalkId, callTypCd, custcoId);
	                            }
	                        }
	                    }
	                    // 고객이 클릭한 버튼의 값이 마지막 레벨의 문의유형이 아니라면, 다음 문의유형 전송
	                    else {
	                        log.info("===>"+ inJson.getString("TALK_USER_KEY") +":고객이 클릭한 버튼의 값이 마지막 레벨의 문의유형이 아니라면, 다음 문의유형 전송 strReadyCode {} ", strReadyCode);
	                        if("QSTN_CHCING".equals(strReadyCode)) {
	                            // 문의유형 업데이트
	                            updateTalkUserInqryCd(inJson);
	                            rcvJson.put("INQRY_STATUS", "NEXT");    // 문의유형 선택 FLAG 
	
	                            transToKakaoService.sendInqryLevelTypeBtnMsg(userKey, senderKey, inqryTypeMsg, rcvJson, callTypCd);
	                        }
	                    }
	                }
	            }
	        }
	        // 고객의 첫메시지라면 문의유형메시지를 전송한다.
	        else if(intSelectingCnt == 0) {
	            log.info("===>"+ inJson.getString("TALK_USER_KEY") +":고객의 첫메시지라면 문의유형메시지를 전송한다 inJson {} ", inJson);
	            // 고객문의유형선택 배분 대기 및 상세 병합
	            if(chnClsfCd.equals("NTT")) {
		            log.info("processInqryType 네이버톡톡 고객버튼클릭 >>"+rcvJson.getString("textContent"));
		        	JSONObject jsonObject = JSONObject.fromObject(rcvJson.getString("textContent"));
		        	TelewebJSON codeContentParam = new TelewebJSON();
					//mts 발송결과값 세팅
					for (Object key : jsonObject.keySet()) {
						log.info(key + " : " + jsonObject.get(key));
						codeContentParam.setString(key.toString(), jsonObject.get(key).toString());
			        }
					
					if(codeContentParam.getString("code").equals("counseling")) {
						outJson = routingToAgentReadyDAO.insertChbtCutt(inJson);
					} else {
			            outJson = routingToAgentReadyDAO.insertTalkUserReady(inJson);
					}
	            } else {
	            	outJson = routingToAgentReadyDAO.insertTalkUserReady(inJson);
	            }
	
	            log.info("===>"+ inJson.getString("TALK_USER_KEY") +":receive Json ::: {}", rcvJson);
	
	            // 2018.11.14 kmg 기존 문의유형 내보는 함수 사용하지 않음.
	            if(rcvJson.containsKey("BOT_SERVICE_NM") && !"".equals(rcvJson.get("BOT_SERVICE_NM"))) {
	                log.info("===>"+ inJson.getString("TALK_USER_KEY") +":[{}] service nm exists {}", telIdentifier, inJson);
	
	                outJson = routingToAgentReadyDAO.updateTalkUserReady10(inJson);
	                transToKakaoService.sendSystemMsg(userKey, senderKey, readyToTalk, readyToTalkId, callTypCd, custcoId);
	            }
	            else {
	                log.info("===>"+ inJson.getString("TALK_USER_KEY") +":[{}] service nm not exists {}", telIdentifier, inJson);
	                rcvJson.put("INQRY_STATUS", "BEGIN");   // 문의유형 선택 FLAG 
	
	                log.info("===>"+ inJson.getString("TALK_USER_KEY") +":rrrrrrrrrrrrrrrrrcvJsonnnnnnnnnnnnnnnnnnn" + rcvJson);
	
	                transToKakaoService.sendInqryLevelTypeBtnMsg(userKey, senderKey, inqryTypeMsg, rcvJson, callTypCd);
	            }
	        }
	        else {
	            log.info("===>"+ inJson.getString("TALK_USER_KEY") +":[{}] **intSelectingCnt ::: {}", telIdentifier, intSelectingCnt);
	        }
	        return outJson;
	    }


    /**
     * 상담대기 처리 (사용안함 - 20191030)
     * 
     * @param TelewebJSON inJson
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   isolation = Isolation.READ_COMMITTED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public void processTalkReady(TelewebJSON inJson) throws TelewebAppException
    {
        //READY:AGENT_READY - 상담사대기완료
        String userId = inJson.getString("USER_ID");
        chatReadyRepository.setStompVO(ChatStompVO.builder().agentId(userId).build());
        routingToAgentReadyDAO.insertTalkReady(inJson);

//        //2018.11.12 kmg readyOff 데이터 이관 및 초기화 추가
//        //registerReadyOffHist(inJson);
    }


    /**
     * 상담대기 삭제 처리
     * 
     * @param TelewebJSON inJson
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   isolation = Isolation.READ_COMMITTED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public void processDeleteTalkReady(TelewebJSON inJson) throws TelewebAppException
    {
        //READY:DISCONNECT - 상담대기 연결분리완료
        int userId = inJson.getInt("USER_ID");
        chatReadyRepository.removeUserId(userId);
        if(!"NothingAgentId".equals(userId)) {
            routingToAgentReadyDAO.deleteTalkReady(inJson);
        }
    }


    /**
     * 진행 중 상담 삭제
     * 
     * @param  HashMap
     * @return         TelewebJSON 형식의 처리 결과 데이터
     */
    public TelewebJSON deleteTalkIng(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "deleteTalkIng", mjsonParams);

        return objRetParams;
    }


    /**
     * 진행 중 상담 삭제
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    public TelewebJSON mergeTalkLast(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "mergeTalkLast", mjsonParams);

        return objRetParams;
    }


    /**
     * 배분 대기 건 수 조회(래퍼런스에서 사용)
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    public TelewebJSON selectCntTalkUserReadyKey(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectCntTalkUserReadyKey", mjsonParams);

        return objRetParams;
    }


    /**
     * 배분대기 라우팅 챗봇 CD 변경
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    public TelewebJSON updateTalkUserReadyRouting(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "updateTalkUserReadyRouting", mjsonParams);

        return objRetParams;
    }


    /**
     * 상담 건수 등록
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    public TelewebJSON mergeTalkStack(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "mergeTalkStack", mjsonParams);

        return objRetParams;
    }


    /**
     * 대기건 상세정보 조회
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    public TelewebJSON selectTalkUserReadyInfoByWcnct(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectTalkUserReadyInfoByWcnct", mjsonParams);

        return objRetParams;
    }


    public TelewebJSON selectTalkUserInfo(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectTalkUserInfo", mjsonParams);

        return objRetParams;
    }


    public TelewebJSON selectTalkUserCustSeq(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "getUserCustseq", mjsonParams);

        return objRetParams;

    }


    /**
     * 라우팅 조회
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    public TelewebJSON selectTalkRouteToAgent(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectTalkRouteToAgent", mjsonParams);

        return objRetParams;
    }


    /**
     * 전문상담 라우팅 조회
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    public TelewebJSON selectTalkRouteToSpecAgent(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectTalkRouteToSpecAgent", mjsonParams);

        return objRetParams;
    }


    /**
     * 문의유형 코드 업데이트
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    public TelewebJSON updateTalkUserInqryCd(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "updateTalkUserInqryCd", mjsonParams);

    	String chtCuttDtlId = Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID"));
    	mjsonParams.setString("CHT_CUTT_DTL_ID", chtCuttDtlId);
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "insertCuttDtlReady", mjsonParams);
        
        return objRetParams;
    }


    /**
     * 다음레벨 문의유형 조회
     * 
     * @author        kmg
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2018.11.08
     */
    public TelewebJSON selectInqryLevelType(JSONObject rcvJson) throws TelewebAppException
    {
        TelewebJSON jsonParams = new TelewebJSON();

        if(rcvJson.containsKey("extra")) {
            jsonParams.setString("TALK_INQRY_CD", rcvJson.getString("extra").replaceAll("back_", "").toString());
            jsonParams.setString("QSTN_TYPE_ID", rcvJson.getString("extra").replaceAll("back_", "").toString());
        }
        else {
            if(!"BEGIN".equals(rcvJson.getString("INQRY_STATUS"))) {
                jsonParams.setString("TALK_INQRY_CD", rcvJson.getString("TALK_INQRY_CD"));
                jsonParams.setString("QSTN_TYPE_ID", rcvJson.getString("QSTN_TYPE_ID"));
            }
        }

        jsonParams.setString("INQRY_STATUS", rcvJson.getString("INQRY_STATUS"));
        jsonParams.setString("INQRY_USE_CHANNEL", rcvJson.getString("asp_sndrKey"));
        jsonParams.setString("CUSTCO_ID", rcvJson.getString("CUSTCO_ID"));
        jsonParams.setString("SNDR_KEY", rcvJson.getString("SNDR_KEY"));

        TelewebJSON objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectInqryLevelType", jsonParams);

        return objRetParams;
    }


    /**
     * 문의유형값 존재 여부 조회
     * 
     * @author        kmg
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2018.11.08
     */
    public TelewebJSON selectInqryCode(JSONObject rcvJson, String InqryCode) throws TelewebAppException
    {
        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setString("INQRY_USE_CHANNEL", rcvJson.getString("asp_sndrKey"));
        jsonParams.setString("SNDR_KEY", rcvJson.getString("SNDR_KEY"));
        jsonParams.setString("CUSTCO_ID", rcvJson.getString("custco_id"));
        jsonParams.setString("TALK_INQRY_CD", InqryCode);
        jsonParams.setString("QSTN_TYPE_ID", InqryCode);

        //DAO검색 메서드 호출
        TelewebJSON objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectInqryCode", jsonParams);
        return objRetParams;
    }


    /**
     * 하위레벨의 문의유형이 존재하는지 확인
     * 
     * @author        kmg
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2018.11.08
     */
    public TelewebJSON selectInqryExistAlone(String InqryCode, JSONObject rcvJson) throws TelewebAppException
    {
        TelewebJSON jsonParams = new TelewebJSON();
        
        jsonParams.setString("INQRY_USE_CHANNEL", rcvJson.getString("asp_sndrKey"));
        jsonParams.setString("SNDR_KEY", rcvJson.getString("SNDR_KEY"));
        jsonParams.setString("CUSTCO_ID", rcvJson.getString("custco_id"));
        jsonParams.setString("TALK_INQRY_CD", InqryCode);
        jsonParams.setString("QSTN_TYPE_ID", InqryCode);

        //DAO검색 메서드 호출
        TelewebJSON objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectChildInqryCode", jsonParams);
        return objRetParams;
    }


    /**
     * 문의유형 타입 판단 (MESSAGE / AGENT) * @author kmg
     * 
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2018.11.08
     */
    public TelewebJSON selectInqryTypeIsAgentOrMessage(String InqryCode, JSONObject rcvJson) throws TelewebAppException
    {
        TelewebJSON jsonParams = new TelewebJSON();
        
        jsonParams.setString("INQRY_USE_CHANNEL", rcvJson.getString("asp_sndrKey"));
        jsonParams.setString("SNDR_KEY", rcvJson.getString("SNDR_KEY"));
        jsonParams.setString("CUSTCO_ID", rcvJson.getString("custco_id"));
        jsonParams.setString("TALK_INQRY_CD", InqryCode);
        jsonParams.setString("QSTN_TYPE_ID", InqryCode);

        //DAO검색 메서드 호출
        TelewebJSON objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectInqryTypeIsAgentOrMessage", jsonParams);
        return objRetParams;
    }


    /**
     * 메시지ID에 해당하는 링크버튼 리스트 조회
     * 
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     */
    public TelewebJSON selectLinksButtonList(String strSystemMsgId, String strCustcoId) throws TelewebAppException
    {
        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setString("SYS_MSG_ID", strSystemMsgId);
        jsonParams.setString("CUSTCO_ID", strCustcoId);

        //DAO검색 메서드 호출
        TelewebJSON objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectLinksButtonList", jsonParams);
        return objRetParams;
    }


    /**
     * 입력된 코드가 문의유형코드가 맞는지 여부 리쿼시브 함수 제거로 로직 변경 ( 전체 문의유형을 가지고 올 필요가 없음 ) , 20190507 SJH
     * 
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     */
    public boolean isExistInqryType(JSONObject rcvJson) throws TelewebAppException
    {
    	log.info("isExistInqryType 고객이 선택한 문의유형이 무엇인지 체크 isExistInqryType >>현재 채널>>"+rcvJson.getString("chnClsfCd"));
        boolean isExist = false;
        String strInCode = "";
        TelewebJSON inqryTypeJson = new TelewebJSON();

    	log.info("isExistInqryType 고객이 상담원에게 전달한 메시지 전문 rcvJson >>"+rcvJson);
        if(rcvJson.getString("chnClsfCd").equals("NTT")) {
        	log.info("isExistInqryType 네이버톡톡일때 선택한 버튼(code)가 textContent라는 object에 들어있음 >>"+rcvJson.getString("textContent"));
        	JSONObject jsonObject = JSONObject.fromObject(rcvJson.getString("textContent"));
        	TelewebJSON codeContentParam = new TelewebJSON();
			//mts 발송결과값 세팅
			for (Object key : jsonObject.keySet()) {
				log.info(key + " : " + jsonObject.get(key));
				codeContentParam.setString(key.toString(), jsonObject.get(key).toString());
	        }
            strInCode = codeContentParam.getString("code");
            if(strInCode.contains("back_")) {
                isExist = true;

                return isExist;
            }
        } else {
	        if(rcvJson.has("extra")) {
	            strInCode = rcvJson.getString("extra");
	            if(strInCode.contains("back_")) {
	                isExist = true;
	
	                return isExist;
	            }
	        }
        }

        rcvJson.put("TALK_INQRY_CD", strInCode);
        rcvJson.put("QSTN_TYPE_ID", strInCode);
        inqryTypeJson = selectInqryCode(rcvJson, strInCode);
        if(inqryTypeJson.getHeaderInt("TOT_COUNT") >= 1) {
            isExist = true;
        }
        

        log.info("isExistInqryType res ====== " + isExist);
        return isExist;
    }


    /**
     * 입력된 코드가 마지막 문의유형 레벨인지 판단 * 리쿼시브 함수 제거로 로직 변경 ( 전체 문의유형을 가지고 올 필요가 없음 ) , 20190507 SJH
     * 
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     */
    public boolean isInqryFinalLevel(JSONObject rcvJson) throws TelewebAppException
    {
    	log.info("isInqryFinalLevel 고객이 선택한 문의유형이 무엇인지 체크 isExistInqryType >>현재 채널>>"+rcvJson.getString("chnClsfCd"));
        // 문의유형 사용 레벨 값
        int finalInqryLevel = Integer.parseInt(HcTeletalkDbEnvironment.getInstance().getString(rcvJson.getString("custco_id"), "INQRY_USE_LVL"));

        boolean isExist = false;
        String strInCode = "";
        TelewebJSON inqryTypeJson = new TelewebJSON();

        TelewebJSON inqryListJson = new TelewebJSON();
        TelewebJSON inqryExistAloneJson = new TelewebJSON();

        log.info("isInqryFinalLevel 고객이 상담원에게 전달한 메시지 전문 rcvJson >>"+rcvJson);
        if(rcvJson.getString("chnClsfCd").equals("NTT")) {
        	log.info("isInqryFinalLevel 네이버톡톡일때 선택한 버튼(code)가 textContent라는 object에 들어있음 >>"+rcvJson.getString("textContent"));
        	JSONObject jsonObject = JSONObject.fromObject(rcvJson.getString("textContent"));
        	TelewebJSON codeContentParam = new TelewebJSON();
			//mts 발송결과값 세팅
			for (Object key : jsonObject.keySet()) {
				log.info(key + " : " + jsonObject.get(key));
				codeContentParam.setString(key.toString(), jsonObject.get(key).toString());
	        }
            strInCode = codeContentParam.getString("code");
            if(strInCode.contains("back_")) {
                isExist = true;

                return isExist;
            }
        } else {
	        if(rcvJson.has("extra")) {
	            strInCode = rcvJson.getString("extra");
	        }
        }

        inqryExistAloneJson = selectInqryExistAlone(strInCode, rcvJson);

        // 1. 현재 선택된 문의유형의 하위레벨 문의유형이 있는지 확인 (없을 경우, 현재 문의유형이 마지막 문의유형)
        if(inqryExistAloneJson.getHeaderInt("TOT_COUNT") == 0) {
            isExist = true;
        }
        else {
            inqryTypeJson = selectInqryCode(rcvJson, strInCode);
            if(inqryTypeJson.getHeaderInt("TOT_COUNT") >= 1) {
                JSONArray inqryListJsonArr = inqryTypeJson.getDataObject("DATA");
                if(inqryListJsonArr.getJSONObject(0).getString("QSTN_TYPE_SE_CD") != null) {
                    int inqryLvl = Integer.parseInt(inqryListJsonArr.getJSONObject(0).getString("QSTN_TYPE_SE_CD"));
                    if(finalInqryLevel == inqryLvl) {
                        isExist = true;
                    }
                }
            }
        }

       log.info("isInqryFinalLevel res ====== " + isExist);
        return isExist;
    }


    /**
     * 입력된 문의유형의 타입 및 DESCRIPTION 정보 조회
     * 
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     */
    public JSONArray selectInqryTypeIsMessage(JSONObject rcvJson) throws TelewebAppException
    {
    	log.info("selectInqryTypeIsMessage 고객이 선택한 문의유형이 무엇인지 체크 isExistInqryType >>현재 채널>>"+rcvJson.getString("chnClsfCd"));
        String strInCode = "";
        TelewebJSON inqryJson = new TelewebJSON();
        
        log.info("isInqryFinalLevel 고객이 상담원에게 전달한 메시지 전문 rcvJson >>"+rcvJson);
        if(rcvJson.getString("chnClsfCd").equals("NTT")) {
        	log.info("isInqryFinalLevel 네이버톡톡일때 선택한 버튼(code)가 textContent라는 object에 들어있음 >>"+rcvJson.getString("textContent"));
        	JSONObject jsonObject = JSONObject.fromObject(rcvJson.getString("textContent"));
        	TelewebJSON codeContentParam = new TelewebJSON();
			//mts 발송결과값 세팅
			for (Object key : jsonObject.keySet()) {
				log.info(key + " : " + jsonObject.get(key));
				codeContentParam.setString(key.toString(), jsonObject.get(key).toString());
	        }
            strInCode = codeContentParam.getString("code");
        } else {
	        if(rcvJson.has("extra")) {
	            strInCode = rcvJson.getString("extra");
	        }
        }

        inqryJson = selectInqryTypeIsAgentOrMessage(strInCode, rcvJson);
        JSONArray inqryJsonArr = inqryJson.getDataObject("DATA");

        return inqryJsonArr;
    }


    /**
     * 이전 버튼 클릭 여부 확인
     * 
     * @param  routeJson
     * @param  inJson
     * @throws TelewebAppException
     */
    public boolean isInqryBeforeBtn(JSONObject rcvJson) throws TelewebAppException
    {
    	log.info("isInqryBeforeBtn 고객이 선택한 문의유형이 무엇인지 체크 isExistInqryType >>현재 채널>>"+rcvJson.getString("chnClsfCd"));
        boolean isExist = false;
        String strInCode = "";
        log.info("isInqryFinalLevel 고객이 상담원에게 전달한 메시지 전문 rcvJson >>"+rcvJson);
        if(rcvJson.getString("chnClsfCd").equals("NTT")) {
        	log.info("isInqryFinalLevel 네이버톡톡일때 선택한 버튼(code)가 textContent라는 object에 들어있음 >>"+rcvJson.getString("textContent"));
        	JSONObject jsonObject = JSONObject.fromObject(rcvJson.getString("textContent"));
        	TelewebJSON codeContentParam = new TelewebJSON();
			//mts 발송결과값 세팅
			for (Object key : jsonObject.keySet()) {
				log.info(key + " : " + jsonObject.get(key));
				codeContentParam.setString(key.toString(), jsonObject.get(key).toString());
	        }
            strInCode = codeContentParam.getString("code");
            if(strInCode.contains("back_")) {
                isExist = true;

                return isExist;
            }
        } else {
	        if(rcvJson.has("extra")) {
	            strInCode = rcvJson.getString("extra");
	        }
        }

        if(strInCode.contains("back_")) {
            isExist = true;
        }
        

        log.info("isInqryBeforeBtn res ====== " + isExist);
        return isExist;
    }


    /**
     * 상담이력 자동인사여부 조회
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   isolation = Isolation.READ_COMMITTED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON selectTalkContactAutoGreeting(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectTalkContactAutoGreeting", mjsonParams);

        return objRetParams;
    }


    /**
     * 상담이력 자동인사여부 'Y'로 업데이트
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW,
                   isolation = Isolation.READ_COMMITTED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON updateTalkContactAutoGreetingY(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateTalkContactAutoGreetingY", mjsonParams);

        return objRetParams;
    }


    /**
     * 2018.11.30 kmg 이관 전 문의유형 업데이트
     * 
     * @param TelewebJSON
     */
    public void updateTalkUserReadyInqryCdIsThisCode(TelewebJSON mjsonParams) throws TelewebAppException
    {
        mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateTalkUserReadyInqryCdIsThisCode", mjsonParams);
    }


    /**
     * 채팅-문의유형-예약시간 버튼 세팅
     * 
     * @param TelewebJSON
     */
    public TelewebJSON getRsvtBtn(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "getRsvtBtn", mjsonParams);
    }


    /**
     * 2018.11.30 kmg 문의유형 이관처리.
     * 
     * @param TelewebJSON
     */
    public void processTalkReadyToTalkContact(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON inJson = new TelewebJSON();
        String strBizCase = "TALK";

        //상담이력 ID를 가져온다.
        inJson.setString("CHT_USER_HSTRY_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_USER_HSTRY_ID")));
        inJson.setString("TALK_USER_KEY", mjsonParams.getString("TALK_USER_KEY"));
        inJson.setString("CHT_USER_KEY", mjsonParams.getString("CHT_USER_KEY"));
        inJson.setString("SNDR_KEY", mjsonParams.getString("SNDR_KEY"));

        //메시지 전송 후 데이터 이관
//        mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertTalkReadyToTalkContact", inJson);

        //고객대기에서 대기이력테이블에 데이터 저장
        mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertRtnTalkReadyHist", inJson);

        //고객대기에서 해당 데이터 삭제
        mobjDao.delete("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "deleteRtnTalkReadyInfo", inJson);

        //상담이력상세에 저장
//        mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertRtnTalkHistDetailInfo", inJson);

        //고객대기상세에서 해당 데이터 삭제
//        mobjDao.delete("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "deleteRtnTalkReadyDetailInfo", inJson);
    }


    public void insertOrgContent(OrgContentVO orgContentVO) throws TelewebAppException
    {
        orgContentVO.setOrgContId(innbCreatCmmnService.getSeqNo("TWB_TALK_CONTACT_ORG_CONT_SEQ"));
        talkRouteMapper.insertOrgContent(orgContentVO);
    }


    public void insertOrgFile(OrgFileVO orgFileVO) throws TelewebAppException
    {
        talkRouteMapper.insertOrgFile(orgFileVO);
    }
    
    /**
     * 인입 메시지 조회
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW,
                   isolation = Isolation.READ_COMMITTED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON selectNowCutt(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectNowCutt", mjsonParams);

        return objRetParams;
    }


    
    /**
     * 상담 예약 조회
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    public String rsvtProcess(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	log.info("상담예약조회_rsvtProcess"+ mjsonParams);
    	
    	String input = mjsonParams.getString("CONTENT").replaceAll(" ","");
    	log.info("input"+input);
    	String pattern = "\\d{4}년\\d{2}월\\d{2}일\\d{2}시\\d{2}분";
    	log.info("pattern"+pattern);
    	
    	String sysMsg = "";
    	if(!mjsonParams.getString("TYPE").equals("text")) {
    		//고객이 입력한 채팅이 문자가 아니라면
    		sysMsg = "예약확인은 문자를 직접 입력하여 확인 가능합니다.";
    	} else if(!Pattern.matches(pattern, input)){
    		//고객이 입력한 채팅의 형식이 맞지 않는다면
    		sysMsg = "예시)의 문자형식을 정확히 맞추어 입력해주세요";
    	} else if(mjsonParams.getString("TYPE").equals("text") && Pattern.matches(pattern, input)) {
    		if(input.equals("2024년05월05일13시30분")) {
    			sysMsg = "해당 날짜는 예약 가능합니다.";
    		} else {
    			sysMsg = "해당 날짜는 예약이 불가능합니다.";
    		}
    	}
    	return sysMsg;
    }
    
    /**
     * 고객 안내메시지 입력
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    public TelewebJSON processInputInfoMsg(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	log.info("안내메시지 입력_processInputInfoMsg"+ mjsonParams);

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateInputInfoMsg", mjsonParams);

        return objRetParams;
    }
    
    /**
     * 상담예약
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    public TelewebJSON processRsvt(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	log.info("상담예약_processRsvt"+ mjsonParams);

		TelewebJSON extRetParams = new TelewebJSON(mjsonParams);
		String apiUrl = "getMember?MEMBER_ID=" + mjsonParams.getString("CHT_USER_KEY");
		extRetParams = getExtApi(apiUrl);
		log.info("sw고객정보 가져오기_extRetParams >> " + extRetParams);
		mjsonParams.setString("CUST_NM", extRetParams.getString("CUST_NM"));
		mjsonParams.setString("CUST_TEL_NO", extRetParams.getString("CUST_TEL_NO"));
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //채팅 고객 ID 가져오기
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectRsvtCust", mjsonParams);
        //예약고객이 이미 전화번호를 가지고 있을 때
        if(objRetParams.getString("CUST_PHN_NO").equals(extRetParams.getString("CUST_TEL_NO"))) {
        	//아무 동작 하지 않음
        } else if(objRetParams.getString("CUST_PHN_NO").equals("0")) {
        	//예약고객이 전화번호가 저장되어있지 않을 때
        	//해당 전화번호를 가지고 있는 팔레트 고객 조회
        	mjsonParams.setString("BF_CUST_ID", objRetParams.getString("CUST_ID"));
            objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectRsvtPaletteCust", mjsonParams);
            JSONArray custArr = objRetParams.getDataObject();
            //전화번호를 가지고 있는 팔레트 고객이 있다면
            if(custArr.size()>0) {
        		log.info("고객 통합 로직 실행_extRetParams >> " + mjsonParams);
            	mjsonParams.setString("AF_CUST_ID", objRetParams.getString("CUST_ID"));
        		custInfoService.mergeCust(mjsonParams);
            } else {
            	//해당 전화번호를 가진 고객이 없다면 고객 전화번호만 업데이트
                objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateRsvtCust", mjsonParams);
            }
        }
        
		
        if(mjsonParams.getString("MSG_TY").equals("insertRsvt")) {
            mjsonParams.setString("RSVT_ID", Integer.toString(innbCreatCmmnService.createSeqNo("RSVT_ID")));
            
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertRsvt", mjsonParams);
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertChtRsvt", mjsonParams);
        } else {
            objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectChtRsvt", mjsonParams);
            JSONArray objRetArr = objRetParams.getDataObject();
            if(objRetArr.size()>0) {
	            mjsonParams.setString("RSVT_ID", objRetParams.getString("RSVT_ID"));
	            objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateRsvt", mjsonParams);
	            objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "deleteRsvtAltmnt", mjsonParams);
            }
        }
    	log.info("상담예약등록 후 바로 배분_processRsvtAltmnt"+ mjsonParams);
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertRsvtAltmnt", mjsonParams);
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateRsvtAltmntInfo", mjsonParams);

        return objRetParams;
    }
    
    /**
     * 상담예약가능 여부 YN
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    public TelewebJSON processGetRsvtPsbltyYn(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	log.info("상담예약가능 여부 YN_processGetRsvtPsbltyYn"+ mjsonParams);

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        String cuslCnt = "";
        String rsvtCnt = "";
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectCountCusl", mjsonParams);
        cuslCnt = objRetParams.getString("CNT");
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectCountRsvt", mjsonParams);
        rsvtCnt = objRetParams.getString("CNT");
        if(cuslCnt.equals("")) {
        	cuslCnt = "0";
        }
        if(rsvtCnt.equals("")) {
        	rsvtCnt = "0";
        }
        if(Integer.parseInt(cuslCnt) > Integer.parseInt(rsvtCnt)) {
        	//예약보다 상담 가능상담원이 더 많을 때
            objRetParams.setString("RSVT_PSBLTY_YN", "Y");
        } else {
            objRetParams.setString("RSVT_PSBLTY_YN", "N");
        }
        log.info("상담예약가능 여부 YN_objRetParams" + objRetParams);

        return objRetParams;
    }
    
    /**
     * 상담예약가능 날짜 조회
     * 채팅 휴일 기준
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    public TelewebJSON processGetRsvtPsbltyDate(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	log.info("상담예약가능 날짜 조회_processGetRsvtPsbltyDate"+ mjsonParams);
    	
    	String retString = "";
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "getRsvtDate", mjsonParams);
        JSONArray rsvtHrArr = objRetParams.getDataObject();
        if(rsvtHrArr.size() > 0) {
            for(int i = 0; i < rsvtHrArr.size(); i++) {
            	if(i == 0) {
            		retString = "[";
            	}
            	JSONObject obj = rsvtHrArr.getJSONObject(i);
            	String hldy = obj.getString("HLDY").replaceAll(":", "");
                retString = retString + hldy + ",";
            }
            retString = retString+"]";
        }

        objRetParams.setString("RSVT_HLDY_ARR", retString);
        log.info("상담예약 가능 날짜 ARRAY_retString" + retString);

        return objRetParams;
    }
    
    
    /**
     * 상담예약 가능 시간 ARRAY
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    public TelewebJSON processGetRsvtPsbltyArr(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	log.info("상담예약 가능 시간 ARRAY_processGetRsvtPsbltyArr"+ mjsonParams);

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        String bol = "0";
        String retString = "";
        //상담예약 시간 조회
        //상담 시작시간 - 상담 종료시간 - 상담 주기
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "getRsvtBtn", mjsonParams);
        JSONArray rsvtHrArr = objRetParams.getDataObject();
        if(rsvtHrArr.size() > 0) {
            for(int i = 0; i < rsvtHrArr.size(); i++) {
            	if(i == 0) {
            		retString = "[";
            	}
            	JSONObject obj = rsvtHrArr.getJSONObject(i);
            	String hr = obj.getString("HR").replaceAll(":", "");
            	String dt = mjsonParams.getString("rsvtDt");
            	mjsonParams.setString("RSVT_HR", obj.getString("HR").replaceAll(":", ""));
            	mjsonParams.setString("RSVT_DT", dt + hr);

                String cuslCnt = "";
                String rsvtCnt = "";
                objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectCountCusl", mjsonParams);
                cuslCnt = objRetParams.getString("CNT");
                objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectCountRsvt", mjsonParams);
                rsvtCnt = objRetParams.getString("CNT");
                if(cuslCnt.equals("")) {
                	cuslCnt = "0";
                }
                if(rsvtCnt.equals("")) {
                	rsvtCnt = "0";
                }
                if(Integer.parseInt(cuslCnt) > Integer.parseInt(rsvtCnt)) {
                	//예약보다 상담 가능상담원이 더 많을 때
                	bol = "1";
                } else {
                	bol = "0";
                }
                retString = retString + bol + ",";
            }
            retString = retString+"]";
        }

        objRetParams.setString("RSVT_PSBLTY_ARR", retString);
        log.info("상담예약 가능 시간 ARRAY_retString" + retString);
        
        return objRetParams;
    }
    
    
    /**
     * sndr_key 조회
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    public String getSndrKey(String uuid) throws TelewebAppException
    {
    	log.info("sndr_key 조회_getSndrKey"+ uuid);

        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON mjsonParams = new TelewebJSON();
        mjsonParams.setString("UUID", uuid);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "getSndrKey", mjsonParams);
        
        return objRetParams.getString("DSPTCH_PRF_KEY");
    }
	
    /**
     * 외부API 조회 FUNCTION
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
	TelewebJSON getExtApi(String param) throws TelewebApiException
	{
		TelewebJSON objRetParams = new TelewebJSON();
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Authorization", token);
		HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
		String url = extUrl + param;
		log.info("ext api url >> " + url);
		log.info("ext api headers >> " + headers);
		String response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class).getBody();
        
        log.info("getExtApi response" + response);
        
        // JSON 문자열을 JSONObject 객체로 변환
        JSONObject jsonObject = JSONObject.fromObject(response);
        // "responseData" 키로 JSONArray 추출
        JSONArray responseData = jsonObject.getJSONArray("responseData");
        
		objRetParams.setDataObject("DATA", responseData);
        
		return objRetParams;
	}
	
    /**
     * 외부API POST FUNCTION
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
	public TelewebJSON postExtApi(String param) throws TelewebApiException
	{
		TelewebJSON objRetParams = new TelewebJSON();
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Authorization", token);
		HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
		String url = extUrl + param;
		log.info("ext api url >> " + url);
		log.info("ext api headers >> " + headers);
		String response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class).getBody();
        
        log.info("getExtApi response" + response);
        
        // JSON 문자열을 JSONObject 객체로 변환
        JSONObject jsonObject = JSONObject.fromObject(response);
        // "responseData" 키로 JSONArray 추출
        JSONArray responseData = jsonObject.getJSONArray("responseData");
        
		objRetParams.setDataObject("DATA", responseData);
        
		return objRetParams;
	}
    
}
