package kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.kakaobzc.app;


import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.kakaobzc.domain.KakaobzcOnExpiredSessionEvent;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 세션만료 수신 서비스 인터페이스
 * 
 * @author Orange
 */
public interface HkcdvKakaobzcReceiveExpiredSession
{
    void onExpiredSession(final KakaobzcOnExpiredSessionEvent expiredSessionEvent) throws TelewebAppException;
}
