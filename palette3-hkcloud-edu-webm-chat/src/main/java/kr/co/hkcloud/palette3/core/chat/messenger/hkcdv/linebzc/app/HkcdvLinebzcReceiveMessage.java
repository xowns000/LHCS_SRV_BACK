package kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.linebzc.app;


import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.linebzc.domain.LinebzcOnMessageEvent;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 메세지 수신 서비스 인터페이스
 * 
 * @author Orange
 *
 */
public interface HkcdvLinebzcReceiveMessage
{
    void onMessage(final LinebzcOnMessageEvent messageEvent) throws TelewebAppException;
}
