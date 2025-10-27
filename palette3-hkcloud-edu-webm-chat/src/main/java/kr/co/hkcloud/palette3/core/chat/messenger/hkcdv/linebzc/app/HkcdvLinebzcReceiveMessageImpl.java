package kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.linebzc.app;


import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import kr.co.hkcloud.palette3.common.chat.domain.OrgContentVO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbEnvironment;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbSystemMessage;
import kr.co.hkcloud.palette3.config.properties.chat.ChatProperties;
import kr.co.hkcloud.palette3.config.properties.proxy.ProxyProperties;
import kr.co.hkcloud.palette3.core.chat.busy.app.TalkBusyService;
import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.linebzc.domain.LinebzcOnMessageEvent;
import kr.co.hkcloud.palette3.core.chat.messenger.util.TeletalkReceiveUtils;
import kr.co.hkcloud.palette3.core.chat.msg.app.TalkMsgDataProcessService;
import kr.co.hkcloud.palette3.core.chat.redis.util.TalkRedisChatUtils;
import kr.co.hkcloud.palette3.core.chat.router.app.TalkDataProcessService;
import kr.co.hkcloud.palette3.core.chat.transfer.app.TransToKakaoService;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTaskTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryPathTypeCd;
import kr.co.hkcloud.palette3.file.util.FileRulePropertiesUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 메시지 수신 서비스
 * 
 * @author Orange
 */
@Slf4j
@RequiredArgsConstructor
@Service("HkcdvLinebzcReceiveMessage")
public class HkcdvLinebzcReceiveMessageImpl implements HkcdvLinebzcReceiveMessage
{
    private static final String CALLED_API = "/message";

    private final FileRulePropertiesUtils fileRulePropertiesUtils;
    private final ProxyProperties         proxyProperties;
    private final ChatProperties          chatProperties;
    private final TalkDataProcessService         talkDataProcessService;
    private final TalkMsgDataProcessService      talkMsgDataProcess;
    private final TalkBusyService         talkBusyService;
    private final TransToKakaoService     transToKakaoService;
    private final TalkRedisChatUtils      talkRedisChatUtils;
    private final TeletalkReceiveUtils    teletalkReceiveUtils;


