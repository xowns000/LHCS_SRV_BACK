package kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.ttalkbzc.app;


import java.net.URI;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import kr.co.hkcloud.palette3.common.chat.domain.OrgContentVO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;  
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbEnvironment;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbSystemMessage;
import kr.co.hkcloud.palette3.config.properties.proxy.ProxyProperties;
import kr.co.hkcloud.palette3.core.chat.busy.app.TalkBusyService;
import kr.co.hkcloud.palette3.core.chat.busy.dao.TalkBusyDAO;
import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.ttalkbzc.domain.TtalkOnMessageEvent;
import kr.co.hkcloud.palette3.core.chat.messenger.util.TeletalkReceiveUtils;
import kr.co.hkcloud.palette3.core.chat.msg.app.TalkMsgDataProcessService;
import kr.co.hkcloud.palette3.core.chat.redis.util.TalkRedisChatUtils;
import kr.co.hkcloud.palette3.core.chat.router.app.TalkDataProcessService;
import kr.co.hkcloud.palette3.core.chat.transfer.app.SendToKakaoService;
import kr.co.hkcloud.palette3.core.chat.transfer.app.TransToKakaoService;
import kr.co.hkcloud.palette3.core.chat.transfer.util.TransferUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTaskTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryPathTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTrgtTypeCd;
import kr.co.hkcloud.palette3.file.util.FileRulePropertiesUtils;
import kr.co.hkcloud.palette3.setting.customer.app.SettingCustomerInformationListService;
import kr.co.hkcloud.palette3.setting.customer.dto.CustomerVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 티톡 메시지 수신 인터페이스 구현체
 * 
 * @author User
 */
@Slf4j
@RequiredArgsConstructor
@Service("hkcdvTtalkbzcReceiveMessage")
public class HkcdvTtalkbzcReceiveMessageImpl implements HkcdvTtalkbzcReceiveMessage
{
    private static final String calledApi = "/message";

    private final FileRulePropertiesUtils               fileRulePropertiesUtils;
    private final ProxyProperties                       proxyProperties;
    private final TalkDataProcessService                talkDataProcessService;
    private final TalkMsgDataProcessService             talkMsgDataProcess;
    private final TalkBusyService                       talkBusyService;
    private final TransToKakaoService                   transToKakaoService;
    private final TalkRedisChatUtils                    talkRedisChatUtils;
    private final TeletalkReceiveUtils                  teletalkReceiveUtils;
    private final TalkBusyDAO                           busyDAO;
    private final SettingCustomerInformationListService settingCustomerInformationListService;
    private final InnbCreatCmmnService      innbCreatCmmnService;

    private final SendToKakaoService sendToKakaoService;
    private final TransferUtils transferUtils;


