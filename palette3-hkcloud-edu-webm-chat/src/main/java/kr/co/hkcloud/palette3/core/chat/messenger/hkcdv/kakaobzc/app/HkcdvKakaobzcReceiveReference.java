package kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.kakaobzc.app;


import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.kakaobzc.domain.KakaobzcOnReferenceEvent;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 레퍼런스 수신 서비스 인터페이스
 * 
 * @author Orange
 *
 */
public interface HkcdvKakaobzcReceiveReference
{
    void onReference(final KakaobzcOnReferenceEvent referenceEvent) throws TelewebAppException;
}
