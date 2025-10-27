package kr.co.hkcloud.palette3.chat.qa.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface ChatQAExecuteManagePopupService
{
    TelewebJSON selectRtnQaEval(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertRtnQaRsltMst(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertRtnQaRslt(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateRtnQaRsltMst(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateRtnQaRslt(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectChkQaRsltMst(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectChkQaRslt(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteRtnQaRslt(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteRtnQaRsltMst(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnID(TelewebJSON jsonParams) throws TelewebAppException;
}