    /**
     * 메시지 이벤트 수신
     * 
     * @param   TtalkOnReferenceEvent referenceEvent
     * @throws  Exception
     * @version                       5.0
     */
    @EventListener
    public void onMessage(final TtalkOnMessageEvent messageEvent) throws TelewebAppException
    {
        // 메시지 수신
        JSONObject messageJson = messageEvent.getMessageJson();
        log.debug("onMessage - {}", messageJson.toString());
        
        log.trace("onMessage - {}", messageJson.toString());
        if(messageJson.getString("type").equals("inputInfoMsg")) {
        	//티톡에서 고객정보를 입력했을 때
        	log.info("티톡 - 정보입력");
        	TelewebJSON objParams = new TelewebJSON();
        	objParams.setString("CHT_CUTT_DTL_ID", messageJson.getString("CHT_CUTT_DTL_ID"));
        	objParams.setString("MSG", messageJson.getString("transeMsg"));
        	
        	talkDataProcessService.processInputInfoMsg(objParams);
        } else if(messageJson.getString("type").equals("getRsvtPsbltyYn")) {
        	//티톡에서 고객정보를 입력했을 때
        	log.info("티톡 - 예약가능날짜 확인");
        	TelewebJSON objParams = new TelewebJSON();
        	//선택날짜가 시간까지 포함되어있다면 해당 시간에 대한 예약이 가능한지
        	//선택날짜가 시간이 포함되어있지 않다면 해당 날짜에 대한 예약 가능시간을 배열로 보내줘야 함
        	objParams.setString("rsvtDt", messageJson.getString("rsvtDt"));
        	objParams.setString("RSVT_DT", messageJson.getString("rsvtDt"));
        	objParams.setString("CUSTCO_ID", messageJson.getString("custco_id"));
        	
            final URI uri = transferUtils.getTargetUrl("write", "TTALK");
            JSONObject writeData = new JSONObject();
            //uuid로 sndrKey 가져오기 
            //sndrKey는 팔레트상에서 채번되는 키가 아닌 발신 프로필 키
            String sndrKey = talkDataProcessService.getSndrKey(messageJson.getString("uuid"));
            writeData.put("sndrKey",sndrKey);
            writeData.put("CHT_USER_KEY",messageJson.getString("user_key"));
            //sendToKakaoService.sendTextToKakao에서 CHT_CUTT_DTL_ID가 필수값이므로 0주입
            writeData.put("CHT_CUTT_DTL_ID", "0");
        	TelewebJSON objRsvyPsbltyParam = new TelewebJSON();
        	TelewebJSON objRsvyPsbltyDateParam = new TelewebJSON();
        	int dtLeng = messageJson.getString("rsvtDt").length();
        	log.info("rsvtDt" + dtLeng);
        	if(dtLeng>8) {
        		objRsvyPsbltyParam = talkDataProcessService.processGetRsvtPsbltyYn(objParams);
        		writeData.put("RSVT_PSBLTY_YN", objRsvyPsbltyParam.getString("RSVT_PSBLTY_YN"));
        	} else {
        		objRsvyPsbltyParam = talkDataProcessService.processGetRsvtPsbltyArr(objParams);
        		writeData.put("RSVT_PSBLTY_ARR", objRsvyPsbltyParam.getString("RSVT_PSBLTY_ARR"));
        		
        		objRsvyPsbltyDateParam = talkDataProcessService.processGetRsvtPsbltyDate(objParams);
        		writeData.put("RSVT_HLDY_ARR", objRsvyPsbltyDateParam.getString("RSVT_HLDY_ARR"));
        	}
        	JSONObject retSendText = new JSONObject();
        	retSendText = sendToKakaoService.sendTextToKakao(uri, writeData);
        	
        	log.info("objParams" + objParams);
        	log.info("retSendText" + retSendText);
        } else if(messageJson.getString("type").equals("updateRsvt") || messageJson.getString("type").equals("insertRsvt")) {
        	//티톡에서 고객정보를 입력했을 때
        	log.info("티톡 - 예약하기");
        	TelewebJSON objParams = new TelewebJSON();
        	objParams.setString("CHT_CUTT_DTL_ID", messageJson.getString("CHT_CUTT_DTL_ID"));
        	objParams.setString("RSVT_DT", messageJson.getString("rsvtDt"));
        	objParams.setString("STLM_YN", messageJson.getString("stlm_yn"));
        	objParams.setString("DRWI_SE_NM", messageJson.getString("drwi_se_nm"));
        	objParams.setString("CHT_USER_KEY", messageJson.getString("user_key"));
        	objParams.setString("CUSTCO_ID", messageJson.getString("custco_id"));
        	objParams.setString("MSG_TY", messageJson.getString("type"));
        	objParams.setString("SNDR_KEY", messageJson.getString("sndrKey"));
        	
        	log.info("objParams" + objParams);
        	talkDataProcessService.processRsvt(objParams);
        	
            final URI uri = transferUtils.getTargetUrl("write", "TTALK");
            JSONObject writeData = new JSONObject();
            //uuid로 sndrKey 가져오기 
            //sndrKey는 팔레트상에서 채번되는 키가 아닌 발신 프로필 키
            String sndrKey = talkDataProcessService.getSndrKey(messageJson.getString("uuid"));
            writeData.put("sndrKey",sndrKey);
            writeData.put("CHT_USER_KEY",messageJson.getString("user_key"));
            writeData.put("CHT_CUTT_DTL_ID", "0");
    		writeData.put("RSVT_CMPL", "0");
        	JSONObject retSendText = new JSONObject();
        	retSendText = sendToKakaoService.sendTextToKakao(uri, writeData);
        	
        	log.info("objParams" + objParams);
        	log.info("retSendText" + retSendText);
        } else {

	        try {
	            Thread.sleep(100);
	        }
	        catch(InterruptedException e) {
	            log.error(e.getLocalizedMessage(), e);
	            throw new TelewebAppException(e.getLocalizedMessage(), e);
	        }
	
	        messageJson.put("response_yn", "Y");
	        messageJson.put("IMAGE_TALK_PATH", "");
	        messageJson.put("IMAGE_URL", "");
	        messageJson.put("IMG_URL", "");
	
	        messageJson.put("VIDEO_TALK_PATH", "");
	        messageJson.put("VIDEO_URL", "");
	        messageJson.put("VIDEO_THUMNAIL_PATH", "");
	
	        String userKey = messageJson.getString("user_key");
	        String senderKey = messageJson.getString("sndrKey");
	        String serialNumber = Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID"));
	        String type = messageJson.getString("type");
	        String callTypCd = messageJson.getString("call_typ_cd");
	        String custcoId = messageJson.getString("custco_id");
	        String extra = messageJson.has("extra") ? messageJson.getString("extra") : "";
	
	        messageJson.put("CUSTCO_ID", custcoId);
	        messageJson.put("SNDR_KEY", senderKey);
	        messageJson.put("CHT_CUTT_DTL_ID", serialNumber);
	        messageJson.put("TALK_SERIAL_NUMBER", serialNumber);
	
	        TelewebJSON objParams = new TelewebJSON();
	        objParams.setString("CUSTCO_ID", custcoId);
	        objParams.setString("TALK_USER_KEY", userKey);
	        objParams.setString("CHT_USER_KEY", userKey);
	        objParams.setString("TALK_SERIAL_NUMBER", serialNumber);
	        objParams.setString("CHT_CUTT_DTL_ID", serialNumber);
	        objParams.setString("TYPE", type);
	        objParams.setString("MSG_TYPE_CD", type);
	        objParams.setString("PROC_ID", "SYSTEM");
	        objParams.setString("USER_ID", "SYSTEM");
	        objParams.setString("IMAGE_URL", ""); // 이미지 처리 기본 설정
	        objParams.setString("IMG_URL", ""); // 이미지 처리 기본 설정
	        objParams.setString("IMAGE_TALK_PATH", "");
	
	        objParams.setString("VIDEO_TALK_PATH", "");
	        objParams.setString("VIDEO_URL", "");
	        objParams.setString("VIDEO_THUMNAIL_PATH", "");
	
	        objParams.setString("CALL_TYP_CD", callTypCd);
	        objParams.setString("CHN_CLSF_CD", callTypCd);
	        objParams.setString("SNDR_KEY", senderKey);
	        objParams.setString("CUSTCO_ID", custcoId);
	
	        CustomerVO customerVO = new CustomerVO();
	        customerVO.setCustomerId(userKey);
	        customerVO.setTalkUserKey(userKey);
	        customerVO.setCustcoId(custcoId);
	        customerVO.setSndrKey(senderKey);
	        customerVO.setChnClsfCd(callTypCd);
	        settingCustomerInformationListService.mergeCustomerBaseInfo(customerVO);
	
	        // 지원 가능/불가능 타입에 따라 return 처리함.
	        if(teletalkReceiveUtils.isAvailableType(type, messageJson) == false) {
	            // 영업시간 
	            if("worktime".equals(type)) {
	
	                // ttalk 영업시간 전송 
	                String workStartTime = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "WORK_START_TIME");
	                String workEndTime = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "WORK_END_TIME");
	
	                objParams.setString("wst", workStartTime);
	                objParams.setString("wet", workEndTime);
	
	                // 4. 휴일 체크
	                String holidayYn = busyDAO.selectTalkHoliday(workStartTime, workEndTime, custcoId);
	                objParams.setString("holidayYn", holidayYn);
	
	                teletalkReceiveUtils.sendWorktimeMsg(type, objParams, messageJson, callTypCd);
	                return;
	
	            }
	
	            teletalkReceiveUtils.noSendSystemMsg(type, objParams, messageJson, callTypCd);
	            return;
	        }
	
