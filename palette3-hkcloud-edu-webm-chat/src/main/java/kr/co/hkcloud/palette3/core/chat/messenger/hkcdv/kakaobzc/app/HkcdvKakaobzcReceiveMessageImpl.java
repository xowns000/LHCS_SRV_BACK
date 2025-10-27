package kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.kakaobzc.app;


import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import kr.co.hkcloud.palette3.common.chat.domain.OrgContentVO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;   
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbEnvironment;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbSystemMessage;
import kr.co.hkcloud.palette3.core.chat.busy.app.TalkBusyService;
import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.kakaobzc.domain.KakaobzcOnMessageEvent;
import kr.co.hkcloud.palette3.core.chat.messenger.util.TeletalkReceiveUtils;
import kr.co.hkcloud.palette3.core.chat.msg.app.TalkMsgDataProcessService;
import kr.co.hkcloud.palette3.core.chat.redis.util.TalkRedisChatUtils;
import kr.co.hkcloud.palette3.core.chat.router.app.TalkDataProcessService;
import kr.co.hkcloud.palette3.core.chat.transfer.app.TransToKakaoService;
import kr.co.hkcloud.palette3.core.util.PaletteJsonUtils;
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
 * 카카오톡 상담톡 사용자 메시지 수신(path: /message)
 * 가이드 문서 : https://bzm-center.kakao.com/guide/document/bizchat
 * 
 * @author RND
 */
@Slf4j
@RequiredArgsConstructor
@Service("hkcdvKakaobzcReceiveMessage")
public class HkcdvKakaobzcReceiveMessageImpl implements HkcdvKakaobzcReceiveMessage
{
    private static final String CALLED_API = "/message";

    private final FileRulePropertiesUtils   fileRulePropertiesUtils;
    private final TalkDataProcessService    talkDataProcessService;
    private final TalkMsgDataProcessService talkMsgDataProcess;
    private final TalkBusyService           talkBusyService;
    private final TransToKakaoService       transToKakaoService;
    private final TalkRedisChatUtils        talkRedisChatUtils;
    private final TeletalkReceiveUtils      teletalkReceiveUtils;
    private final PaletteJsonUtils          paletteJsonUtils;
    private final InnbCreatCmmnService      innbCreatCmmnService;
    private final SettingCustomerInformationListService settingCustomerInformationListService;

    String logDevider = "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";

