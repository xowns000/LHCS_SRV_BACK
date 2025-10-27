package kr.co.hkcloud.palette3.phone.lm.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneLmGradingManageService
{
    TelewebJSON selectRtnLmGrading(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnLmGradingDetail(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateRtnLmGradingMultiChoice(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnLmUserAns(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON processRtnLmGrading(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnAdminYn(TelewebJSON jsonParams) throws TelewebAppException;
}
