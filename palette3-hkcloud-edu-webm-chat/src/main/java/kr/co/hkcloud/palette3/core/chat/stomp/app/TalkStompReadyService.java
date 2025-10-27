package kr.co.hkcloud.palette3.core.chat.stomp.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * STOMP 서비스
 * 
 * @author jangh
 *
 */
public interface TalkStompReadyService
{
    TelewebJSON insertReady(TelewebJSON mjsonParams) throws TelewebAppException;
    void deleteReady(TelewebJSON inJson) throws TelewebAppException;
}
