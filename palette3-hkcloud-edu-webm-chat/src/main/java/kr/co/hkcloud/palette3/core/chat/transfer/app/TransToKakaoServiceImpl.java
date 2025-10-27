package kr.co.hkcloud.palette3.core.chat.transfer.app;


import java.net.URI;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.properties.palette.PaletteProperties;
import kr.co.hkcloud.palette3.core.chat.messenger.domain.ChatOnExpiredSessionEvent;
import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.kakaobzc.domain.KakaobzcOnExpiredSessionEvent;
import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.linebzc.domain.LinebzcOnExpiredSessionEvent;
import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.naverbzc.domain.NaverbzcOnExpiredSessionEvent;
import kr.co.hkcloud.palette3.core.chat.router.app.TalkDataProcessService;
import kr.co.hkcloud.palette3.core.chat.router.dao.RoutingToAgentReadyDAO;
import kr.co.hkcloud.palette3.core.chat.transfer.domain.TransToEndTalkEvent;
import kr.co.hkcloud.palette3.core.chat.transfer.util.TransferUtils;
import kr.co.hkcloud.palette3.core.util.PaletteFilterUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.enumer.RepositoryPathTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTaskTypeCd;
import kr.co.hkcloud.palette3.file.util.FileRulePropertiesUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 *
 * @author Orange
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("transToKakaoService")
public class TransToKakaoServiceImpl implements TransToKakaoService {

    private final TwbComDAO mobjDao;
    private final FileRulePropertiesUtils fileRulePropertiesUtils;
    private final PaletteProperties paletteProperties;
    private final SendToKakaoService sendToKakaoService;
    private final RoutingToAgentReadyDAO routingToAgentReadyDAO;
    private final PaletteFilterUtils paletteFilterUtils;
    private final TransferUtils transferUtils;
    private final ApplicationEventPublisher eventPublisher;
    private final InnbCreatCmmnService innbCreatCmmnService;
    private final SendTestService myService;
    //    private final TalkDataProcessService    talkDataProcessService;

	@Autowired
    Environment environment;
    
    @Autowired
    private TalkDataProcessService dataProcess;

    String logDevider = "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";

    public void setDataProcess(TalkDataProcessService dataProcess) {
        this.dataProcess = dataProcess;
    }


    /**
     *
     * @param  active              구분
     * @param  writeData           데이터
     * @return JSONObject
     * @throws TelewebAppException
     */
    public JSONObject transToKakao(String active, JSONObject writeData, String callTypCd) throws TelewebAppException {
        //============================
        //이메일인 경우 메세지를 전송하지 않는다.
        if("EMAIL".equals(callTypCd)) {
            return new JSONObject();
        }
        //============================
        if (active.equals("image") && callTypCd.equals("NTT")) {
            active = "image_navertalktalk";
        } else if (active.equals("links") && callTypCd.equals("NTT")) {
            active = "composite";
        }
        final URI uri = transferUtils.getTargetUrl(active, callTypCd);
        JSONObject custInfoObj = null;
        String userKey = "";
        log.info("!!!!@@@@#####" + active);
        log.info("!!!!@@@@#####" + writeData);
        writeData.put("active",active);
        log.info("!!!!@@@@#####" + callTypCd);
        userKey = writeData.getString("user_key");

        // prfofile 등록시 senderkey 없음( 티톡) 
        if (active != null && !active.equals("profile")) {

            custInfoObj = transferUtils.getDsptchPrfKeyByAspSenderKey(writeData);

            writeData.put("sndrKey", custInfoObj.getString("dsptchPrfKey"));
            writeData.put("CHT_USER_KEY", userKey);
            writeData.put("call_typ_cd", callTypCd);
        }

        // 라인은 이미지도 메시지와 같이 동일하게 전송함.
        if (callTypCd != null && callTypCd.equals("LINE") && active.equals("image")) {
            active = "image_line";
        }

        if (callTypCd != null && (callTypCd.equals("LINE") || callTypCd.equals("NTT")) && writeData != null && !writeData.isEmpty()) {
            //LINE과 NTT에서는 Authorization필요 => UUID를 Authorization로 사용
            //            writeData.put("talk_access_token", custInfoObj.getString("talk_access_token"));
            writeData.put("talk_access_token", custInfoObj.getString("talk_access_token"));
        }

        JSONObject retJson = null;
        switch (active) {
            //일반메세지
            //링크메세지 (일반메세지와 KAKAO path 동일하다.)
            case "write": {
                // ttalk 영업시간 전송 
                if (callTypCd != null && callTypCd.equals("TTALK") && writeData != null && !writeData.isEmpty() && !"".equals(
                    custInfoObj.getString("custco_id"))) {

                    transferUtils.setWorkingTime(writeData, custInfoObj.getString("custco_id"));

                }
                log.info("check active");
                log.info("!!!!@@@@#####" + active);
                log.info("!!!!@@@@#####" + writeData);
                log.info("!!!!@@@@#####" + callTypCd);
                
                //메신저별 전송할 데이터 포맷 생성
                writeData = transferUtils.tranToFormat("text", writeData, callTypCd);
                retJson = sendToKakaoService.sendTextToKakao(uri, writeData);
                break;
            }
            case "links": {
                final URI writeUri = transferUtils.getTargetWriteUrl(callTypCd);
                final URI imageUri = transferUtils.getTargetImageUrl(callTypCd);
                writeData.put("CUSTCO_ID", custInfoObj.getString("custco_id"));

                // ttalk 영업시간 전송 
                if (callTypCd != null && callTypCd.equals("TTALK") && writeData != null && !writeData.isEmpty() && !"".equals(
                    custInfoObj.getString("custco_id"))) {

                    transferUtils.setWorkingTime(writeData, custInfoObj.getString("custco_id"));

                }
                writeData = transferUtils.tranToFormat("text", writeData, callTypCd);
                log.info("TransToKakaoServiceImpl=======" + writeUri);
                log.info("TransToKakaoServiceImpl=======" + imageUri);
                log.info("TransToKakaoServiceImpl=======" + writeData);
                retJson = sendToKakaoService.sendLinkTextToKakao(writeUri, imageUri, writeData);
                break;
            }
            //이미지
            case "image": {
                final URI writeUri = transferUtils.getTargetWriteUrl(callTypCd);
                retJson = sendToKakaoService.sendImageToKakao(writeUri, uri, writeData);
                break;
            }
            //이미지 ( 라인)
            case "image_line": {
                writeData = transferUtils.tranToFormat("image", writeData, callTypCd);
                retJson = sendToKakaoService.sendTextToKakao(uri, writeData);
                break;
            }
            //이미지 ( 네이버톡톡)
            case "image_navertalktalk": {
                writeData = transferUtils.tranToFormat("image", writeData, callTypCd);
                retJson = sendToKakaoService.sendTextToKakao(uri, writeData);
                break;
            }
            //컴포지트 : 문의유형( 네이버톡톡)
            case "composite": {
                writeData = transferUtils.tranToFormat("composite", writeData, callTypCd);
                retJson = sendToKakaoService.sendTextToKakao(uri, writeData);
                break;
            }
            //프로필생성 티톡 
            case "profile": {
                retJson = sendToKakaoService.sendTextToKakao(uri, writeData);
                break;
            }
            //봇연결
            case "botevent": {
                retJson = sendToKakaoService.endWithBot(uri, writeData);
                break;
            }
            case "file": {
                log.info("check active");
                log.info("!!!!@@@@#####" + active);
                log.info("!!!!@@@@#####" + writeData);
                log.info("!!!!@@@@#####" + callTypCd);
                
                //메신저별 전송할 데이터 포맷 생성
                writeData = transferUtils.tranToFormat("file", writeData, callTypCd);
                retJson = sendToKakaoService.sendTextToKakao(uri, writeData);
                break;
            }
            case "audio": {
                log.info("check active");
                log.info("!!!!@@@@#####" + active);
                log.info("!!!!@@@@#####" + writeData);
                log.info("!!!!@@@@#####" + callTypCd);
                
                //메신저별 전송할 데이터 포맷 생성
                writeData = transferUtils.tranToFormat("audio", writeData, callTypCd);
                retJson = sendToKakaoService.sendTextToKakao(uri, writeData);
                break;
            }
            default: {
                break;
            }
        }
        return retJson;
    }


