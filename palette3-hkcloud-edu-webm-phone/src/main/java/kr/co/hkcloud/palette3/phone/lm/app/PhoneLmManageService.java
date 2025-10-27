package kr.co.hkcloud.palette3.phone.lm.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneLmManageService
{
    TelewebJSON selectRtnLmsEva(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertRtnLmEva(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateRtnLmEva(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertRtnCopyLmEva(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteRtnLmEva(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnLmQs(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertRtnLmQs(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertRtnCopyLmQs(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteRtnLmQs(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnLmVe(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnLmEvaRst(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertRtnLmEvaRst(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnLmPreView(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnLmVePreView(TelewebJSON jsonParams) throws TelewebAppException;
}
