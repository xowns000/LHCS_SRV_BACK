package kr.co.hkcloud.palette3.setting.system.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface SettingSystemBatchManageService
{
    TelewebJSON INSERT_PLT_BAT(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON UPDATE_PLT_BAT(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON DELETE_PLT_BAT(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON SELECT_PLT_BAT(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON SELECT_PAGE_PLT_BAT(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON SELECT_PLT_BAT_MNG(TelewebJSON jsonParams) throws TelewebAppException;
}