    /**
     * 메시지 이벤트(사용자 메시지) 수신
     * 
     * @param   KakaobzcOnMessageEvent messageEvent
     * @throws  Exception
     * @version                        5.0
     */
    @EventListener
    public void onMessage(final KakaobzcOnMessageEvent messageEvent) throws TelewebAppException
    {
        JSONObject messageJson = messageEvent.getMessageJson();
        
        String logPrefix = logDevider + ".onMessage" + "___" + messageJson.get("user_key") + "___KAKAO___";
        int logNum = 1;
        log.info(logPrefix + (logNum++) + " ::: 메시지 이벤트(사용자 메시지) 수신 start");
        log.info(logPrefix + (logNum++) + " ::: onMessage - {}", messageJson.toString());

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

        //=========================================================
        //= 고객정보 ==============================================
        //=========================================================
        CustomerVO customerVO = new CustomerVO();
        customerVO.setCustomerId(userKey);
        customerVO.setTalkUserKey(userKey);
        customerVO.setCustcoId(custcoId);
        customerVO.setSndrKey(senderKey);
        customerVO.setChnClsfCd(callTypCd);
        log.info(logPrefix + (logNum++) + " ::: call before settingCustomerInformationListService.mergeCustomerBaseInfo(customerVO) ::: " + 
            "\ncustomerVO.getCustomerId :::  " + customerVO.getCustomerId() + 
            "\ncustomerVO.getTalkUserKey :::  " + customerVO.getTalkUserKey() + 
            "\ncustomerVO.getCustcoId :::  " + customerVO.getCustcoId() + 
            "\ncustomerVO.getSndrKey :::  " + customerVO.getSndrKey() +
            "\ncustomerVO.getChnClsfCd :::  " + customerVO.getChnClsfCd()
        );
        settingCustomerInformationListService.mergeCustomerBaseInfo(customerVO);
        log.info(logPrefix + (logNum++) + " ::: call after settingCustomerInformationListService.mergeCustomerBaseInfo(customerVO)");

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

        // 챗봇연결 문의유형 선택 - 종료처리
        log.info(logPrefix + (logNum++) + " ::: extra ::: " + extra);
        if(extra.startsWith("ENDWITHBOT_")) {
            transToKakaoService.endWithBot(userKey, senderKey, extra.replaceAll("ENDWITHBOT_", ""));
            return;
        }

        log.info(logPrefix + (logNum++) + " ::: 수신가능 type에 따라 지원가능/불가능 check ::: " + messageJson.get("user_key"));
        // 지원 가능/불가능 타입에 따라 return 처리함.
        if(teletalkReceiveUtils.isAvailableType(type, messageJson) == false) {
            log.info(logPrefix + (logNum++) + " ::: teletalkReceiveUtils.isAvailableType(type, messageJson) == false ::: call before teletalkReceiveUtils.noSendSystemMsg(type, objParams, messageJson, callTypCd) ::: " +
                "\ntype :::  " + type + 
                "\nobjParams :::  " + objParams.toString() + 
                "\nmessageJson :::  " + messageJson.toString() + 
                "\ncallTypCd :::  " + callTypCd
            );
            teletalkReceiveUtils.noSendSystemMsg(type, objParams, messageJson, callTypCd);
            log.info(logPrefix + (logNum++) + " ::: teletalkReceiveUtils.isAvailableType(type, messageJson) == false ::: call after teletalkReceiveUtils.noSendSystemMsg(type, objParams, messageJson, callTypCd) ::: ");
            return;
        }

        log.info(logPrefix + (logNum++) + " ::: type ::: " + type);
        RepositoryTrgtTypeCd trgtTypeCd = null;
        if("photo".equals(type)) {
            String imageTalkUrl = ((JSONObject) messageJson.get("content")).getString("url");
            
            //다중이미지 처리
            StringBuilder imagesTalkUrl = new StringBuilder();
            JSONArray imagesTalkUrlObjArray = ((JSONArray) messageJson.get("contents"));
            
            for(int i = 0; i < imagesTalkUrlObjArray.size(); i++) {
            	JSONObject imagesTalkUrlObj = ((JSONObject) imagesTalkUrlObjArray.get(i));
                String url = imagesTalkUrlObj.getString("url");
                
                // URL을 StringBuilder에 추가
                imagesTalkUrl.append(url);
                
                // 마지막이 아니라면 콤마 추가
                if (i < imagesTalkUrlObjArray.size() - 1) {
                	imagesTalkUrl.append(",");
                }
            }
            
            messageJson.put("content", imageTalkUrl);
            messageJson.put("contents", imagesTalkUrl);
            messageJson.put("IMAGE_URL", imageTalkUrl);
            messageJson.put("IMG_URL", imageTalkUrl);

            objParams.setString("CONTENT", imageTalkUrl);
            objParams.setString("CONTENTS", imagesTalkUrl.toString());
            objParams.setString("IMAGE_URL", imageTalkUrl);
            objParams.setString("IMG_URL", imageTalkUrl);

            //이미지 팔레트서버 보관x
            //기존 서버에 있는 이미지 보여주는 로직에서 각 채널에서 받아오는 도메인 이미지 보여주므로 저장 필요 없음
            //이미지 제한 시스템 메시지도 발송 필요 없음
            
//            // [파일o] 메시지 수신(카카오톡): 채팅-이미지(고객)
//            final RepositoryTaskTypeCd taskTypeCd = RepositoryTaskTypeCd.chat;    //채팅
//            final RepositoryPathTypeCd pathTypeCd = RepositoryPathTypeCd.images;  //이미지(고객)
//            final FilePropertiesResponse fileProperties = fileRulePropertiesUtils.getProperties(taskTypeCd, pathTypeCd);
//            log.debug("fileProperties>>>{}", fileProperties);
//
//            trgtTypeCd = fileProperties.getTrgtTypeCd();
//            fileProperties.setCustcoId(custcoId);
//
//            //이미지 레파지토리 저장 
//            final JSONObject jsonFile = teletalkReceiveUtils.savePhototoRepository(messageJson, objParams, callTypCd, fileProperties);
//            messageJson.put("FILE_GROUP_KEY", jsonFile.getString("FILE_GROUP_KEY"));
//            messageJson.put("FILE_KEY", jsonFile.getString("FILE_KEY"));
//            objParams.setString("FILE_GROUP_KEY", jsonFile.getString("FILE_GROUP_KEY"));
//            objParams.setString("FILE_KEY", jsonFile.getString("FILE_KEY"));

//            // 파일 BLOB 처리이면
//            switch(trgtTypeCd)
//            {
//                case DB:
//                {
//                    //이미지 BLOB 저장 
//                    teletalkReceiveUtils.savePhototoBlob(messageJson, objParams, callTypCd, fileProperties);
//                    break;
//                }
//                default:
//                {
//                    //이미지 레파지토리 저장 
//                    teletalkReceiveUtils.savePhototoRepository(messageJson, objParams, callTypCd, fileProperties);
//                    break;
//                }
//            }
        }
        else if("video".equals(type)) {
            String videoTalkUrl = ((JSONObject) messageJson.get("content")).getString("url");
            messageJson.put("content", videoTalkUrl);
            messageJson.put("VIDEO_URL", videoTalkUrl);
            messageJson.put("VOD_URL", videoTalkUrl);

            objParams.setString("CONTENT", videoTalkUrl);
            objParams.setString("VIDEO_URL", videoTalkUrl);
            objParams.setString("VOD_URL", videoTalkUrl);
        }
        else if("custInfo".equals(type)) {
            //카카오톡에서 고객정보 입력받았을 때
        	//채팅방에 고객정보 입력에 대한 동작 전달
        	
            TelewebJSON socketParam = new TelewebJSON();
            socketParam.setHeader("called_api", CALLED_API);
            socketParam.setHeader("code", 0);
            socketParam.setHeader("ERROR_FLAG", false);
            socketParam.setHeader("ERROR_MSG", "");
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(0, messageJson);
            socketParam.setDataObject(jsonArray);
            objParams.setString("CONTENT", messageJson.getString("content"));
            //상담이력에 고객정보입력데이터 INSERT
            //상담사에게 고객정보입력데이터 전달
        	talkRedisChatUtils.isSendSocketToAgent(CALLED_API, userKey, socketParam, objParams);
        	return;
        }
        else {
            String content = messageJson.getString("content");
            objParams.setString("CONTENT", paletteJsonUtils.valueToStringWithoutQutoes(content));
        }

        //장문
//        if(messageJson.containsKey("attachment")) {
//            OrgContentVO orgContentVO = new OrgContentVO();
//            orgContentVO.setCustcoId(custcoId);
//            teletalkReceiveUtils.insertOrgContent(messageJson, objParams, orgContentVO);
//        }

        TelewebJSON telewebJSON = new TelewebJSON();
        telewebJSON.setHeader("called_api", CALLED_API);
        telewebJSON.setHeader("code", 0);
        telewebJSON.setHeader("ERROR_FLAG", false);
        telewebJSON.setHeader("ERROR_MSG", "");
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(0, messageJson);
        telewebJSON.setDataObject(jsonArray);

        //상담사에게 메시지 전달 또는 재배정
        log.info(logPrefix + (logNum++) + " ::: 상담사에게 메시지 전달 또는 재배정 ::: " + messageJson.get("user_key"));
        Boolean blnSendSocketToAgent = talkRedisChatUtils.isSendSocketToAgent(CALLED_API, userKey, telewebJSON, objParams);
        log.info(logPrefix + (logNum++) + " ::: blnSendSocketToAgent ::: " + blnSendSocketToAgent);
        if(blnSendSocketToAgent) { return; }

        //채팅이 가능하지 않은 상태인 지 체크한다 . 상담원 무응답으로 인한 재배정은 다른 상태관계 없이 무조건 배정 처리함. ( SJH 20181024 ) 
        String contactID = objParams.getString("TALK_CONTACT_ID");
//        if (!(contactID != null && !"".equals(contactID)) && talkBusyService.isChatDisable(userKey, senderKey, callTypCd))
        log.info(logPrefix + (logNum++) + " ::: contactID ::: " + contactID);
        log.info(logPrefix + (logNum++) + " ::: call before if(talkBusyService.isChatDisable(userKey, senderKey, callTypCd, custcoId, false)) ::: "
            + "\nuserKey ::: " + userKey
            + "\nsenderKey ::: " + senderKey
            + "\ncallTypCd ::: " + callTypCd
            + "\ncustcoId ::: " + custcoId
            + "\nfalse"
        );
        //!!!!!!!!!!!!!!!!!!!!!상담(채팅) 불가 여부!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if(talkBusyService.isChatDisable(userKey, senderKey, callTypCd, custcoId, false)) {
            log.info(logPrefix + (logNum++) + " ::: 상담(채팅) 불가 여부 - if(talkBusyService.isChatDisable(userKey, senderKey, callTypCd, custcoId, false)) ::: true");
            return; 
        } else {
            log.info(logPrefix + (logNum++) + " ::: 상담(채팅) 불가 여부 - if(talkBusyService.isChatDisable(userKey, senderKey, callTypCd, custcoId, false)) ::: false");
        }

