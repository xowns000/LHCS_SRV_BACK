package kr.co.hkcloud.palette3.phone.qa2.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneQAExecutManageService
{
    TelewebJSON selectRtnExecutList(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnQaDataRst(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON processRtnQaDataRst(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnQaManageYn(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnAdminYn(TelewebJSON jsonParams) throws TelewebAppException;
}