    /**
     * endtalk 만 이벤트로 처리하도록 변경
     * eventPublisher.publishEvent 리스너 - TransToEndTalkEvent 이벤트 처리.
     * @param  endTalkEvent
     * @return
     * @throws TelewebAppException
     */
    @EventListener
    public JSONObject transToEndTalkEvent(final TransToEndTalkEvent endTalkEvent) throws TelewebAppException {
        String logPrefix = logDevider + ".transToEndTalkEvent" + "___";
        int logNum = 1;
        log.info(logPrefix + (logNum++) + " ::: transToEndTalkEvent start ::: ");
        String active = endTalkEvent.getActive();
        JSONObject writeData = endTalkEvent.getWriteData();
        String callTypCd = endTalkEvent.getCallTypCd();

        final URI uri = transferUtils.getTargetUrl(active, callTypCd);
        JSONObject custInfoObj = null;
        String userKey = "";
        
        log.info("active" + active);
        log.info("writeData" + writeData);
        log.info("callTypCd" + callTypCd);
        log.info("endTalkEvent" + endTalkEvent);
        log.info("uri" + uri);

        // prfofile 등록시 senderkey 없음( 티톡) 
        if (active != null && !active.equals("profile")) {

            custInfoObj = transferUtils.getDsptchPrfKeyByAspSenderKey(writeData);
            userKey = writeData.getString("user_key");
            //채널 유형 - 이메일, 게시판 은 dsptchPrfKey 없음
            writeData.put("sndrKey", custInfoObj.has("dsptchPrfKey") ? custInfoObj.getString("dsptchPrfKey") : "");
            writeData.put("user_key", transferUtils.getUserKeyByTalkUserKey(writeData.getString("user_key")));
            writeData.put("CHT_USER_KEY", transferUtils.getUserKeyByTalkUserKey(writeData.getString("user_key")));
            writeData.put("call_typ_cd", callTypCd);
            
            log.info("userKey" + userKey);
            log.info("writeData" + writeData);
        }

        // 라인은 이미지도 메시지와 같이 동일하게 전송함.
        if (callTypCd != null && callTypCd.equals("LINE") && active.equals("image")) {
            active = "image_line";
        }

        if (callTypCd != null && (callTypCd.equals("LINE") || callTypCd.equals("NTT")) && writeData != null && !writeData.isEmpty()) {
            writeData.put("talk_access_token", custInfoObj.getString("talk_access_token"));
        }

        JSONObject retJson = null;
        log.info(logPrefix + (logNum++) + " ::: active ::: " + active);
        switch (active) {
            //상담종료
            case "endtalk": {
                // endTalk
                
                if (callTypCd.equals("LINE")) {   // line ( 종료 api 별도 없음. 종료event 발생처리함)

                    writeData.put("asp_sndrKey", custInfoObj.getString("sndrKey"));
                    writeData.put("user_key", userKey);
                    writeData.put("CHT_USER_KEY", userKey);
                    writeData.put("custco_id", custInfoObj.getString("custco_id"));

                    eventPublisher.publishEvent(LinebzcOnExpiredSessionEvent.builder().expiredSessionJson(writeData).build());

                    return retJson;
                }

                if (callTypCd.equals("NTT")) {
                    writeData.put("asp_sndrKey", custInfoObj.getString("sndrKey"));
                    writeData.put("user_key", userKey);
                    writeData.put("CHT_USER_KEY", userKey);
                    writeData.put("custco_id", custInfoObj.getString("custco_id"));

                    eventPublisher.publishEvent(NaverbzcOnExpiredSessionEvent.builder().expiredSessionJson(writeData).build());

                    return retJson;
                }

                if (callTypCd.equals("KAKAO")) {  // KAKAO ( KAKAO 에서 SESSION ID 를 받지 못하는 경우 종료event 발생처리함)
                    writeData.put("CHT_USER_KEY", userKey);
                    retJson = sendToKakaoService.sendTextToKakao(uri, writeData);
                    log.info(logPrefix + (logNum++) + " ::: call after sendToKakaoService.sendTextToKakao(uri, writeData) ::: " + retJson);
                    if (!retJson.containsKey("sessionId"))  // session id 가 없다면 현재 이미 종료 된 정상적이지 않는 세션임.(강제 종료 처리함)
                    {
                        writeData.put("asp_sndrKey", custInfoObj.getString("sndrKey"));
                        writeData.put("user_key", userKey);
                        writeData.put("CHT_USER_KEY", userKey);
                        writeData.put("custco_id", custInfoObj.getString("custco_id"));
                        log.info(logPrefix + (logNum++) + " ::: session id 가 없다면 현재 이미 종료 된 정상적이지 않는 세션임.(강제 종료 처리함) ::: " + writeData);

                        eventPublisher.publishEvent(KakaobzcOnExpiredSessionEvent.builder().expiredSessionJson(writeData).build());
                    }

                    return retJson;
                }
                
                //이메일 상담 종료 처리
                if (callTypCd.equals("EMAIL")) {
                    
                    writeData.put("asp_sndrKey", custInfoObj.getString("sndrKey"));
                    writeData.put("user_key", userKey);
                    writeData.put("CHT_USER_KEY", userKey);
                    writeData.put("custco_id", custInfoObj.getString("custco_id"));

                    eventPublisher.publishEvent(ChatOnExpiredSessionEvent.builder().expiredSessionJson(writeData).build());

                    return retJson;
                }
                
                //TTALK 케이스
                writeData.put("CHT_USER_KEY", userKey);
                retJson = sendToKakaoService.sendTextToKakao(uri, writeData);
                break;
            }
            default: {
                break;
            }
        }
        return retJson;
    }

    //    /**
    //     *
    //     * @param active
    //     * @param userKey
    //     * @return
    //     * @throws TelewebAppException
    //     */
    //    public JSONObject commandToKakao(String active, String userKey, String callTypCd) throws TelewebAppException
    //    {
    //        String url = null;
    //    	  final String activateUrl   = chatProperties.getMessenger().getKakaotalk().getUrls().getProfileChatActivate().toString();
    //	      final String deactivateUrl = chatProperties.getMessenger().getKakaotalk().getUrls().getProfileChatDeactivate().toString();
    //	      final String blockeUrl     = chatProperties.getMessenger().getKakaotalk().getUrls().getProfileUserBlock().toString();
    //	      final String unblockUrl    = chatProperties.getMessenger().getKakaotalk().getUrls().getProfileUserUnblock().toString();
    //	      final String senderKey     = chatProperties.getMessenger().getKakaotalk().getSenderkey().toString();
    //	      final String boteventUrl     = chatProperties.getMessenger().getKakaotalk().getUrls().getChatEndWithBot().toString();

    //        if ("active".equals(active))
    //        {
    //            url = activateUrl;
    //        }
    //        else if ("deactive".equals(active))
    //        {
    //            url = deactivateUrl;
    //        }
    //        else if ("block".equals(active))
    //        {
    //            url = blockeUrl;
    //        }
    //        else if ("unblock".equals(active))
    //        {
    //            url = unblockUrl;
    //        }
    //        else if ("boteventUrl".equals(active))
    //        {
    //            url = boteventUrl;
    //        }
    //        else
    //        {
    //            return null;
    //        }
    //
    //        HttpPost post = new HttpPost(url);
    //        post.setHeader("User-Agent", "Mozilla/5.0");
    //        post.setHeader("Accept", "application/json");
    //        post.setHeader("Content-Type", "application/json");
    //
    //        SSLContextBuilder builder = new SSLContextBuilder();
    //        builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
    //        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
    //
    //        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
    //        CloseableHttpResponse response = null;
    //        HttpEntity responseEntity = null;
    //
    //        Sender sender = new Sender();
    //        Gson gson = new Gson();
    //        JSONObject retJson = null;
    //
    //        StringEntity postEntity = new StringEntity(gson.toJson(sender));
    //        sender.setSender_key(senderKey);
    //        sender.setUser_key(userKey);
    //
    //        try
    //        {
    //            post.setEntity(postEntity);
    //            response = httpclient.execute(post);
    //            responseEntity = response.getEntity();
    //            String jsonString = EntityUtils.toString(responseEntity);
    //            retJson = JSONObject.fromObject(jsonString);
    //        }
    //        finally
    //        {
    //            log.debug("SndKakao ::{}", EntityUtils.toString(postEntity));
    //            log.debug("RcvKakao ::{}", retJson.toString());
    //            try { EntityUtils.consume(responseEntity); } finally {}// 필요함.
    //            try { response.close(); } finally {}
    //            try { httpclient.close(); } finally {}
    //        }
    //        return retJson;
    //    }


