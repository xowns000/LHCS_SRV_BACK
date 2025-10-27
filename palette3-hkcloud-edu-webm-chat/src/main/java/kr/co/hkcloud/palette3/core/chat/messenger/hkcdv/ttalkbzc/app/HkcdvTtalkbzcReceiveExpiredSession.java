package kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.ttalkbzc.app;


import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.ttalkbzc.domain.TtalkOnExpiredSessionEvent;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 세션만료 수신 서비스 인터페이스
 * 
 * @author Orange
 */
public interface HkcdvTtalkbzcReceiveExpiredSession
{
    void onExpiredSession(final TtalkOnExpiredSessionEvent expiredSessionEvent) throws TelewebAppException;
}
