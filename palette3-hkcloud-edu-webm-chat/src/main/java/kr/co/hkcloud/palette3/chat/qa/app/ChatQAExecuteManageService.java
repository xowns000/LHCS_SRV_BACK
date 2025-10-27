package kr.co.hkcloud.palette3.chat.qa.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface ChatQAExecuteManageService
{
    TelewebJSON selectRtnQA(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnQAIN(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnQABul(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnQaEvalCnt(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON processRtnCancel(TelewebJSON jsonParams) throws TelewebAppException;
}