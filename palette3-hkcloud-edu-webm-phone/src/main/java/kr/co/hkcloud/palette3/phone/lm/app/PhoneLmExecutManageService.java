package kr.co.hkcloud.palette3.phone.lm.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneLmExecutManageService
{
    TelewebJSON selectRtnLmExecut(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnLmDetail(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnLmEvaPaper(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON processRtnExecut(TelewebJSON jsonParams) throws TelewebAppException;
}
