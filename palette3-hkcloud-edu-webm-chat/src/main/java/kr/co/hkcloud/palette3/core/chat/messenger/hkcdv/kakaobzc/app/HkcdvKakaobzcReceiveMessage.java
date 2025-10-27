package kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.kakaobzc.app;


import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.kakaobzc.domain.KakaobzcOnMessageEvent;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 메세지 수신 서비스 인터페이스
 * 
 * @author Orange
 *
 */
public interface HkcdvKakaobzcReceiveMessage
{
    void onMessage(final KakaobzcOnMessageEvent messageEvent) throws TelewebAppException;
}
