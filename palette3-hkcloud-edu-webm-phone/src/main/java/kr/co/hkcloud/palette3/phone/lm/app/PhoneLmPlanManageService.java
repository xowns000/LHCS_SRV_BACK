package kr.co.hkcloud.palette3.phone.lm.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneLmPlanManageService
{
    TelewebJSON selectRtnLm(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertRtnLm(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnEvlPaper(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteRtnLm(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateRtnLm(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnLmUser(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnLmData(TelewebJSON jsonParams) throws TelewebAppException;
}
