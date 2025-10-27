package kr.co.hkcloud.palette3.core.chat.stomp.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 
 * @author Orange
 *
 */
public interface TalkStompSendToAgentService
{
    boolean sendToAgent(ChatMessage chatMessage, TelewebJSON sendJson) throws TelewebAppException;
    boolean sendToReadyAgent(ChatMessage chatMessage, TelewebJSON sendJson) throws TelewebAppException;
    void sendConsultEndToAgent(ChatMessage chatMessage) throws TelewebAppException;
}