    /**
     * 카카오 시스템메세지 전송(고객 대기중 - READY)
     *
     * @param userKey
     * @param senderKey
     * @param message
     */
    public void sendSystemMsg(String userKey, String senderKey, String message, String strSystemMsgId, String callTypCd,
        String custcoId) throws TelewebAppException {
        String logPrefix = logDevider + ".sendSystemMsg___" + userKey + "___" + senderKey + "___" + strSystemMsgId + "___" + callTypCd + "___";
        int logNum = 1;
        log.info(logPrefix + (logNum++) + " ::: 카카오 시스템메세지 전송(고객 대기중 - READY) - sendSystemMsg start ::: "
            + "\nuserKey ::: " + userKey
            + "\nsenderKey ::: " + senderKey
            + "\nmessage ::: " + message
            + "\nstrSystemMsgId ::: " + strSystemMsgId
            + "\ncallTypCd ::: " + callTypCd
        );
        
        String partnerId = paletteProperties.getPartnerId();
//        int userId = (PaletteUserContextSupport.getCurrentUser() == null ? 1 : PaletteUserContextSupport.getCurrentUser().getUserId());
        //시스템 메시지 발송자는 무조건 '2'(시스템)
        int userId = 2;
        //시스템메시지 db에서 바로 가져오기
        TelewebJSON result = myService.selectMessage(custcoId, strSystemMsgId, userId);
//        String filter2Msg = message;
        String filter2Msg = result.getString("MSG_CN");
        JSONObject sendJson = null;
        String serial = Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID"));
        String sendString = "";
        String msgTypeCd = "GENMSG";
        sendString = String.format(
            "{\"user_key\":\"%s\",\"sndrKey\":\"%s\",\"CHT_CUTT_DTL_ID\":\"%s\",\"message_type\":\"TX\",\"message\":\"%s\"}", userKey,
            senderKey, serial, filter2Msg);
        // 상담요청 (접수) 상태를 알려줌.상담이 요청 된 건은 스토리지 정보 초기화 하지 않음.
        if ("TTALK".equals(callTypCd) && "14".equals(strSystemMsgId)) {
            if(message.indexOf("||[{&quot;") == -1) {
	            sendString = String.format(
	                "{\"user_key\":\"%s\",\"sndrKey\":\"%s\",\"CHT_CUTT_DTL_ID\":\"%s\",\"message_type\":\"TX\",\"message\":\"%s\",\"request_chat\":\"true\"}",
	                userKey, senderKey, serial, filter2Msg);
            } else {
                sendString = String.format(
                        "{\"user_key\":\"%s\",\"sndrKey\":\"%s\",\"CHT_CUTT_DTL_ID\":\"%s\",\"message_type\":\"INPUT\",\"message\":\"%s\",\"request_chat\":\"true\"}",
                        userKey, senderKey, serial, filter2Msg);
                msgTypeCd = "INFOMSG";
            }
        }
        log.info(logPrefix + (logNum++) + " ::: sendString ::: " + sendString);

        sendJson = JSONObject.fromObject(sendString);

        TelewebJSON inJson = new TelewebJSON();
        inJson.setString("TALK_USER_KEY", userKey);
        inJson.setString("CHT_USER_KEY", userKey);
        inJson.setString("TALK_SERIAL_NUMBER", serial);
        inJson.setString("SNDR_KEY", senderKey);
        inJson.setString("CUSTCO_ID", custcoId);
        inJson.setString("TALK_API_CD", "/message");
        inJson.setString("TYPE", "system");
        inJson.setString("CONTENT", filter2Msg);
        inJson.setString("SYS_MSG_ID", strSystemMsgId);
        inJson.setString("CHT_CUTT_DTL_ID", serial);

        log.info(logPrefix + (logNum++) + " ::: CHT_CUTT_DTL_ID ::: " + inJson.getString("CHT_CUTT_DTL_ID"));
    	TelewebJSON cntJson = new TelewebJSON();
    	log.info(logPrefix + (logNum++) + " ::: SELECT_PLT_CHT_CUTT_DTL_INSERT_PARAM ::: " + inJson);
    	cntJson = mobjDao.select("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgMapper", "SELECT_PLT_CHT_CUTT_DTL_INSERT_PARAM", inJson);
    	
    	if(cntJson.getString("CNT").equals("0")) {
            log.info("-------------------------------------------");
            log.info(logPrefix + (logNum++) + "=======> 2...CAN'T INSERT CHT_CUTT_DTL :: " + inJson);
            log.info("-------------------------------------------");
            
            log.info(logPrefix + (logNum++) + "message" + filter2Msg);
	        log.info(logPrefix + (logNum++) + "filter2Msg" + filter2Msg);
	        log.info(logPrefix + (logNum++) + "inJson" + inJson);
	
	        //inJson.setString("CHT_CUTT_ID", messageJson.getString("CHT_CUTT_ID"));
	
	        inJson.setString("CUSL_ID", "2");
	        inJson.setString("USER_ID", "2");
	        inJson.setString("MSG_TYPE_CD", msgTypeCd);
	        inJson.setString("RCPTN_DSPTCH_CD", "SND");
	        
	        log.info(logPrefix + (logNum++) + " ::: 처음 인입되는 채팅이 받을 수 없는 채팅이라면(용량제한 이미지 등) 상담 이력에 쌓지 않고 고객에서 시스템 메시지 보냄 ::: "
	            + "\nwrite ::: " + "write"
	            + "\nsendJson ::: " + sendJson
	            + "\ncallTypCd ::: " + callTypCd
	        );
	
	        JSONObject ret = transToKakao("write", sendJson, callTypCd);
    	} else {
	        log.info(logPrefix + (logNum++) + "message" + filter2Msg);
	        log.info(logPrefix + (logNum++) + "filter2Msg" + filter2Msg);
	        log.info(logPrefix + (logNum++) + "inJson" + inJson);
	
	        //inJson.setString("CHT_CUTT_ID", messageJson.getString("CHT_CUTT_ID"));
	
	        inJson.setString("CUSL_ID", "2");
	        inJson.setString("USER_ID", "2");
	        inJson.setString("MSG_TYPE_CD", msgTypeCd);
	        inJson.setString("RCPTN_DSPTCH_CD", "SND");
	        
	        log.info(logPrefix + (logNum++) + " ::: call before transToKakao(\"write\", sendJson, callTypCd) ::: "
	            + "\nwrite ::: " + "write"
	            + "\nsendJson ::: " + sendJson
	            + "\ncallTypCd ::: " + callTypCd
	        );
	
	        JSONObject ret = transToKakao("write", sendJson, callTypCd);
	        log.info(logPrefix + (logNum++) + " ::: dataProcess.insertTalkContactDetail(inJson) ::: " + inJson);
	        dataProcess.insertTalkContactDetail(inJson);
	
	        //채팅대기상세 테이블 없어짐
	        //routingToAgentReadyDAO.insertTalkUserReadyDetail(inJson);
    	}
    }


    /**
     * 카카오 시스템메세지 전송(수신불가메시지전송)
     *
     * @param userKey
     * @param senderKey
     * @param message
     */
    public void noSendSystemMsg(String userKey, String senderKey, String message, String callTypCd) throws TelewebAppException {
        String partnerId = paletteProperties.getPartnerId();
        String filter2Msg = paletteFilterUtils.filter3(message);
        JSONObject sendJson = null;
        String serial = Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID"));

        String sendString = String.format(
            "{\"user_key\":\"%s\",\"sndrKey\":\"%s\",\"CHT_CUTT_DTL_ID\":\"%s\",\"message_type\":\"TX\",\"message\":\"%s\"}", userKey,
            senderKey, serial, filter2Msg);
        sendJson = JSONObject.fromObject(sendString);

        TelewebJSON inJson = new TelewebJSON();
        inJson.setString("TALK_USER_KEY", userKey);
        inJson.setString("TALK_SERIAL_NUMBER", serial);
        inJson.setString("CHT_CUTT_DTL_ID", serial);
        inJson.setString("SNDR_KEY", senderKey);
        inJson.setString("TALK_API_CD", "/message");
        inJson.setString("TYPE", "system");
        inJson.setString("CONTENT", filter2Msg);

        JSONObject ret = transToKakao("write", sendJson, callTypCd);
    }


    /**
     * 티톡영업시간전송
     *
     * @param userKey
     * @param senderKey
     * @param message
     */
    public void sendWorktimeMsg(String userKey, String senderKey, TelewebJSON objParams, String callTypCd) throws TelewebAppException {
        String partnerId = paletteProperties.getPartnerId();
        JSONObject sendJson = null;
        String serial = objParams.getString("CHT_CUTT_DTL_ID");

        String wst = objParams.getString("wst");
        String wet = objParams.getString("wet");
        String holidayYn = objParams.getString("holidayYn");

        String sendString = String.format(
            "{\"user_key\":\"%s\",\"sndrKey\":\"%s\",\"CHT_CUTT_DTL_ID\":\"%s\",\"message_type\":\"workTime\",\"wst\":\"%s\",\"wet\":\"%s\",\"holidayYn\":\"%s\"}",
            userKey, senderKey, serial, wst, wet, holidayYn);
        sendJson = JSONObject.fromObject(sendString);

        JSONObject ret = transToKakao("write", sendJson, callTypCd);
    }


    /**
     * 티톡콜백여부전송
     *
     * @param userKey
     * @param senderKey
     * @param message
     */
    public void sendCallbackYnMsg(String userKey, String senderKey, String callbackYn, String callTypCd) throws TelewebAppException {
        String partnerId = paletteProperties.getPartnerId();
        JSONObject sendJson = null;
        String serial = Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID"));
        String message = "콜백예약 되었습니다.";

        String sendString = String.format(
            "{\"user_key\":\"%s\",\"sndrKey\":\"%s\",\"CHT_CUTT_DTL_ID\":\"%s\",\"message_type\":\"TX\",\"callbackYN\":\"%s\",\"message\":\"%s\"}",
            userKey, senderKey, serial, callbackYn, message);
        sendJson = JSONObject.fromObject(sendString);

        JSONObject ret = transToKakao("write", sendJson, callTypCd);
    }


    /**
     * 카카오 시스템메세지 전송(고객 채팅중 - CONTACT)
     *
     * @param userKey
     * @param senderKey
     * @param message
     */
    public void sendCustNoResponseSystemMsg(String userKey, String senderKey, TelewebJSON messageJson, String strSystemMsgId,
        String callTypCd, String custcoId) throws TelewebAppException {
        String partnerId = paletteProperties.getPartnerId();
        String message = messageJson.getString("MSG_CN");
        String filter2Msg = paletteFilterUtils.filter3(message);

        String type = messageJson.getString("TYPE");

        JSONObject sendJson = null;
        String serial = messageJson.getString("CHT_CUTT_DTL_ID");
        
        String sendString = String.format(
            "{\"user_key\":\"%s\",\"sndrKey\":\"%s\",\"CHT_CUTT_DTL_ID\":\"%s\",\"message_type\":\"TX\",\"message\":\"%s\"}", userKey,
            senderKey, serial, filter2Msg);
        sendJson = JSONObject.fromObject(sendString);

        TelewebJSON inJson = new TelewebJSON();
        inJson.setString("CHT_CUTT_DTL_ID", messageJson.getString("CHT_CUTT_DTL_ID"));
        inJson.setString("CHT_CUTT_ID", messageJson.getString("CHT_CUTT_ID"));
        inJson.setString("CUSTCO_ID", messageJson.getString("CUSTCO_ID"));
        inJson.setString("CUSL_ID", "2");
        inJson.setString("USER_ID", "2");
        inJson.setString("CHT_USER_KEY", messageJson.getString("CHT_USER_KEY"));
        inJson.setString("MSG_TYPE_CD", messageJson.getString("MSG_TYPE_CD"));
        inJson.setString("CONTENT", messageJson.getString("MSG_CN"));
        inJson.setString("RCPTN_DSPTCH_CD", "SND");
        inJson.setString("SYS_MSG_ID", "6");

        JSONObject ret = transToKakao("write", sendJson, callTypCd);

        dataProcess.insertTalkContactDetail(inJson);
    }


