package kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.ttalkbzc.app;


import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.ttalkbzc.domain.TtalkOnMessageEvent;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 메세지 수신 서비스 인터페이스
 * 
 * @author Orange
 *
 */
public interface HkcdvTtalkbzcReceiveMessage
{
    void onMessage(final TtalkOnMessageEvent messageEvent) throws TelewebAppException;
}