    /**
     * 메시지 이벤트 수신
     * 
     * @param   KakaobzcOnMessageEvent messageEvent
     * @throws  Exception
     * @version                        5.0
     */
    @EventListener
    public void onMessage(final LinebzcOnMessageEvent messageEvent) throws TelewebAppException
    {
        JSONObject messageJson = messageEvent.getMessageJson();
        log.debug("onMessage - {}", messageJson.toString());

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

        messageJson.put("VIDEO_TALK_PATH", "");
        messageJson.put("VIDEO_URL", "");
        messageJson.put("VIDEO_THUMNAIL_PATH", "");

        String userKey = messageJson.getString("user_key");
        String senderKey = messageJson.getString("asp_sndrKey");

        String serialNumber = messageJson.getString("replyToken");
        String msg = messageJson.getString("messageText");				// text 내용 
        String type = messageJson.getString("messageType");
        String msgId = messageJson.getString("messageId");

        String callTypCd = messageJson.getString("call_typ_cd");
        String custcoId = messageJson.getString("custco_id");

        messageJson.put("CUSTCO_ID", custcoId);
        messageJson.put("SNDR_KEY", senderKey);
        messageJson.put("content", msg);
        messageJson.put("CHT_CUTT_DTL_ID", serialNumber);

        TelewebJSON objParams = new TelewebJSON();
        objParams.setString("TALK_USER_KEY", userKey);
        objParams.setString("TALK_SERIAL_NUMBER", serialNumber);
        objParams.setString("TYPE", type);
        objParams.setString("PROC_ID", "SYSTEM");
        objParams.setString("IMAGE_URL", ""); // 이미지 처리 기본 설정
        objParams.setString("IMAGE_TALK_PATH", "");

        objParams.setString("VIDEO_TALK_PATH", "");
        objParams.setString("VIDEO_URL", "");
        objParams.setString("VIDEO_THUMNAIL_PATH", "");

        objParams.setString("CALL_TYP_CD", callTypCd);
        objParams.setString("SNDR_KEY", senderKey);
        objParams.setString("CUSTCO_ID", custcoId);

        // 지원 가능/불가능 타입에 따라 return 처리함.
        if(teletalkReceiveUtils.isAvailableType(type, messageJson) == false) {
            teletalkReceiveUtils.noSendSystemMsg(type, objParams, messageJson, callTypCd);
            return;
        }

        if("photo".equals(type)) {
//            String imageTalkUrl = ((JSONObject) messageJson.get("content")).getString("url");
//            messageJson.put("content"   , imageTalkUrl);
//            messageJson.put("IMAGE_URL" , imageTalkUrl);
//            
//            objParams.setString("CONTENT"   , imageTalkUrl);
//            objParams.setString("IMAGE_URL" , imageTalkUrl);

            // [파일o] 메시지 수신(라인): 채팅-이미지(고객)
            final RepositoryTaskTypeCd taskTypeCd = RepositoryTaskTypeCd.chat;    //채팅
            final RepositoryPathTypeCd pathTypeCd = RepositoryPathTypeCd.images;  //이미지(고객)
            final FilePropertiesResponse fileProperties = fileRulePropertiesUtils.getProperties(taskTypeCd, pathTypeCd);
            log.debug("fileProperties>>>{}", fileProperties);
//            
            //이미지 레파지토리 저장 
            teletalkReceiveUtils.savePhototoRepositoryGetMethod(messageJson, objParams, callTypCd, fileProperties);
        }
        else if("video".equals(type)) {
            //지원 안함 - 20210615
        }
        else {
            objParams.setString("CONTENT", msg);
        }

        //장문 (1000 자이상)
        if(objParams.getString("CONTENT").length() > 1000) {
            OrgContentVO orgContentVO = new OrgContentVO();
            orgContentVO.setCustcoId(custcoId);
            teletalkReceiveUtils.insertOrgContentWithoutUrl(messageJson, objParams, orgContentVO);
        }

        TelewebJSON telewebJSON = new TelewebJSON();
        telewebJSON.setHeader("called_api", CALLED_API);
        telewebJSON.setHeader("code", 0);
        telewebJSON.setHeader("ERROR_FLAG", false);
        telewebJSON.setHeader("ERROR_MSG", "");
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(0, messageJson);
        telewebJSON.setDataObject(jsonArray);

        //상담사에게 메시지 전달 또는 재배정
        Boolean blnSendSocketToAgent = talkRedisChatUtils
            .isSendSocketToAgent(CALLED_API, userKey, telewebJSON, objParams);
        if(blnSendSocketToAgent) { return; }

        //채팅이 가능하지 않은 상태인 지 체크한다 . 상담원 무응답으로 인한 재배정은 다른 상태관계 없이 무조건 배정 처리함. ( SJH 20181024 ) 
        String contactID = objParams.getString("TALK_CONTACT_ID");
//        if (!(contactID != null && !"".equals(contactID)) && talkBusyService.isChatDisable(userKey, senderKey, callTypCd))
        if(talkBusyService.isChatDisable(userKey, senderKey, callTypCd, custcoId, true)) { return; }

        // 배분 대기 등록
        TelewebJSON inJson = new TelewebJSON();
        inJson.setString("TALK_USER_KEY", userKey);
        inJson.setString("TALK_SERIAL_NUMBER", serialNumber);
        inJson.setString("DSPTCH_PRF_KEY", senderKey);
        inJson.setString("TALK_API_CD", "/message");
        inJson.setString("SESSION_ID", "");
        inJson.setString("TYPE", type);
        inJson.setString("CONTENT", msg);
        inJson.setString("IMAGE_URL", messageJson.getString("IMAGE_URL"));
        inJson.setString("IMAGE_TALK_PATH", messageJson.getString("IMAGE_TALK_PATH"));
        inJson.setString("CUSTCO_ID", custcoId);
        inJson.setString("SNDR_KEY", senderKey);

        //2018.12.26 KMG 동영상 정보 세팅
        inJson.setString("VIDEO_TALK_PATH", messageJson.getString("VIDEO_TALK_PATH"));
        inJson.setString("VIDEO_URL", messageJson.getString("VIDEO_URL"));
        inJson.setString("VIDEO_THUMNAIL_PATH", messageJson.getString("VIDEO_THUMNAIL_PATH"));

        if(objParams.getString("ORG_CONT_ID") != null && !objParams.getString("ORG_CONT_ID").equals("")) {
            inJson.setString("LINKS", objParams.getString("ORG_CONT_ID"));
            inJson.setString("ORG_CONT_ID", objParams.getString("ORG_CONT_ID"));
        }
        else {
            inJson.setString("LINKS", "");
            inJson.setString("ORG_CONT_ID", "");
        }

        //고객문의유형 사용여부
        String inqryTypeYn = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "INQRY_TYPE_YN");

