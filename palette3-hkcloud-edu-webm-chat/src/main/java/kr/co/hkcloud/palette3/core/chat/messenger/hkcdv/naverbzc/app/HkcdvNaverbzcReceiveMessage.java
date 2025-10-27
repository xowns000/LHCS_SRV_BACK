package kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.naverbzc.app;


import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.naverbzc.domain.NaverbzcOnMessageEvent;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 메세지 수신 서비스 인터페이스
 * 
 * @author 이동욱
 *
 */
public interface HkcdvNaverbzcReceiveMessage
{
    void onMessage(final NaverbzcOnMessageEvent messageEvent) throws TelewebAppException;
}
