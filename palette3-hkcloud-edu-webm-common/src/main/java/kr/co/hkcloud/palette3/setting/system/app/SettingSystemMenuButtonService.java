package kr.co.hkcloud.palette3.setting.system.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 버튼 서비스 인터페이스
 * 
 * @author R&D
 *
 */
public interface SettingSystemMenuButtonService
{
    TelewebJSON selectBtn(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertBtn(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateBtn(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteRtnBtn(TelewebJSON jsonParams) throws TelewebAppException;
}