	        log.trace("type", type);
	        RepositoryTrgtTypeCd trgtTypeCd = null;
	        if("photo".equals(type)) {
	        	//티톡 이미지 따로 저장 안함
	            String imageTalkUrl = ((JSONObject) messageJson.get("content")).getString("url");
	            log.trace("imageTalkUrl" + imageTalkUrl);
	            messageJson.put("content", imageTalkUrl);
	            messageJson.put("contents", imageTalkUrl);
	            messageJson.put("IMAGE_URL", imageTalkUrl);
	            messageJson.put("IMG_URL", imageTalkUrl);
	
	            objParams.setString("CONTENT", imageTalkUrl);
	            objParams.setString("CONTENTS", imageTalkUrl);
	            objParams.setString("IMAGE_URL", imageTalkUrl);
	            objParams.setString("IMG_URL", imageTalkUrl);
	
	            log.trace("here");
	
	            // [파일o] 메시지 수신(티톡): 채팅-이미지(고객)
	            final RepositoryTaskTypeCd taskTypeCd = RepositoryTaskTypeCd.chat;    //채팅
	            final RepositoryPathTypeCd pathTypeCd = RepositoryPathTypeCd.images;  //이미지(고객)
	            final FilePropertiesResponse fileProperties = fileRulePropertiesUtils.getProperties(taskTypeCd, pathTypeCd);
	            log.debug("fileProperties>>>{}", fileProperties);
	            
	            log.trace("fileProperties>>>{}", fileProperties);
	
	            trgtTypeCd = fileProperties.getTrgtTypeCd();
	            fileProperties.setCustcoId(custcoId);
	
	            //이미지 레파지토리 저장 
	//            final JSONObject jsonFile = teletalkReceiveUtils.savePhototoRepository(messageJson, objParams, callTypCd, fileProperties);
	//            messageJson.put("FILE_GROUP_KEY", jsonFile.getString("FILE_GROUP_KEY"));
	//            messageJson.put("FILE_KEY", jsonFile.getString("FILE_KEY"));
	//            objParams.setString("FILE_GROUP_KEY", jsonFile.getString("FILE_GROUP_KEY"));
	//            objParams.setString("FILE_KEY", jsonFile.getString("FILE_KEY"));
	        }
	        else if("video".equals(type)) {
	            //지원 안함 - 20210615
	        }
	        else {
	            String content = messageJson.getString("content");
	            objParams.setString("CONTENT", messageJson.getString("content"));
	        }
	
