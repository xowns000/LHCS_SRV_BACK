package kr.co.hkcloud.palette3.infra.chat.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/*
 * 챗봇 대화이력 서비스
 */
public interface TeletalkChatbotContactHistoryService
{
    TelewebJSON insertChatbotContact(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertChatbotContactDetail(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectCustInfo(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertCustInfo(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON checkLastChatbotBusiness(TelewebJSON jsonParams) throws TelewebAppException;
}
