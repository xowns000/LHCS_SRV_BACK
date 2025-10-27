package kr.co.hkcloud.palette3.core.chat.redis.app;


import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 
 * @author Orange
 *
 */
public interface TalkRedisChatPublisherService
{
    String getUserKey(String destination) throws TelewebAppException;
    void sendPubMessage(ChatMessage chatMessage) throws TelewebAppException;
}