        log.info(logPrefix + (logNum++) + " ::: 배분 대기 등록 ::: ");
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
        inJson.setString("MSG_TYPE_CD", objParams.getString("MSG_TYPE_CD"));
        inJson.setString("RCPTN_DSPTCH_CD", "RCV");

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

        //고객문의유형 사용여부 ( 사용여부 y and 문의유형 체크 ) 
        messageJson.put("INQRY_STATUS", "BEGIN");
        String inqryTypeYn = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "INQRY_TYPE_YN");
        boolean isUseInqry = "Y".equals(inqryTypeYn) && talkDataProcessService.selectInqryLevelType(messageJson).getSize() > 0;

        log.info(logPrefix + (logNum++) + " ::: 고객문의유형 사용여부 ::: " + isUseInqry);

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
        log.info(logPrefix + (logNum++) + " ::: 상담사에게 메시지 전달 inJson ::: " + inJson);
        log.info(logPrefix + (logNum++) + " ::: before call talkRedisChatUtils.isSendSocketToAgent(CALLED_API, userKey, telewebJSON, objParams) ::: "
            + "\nCALLED_API ::: " + CALLED_API
            + "\nuserKey ::: " + userKey
            + "\ntelewebJSON ::: " + telewebJSON
            + "\nobjParams ::: " + objParams
        );
        
        blnSendSocketToAgent = talkRedisChatUtils.isSendSocketToAgent(CALLED_API, userKey, telewebJSON, objParams);
        
        log.info(logPrefix + (logNum++) + " ::: after call talkRedisChatUtils.isSendSocketToAgent(CALLED_API, userKey, telewebJSON, objParams) ::: " + blnSendSocketToAgent);

        // 기존의 상담원이 비정상 종료 되었으므로 , 신규배분 되나 고객문의 유형 사용 안함. 2018.10.10 SJH
        //contactID = objParams.getString("TALK_CONTACT_ID");
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

                log.info(logPrefix + (logNum++) + " ::: true 고객문의유형 처리 ::: " + msgInfoJson);
                log.info(logPrefix + (logNum++) + " ::: call before talkDataProcessService.processInqryType(msgInfoJson, inJson, messageJson) ::: "
                    + "\nmsgInfoJson ::: " + msgInfoJson
                    + "\ninJson ::: " + inJson
                    + "\nmessageJson ::: " + messageJson
                );
                
                //고객문의유형 처리
                talkDataProcessService.processInqryType(msgInfoJson, inJson, messageJson);
                log.info(logPrefix + (logNum++) + " ::: call after talkDataProcessService.processInqryType(msgInfoJson, inJson, messageJson) ::: ");
            }
            else {
                log.info("===>" + messageJson.get("user_key") + "::onMessage - false 고객문의유형 처리");
                String systemMsgId = "14";
                // 이전 채팅이 비정상 종료 되어 이전 채팅의 후처리가 필요한 경우 2018.10.10 SJH
                if(contactID != null && !"".equals(contactID)) {
                    //22 비정상 종료 코드 업데이트
                    log.info(logPrefix + (logNum++) + " ::: 비정상 종료 코드 업데이트 ::: ");
                    talkMsgDataProcess.updateTalkContactStatusCd(objParams); //22 비정상 종료 코드 업데이트

                    inJson.setString("TALK_READY_CD", "10");
                    inJson.setString("INQRY_CD", objParams.getString("TALK_INQRY_CD"));
                    inJson.setString("TALK_CONTACT_ID", contactID);

                    systemMsgId = "20";
                }
                
                log.info(logPrefix + (logNum++) + " ::: 배분 대기 및 상세 등록 - 채팅 상담 등록 ::: ");
                // !!!!!!!!!!!!!!!배분 대기 및 상세 등록 - 채팅 상담 등록!!!!!!!!!!!!!
                TelewebJSON outJson = talkDataProcessService.processInsertTalkReady(inJson);
                log.info(logPrefix + (logNum++) + ":!!!@@##!@#@#@#@#@#: {}", inJson);
                log.info(logPrefix + (logNum++) + ":!!!@@##!@#@#@#@#@#: {}", messageJson);
                log.info(logPrefix + (logNum++) + ":!!!@@##!@#@#@#@#@#: {}", outJson);
                log.info(logPrefix + (logNum++) + ":!!!@@##!@#@#@#@#@#: {}", systemMsgId);
                log.info(logPrefix + (logNum++) + " ::: call before talkMsgDataProcess.insertSndMsg(inJson) ::: ");
                talkMsgDataProcess.insertSndMsg(inJson);
                log.info(logPrefix + (logNum++) + " ::: call after talkMsgDataProcess.insertSndMsg(inJson) ::: ");
                
                ((JSONObject) (inJson.getDataObject().get(0))).put("SYS_MSG_ID", systemMsgId);

                int cnt2 = outJson.getInt("IS_UPDATE");
                log.info(logPrefix + (logNum++) + " ::: 메세지가 들어왔을경우에만 대기알람메세지를 전송한다 ::: cnt2 ::: " + cnt2);
                // 메세지가 들어왔을경우에만 대기알람메세지를 전송한다
                if(cnt2 == 0) {
                    log.info(logPrefix + (logNum++) + " ::: call before transToKakaoService.sendSystemMsg  ::: "
                        + "\ncustcoId ::: " + custcoId
                        + "\nsystemMsgId ::: " + systemMsgId
                        + "\nHcTeletalkDbSystemMessage.getInstance().getTelewebJsonBySystemMsgId(custcoId, systemMsgId) ::: " + HcTeletalkDbSystemMessage.getInstance().getTelewebJsonBySystemMsgId(custcoId, systemMsgId)
                        + "\ncallTypCd ::: " + callTypCd
                    );
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
