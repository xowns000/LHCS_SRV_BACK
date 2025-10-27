package kr.co.hkcloud.palette3.infra.chat.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import kr.co.hkcloud.palette3.config.aspect.NoAspectAround;
import kr.co.hkcloud.palette3.config.aspect.NoBizLog;
import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.kakaobzc.domain.KakaobzcOnExpiredSessionEvent;
import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.kakaobzc.domain.KakaobzcOnMessageEvent;
import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.kakaobzc.domain.KakaobzcOnReferenceEvent;
import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.linebzc.domain.LinebzcOnExpiredSessionEvent;
import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.linebzc.domain.LinebzcOnMessageEvent;
import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.naverbzc.domain.NaverbzcOnExpiredSessionEvent;
import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.naverbzc.domain.NaverbzcOnMessageEvent;
import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.ttalkbzc.domain.TtalkOnExpiredSessionEvent;
import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.ttalkbzc.domain.TtalkOnMessageEvent;
import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.ttalkbzc.domain.TtalkOnReferenceEvent;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.infra.chat.util.TeletalkReceiverUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @author Orange
 *
 */
@Slf4j
@AllArgsConstructor
@RestController
public class TeletalkReceiverRestController {

    private final TeletalkReceiverUtils teletalkReceiverUtils;
    private final ApplicationEventPublisher eventPublisher;


    /**
     * 메시지 수신 - 카카오톡, 티톡
     */
    @PostMapping("/message")
    public void onMessage(@RequestBody JSONObject jsonBody, HttpServletRequest request) throws TelewebApiException {
        String logDevider = "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";
        String logPrefix = logDevider + ".onMessage" + "___" + jsonBody.get("user_key") + "___ ::: ";
        log.info(logPrefix + "메시지 수신");
        //유효성 체크 및 PALETTE 채널 정보 추가
        JSONObject messageJson = teletalkReceiverUtils.checkValidationMessage("message", request, jsonBody);
        //extra 파라미터 변환 - extra param to json ( jsonbody 가 string 으로 들어옴 객체로 변환 )
        JSONArray extraJson = teletalkReceiverUtils.convertExtraParamToJson(messageJson);

        String callTypeCd = messageJson.getString("call_typ_cd");
        if ("TTALK".equals(callTypeCd)) {
            eventPublisher.publishEvent(TtalkOnMessageEvent.builder().messageJson(messageJson).build());
            return;
        }

        eventPublisher.publishEvent(KakaobzcOnMessageEvent.builder().messageJson(messageJson).extraJson(extraJson).build());
    }

    
    /**
     * 메시지 수신 - 네이버톡톡
     */
    @RequestMapping(value = "/message/navertalktalk/{talkUuid}", method = RequestMethod.POST)
    public void onMessageById_navertalktalk(@RequestBody JSONObject jsonBody, HttpServletRequest request,
        @PathVariable String talkUuid) throws TelewebApiException {
        String logDevider = "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";
        String logPrefix = logDevider + ".onMessageById_navertalktalk" + "___ ::: ";
        log.info(logPrefix + "메시지 수신 - 네이버톡톡");
        jsonBody.put("call_typ_cd", "NTT"); // 네이버톡톡
        jsonBody.put("chnClsfCd", "NTT"); // 네이버톡톡
        jsonBody.put("sender_key", talkUuid);   // talkUuid를 sender key로 사용

        //유효성 체크
        JSONObject messageJson = teletalkReceiverUtils.checkValidationMessage("message", request, jsonBody);

        // 고객이 채팅종료하는 경우 종료처리 : 채팅방 나가는 경우(leave 이벤트), !종료 처리
        if (messageJson.getString("event").equals("leave") || (messageJson.containsKey("textContent") && messageJson.getJSONObject(
            "textContent").getString("text").equals("!종료"))) {
            eventPublisher.publishEvent(NaverbzcOnExpiredSessionEvent.builder().expiredSessionJson(messageJson).build());
            return;
        }

        //임시로 랜덤한 8자리숫자를 만들어 id로 사용
        //        String serialNumber = Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_ID"));
        String type = "";
        String msg = "";        // text 내용

        // 수신 메시지의 타입(text, image) 정의
        if (messageJson.containsKey("textContent")) {
        	//네이바톡톡 메시지 인입
            type = "text";
            if(messageJson.getJSONObject("textContent").getString("inputType").equals("product")) {
            	//상품문의버튼으로 들어온 채팅
            	type = "product";
            	msg = messageJson.getJSONObject("options").getJSONObject("product").toString();
            } else {
            	msg = messageJson.getJSONObject("textContent").getString("text");             // text 내용 
            }
        } else if (messageJson.containsKey("imageContent")) {
            type = "photo";
        } else {
        	//네이버톡톡에서 상담중일때 open이벤트를 받으면 "textContent" 이나 "imageContent"가 없음
        	if(messageJson.getString("event").equals("open")){
        		type = "text";
        		msg = "";
        	}
        }

        //        messageJson.put("serial_number", serialNumber);
        messageJson.put("content", msg);
        messageJson.put("type", type);
        log.info("messageJson >> 메시지 내용 정리" + messageJson);
        eventPublisher.publishEvent(NaverbzcOnMessageEvent.builder().messageJson(messageJson).build());
    }

    
    /**
     * 레퍼런스 수신 - 카카오톡, 티톡
     */
    @NoBizLog
    @NoAspectAround
    @PostMapping("/reference")
    public void onReference(@RequestBody JSONObject jsonBody, HttpServletRequest request) throws TelewebApiException {
        String logDevider = "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";
        String logPrefix = logDevider + ".onReference" + "___ ::: ";
        log.info(logPrefix + "레퍼런스 수신");
        //유효성 체크 및 PALETTE 채널 정보 추가
        JSONObject referenceJson = teletalkReceiverUtils.checkValidationMessage("reference", request, jsonBody);
        //extra 파라미터 변환 - extra param to json ( jsonbody 가 string 으로 들어옴 객체로 변환 )
        JSONArray extraJson = teletalkReceiverUtils.convertExtraParamToJson(referenceJson);
        
        String callTypeCd = referenceJson.getString("call_typ_cd");
        if ("TTALK".equals(callTypeCd)) {
            eventPublisher.publishEvent(TtalkOnReferenceEvent.builder().referenceJson(referenceJson).extraJson(extraJson).build());
            return;
        }
        eventPublisher.publishEvent(KakaobzcOnReferenceEvent.builder().referenceJson(referenceJson).extraJson(extraJson).build());
    }

