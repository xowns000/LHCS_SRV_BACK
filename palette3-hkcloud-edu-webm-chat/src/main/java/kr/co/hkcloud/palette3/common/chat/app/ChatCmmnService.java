package kr.co.hkcloud.palette3.common.chat.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 채팅공통 서비스 인터페이스
 * 
 * @author Orange
 *
 */
public interface ChatCmmnService
{
    TelewebJSON selectRtnPageAgentDeliver(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnCnslProp(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectSTJobTime(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectENDJobTime(TelewebJSON jsonParams) throws TelewebAppException;
}