	        //장문
	        if(messageJson.containsKey("attachment")) {
	            OrgContentVO orgContentVO = new OrgContentVO();
	            orgContentVO.setCustcoId(custcoId);
	            teletalkReceiveUtils.insertOrgContent(messageJson, objParams, orgContentVO);
	        }
	
	        TelewebJSON telewebJSON = new TelewebJSON();
	        telewebJSON.setHeader("called_api", calledApi);
	        telewebJSON.setHeader("code", 0);
	        telewebJSON.setHeader("ERROR_FLAG", false);
	        telewebJSON.setHeader("ERROR_MSG", "");
	        JSONArray jsonArray = new JSONArray();
	        jsonArray.add(0, messageJson);
	        telewebJSON.setDataObject(jsonArray);
	
	        //상담사에게 메시지 전달 또는 재배정
	        log.info(" ::: 상담사에게 메시지 전달 또는 재배정 ::: " + messageJson.get("user_key"));
	        Boolean blnSendSocketToAgent = talkRedisChatUtils.isSendSocketToAgent(calledApi, userKey, telewebJSON, objParams);
	        log.info(" ::: blnSendSocketToAgent ::: 레디스에서 상담 상태를체크하기때문에 로컬에서는 항상 false ::: " + blnSendSocketToAgent);
	        if(blnSendSocketToAgent) { return; }
	
