package kr.co.hkcloud.palette3.chat.qa.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface ChatQAEvaluationPaperManageService
{
    TelewebJSON selectRtnEvSheet(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertRtnQASHEET(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertRtnCopySheet(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteRtnQASheet(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateRtnQASheet(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectChkQASeet(TelewebJSON jsonParams) throws TelewebAppException;
}