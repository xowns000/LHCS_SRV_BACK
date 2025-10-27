package kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.linebzc.app;


import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.linebzc.domain.LinebzcOnExpiredSessionEvent;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 세션만료 수신 서비스 인터페이스
 * 
 * @author Orange
 */
public interface HkcdvLinebzcReceiveExpiredSession
{
    void onExpiredSession(final LinebzcOnExpiredSessionEvent expiredSessionEvent) throws TelewebAppException;
}
