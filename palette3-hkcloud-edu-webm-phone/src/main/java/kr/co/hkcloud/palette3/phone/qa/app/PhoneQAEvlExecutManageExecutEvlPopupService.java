package kr.co.hkcloud.palette3.phone.qa.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * SMS 서비스 인터페이스
 *
 */
public interface PhoneQAEvlExecutManageExecutEvlPopupService
{
    TelewebJSON selectRtnEvSheet(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnDetails(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON processRtn(TelewebJSON jsonParams) throws TelewebAppException;
}