    /**
     * 카카오 시스템메세지 전송(메시지 타입에 따른 처리)
     *
     * @param userKey
     * @param senderKey
     * @param messageJson 메시지 JSON 객체
     */
    public void sendSystemMsg(String userKey, String senderKey, TelewebJSON messageJson, String callTypCd) throws TelewebAppException {
        
        String logPrefix = logDevider + ".sendSystemMsg___" + userKey + "___" + senderKey + "___" + callTypCd + "___";
        int logNum = 1;
        log.info(logPrefix + (logNum++) + " ::: 카카오 시스템메세지 전송(메시지 타입에 따른 처리) - sendSystemMsg start ::: "
            + "\nuserKey ::: " + userKey
            + "\nsenderKey ::: " + senderKey
            + "\nmessageJson ::: " + messageJson
            + "\ncallTypCd ::: " + callTypCd
        );
        String strSystemMsgId = messageJson.getString("SYS_MSG_ID");
        String strSndRcvCd = messageJson.getString("RCPTN_DSPTCH_CD");
        String strMsgType = messageJson.getString("MSG_TYPE_CD");
        String strMsgContent = messageJson.getString("MSG_CN");
        String strMsgCl = messageJson.getString("MSG_SE_CD");
        String strLinksType = messageJson.getString("LNK_TYPE_CD");
        String strCustcoId = messageJson.getString("CUSTCO_ID");

        log.trace("sendSystemMsg messageJson ::: {}", messageJson);

    	TelewebJSON cntJson = new TelewebJSON();

    	messageJson.setString("SNDR_KEY", senderKey);
    	messageJson.setString("CHT_USER_KEY", userKey);
    	cntJson = mobjDao.select("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgMapper", "SELECT_PLT_CHT_CUTT_DTL_INSERT_PARAM", messageJson);
    	
        if (StringUtils.isEmpty(userKey)) {
            log.info("-------------------------------------------");
            log.info(logPrefix + (logNum++) + " userKey is empty");
            log.info("-------------------------------------------");
        } else if (cntJson.getString("CNT").equals("0")){
            log.info("-------------------------------------------");
            log.info(logPrefix + (logNum++) + " 1...CAN'T INSERT CHT_CUTT_DTL :: " + messageJson);
            log.info("-------------------------------------------");
            if ("SND".equals(strSndRcvCd)) {
                log.info(logPrefix + (logNum++) + " strSndRcvCd = SND // 메시지 발송 " + messageJson);
                if ("GENMSG".equals(strMsgType)) {
                    log.info(logPrefix + (logNum++) + " strMsgType = GENMSG // 일반메시지 " + messageJson);
                    //시스템메시지(SYSMSG), 안내메시지(DLYMSG) 는 TWB_TALK_USER_DETAIL에 저장된다.
                    if ("SYSMSG".equals(strMsgCl) || "DLYMSG".equals(strMsgCl) || "ATOMSG".equals(strMsgCl) || "RCPMSG".equals(strMsgCl)
                        || "CUTTEND".equals(strMsgCl) || "TRANMSG".equals(strMsgCl)) {
                        log.info(logPrefix + (logNum++) + " ::: call before sendSystemMsg(userKey, senderKey, strMsgContent, strSystemMsgId, callTypCd, strCustcoId) ::: "
                            + "\nuserKey ::: " + userKey
                            + "\nsenderKey ::: " + senderKey
                            + "\nstrMsgContent ::: " + strMsgContent
                            + "\nstrSystemMsgId ::: " + strSystemMsgId
                            + "\ncallTypCd ::: " + callTypCd
                            + "\nstrCustcoId ::: " + strCustcoId
                        );
                        
                        sendSystemMsg(userKey, senderKey, strMsgContent, strSystemMsgId, callTypCd, strCustcoId);
                    }
                }
            }
        } else {
            log.info(logPrefix + (logNum++) + " ::: strSndRcvCd ::: " + strSndRcvCd);
            log.info(logPrefix + (logNum++) + " ::: strMsgCl ::: " + strMsgCl);
                
            if ("SND".equals(strSndRcvCd)) {
                if ("GENMSG".equals(strMsgType)) {
                    //시스템메시지(SYSMSG), 안내메시지(DLYMSG) 는 TWB_TALK_USER_DETAIL에 저장된다.
                    if ("SYSMSG".equals(strMsgCl) || "DLYMSG".equals(strMsgCl) || "ATOMSG".equals(strMsgCl) || "RCPMSG".equals(strMsgCl)
                        || "CUTTEND".equals(strMsgCl) || "TRANMSG".equals(strMsgCl)) {
                        log.info(logPrefix + (logNum++) + " ::: call before sendSystemMsg(userKey, senderKey, strMsgContent, strSystemMsgId, callTypCd, strCustcoId) ::: "
                            + "\nuserKey ::: " + userKey
                            + "\nsenderKey ::: " + senderKey
                            + "\nstrMsgContent ::: " + strMsgContent
                            + "\nstrSystemMsgId ::: " + strSystemMsgId
                            + "\ncallTypCd ::: " + callTypCd
                            + "\nstrCustcoId ::: " + strCustcoId
                        );
                        
                        
                        sendSystemMsg(userKey, senderKey, strMsgContent, strSystemMsgId, callTypCd, strCustcoId);

                        //                    if(strSystemMsgId.equals("14")) {
                        //                    	log.info("nowCutt" + messageJson);
                        //                    	TelewebJSON cuttJson = new TelewebJSON();
                        //                    	messageJson.setString("CHN_CLSF_CD", callTypCd);
                        //                    	messageJson.setString("CHT_USER_KEY", userKey);
                        //                    	messageJson.setString("SNDR_KEY", senderKey);
                        //                    	cuttJson = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectNowCutt", messageJson);
                        //	                    messageJson.setString("CHT_CUTT_ID", cuttJson.getString("CHT_CUTT_ID"));
                        //
                        //                    	log.info("nowCutt" + cuttJson);
                        //	                	String chtCuttDtlId = Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID"));
                        //	                    messageJson.setString("CHT_CUTT_DTL_ID", chtCuttDtlId);
                        //                    	messageJson.setString("CONTENT", strMsgContent);
                        //                    	messageJson.setString("RCPTN_SNDPTY_ID", "2");
                        //                    	messageJson.setString("USER_ID", "2");
                        //	                    mobjDao.insert("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgMapper", "INSERT_PLT_CHT_CUTT_DTL", messageJson);
                        //                    }
                    }
                    //고객무응답메시지(NOAMSG) 는 TWB_TALK_CONTACT_DETAIL에 저장된다.
                    else if ("NOAMSG".equals(strMsgCl)) {
                        log.info(logPrefix + (logNum++) + " ::: call before sendCustNoResponseSystemMsg(userKey, senderKey, messageJson, strSystemMsgId, callTypCd, strCustcoId) ::: "
                            + "\nuserKey ::: " + userKey
                            + "\nsenderKey ::: " + senderKey
                            + "\nmessageJson ::: " + messageJson
                            + "\nstrSystemMsgId ::: " + strSystemMsgId
                            + "\ncallTypCd ::: " + callTypCd
                            + "\nstrCustcoId ::: " + strCustcoId
                        );
                        sendCustNoResponseSystemMsg(userKey, senderKey, messageJson, strSystemMsgId, callTypCd, strCustcoId);
                    }
                    //응대지연메시지(NOAMSG) 는 TWB_TALK_CONTACT_DETAIL에 저장된다.
                    else if ("DLYAUTOMSG".equals(strMsgCl)) {
                        messageJson.setString("CHT_CUTT_DTL_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID")));
                        
                        log.info(logPrefix + (logNum++) + " ::: call before sendCustNoResponseSystemMsg(userKey, senderKey, messageJson, strSystemMsgId, callTypCd, strCustcoId) ::: "
                            + "\nuserKey ::: " + userKey
                            + "\nsenderKey ::: " + senderKey
                            + "\nmessageJson ::: " + messageJson
                            + "\nstrSystemMsgId ::: " + strSystemMsgId
                            + "\ncallTypCd ::: " + callTypCd
                            + "\nstrCustcoId ::: " + strCustcoId
                        );
                        sendCustNoResponseSystemMsg(userKey, senderKey, messageJson, strSystemMsgId, callTypCd, strCustcoId);
                    } else {
                        log.error(logPrefix + (logNum++) + " ::: Error : sendSystemMsg - TX message classification is fault.");
                        throw new TelewebAppException("TX message classification is fault.");
                    }
                } else if ("LIKMSG".equals(strMsgType)) {
                    //버튼 리스트를 가져온다.
                    TelewebJSON linksJson = new TelewebJSON();
                    linksJson = dataProcess.selectLinksButtonList(strSystemMsgId, strCustcoId);
                    JSONArray linksJsonArr = linksJson.getDataObject("DATA");
                    int intArrSize = linksJsonArr.size();

                    //String strlinks = "";
                    StringBuffer strlinks = new StringBuffer();

                    if (intArrSize > 5) {
                        intArrSize = 5;
                    }

                    //WL : url_mobile(필수), url_pc
                    if ("WEBLINK".equals(strLinksType) || "WL".equals(strLinksType)) {
                        for (int i = 0; i < intArrSize; i++) {
                            String strCdNm = linksJsonArr.getJSONObject(i).getString("BTN_NM");
                            String strCd = linksJsonArr.getJSONObject(i).getString("EXTRA");
                            String strUrlMobile = linksJsonArr.getJSONObject(i).getString("URL_MOBILE");
                            String strUrlPc = linksJsonArr.getJSONObject(i).getString("URL_PC");

                            if (i != 0) {
                                strlinks.append(",");
                            }
                            strlinks.append("\"{\\\"name\\\":\\\"").append(strCdNm).append("\\\"").append(", \\\"type\\\":\\\"WL\\\"")
                                .append(", \\\"url_mobile\\\":\\\"").append(strUrlMobile).append("\\\"").append(", \\\"url_pc\\\":\\\"")
                                .append(strUrlPc).append("\\\"").append(", \\\"extra\\\":\\\"").append(strCd).append("\\\"}\"");
                        }
                    }
                    //AL : scheme_adroid, scheme_ios, url_mobile, url_pc(3가지 중 2가지는 필수)
                    else if ("AL".equals(strLinksType)) {
                        log.info("Info : sendSystemMsg - strLinksType AL");
                    }
                    //BK : 해당 버튼 텍스트 전송
                    else if ("BUTTON".equals(strLinksType) || "BK".equals(strLinksType)) {
                        for (int i = 0; i < intArrSize; i++) {
                            String strCdNm = linksJsonArr.getJSONObject(i).getString("BTN_NM");
                            String strCd = linksJsonArr.getJSONObject(i).getString("EXTRA");

                            if (i != 0) {
                                strlinks.append(",");
                            }
                            strlinks.append("\"{\\\"name\\\":\\\"").append(strCdNm).append("\\\"").append(", \\\"type\\\":\\\"BK\\\"")
                                .append(", \\\"extra\\\":\\\"").append(strCd).append("\\\"}\"");
                        }
                    }
                    //MD : 해당 버튼 텍스트 + 메시지 본문 전송
                    else if ("MD".equals(strLinksType)) {
                        log.info("Info : sendSystemMsg - strLinksType MD");
                    } else {
                        log.error("Error : sendSystemMsg - message links type is fault.");
                        throw new TelewebAppException("message links type is fault.");
                    }

                    //시스템메시지(SYSMSG), 안내메시지(DLYMSG) 는 TWB_TALK_USER_DETAIL에 저장된다.
                    if ("SYSMSG".equals(strMsgCl) || "DLYMSG".equals(strMsgCl) || "DLYAUTOMSG".equals(strMsgCl)) {
                        //시스템 버튼 메시지 전송 (TWB_TALK_USER_READY_DETAIL 저장)
                        //sendSystemButtonMsgReadyDetail(userKey, senderKey, strMsgContent, strlinks.toString(), strSystemMsgId, callTypCd, strCustcoId);
                        messageJson.setString("CHT_CUTT_DTL_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID")));
                        sendSystemButtonMsgContactDetail(userKey, senderKey, messageJson, strlinks.toString(), strSystemMsgId, callTypCd,
                            strCustcoId);
                    }
                    //고객무응답메시지(NOAMSG) 는 TWB_TALK_CONTACT_DETAIL에 저장된다.
                    else if ("NOAMSG".equals(strMsgCl)) {
                        // 시스템 버튼 메시지 전송 (TWB_TALK_CONTACT_DETAIL 저장)
                        sendSystemButtonMsgContactDetail(userKey, senderKey, messageJson, strlinks.toString(), strSystemMsgId, callTypCd,
                            strCustcoId);
                    }
                    //2018.10.15 lsm 자동응답메시지(ATOMSG) 는 TWB_TALK_USER_DETAIL에 저장된다.
                    else if ("ATOMSG".equals(strMsgCl)) {
                        //시스템 버튼 메시지 전송 (TWB_TALK_USER_READY_DETAIL 저장)
                        sendAutoSystemButtonMsgReadyDetail(userKey, senderKey, strMsgContent, strSystemMsgId, callTypCd, strCustcoId);
                    }
                } else {
                    log.error("Error : sendSystemMsg - message type is fault.");
                    throw new TelewebAppException("message type is fault.");
                }
            } else if ("RCV".equals(strSndRcvCd)) {
                log.info("info : sendSystemMsg - It is Receive message.");
            } else {
                log.error("Error : sendSystemMsg - send/receive code is fault.");
                throw new TelewebAppException("send/receive code is fault.");
            }
        }
    }


    /**
     * 대기중 링크버튼 메시지를 전송한다.
     *
     * @param userKey
     * @param senderKey
     * @param message
     * @param strlinks
     */
    public void sendSystemButtonMsgReadyDetail(String userKey, String senderKey, String message, String strlinks, String strSystemMsgId,
        String callTypCd, String custcoId) throws TelewebAppException {
        TelewebJSON inJson = new TelewebJSON();
        String partnerId = paletteProperties.getPartnerId();
        JSONObject sendJson = null;
        String serial = Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID"));
        String filter2Msg = paletteFilterUtils.filter4(message);
        String sendString = "";
        if (callTypCd.equals("NTT")) {  // 메신저별로 추가
        	sendString = String.format(
                    "{\"user_key\":\"%s\",\"sndrKey\":\"%s\",\"CHT_CUTT_DTL_ID\":\"%s\",\"compositeList\": [{\"description\": \"%s\", \"buttonList\": [%s]}]}",
                    userKey, senderKey, serial, filter2Msg, strlinks);

                log.info("NTT / 대기중 버튼메시지 보내기" + sendString);
                sendJson = JSONObject.fromObject(sendString);
                sendJson.put("USER_ID", "system");
                
                log.info("NTT / 대기중 버튼메시지 보내기 파라미터 체크" + sendJson);
                JSONObject ret = transToKakao("composite", sendJson, callTypCd);
        } else {
	        sendString = String.format(
	            "{\"user_key\":\"%s\",\"sndrKey\":\"%s\",\"CHT_CUTT_DTL_ID\":\"%s\",\"message_type\":\"LI\",\"message\":\"%s\",\"links\":[%s]}",
	            userKey, senderKey, serial, filter2Msg, strlinks);
	        sendJson = JSONObject.fromObject(sendString);
	
	        JSONObject ret = transToKakao("write", sendJson, callTypCd);
        }
        if (message != null && !"".equals(message)) {
            inJson.setString("TALK_USER_KEY", userKey);
            inJson.setString("TALK_SERIAL_NUMBER", serial);
            inJson.setString("CHT_CUTT_DTL_ID", serial);
            inJson.setString("SNDR_KEY", senderKey);
            inJson.setString("CUSTCO_ID", custcoId);
            inJson.setString("TALK_API_CD", "/message");
            inJson.setString("TYPE", "system");
            inJson.setString("CONTENT", filter2Msg);
            inJson.setString("SYS_MSG_ID", strSystemMsgId);

            routingToAgentReadyDAO.insertTalkUserReadyDetail(inJson);
        }
    }


    /**
     * 대기중 링크버튼 메시지를 전송한다.
     *
     * @param userKey
     * @param senderKey
     * @param message
     * @param strlinks
     */
    public void sendSystemButtonLinkMsgReadyDetail(String userKey, String senderKey, String message, String strlinks, String strSystemMsgId,
        String callTypCd, String custcoId, JSONObject param) throws TelewebAppException {
    	log.info("대기중 링크버튼 메시지를 전송한다 == sendSystemButtonLinkMsgReadyDetail == param == "+param);
        TelewebJSON inJson = new TelewebJSON();
        String partnerId = paletteProperties.getPartnerId();
        JSONObject sendJson = null;
        String serial = Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID"));
        String filter2Msg = paletteFilterUtils.filter4(message);
        String image = param.getString("IMAGE");
        String fileGroupKey = param.getString("FILE_GROUP_KEY");
        String fileKey = param.getString("FILE_KEY");
        String sendString = "";

        if (!StringUtils.isEmpty(fileGroupKey) && !StringUtils.isEmpty(fileKey)) {
            if (callTypCd.equals("NTT")) {  // 메신저별로 추가
                sendString = String.format(
                    "{\"user_key\":\"%s\",\"sndrKey\":\"%s\",\"CHT_CUTT_DTL_ID\":\"%s\",\"compositeList\": [{\"description\": \"%s\", \"image\": {\"imageUrl\": \"%s\"}, \"buttonList\": [%s]}]}",
                    userKey, senderKey, serial, filter2Msg, environment.getProperty("stomp.allow.origin") + "/upload/" + image, strlinks);

                log.info("NTT / 대기중 링크메시지 보내기" + sendString);
                sendJson = JSONObject.fromObject(sendString);
                //이미지 처리
                sendJson.put("IMAGE_TYPE", param.getString("IMAGE_TYPE"));
                sendJson.put("FILE_GROUP_KEY", param.getString("FILE_GROUP_KEY"));
                sendJson.put("FILE_KEY", param.getString("FILE_KEY"));
                sendJson.put("USER_ID", "system");
                
                log.info("NTT / 대기중 링크메시지 보내기 파라미터 체크" + sendJson);
                JSONObject ret = transToKakao("composite", sendJson, callTypCd);

            } else {
                sendString = String.format(
                    "{\"user_key\":\"%s\",\"sndrKey\":\"%s\",\"CHT_CUTT_DTL_ID\":\"%s\",\"message_type\":\"LI\",\"message\":\"%s\",\"links\":[%s],\"image\":\"%s\"}",
                    userKey, senderKey, serial, filter2Msg, strlinks, image);
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

                JSONObject ret = transToKakao("links", sendJson, callTypCd);
            }
        } else {
        	if (callTypCd.equals("NTT")) {  // 메신저별로 추가
        		sendString = String.format(
                    "{\"user_key\":\"%s\",\"sndrKey\":\"%s\",\"CHT_CUTT_DTL_ID\":\"%s\",\"compositeList\": [{\"description\": \"%s\", \"buttonList\": [%s]}]}",
                    userKey, senderKey, serial, filter2Msg, strlinks);
	            sendJson = JSONObject.fromObject(sendString);
	            JSONObject ret = transToKakao("composite", sendJson, callTypCd);
        	} else {
	            sendString = String.format(
	                "{\"user_key\":\"%s\",\"sndrKey\":\"%s\",\"CHT_CUTT_DTL_ID\":\"%s\",\"message_type\":\"LI\",\"message\":\"%s\",\"links\":[%s]}",
	                userKey, senderKey, serial, filter2Msg, strlinks);
	            sendJson = JSONObject.fromObject(sendString);
	            JSONObject ret = transToKakao("write", sendJson, callTypCd);
        	}
        }

        if (message != null && !"".equals(message)) {
            inJson.setString("TALK_USER_KEY", userKey);
            inJson.setString("TALK_SERIAL_NUMBER", serial);
            inJson.setString("CHT_CUTT_DTL_ID", serial);
            inJson.setString("SNDR_KEY", senderKey);
            inJson.setString("CUSTCO_ID", custcoId);
            inJson.setString("TALK_API_CD", "/message");
            inJson.setString("TYPE", "system");
            inJson.setString("CONTENT", filter2Msg);
            inJson.setString("SYS_MSG_ID", strSystemMsgId);
            inJson.setString("IMAGE_URL", image);
            inJson.setString("IMAGE_TALK_PATH", image);

            routingToAgentReadyDAO.insertTalkUserReadyDetail(inJson);
        }
    }


    /**
     * 상담중 링크버튼 메시지를 전송한다.
     *
     * @param dataProcess
     * @param userKey
     * @param senderKey
     * @param message
     * @param strlinks
     */
    public void sendSystemButtonMsgContactDetail(String userKey, String senderKey, TelewebJSON messageJson, String strlinks,
        String strSystemMsgId, String callTypCd, String custcoId) throws TelewebAppException {
        String message = messageJson.getString("MSG_CN");
        //        String strMsgCl = messageJson.getString("MSG_CL");
        String partnerId = paletteProperties.getPartnerId();
        JSONObject sendJson = null;
        String serial = messageJson.getString("CHT_CUTT_DTL_ID");
        String filter2Msg = paletteFilterUtils.filter4(message);
        String sendString = String.format(
            "{\"user_key\":\"%s\",\"sndrKey\":\"%s\",\"CHT_CUTT_DTL_ID\":\"%s\",\"message_type\":\"LI\",\"message\":\"%s\",\"links\":[%s]}",
            userKey, senderKey, serial, filter2Msg, strlinks);
        sendJson = JSONObject.fromObject(sendString);

        TelewebJSON inJson = new TelewebJSON();
        inJson.setString("CHT_CUTT_DTL_ID", messageJson.getString("CHT_CUTT_DTL_ID"));
        inJson.setString("TALK_SERIAL_NUMBER", serial);
        inJson.setString("SNDR_KEY", senderKey);
        inJson.setString("CUSTCO_ID", custcoId);
        inJson.setString("TALK_API_CD", "/message");
        inJson.setString("CUSL_ID", "2");
        inJson.setString("USER_ID", "2");
        inJson.setString("CHT_USER_KEY", userKey);
        inJson.setString("MSG_TYPE_CD", messageJson.getString("MSG_TYPE_CD"));
        inJson.setString("TYPE", "system");
        inJson.setString("CONTENT", filter2Msg);
        inJson.setString("RCPTN_DSPTCH_CD", "SND");
        inJson.setString("SYS_MSG_ID", strSystemMsgId);

        JSONObject ret = transToKakao("write", sendJson, callTypCd);

        dataProcess.insertTalkContactDetail(inJson);
    }


    /**
     * 2018.10.15 lsm 대기중 링크버튼 자동 메시지를 전송한다. 카카오 자동응답메시지 사용 시
     *
     * @param dataProcess
     * @param userKey
     * @param senderKey
     * @param message
     */
    public void sendAutoSystemButtonMsgReadyDetail(String userKey, String senderKey, String message, String strSystemMsgId,
        String callTypCd, String custcoId) throws TelewebAppException {
        TelewebJSON inJson = new TelewebJSON();
        String partnerId = paletteProperties.getPartnerId();
        JSONObject sendJson = null;
        String serial = Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID"));
        String filter2Msg = paletteFilterUtils.filter4(message);
        String sendString = String.format(
            "{\"user_key\":\"%s\",\"sndrKey\":\"%s\",\"CHT_CUTT_DTL_ID\":\"%s\",\"message_type\":\"LI\",\"auto_answer\":\"%s\"}", userKey,
            senderKey, serial, filter2Msg);
        sendJson = JSONObject.fromObject(sendString);

        JSONObject ret = transToKakao("write", sendJson, callTypCd);

        if (message != null && !"".equals(message)) {
            inJson.setString("TALK_USER_KEY", userKey);
            inJson.setString("TALK_SERIAL_NUMBER", serial);
            inJson.setString("CHT_CUTT_DTL_ID", serial);
            inJson.setString("SNDR_KEY", senderKey);
            inJson.setString("CUSTCO_ID", custcoId);
            inJson.setString("TALK_API_CD", "/message");
            inJson.setString("TYPE", "system");
            inJson.setString("CONTENT", filter2Msg);
            inJson.setString("SYS_MSG_ID", strSystemMsgId);

            routingToAgentReadyDAO.insertTalkUserReadyDetail(inJson);
        }
    }


    /**
     * 2018.11.14 kmg 레벨형 문의유형 시스템 메시지를 전송한다.
     */
    public void sendInqryLevelTypeBtnMsg(String userKey, String senderKey, String message, JSONObject rcvJson,
        String callTypCd) throws TelewebAppException {
        TelewebJSON inqryTypeJson = new TelewebJSON();
        JSONArray inqryTypeJsonArr = null;
        String strMessage = message;
        int intInqryTpeArrSize = 0;
        int intButtonLimitCnt = 0;
        StringBuffer sbTempBtnLink = new StringBuffer();
        log.info("sendInqryLevelTypeBtnMsg===========" + userKey);
        log.info("sendInqryLevelTypeBtnMsg===========" + senderKey);
        log.info("sendInqryLevelTypeBtnMsg===========" + message);
        log.info("sendInqryLevelTypeBtnMsg===========" + rcvJson);
        log.info("sendInqryLevelTypeBtnMsg===========" + callTypCd);

        inqryTypeJson = dataProcess.selectInqryLevelType(rcvJson);

        inqryTypeJsonArr = inqryTypeJson.getDataObject("DATA");

        intInqryTpeArrSize = inqryTypeJsonArr.size();

        intButtonLimitCnt = ("KAKAO").equals(callTypCd) ? 5 : (("NTT").equals(callTypCd) ? 10 : 50);    // 카카오 최대 5개 , 네이버 톡톡 10개, ttalk 외에 제한 해제 sjh

        if (intInqryTpeArrSize == 0) {
            log.error("INQRY_TYPE count is 0. Confirm your settings.");
            return;
        }
        
        log.info("sendInqryLevelTypeBtnMsg===========선택한 문의유형 정보 == " + inqryTypeJsonArr);
        //선택한 문의유형
        String custcoId = inqryTypeJsonArr.getJSONObject(0).getString("CUSTCO_ID");
        String inqryDesc = inqryTypeJsonArr.getJSONObject(0).getString("INQRY_DESC");    //상위 문의유형 DESC
        String strParent = inqryTypeJsonArr.getJSONObject(0).getString("PARENT_CD");
        String image = inqryTypeJsonArr.getJSONObject(0).getString("IMAGE");
        String fileKey = inqryTypeJsonArr.getJSONObject(0).getString("FILE_KEY");
        String fileGroupKey = inqryTypeJsonArr.getJSONObject(0).getString("FILE_GROUP_KEY");

        // [파일o] 문의유형-시스템메시지전송: 채팅-이미지
        final RepositoryTaskTypeCd taskTypeCd = RepositoryTaskTypeCd.chat; //채팅
        final RepositoryPathTypeCd pathTypeCd = RepositoryPathTypeCd.images; //문의유형 이미지
        final FilePropertiesResponse fileProperties = fileRulePropertiesUtils.getProperties(taskTypeCd, pathTypeCd);
        log.debug("fileProperties>>>{}", fileProperties);
        log.info("fileProperties>>>{}", fileProperties);

        //하위 문의유형 (버튼)
        for (int i = 0; i < intInqryTpeArrSize; i++) {
            String strCdNm = inqryTypeJsonArr.getJSONObject(i).getString("CD_NM");
            String strCd = inqryTypeJsonArr.getJSONObject(i).getString("CD");
            String inqryType = inqryTypeJsonArr.getJSONObject(i).getString("INQRY_TYPE");
            String inqryDesc2 = inqryTypeJsonArr.getJSONObject(i).getString("INQRY_DESC2"); //해당 문의유형 DESC
            JSONObject param = new JSONObject();

            //문의유형 메시지 세팅
            if (inqryDesc != null && !inqryDesc.isEmpty())    //메시지가 있는 경우
            {
                strMessage = inqryDesc;
            } else    //메시지가 없는 경우, 디폴트 메시지 설정
            {
                strMessage = message;
            }

            //문의유형 버튼 세팅
            sbTempBtnLink.append((i % intButtonLimitCnt != 0) ? "," : ""); //버튼이 있다면 ',' 추가
            sbTempBtnLink.append(getFormatLinkButton(strCdNm,
                "CHATBOT".equals(inqryType) ? "ENDWITHBOT_" + inqryDesc2 : strCd, callTypCd));    //챗봇유형인 경우, 챗봇이벤트메시지 세팅 아니면 문의유형 코드 세팅

            //1. 마지막 문의유형일 때
            if (i == intInqryTpeArrSize - 1) {
                log.info("intInqryTpeArrSize>>마지막 문의유형일때>>{}");
                //1레벨 문의유형이 아니라면 '이전'버튼 전송
                if (!"*".equals(strParent) && ((i + 1) % intButtonLimitCnt != 0)) {
                    log.info("intInqryTpeArrSize>>1레벨 문의유형이 아니라면>>이전버튼 만들기>>");
                    sbTempBtnLink.append(",");
                    sbTempBtnLink.append(getFormatLinkButton("< 이전", "back_" + strParent,callTypCd));
                }

                if (i < intButtonLimitCnt && !StringUtils.isEmpty(fileGroupKey) && !StringUtils.isEmpty(fileKey)) {
                    log.info("sendSystemButtonLinkMsgReadyDetail>>링크메시지 만들기>>");
                    param.put("IMAGE", image);
                    param.put("IMAGE_TYPE", RepositoryPathTypeCd.images.toString());
                    param.put("FILE_KEY", fileKey);
                    param.put("FILE_GROUP_KEY", fileGroupKey);
                    sendSystemButtonLinkMsgReadyDetail(userKey, senderKey, strMessage, sbTempBtnLink.toString(), "", callTypCd, custcoId,
                        param);
                } else {
                    log.info("sendSystemButtonMsgReadyDetail>>버튼메시지 만들기>>");
                    sendSystemButtonMsgReadyDetail(userKey, senderKey, strMessage, sbTempBtnLink.toString(), "", callTypCd, custcoId);
                }
                sbTempBtnLink.setLength(0);
                sleep(500);    //문의유형 발송 후 SLEEP

                if (!"*".equals(strParent) && (i + 1) % intButtonLimitCnt == 0) {
                    sbTempBtnLink.append(getFormatLinkButton("< 이전", "back_" + strParent,callTypCd));
                    sendSystemButtonMsgReadyDetail(userKey, senderKey, strMessage, sbTempBtnLink.toString(), "", callTypCd, custcoId);
                    sbTempBtnLink.setLength(0);
                }

                return;
            }
            //2. 버튼 제한건수일 때
            else if ((i + 1) % intButtonLimitCnt == 0) {
                log.info("intButtonLimitCnt>>버튼제한건수일때>>{}");
                if (i < intButtonLimitCnt && image != null && !image.isEmpty()) {
                    log.info("sendSystemButtonLinkMsgReadyDetail>>링크메시지 만들기>>");
                    param.put("IMAGE", image);
                    param.put("FILE_KEY", fileKey);
                    param.put("FILE_GROUP_KEY", fileGroupKey);
                    sendSystemButtonLinkMsgReadyDetail(userKey, senderKey, strMessage, sbTempBtnLink.toString(), "", callTypCd, custcoId,
                        param);
                } else {
                    log.info("sendSystemButtonMsgReadyDetail>>버튼메시지 만들기>>");
                    sendSystemButtonMsgReadyDetail(userKey, senderKey, strMessage, sbTempBtnLink.toString(), "", callTypCd, custcoId);
                }
                sbTempBtnLink.setLength(0);
                sleep(500);    //문의유형 발송 후 SLEEP

                if (!"*".equals(strParent) && i == intInqryTpeArrSize - 1) {
                    sbTempBtnLink.append(getFormatLinkButton("< 이전", "back_" + strParent,callTypCd));
                    sendSystemButtonMsgReadyDetail(userKey, senderKey, strMessage, sbTempBtnLink.toString(), "", callTypCd, custcoId);
                    sbTempBtnLink.setLength(0);
                }
            }
        }
    }

    /**
     * 2018.11.14 kmg 레벨형 문의유형 시스템 메시지를 전송한다.
     */
    //	public void sendInqryLevelTypeBtnMsg(String userKey, String senderKey, String message, JSONObject rcvJson, String callTypCd) throws TelewebAppException
    //	{
    //		TelewebJSON inqryTypeJson = new TelewebJSON();
    //		String strlinks = "";
    //		String strMessage = message;
    //
    //		inqryTypeJson = dataProcess.selectInqryLevelType(rcvJson);
    //		JSONArray inqryTypeJsonArr = inqryTypeJson.getDataObject("DATA");
    //		int intInqryTpeArrSize = inqryTypeJsonArr.size();
    //		int intButtonLimitCnt = ("KAKAO").equals(callTypCd) ? 5:50;	// 카카오 최대 5개 , ttalk 외에 제한 해제 sjh
    //
    //		StringBuffer sbTempBtnLink = new StringBuffer();
    //		int intButtonCnt = 0;	//문의유형 버튼 ',' 초기화를 위한 변수
    //
    //		if (intInqryTpeArrSize == 0)
    //		{
    //			log.error("INQRY_TYPE count is 0. Confirm your settings.");
    //			return;
    //		}
    //
    //		for (int i = 0; i < intInqryTpeArrSize; i++)
    //		{
    //			String strCdNm = inqryTypeJsonArr.getJSONObject(i).getString("CD_NM");
    //		    String strCd = inqryTypeJsonArr.getJSONObject(i).getString("CD");
    //		    String strParent = inqryTypeJsonArr.getJSONObject(i).getString("PARENT_CD");
    //		    String inqryType = inqryTypeJsonArr.getJSONObject(i).getString("INQRY_TYPE");
    //		    String inqryDesc = inqryTypeJsonArr.getJSONObject(i).getString("INQRY_DESC");	//상위 문의유형 DESC
    //		    String inqryDesc2 = inqryTypeJsonArr.getJSONObject(i).getString("INQRY_DESC2"); //해당 문의유형 DESC
    //		    String custcoId = inqryTypeJsonArr.getJSONObject(0).getString("CUSTCO_ID");
    //		    String image = inqryTypeJsonArr.getJSONObject(i).getString("IMAGE");
    //		    JSONObject param = new JSONObject();
    //
    //		    //데이터 초기화
    //		    param.put("IMAGE", image);
    //		    strMessage = message;
    //
    //		    //이미지가 있는 문의유형이라면 이전 문의유형 우선 출력
    //		    if (!image.isEmpty() && sbTempBtnLink.length() > 0)
    //		    {
    //				strlinks = sbTempBtnLink.toString();
    //
    //		    	// 시스템 버튼 메시지 전송
    //		    	sendSystemButtonMsgReadyDetail(userKey, senderKey, strMessage, strlinks, "", callTypCd, custcoId);
    //		    	sbTempBtnLink.setLength(0);
    //
    //		    	sleep(500);	//문의유형 발송 후 SLEEP
    //		    	intButtonCnt = 0;
    //		    }
    //
    //		    sbTempBtnLink.append((intButtonCnt > 0) ? "," : ""); //버튼이 있다면 ',' 추가
    //
    //		    //(문의유형)버튼 생성 - 챗봇 연결일 때
    //		    if("CHATBOT".equals(inqryType))
    //		    {
    //		    	sbTempBtnLink.append(getFormatLinkButton(strCdNm, "ENDWITHBOT_"+inqryDesc2));
    //		        intButtonCnt++;
    //		    }
    //		    //(문의유형)버튼 생성 - 그 외
    //		    else
    //		    {
    //		    	sbTempBtnLink.append(getFormatLinkButton(strCdNm, strCd));
    //		        intButtonCnt++;
    //		    }
    //
    //		    //전체 문의유형 개수가 5개(버튼 제한건수) 일 때
    //		    if (intInqryTpeArrSize <= intButtonLimitCnt)
    //		    {
    //		    	//메시지가 있는 경우 세팅
    //				if (inqryDesc2 != null && !inqryDesc2.isEmpty())
    //				{
    //					strMessage = inqryDesc2;
    //				}
    //
    //		    	//마지막 문의유형 처리시
    //		        if (i == intInqryTpeArrSize - 1)
    //		    	{
    //		    		if (!"*".equals(strParent) && intInqryTpeArrSize < intButtonLimitCnt)
    //		            {
    //		            	sbTempBtnLink.append(",");
    //		            	sbTempBtnLink.append(getFormatLinkButton("< 이전", "back_" + strParent));
    //		            }
    //		            strlinks = sbTempBtnLink.toString();
    //
    //		            // 시스템 버튼 메시지 전송
    //		            sendSystemButtonLinkMsgReadyDetail(userKey, senderKey, strMessage, strlinks, "", callTypCd, custcoId, param);
    //		            sbTempBtnLink.setLength(0);
    //
    //		            //20191106 HYG 챗버블 :: 문의유형 5개 노출되는 경우 1레벨 문의유형에서 "< 이전" 버튼 보이는 현상 수정, LIY 20200301
    //		            if(!"*".equals(strParent) && intInqryTpeArrSize == intButtonLimitCnt)
    //		            {
    //		            	strMessage = message;	//초기메시지 세팅
    //		            	sbTempBtnLink.append(getFormatLinkButton("< 이전", "back_" + strParent));
    //		                strlinks = sbTempBtnLink.toString();
    //		                sendSystemButtonMsgReadyDetail(userKey, senderKey, strMessage, strlinks, "", callTypCd, custcoId);
    //		                sbTempBtnLink.setLength(0);
    //		            }
    //
    //		            return;
    //		    	}
    //		        else
    //		        {
    //		        	//이미지가 있는 문의유형 출력
    //		            if (!image.isEmpty() && sbTempBtnLink.length() > 0)
    //		            {
    //		            	strlinks = sbTempBtnLink.toString();
    //
    //		            	// 시스템 버튼 메시지 전송
    //		            	sendSystemButtonLinkMsgReadyDetail(userKey, senderKey, strMessage, strlinks, "", callTypCd, custcoId, param);
    //		            	sbTempBtnLink.setLength(0);
    //
    //		            	sleep(500);	//문의유형 발송 후 SLEEP
    //		            	intButtonCnt = 0;
    //		            }
    //		        }
    //		    }
    //		    //그 외 첫번째 메시지가 아니라면 (아무 조건도 속하지 않을 땐 그냥 스킵)
    //		    else if (i > 0)
    //		    {
    //		    	//5개(버튼제한수) 초과일 때
    //		        if (intInqryTpeArrSize > (intButtonLimitCnt-1)) 	//문의유형 개수가 버튼 제한 개수보다 크면서
    //		        {
    //		        	//메시지가 있는 경우 세팅
    //		    		if (inqryDesc2 != null && !inqryDesc2.isEmpty())
    //		    		{
    //		    			strMessage = inqryDesc2;
    //		    		}
    //
    //		        	//첫번째 전송
    //		            if (i % (intButtonLimitCnt-1) == 0			//제한개수에 속하면서
    //		            	&& i == (intButtonLimitCnt-1))			//제한개수에 속하는 1번째 일때.
    //		            {
    //		                strlinks = sbTempBtnLink.toString();
    //
    //		                // 시스템 버튼 메시지 전송
    //		                sendSystemButtonLinkMsgReadyDetail(userKey, senderKey, strMessage, strlinks, "", callTypCd, custcoId, param);
    //
    //		                sbTempBtnLink.setLength(0);
    //
    //		                sleep(500);	//문의유형 발송 후 SLEEP
    //		            }
    //		            //5개마다 전송 또는 최종 전송
    //		            else if (i % (intButtonLimitCnt-1) == 0 	//제한개수에 속하거나
    //		            		|| (i == intInqryTpeArrSize - 1))	//마지막 문의유형일 때
    //		            {
    //		            	//1레벨 문의유형이 아니라면 '이전'버튼 전송
    //		                if (!"*".equals(strParent))
    //		                {
    //		                	sbTempBtnLink.append(",");
    //		                	sbTempBtnLink.append(getFormatLinkButton("< 이전", "back_" + strParent));
    //		                }
    //
    //		                strlinks = sbTempBtnLink.toString();
    //		                strlinks = strlinks.substring(1, strlinks.length());
    //
    //		                // 시스템 버튼 메시지 전송
    //		                sendSystemButtonLinkMsgReadyDetail(userKey, senderKey, strMessage, strlinks, "", callTypCd, custcoId, param);
    //		                sbTempBtnLink.setLength(0);
    //
    //		                sleep(500);	//문의유형 발송 후 SLEEP
    //		            }
    //		            else
    //		            {
    //		            	//이미지가 있는 문의유형 출력
    //		                if (!image.isEmpty() && sbTempBtnLink.length() > 0)
    //		                {
    //		                	strlinks = sbTempBtnLink.toString();
    //
    //		                	// 시스템 버튼 메시지 전송
    //		                	sendSystemButtonLinkMsgReadyDetail(userKey, senderKey, strMessage, strlinks, "", callTypCd, custcoId, param);
    //		                	sbTempBtnLink.setLength(0);
    //
    //		                	sleep(500);	//문의유형 발송 후 SLEEP
    //		                	intButtonCnt = 0;
    //		                }
    //		            }
    //		        }
    //		    }
    //		}
    //	}


    /**
     * 카카오 챗봇 연결 (문의유형 선택 시)
     *
     * @param userKey
     * @param senderKey
     * @param bot_event
     */
    public void endWithBot(String userKey, String senderKey, String bot_event) throws TelewebAppException {
        String partnerId = paletteProperties.getPartnerId();
        JSONObject sendJson = null;
        String serial = Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID"));

        String sendString = String.format("{\"user_key\":\"%s\",\"sndrKey\":\"%s\",\"CHT_CUTT_DTL_ID\":\"%s\",\"bot_event\":\"%s\"}",
            userKey, senderKey, serial, bot_event);
        sendJson = JSONObject.fromObject(sendString);

        transToKakao("botevent", sendJson, "KAKAO");

    }


    /**
     * 문의유형 발송 후 SLEEP
     *
     * @param sleep 할 시간 (ms)
     */
    private void sleep(int sleepMilliSecond) {
        int sleeptime = sleepMilliSecond > 0 ? sleepMilliSecond : 500;

        try {
            Thread.sleep(sleeptime);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }


    /**
     * 버튼 포맷 - 링크버튼
     *
     * @param  buttonName 버튼명
     * @param  extra      버튼의 extra 값 - 보통 코드로 활용됨
     * @return strRetrun 링크버튼 문자열
     */
    private String getFormatLinkButton(String buttonName, String exttra, String chnClsfCd) {
        StringBuffer sbRetrun = new StringBuffer();
        if(chnClsfCd.equals("NTT")) {
        	sbRetrun.append("{\"type\":\"TEXT\",\"data\":{\"title\":\"").append(buttonName).append("\",\"code\":\"").append(exttra).append("\"}}").toString();
        } else {
        	sbRetrun.append("\"{\\\"name\\\":\\\"").append(buttonName).append("\\\", \\\"type\\\":\\\"BK\\\", \\\"extra\\\":\\\"")
        	.append(exttra).append("\\\"}\"").toString();
        }
        return sbRetrun.toString();
    }


    /**
     * 챗봇사용여부 체크
     *
     * @param userKey
     * @param senderKey
     * @param bot_event
     */
    public boolean chatbotYn(String senderKey) throws TelewebAppException {
        TelewebJSON sndrJson = new TelewebJSON();
        TelewebJSON resultJson = new TelewebJSON();
        sndrJson.setString("SNDR_KEY", senderKey);
        
        resultJson = mobjDao.select("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgMapper", "chatbotYn", sndrJson);
        
        return (resultJson.getString("CHBT_USE_YN").equals("Y")) ? true : false;
    }


    /**
     * 고객이 상담중인지 체크
     *
     * @param userKey
     * @param senderKey
     * @param bot_event
     */
    public boolean custChtIng(String userKey, String senderKey) throws TelewebAppException {
        TelewebJSON sndrJson = new TelewebJSON();
        TelewebJSON resultJson = new TelewebJSON();
        sndrJson.setString("CHT_USER_KEY", userKey);
        sndrJson.setString("SNDR_KEY", senderKey);
        
        resultJson = mobjDao.select("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgMapper", "custChtIng", sndrJson);
        
        return (resultJson.getString("CNT").equals("0")) ? false : true;
    }


    /**
     * 고객이 챗봇상담중인지 체크
     *
     * @param userKey
     * @param senderKey
     * @param bot_event
     */
    public boolean custChtbot(String userKey, String senderKey) throws TelewebAppException {
        TelewebJSON sndrJson = new TelewebJSON();
        TelewebJSON resultJson = new TelewebJSON();
        sndrJson.setString("CHT_USER_KEY", userKey);
        sndrJson.setString("SNDR_KEY", senderKey);
        
        resultJson = mobjDao.select("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgMapper", "custChtbot", sndrJson);
        
        return (resultJson.getString("CNT").equals("0")) ? false : true;
    }


    /**
     * 카카오톡 챗봇 이력 저장
     *
     * @param userKey
     * @param senderKey
     * @param bot_event
     */
    public void insertKakaoChatbotConents(JSONObject contents) throws TelewebAppException {
        TelewebJSON chbtJson = new TelewebJSON();
        TelewebJSON chbtCnt = new TelewebJSON();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeString = sdf.format(contents.get("timestamp"));
        String content = contents.toString();
        content = content.replace("\"", "\"\"");
        
        
        chbtJson.setString("CHBT_DATE", timeString);
        chbtJson.setString("CONTENTS", content);
        chbtJson.setString("CHT_USER_KEY", contents.getString("CHT_USER_KEY"));
        chbtCnt = mobjDao.select("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgMapper", "selectKakaoChatbotConents", chbtJson);
        if(chbtCnt.getString("CNT").equals("0")) {
        	mobjDao.insert("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgMapper", "insertKakaoChatbotConents", chbtJson);
        }
    }
    
    
    /**
     * 네이버톡톡 중복챗봇 체크
     *
     * @param userKey
     * @param senderKey
     * @param bot_event
     */
    public int chbtCnt(String userKey, String senderKey) throws TelewebAppException {
        TelewebJSON chbtJson = new TelewebJSON();
        TelewebJSON chbtCnt = new TelewebJSON();
        chbtJson.setString("CHT_USER_KEY", userKey);
        chbtJson.setString("SNDR_KEY", senderKey);
        chbtCnt = mobjDao.select("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgMapper", "chbtCnt", chbtJson);

    	int n = chbtCnt.getInt("CNT");
    	return n;
    }

    
    
    /**
     * 네이버톡톡 중복 챗봇 삭제
     *
     * @param userKey
     * @param senderKey
     * @param bot_event
     */
    public void deleteChbtDupl(String userKey, String senderKey) throws TelewebAppException {
        TelewebJSON chbtJson = new TelewebJSON();
        chbtJson.setString("CHT_USER_KEY", userKey);
        chbtJson.setString("SNDR_KEY", senderKey);
        mobjDao.delete("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgMapper", "deleteChbtRdyDupl", chbtJson);
        mobjDao.delete("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgMapper", "deleteChbtCuttDupl", chbtJson);
    }
    
}