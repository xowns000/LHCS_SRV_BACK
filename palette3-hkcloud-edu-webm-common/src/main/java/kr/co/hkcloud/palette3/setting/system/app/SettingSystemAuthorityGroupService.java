package kr.co.hkcloud.palette3.setting.system.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 권한그룹 서비스 인터페이스
 * 
 * @author R&D
 *
 */
public interface SettingSystemAuthorityGroupService
{
    TelewebJSON deleteRtnAuthGroup(TelewebJSON mjsonParams) throws TelewebAppException;
    TelewebJSON selectTwbBas05(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertTwbBas05(TelewebJSON mjsonParams) throws TelewebAppException;
    TelewebJSON updateTwbBas05(TelewebJSON mjsonParams) throws TelewebAppException;
}
