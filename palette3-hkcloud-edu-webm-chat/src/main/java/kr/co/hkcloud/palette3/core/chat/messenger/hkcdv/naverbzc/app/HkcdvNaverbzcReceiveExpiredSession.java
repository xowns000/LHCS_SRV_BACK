package kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.naverbzc.app;


import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.naverbzc.domain.NaverbzcOnExpiredSessionEvent;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 세션만료 수신 서비스 인터페이스
 * 
 * @author 이동욱
 */
public interface HkcdvNaverbzcReceiveExpiredSession
{
    void onExpiredSession(final NaverbzcOnExpiredSessionEvent expiredSessionEvent) throws TelewebAppException;
}
