package kr.co.hkcloud.palette3.core.chat.redis.app;


import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 
 * @author Orange
 *
 */
public interface TalkRedisChatSubscriberService
{
    void onSubMessage(String publishedMessage) throws TelewebAppException;
}