	        //채팅이 가능하지 않은 상태인 지 체크한다 . 상담원 무응답으로 인한 재배정은 다른 상태관계 없이 무조건 배정 처리함. ( SJH 20181024 ) 
	        String contactID = objParams.getString("TALK_CONTACT_ID");
	//        if (!(contactID != null && !"".equals(contactID)) && talkBusyService.isChatDisable(userKey, senderKey, callTypCd))
	        if(talkBusyService.isChatDisable(userKey, senderKey, callTypCd, custcoId, true)) { return; }
	
	        // 배분 대기 등록
	        TelewebJSON inJson = new TelewebJSON();
	        inJson.setString("TALK_USER_KEY", userKey);
	        inJson.setString("CHT_USER_KEY", userKey);
	        inJson.setString("TALK_SERIAL_NUMBER", messageJson.getString("CHT_CUTT_DTL_ID"));
	        inJson.setString("CHT_CUTT_DTL_ID", messageJson.getString("CHT_CUTT_DTL_ID"));
	        inJson.setString("DSPTCH_PRF_KEY", senderKey);
	        inJson.setString("TALK_API_CD", "/message");
	        inJson.setString("SESSION_ID", "");
	        inJson.setString("TYPE", messageJson.getString("type"));
	        inJson.setString("CONTENT", messageJson.getString("content"));
	        inJson.setString("IMAGE_URL", messageJson.getString("IMAGE_URL"));
	        inJson.setString("IMG_URL", messageJson.getString("IMG_URL"));
	        inJson.setString("IMAGE_TALK_PATH", messageJson.getString("IMAGE_TALK_PATH"));
	        inJson.setString("CUSTCO_ID", custcoId);
	        inJson.setString("SNDR_KEY", senderKey);
	        inJson.setString("SYS_MSG_ID", objParams.getString("SYS_MSG_ID"));
	
	        //2018.12.26 KMG 동영상 정보 세팅
	        inJson.setString("VIDEO_TALK_PATH", messageJson.getString("VIDEO_TALK_PATH"));
	        inJson.setString("VIDEO_URL", messageJson.getString("VIDEO_URL"));
	        inJson.setString("VIDEO_THUMNAIL_PATH", messageJson.getString("VIDEO_THUMNAIL_PATH"));
	
	        if(messageJson.has("attachment")) {
	            inJson.setString("LINKS", ((JSONObject) messageJson.get("attachment")).getString("url"));
	            inJson.setString("ORG_CONT_ID", objParams.getString("ORG_CONT_ID"));
	        }
	        else {
	            inJson.setString("LINKS", "");
	            inJson.setString("ORG_CONT_ID", "");
	        }
	
	        if("photo".equals(type)) {
	        	//파일을 따로 저장하지 않음
                inJson.setString("FILE_GROUP_KEY", "");
                inJson.setString("FILE_KEY", "");
	        }
	
