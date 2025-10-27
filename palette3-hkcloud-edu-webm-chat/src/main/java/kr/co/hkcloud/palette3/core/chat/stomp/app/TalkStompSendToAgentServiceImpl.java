package kr.co.hkcloud.palette3.core.chat.stomp.app;


import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;

import kr.co.hkcloud.palette3.chat.setting.util.ChatSettingBannedWordUtils;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.chat.redis.app.TalkRedisChatPublisherService;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage.ChatEvent;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage.ChatType;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage.MessageEvent;
import kr.co.hkcloud.palette3.core.util.PaletteFilterUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;


/**
 * 
 * @author Orange
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("talkStompChatSendService")
public class TalkStompSendToAgentServiceImpl implements TalkStompSendToAgentService
{
    private final PaletteFilterUtils            paletteFilterUtils;
    private final ChatSettingBannedWordUtils    chatSettingBannedWordUtils;
    private final TalkRedisChatPublisherService talkRedisChatPublisher;


    /**
     * 상담중인 상담사에게 메시지 전달
     */
    @Override
    public boolean sendToAgent(@NotNull ChatMessage chatMessage, @NotNull TelewebJSON sendJson) throws TelewebAppException
    {
        log.info("===>" + chatMessage.getUserKey() + ":sendToAgent chatMessage: {}" + chatMessage.toString());
        log.info("===>" + chatMessage.getUserKey() + ":sendToAgent sendJson: {}" + sendJson.toString());

        JSONArray jsonArray = chatSettingBannedWordUtils.parseContent_2(sendJson, chatMessage.getCustcoId());
        sendJson.setDataObject("DATA", jsonArray);
        String telewebJsonString = paletteFilterUtils.filter(sendJson.toString());
        chatMessage.setTelewebJsonString(telewebJsonString);

        // 상담사, 컨설턴트 메시지 전송
        try {
            //PUB:INOUT - 상담중 상담사에게 메시지 PUB
            // "/consult" <- onOpen : 시작 알림
            // "/consult" <- onClose : 종료 알림
            // "/expired_noresponse" <- chatEndAfterLastCustNoResponse : 고객무응답종료 알림
            // "/inOut" <- messageIncoming <- onMessage : 상담세션종료/상담전송
            // "/consult" <- messageIncoming <- onMessage : 상담세션종료/상담지원전송
            // "/message" <- isSendSocketToAgent <- onMessage  : 메시지 수신 전달
            // "/reference" <- isSendSocketToAgent <- onReference : 래퍼런스 수신 전달
            // "/expired_session" <- processExpiredSession <- onExpiredSession : 세션만료 수신 전달
            talkRedisChatPublisher.sendPubMessage(chatMessage);
            return true;
        }
        catch(Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }


    /**
     * 대기중인 상담사에게 메시지를 전달
     */
    @Override
    public boolean sendToReadyAgent(@NotNull ChatMessage chatMessage, @NotNull TelewebJSON sendJson) throws TelewebAppException
    {
        //DAO검색 메서드 호출
        JSONArray jsonArray = new JSONArray();
        jsonArray = chatSettingBannedWordUtils.parseContent_2(sendJson, chatMessage.getCustcoId());
        sendJson.setDataObject("DATA", jsonArray);

        String telewebJsonString = paletteFilterUtils.filter(sendJson.toString());
        chatMessage.setTelewebJsonString(telewebJsonString);

        // 대기중인 상담사에게 메시지 전송
        try {
            //PUB:READY - 상담대기 상담사에게 메시지 PUB
            // "/expired_session" <- processExpiredSession <- onExpiredSession : 세션만료 수신 전달
            talkRedisChatPublisher.sendPubMessage(chatMessage);
            return true;
        }
        catch(Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }


    /**
     * 3자 채팅 종료
     */
    @Override
    public void sendConsultEndToAgent(ChatMessage chatMessage) throws TelewebAppException
    {
        TelewebJSON responseJson = new TelewebJSON();
        responseJson.setHeader("cust_noresponse_message_yn", "Y");
        responseJson.setHeader("code", 0);
        responseJson.setHeader("ERROR_FLAG", false);
        responseJson.setHeader("ERROR_MSG", chatMessage.getMessage());
        responseJson.setString("user_key", chatMessage.getUserKey());
        responseJson.setHeader("from_who", "fromweb");
        responseJson.setHeader("called_api", "/expired_agent_session");
        log.trace("sendToAgentLocal ::: {}", responseJson.toString());

        //CONSULT:EXPIRED_AGENT_SESSION:SYSTEM [PUB] - 3자 상담사 세션만료
        log.trace(">>> CONSULT:EXPIRED_AGENT_SESSION:SYSTEM [PUB] - 3자 상담사세션만료");
        talkRedisChatPublisher.sendPubMessage(ChatMessage.builder().chatType(ChatType.CONSULT).chatEvent(ChatEvent.EXPIRED_AGENT_SESSION).messageEvent(MessageEvent.SYSTEM).userKey(chatMessage.getUserKey())
            .telewebJsonString(paletteFilterUtils.filter(responseJson.toString())).custcoId(chatMessage.getCustcoId()).build());

//        if (talkSocketVO.getWebsocketSession().isOpen())
//        {
//            talkSocketVO.getWebsocketSession().getBasicRemote().sendText(filterUtils.filter(responseJson.toString()));
//        }
//
//        //3자채팅 상담사 연결 종료
//        TalkWebsocketChatInoutRepository.getInstance().removeConsultConnection(this);
//        this.onClose();
    }
}
