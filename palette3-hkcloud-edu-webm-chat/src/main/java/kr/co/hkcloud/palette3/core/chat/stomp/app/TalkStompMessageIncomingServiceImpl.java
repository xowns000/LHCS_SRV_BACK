package kr.co.hkcloud.palette3.core.chat.stomp.app;


import org.springframework.stereotype.Service;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage.ChatEvent;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage.ChatType;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage.MessageEvent;
import kr.co.hkcloud.palette3.core.chat.transfer.app.TransToKakaoService;
import kr.co.hkcloud.palette3.core.util.PaletteFilterUtils;
import kr.co.hkcloud.palette3.core.util.PaletteJsonUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


/**
 * 
 * @author Orange
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("talkStompMessageIncomingService")
public class TalkStompMessageIncomingServiceImpl implements TalkStompMessageIncomingService
{
    private final PaletteJsonUtils            paletteJsonUtils;
    private final PaletteFilterUtils          paletteFilterUtils;
    private final TransToKakaoService         transToKakaoService;
    private final TalkStompSendToAgentService stompSendToAgentService;
    private final TalkStompSendToKakaoService stompSendToKakaoService;


    /**
     * 고객/상담사 양쪽에게 메세지 전송
     *  - 상담사가 작성한 메시지 고객에게 전송(카톡, 네이버톡 등)
     *  - 상담사가 메세지 전송 시, 상담사의 메세지를 다시 소켓으로 발행하여 채팅 화면에 출력시킨다.
     */
    @Override
    public void messageIncoming(String userKey, String calledApi, String request, TelewebJSON objParams) throws TelewebAppException
    {
        log.info("objParams" + calledApi);
        log.info("objParams" + request);
        log.info("objParams" + objParams);
        log.info("objParams" + userKey);
        TelewebJSON responseJson = new TelewebJSON(request);
        JSONObject retJson = null;

        String endSession = responseJson.getHeaderString("in_out");
        String callTypCd = objParams.getString("chnClsfCd");
        String custcoId = objParams.getString("CUSTCO_ID");
        responseJson.setString("CUSTCO_ID", custcoId);
        responseJson.setHeader("from_who", "fromweb");
        responseJson.setHeader("called_api", calledApi);

        //READ 여부 업데이트를 위해 파라미터 추가 SJH 20190107
        responseJson.setString("TALK_SERIAL_NUMBER", objParams.getString("CHT_CUTT_DTL_ID"));
        responseJson.setString("CHT_CUTT_DTL_ID", objParams.getString("CHT_CUTT_DTL_ID"));
        responseJson.setString("user_key", userKey);

        //BLOB 이미지를 위해 파라미터 추가
        responseJson.setString("ORG_FILE_ID", objParams.getString("ORG_FILE_ID"));

        String senderKKey = "4f8bb80ee62869721a03c409791c0e430164daa6";
        objParams.setString("senderKKey", senderKKey);

        // 라인은 image 경로를 변경함.
        if(objParams.containsKey("IMAGE_URL") && objParams.getString("IMAGE_URL") != null && !objParams.getString("IMAGE_URL").equals("")) {
            responseJson.setString("IMAGE_URL", objParams.getString("IMAGE_URL"));
            responseJson.setString("image_url", objParams.getString("IMAGE_URL"));
        }

        //상담 세션 종료 요청
        if("endSession".equals(endSession)) {
            try {
                retJson = transToKakaoService.transToKakao("endtalk", responseJson.getDataObject().getJSONObject(0), callTypCd);
                responseJson.setHeader("code", retJson.getInt("code"));
                responseJson.setHeader("ERROR_FLAG", (retJson.getInt("code") == 0) ? false : true);
                responseJson.setHeader("ERROR_MSG", paletteJsonUtils.getJsonString(retJson, "message"));
            }
            catch(Exception e) {
                log.error(e.getMessage(), e);

                String message = String.format("상담 세션 종료 요청 실패.");
                responseJson.setHeader("code", -999);
                responseJson.setHeader("ERROR_MSG", message);
                responseJson.setHeader("ERROR_FLAG", true);
            }
            finally {
                //INOUT:EXPIRED_SESSION_AGENT:SYSTEM [PUB] - 상담사가 세션만료 요청 보냄
                log.trace(">>> INOUT:EXPIRED_SESSION_AGENT:SYSTEM [PUB] - 상담사가 세션만료 요청 보냄");
                stompSendToAgentService.sendToAgent(ChatMessage.builder().chatType(ChatType.INOUT).chatEvent(ChatEvent.EXPIRED_SESSION_AGENT).messageEvent(MessageEvent.SYSTEM).userId(objParams.getString("USER_ID"))
                    .userKey(userKey).custcoId(custcoId).build(), responseJson);
            }
        }
        else {
            try {
            	log.info("########$$$$$$$$$$$$$"+responseJson);
            	//종료버튼 클릭으로 메시지 보낼 때
            	if(responseJson.getString("type").equals("chatClose")) {
            		responseJson.setString("message", objParams.getString("CONTENT"));
            	}
                retJson = stompSendToKakaoService.sendToKakao(userKey, responseJson, callTypCd);
                responseJson.setHeader("code", retJson.getInt("code"));
                responseJson.setHeader("ERROR_FLAG", (retJson.getInt("code") == 0) ? false : true);
                responseJson.setHeader("ERROR_MSG", paletteJsonUtils.getJsonString(retJson, "message"));
            }
            catch(Exception e) {
                log.error(e.getMessage(), e);

                String message = String.format("상담 서버 오류.");
                responseJson.setHeader("code", -999);
                responseJson.setHeader("ERROR_FLAG", true);
                responseJson.setHeader("ERROR_MSG", message);
            }
            finally {
                //INOUT:IN_OUT:TALK [PUB] - 상담사 메시지 전달 (자신의 메시지)
                log.trace(">>> INOUT:IN_OUT:TALK [PUB] - 상담사 메시지 전달 (자신의 메시지)");
                stompSendToAgentService.sendToAgent(ChatMessage.builder().chatType(ChatType.INOUT).chatEvent(ChatEvent.IN_OUT).messageEvent(MessageEvent.TALK).userId(objParams.getString("USER_ID")).userKey(userKey)
                    .userName(objParams.getString("USER_ID")).userNickname(objParams.getString("USER_ID")).roomId(objParams.getString("TALK_CONTACT_ID")).message(objParams.getString("message"))
                    .senderKey(objParams.getString("senderKKey")).contactId(objParams.getString("TALK_CONTACT_ID")).sessionId(objParams.getString("USER_ID")).userCount(1).custcoId(custcoId).build(), responseJson);
            }
        }
    }


    /**
     * 데이터 처리시 데이터체크
     * 
     * @param  jsonParams 전송된 파라메터 데이터
     * @return            -506 : 데이터 길이 초과 오류
     */
    @Override
    public int chkValidationData(TelewebJSON jsonParams) throws TelewebAppException
    {
        int rtnCode = 0;

        String content = jsonParams.getString("CONTENT");
        //sendToKaKao 호출 전, utils.filter2 에서 큰따옴표에 문자열이 붙기 때문에 실제 상담사가 작성한 글자수에서 큰따옴표가 들어가는 경우
        //글자수를 1000자 이상으로 체크하여 카카오 상담톡에서 MessageLengthOverLimitException 를 발생시킴
        content = paletteFilterUtils.filter2(content);

        if(content.length() > 1000) {
            rtnCode = -506;
        }

        return rtnCode;
    }


    @Override
    public void sendToAgentMessageError(String userKey, int rtnCode, String userId, String custcoId) throws TelewebAppException
    {
        TelewebJSON sendJson = new TelewebJSON();
        String message = String.format("[전송불가] 메시지 길이 제한 오류 1000자 초과");
        sendJson.setHeader("code", rtnCode);
        sendJson.setHeader("ERROR_FLAG", true);
        sendJson.setHeader("ERROR_MSG", message);

        //INOUT:IN_OUT:TALK [PUB] - 상담사 메시지 전달 (자신의 메시지)
        log.trace(">>> INOUT:IN_OUT:TALK [PUB] - 상담사 메시지 전달 (자신의 메시지)");
        stompSendToAgentService
            .sendToAgent(ChatMessage.builder().chatType(ChatType.INOUT).chatEvent(ChatEvent.IN_OUT).messageEvent(MessageEvent.TALK).userId(userId).userKey(userKey).custcoId(custcoId).build(), sendJson);
    }

}