	        //고객문의유형 사용여부
	        String inqryTypeYn = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "INQRY_TYPE_YN");
	        messageJson.put("INQRY_STATUS", "BEGIN");
	        boolean isUseInqry = "Y".equals(inqryTypeYn) && talkDataProcessService.selectInqryLevelType(messageJson).getSize() > 0;
	
	        // 고객문의유형 사용할 때
	        if(isUseInqry) {
	        	//문의유형 선택중
	            inJson.setString("TALK_READY_CD", "QSTN_CHCING");
	            inJson.setString("CUTT_STTS_CD", "QSTN_CHCING");
	            inJson.setString("ALTMNT_STTS_CD", "QSTN_CHCING");
	        }
	        else {
	        	//배분대기
	            inJson.setString("TALK_READY_CD", "ALTMNT_WAIT");
	            inJson.setString("CUTT_STTS_CD", "ALTMNT_WAIT");
	            inJson.setString("ALTMNT_STTS_CD", "ALTMNT_WAIT");
	        }
	        inJson.setString("CHATBOT_YN", "N");
	        inJson.setString("CALL_TYP_CD", callTypCd);
	        inJson.setString("CHN_CLSF_CD", callTypCd);
	        inJson.setString("CHN_TYPE_CD", callTypCd);
	
	        // 파일 BLOB 처리이면
	        if(trgtTypeCd != null && trgtTypeCd == RepositoryTrgtTypeCd.DB) {
	            //이미지 BLOB 저장 
	            inJson.setString("ORG_FILE_ID", objParams.getString("ORG_FILE_ID"));
	        }
	
	        //상담사에게 메시지 전달
	        blnSendSocketToAgent = talkRedisChatUtils.isSendSocketToAgent(calledApi, userKey, telewebJSON, objParams);
	
	        // 기존의 상담원이 비정상 종료 되었으므로 , 신규배분 되나 고객문의 유형 사용 안함. 2018.10.10 SJH
	//        contactID = objParams.getString("TALK_CONTACT_ID");
	        contactID = objParams.getString("CHT_CUTT_ID");
	
	        if(!blnSendSocketToAgent) {
	            // 고객문의유형 사용할 때  && (contactID == null || "".equals(contactID))
	            if(isUseInqry && (contactID == null || "".equals(contactID))) {
	                String readyToTalk = HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId(custcoId, "14");
	                String ReqType = HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId(custcoId, "15");
	                String chatAgent = HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId(custcoId, "16");
	                String inqryTypeMsg = new StringBuffer(ReqType).append(" ").append(chatAgent).toString();
	
	                TelewebJSON msgInfoJson = new TelewebJSON();
	                msgInfoJson.setString("MSG_READY_TO_TALK", readyToTalk);
	                msgInfoJson.setString("MSG_INQRY_TYPE_MSG", inqryTypeMsg);
	                msgInfoJson.setString("MSG_READY_TO_TALK_ID", "14");
	
	                //고객문의유형 처리
	                talkDataProcessService.processInqryType(msgInfoJson, inJson, messageJson);
	            }
	            else {
	                String systemMsgId = "14";
	                // 이전 채팅이 비정상 종료 되어 이전 채팅의 후처리가 필요한 경우 2018.10.10 SJH
	                if(contactID != null && !"".equals(contactID)) {
	                    //22 비정상 종료 코드 업데이트
	                    talkMsgDataProcess.updateTalkContactStatusCd(objParams); //22 비정상 종료 코드 업데이트
	
	                    inJson.setString("TALK_READY_CD", "10");
	                    inJson.setString("INQRY_CD", objParams.getString("TALK_INQRY_CD"));
	                    inJson.setString("TALK_CONTACT_ID", contactID);
	
	                    systemMsgId = "20";
	                }
	
	                // 배분 대기 및 상세 등록
	                ((JSONObject) (inJson.getDataObject().get(0))).put("SYS_MSG_ID", systemMsgId);
	                talkDataProcessService.processInsertTalkReady(inJson);
	
	                // 메세지가 들어왔을경우에만 대기알람메세지를 전송한다
	                //TransToKakao.sendSystemMsg(talkDataProcess, userKey, senderKey, env.getString("readyToTalk"));//이건철 20180403 시스템 메시지로 변경
	
	                if(systemMsgId.equals("14")) {
	                	//상담 대기메시지 발송 여부 체크
	                    String custWaitMsgYn = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "CUST_WAIT_MSG_YN");
	                    if(custWaitMsgYn.equals("Y")) {
	                    	transToKakaoService.sendSystemMsg(userKey, senderKey, HcTeletalkDbSystemMessage.getInstance().getTelewebJsonBySystemMsgId(custcoId, systemMsgId), callTypCd);
	                    }
	                } else {
	                	transToKakaoService.sendSystemMsg(userKey, senderKey, HcTeletalkDbSystemMessage.getInstance().getTelewebJsonBySystemMsgId(custcoId, systemMsgId), callTypCd);
	                }
	            }
	        }
        }
    }
}