    /**
     * 세션종료 수신 - 카카오톡, 티톡
     */
    @PostMapping("/expired_session")
    public void onExpiredSession(@RequestBody JSONObject jsonBody, HttpServletRequest request) throws TelewebApiException {
        String logDevider = "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";
        String logPrefix = logDevider + ".onExpiredSession" + "___ ::: ";
        log.info(logPrefix + "세션종료 수신 - 카카오톡, 티톡");
        //유효성 체크
        JSONObject expiredSessionJson = teletalkReceiverUtils.checkValidationMessage("expired_session", request, jsonBody);
        JSONArray extraJson = teletalkReceiverUtils.convertExtraParamToJson(expiredSessionJson);
        String callTypeCd = expiredSessionJson.getString("call_typ_cd");
        if ("TTALK".equals(callTypeCd)) {
            eventPublisher.publishEvent(TtalkOnExpiredSessionEvent.builder().expiredSessionJson(expiredSessionJson).build());
            return;
        } else if ("LINE".equals(callTypeCd)) {
            eventPublisher.publishEvent(LinebzcOnExpiredSessionEvent.builder().expiredSessionJson(expiredSessionJson).build());
            return;
        }
        eventPublisher.publishEvent(KakaobzcOnExpiredSessionEvent.builder().expiredSessionJson(expiredSessionJson).extraJson(extraJson)
            .build());
    }

    /**
     * 메시지 수신 - 라인
     */
    @RequestMapping(value = "/message/{uuid}", method = RequestMethod.POST)
    public void onMessageById(@RequestBody JSONObject jsonBody, HttpServletRequest request,
        @PathVariable String uuid) throws TelewebApiException {
        String logDevider = "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";
        String logPrefix = logDevider + ".onMessageById" + "___ ::: ";
        log.info(logPrefix + "메시지 수신 - 라인");
        
        JSONObject orgRcvBody = JSONObject.fromObject(jsonBody);

        jsonBody.put("call_typ_cd", "LINE"); // 라인상담톡
        jsonBody.put("sndrKey", uuid);   // uuid

        //유효성 체크
        JSONObject messageJson = teletalkReceiverUtils.checkValidationMessage("message", request, jsonBody);
        String callTypeCd = messageJson.getString("call_typ_cd");

        // 파라미터 검증 ( 실패시 exception 처리 ) 
        teletalkReceiverUtils.signatureValidation(request, messageJson, orgRcvBody);

        // n 개 event 로 넘어옴.
        JSONArray events = jsonBody.getJSONArray("events");
        for (int i = 0; i < events.size(); i++) {

            JSONObject obj = events.getJSONObject(i);
            String eventType = obj.getString("type");
            JSONObject messageObj = obj.getJSONObject("message");

            String serialNumber = obj.getString("replyToken");
            String type = messageObj.getString("type");
            String msgId = messageObj.getString("id");
            String msg = "";                // text 내용 

            if (type != null && type.equals("text")) {
                msg = messageObj.getString("text");             // text 내용 
            } else if (type != null && type.equals("image")) {
                type = "photo";                                 // photo 내용 
            }

            messageJson.put("replyToken", serialNumber);
            messageJson.put("messageText", msg);
            messageJson.put("messageType", type);
            messageJson.put("messageId", msgId);

            // 1;1 은 이벤트가 오지 않음. (1;n) 만해당됨.
            //          if(messageJson.getString("event_type").equals("leave") )            // 세션종료 처리  
            //          {
            //              eventPublisher.publishEvent(LinebzcOnExpiredSessionEvent.builder()
            //                        .expiredSessionJson(messageJson)
            //                        .build());
            //          }
            //          else 
            if (eventType != null && eventType.equals("message"))    // 메시지 처리   
            {
                eventPublisher.publishEvent(LinebzcOnMessageEvent.builder().messageJson(messageJson).build());
            } else {
                throw new TelewebApiException("정의되지 않은 이벤트 입니다.");
            }
        }
    }
}
