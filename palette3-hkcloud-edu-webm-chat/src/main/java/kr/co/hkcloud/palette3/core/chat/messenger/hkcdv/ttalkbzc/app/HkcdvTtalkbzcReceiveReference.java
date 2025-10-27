package kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.ttalkbzc.app;


import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.ttalkbzc.domain.TtalkOnReferenceEvent;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 레퍼런스 수신 서비스 인터페이스
 * 
 * @author Orange
 *
 */
public interface HkcdvTtalkbzcReceiveReference
{
    void onReference(final TtalkOnReferenceEvent referenceEvent) throws TelewebAppException;
}
