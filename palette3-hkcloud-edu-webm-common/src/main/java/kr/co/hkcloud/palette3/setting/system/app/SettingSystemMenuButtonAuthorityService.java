package kr.co.hkcloud.palette3.setting.system.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 메뉴버튼권한 서비스 인터페이스
 * 
 * @author R&D
 *
 */
public interface SettingSystemMenuButtonAuthorityService
{
    TelewebJSON selectRtnMenuBtnRole(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON processRtnBtnAuth(TelewebJSON mjsonParams) throws TelewebAppException;
    TelewebJSON deleteRtnBtnAuth(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteRtnBtnAuthExceptDataToRegister(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteRtnBtnAuthByBtnID(TelewebJSON jsonParams) throws TelewebAppException;
}