        // 고객문의유형 사용할 때
        inJson.setString("TALK_READY_CD", "10");
        inJson.setString("CHATBOT_YN", "N");
        inJson.setString("CALL_TYP_CD", callTypCd);

        //상담사에게 메시지 전달
        blnSendSocketToAgent = talkRedisChatUtils.isSendSocketToAgent(CALLED_API, userKey, telewebJSON, objParams);

        // 기존의 상담원이 비정상 종료 되었으므로 , 신규배분 되나 고객문의 유형 사용 안함. 2018.10.10 SJH
        contactID = objParams.getString("TALK_CONTACT_ID");

        if(!blnSendSocketToAgent) {
            // 라인은 문의유형 지원이 안됨. 인사말 전송
            // 고객문의유형 사용할 때  && (contactID == null || "".equals(contactID))
//            if ("Y".equals(inqryTypeYn) && (contactID == null || "".equals(contactID)))
//            {
//                String readyToTalk = HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId(custcoId, "14");
//                String ReqType = HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId(custcoId, "15");
//                String chatAgent = HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId(custcoId, "16");
//                String inqryTypeMsg = new StringBuffer(ReqType).append(" ").append(chatAgent).toString();
//
//                TelewebJSON msgInfoJson = new TelewebJSON();
//                msgInfoJson.setString("MSG_READY_TO_TALK", readyToTalk);
//                msgInfoJson.setString("MSG_INQRY_TYPE_MSG", inqryTypeMsg);
//                msgInfoJson.setString("MSG_READY_TO_TALK_ID", "14");
//
//                //고객문의유형 처리
//                talkDataProcess.processInqryType(msgInfoJson, inJson, messageJson);
//            }
//            else
//            {                

//            }

            // 인사말 발송 
            String systemMsgId = "14";
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
            TelewebJSON outJson = talkDataProcessService.processInsertTalkReady(inJson);

            int cnt2 = outJson.getInt("IS_UPDATE");
            // 메세지가 들어왔을경우에만 대기알람메세지를 전송한다
            //TransToKakao.sendSystemMsg(talkDataProcess, userKey, senderKey, env.getString("readyToTalk"));//이건철 20180403 시스템 메시지로 변경
            if(cnt2 == 0) {
                // 인사말  
                transToKakaoService.sendSystemMsg(userKey, senderKey, HcTeletalkDbSystemMessage.getInstance()
                    .getTelewebJsonBySystemMsgId(custcoId, "20190108130828193MSG48734"), callTypCd);

                // 1차응대 지연 발송 
                transToKakaoService.sendSystemMsg(userKey, senderKey, HcTeletalkDbSystemMessage.getInstance()
                    .getTelewebJsonBySystemMsgId(custcoId, systemMsgId), callTypCd);
            }

        }
    }

}
