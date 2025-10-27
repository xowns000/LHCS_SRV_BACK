package kr.co.hkcloud.palette3.core.chat.stomp.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 
 * @author Orange
 *
 */
public interface TalkStompInoutService
{
    void processTalkStackIng(TelewebJSON inJson) throws TelewebAppException;
    TelewebJSON mergeTalkStack(TelewebJSON mjsonParams) throws TelewebAppException;
    TelewebJSON deleteTalkIng(TelewebJSON mjsonParams) throws TelewebAppException;
    void processTalkIngLast(TelewebJSON inJson) throws TelewebAppException;
    TelewebJSON insertTalkIng(TelewebJSON mjsonParams) throws TelewebAppException;
    void processTalkStackReady(TelewebJSON inJson) throws TelewebAppException;
}
