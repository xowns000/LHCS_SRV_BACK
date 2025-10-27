package kr.co.hkcloud.palette3.setting.system.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 
 * @author Orange
 *
 */
public interface SettingSystemAuthorityByAgentManageService
{
	TelewebJSON selectRtnAuthrtById(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnAuthAlloc(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertAtrtGroupIdByUser(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateAtrtGroupIdByUser(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON processRtnAuthGroupUser(TelewebJSON mjsonParams) throws TelewebAppException;
    TelewebJSON processRtnAuthGroup(TelewebJSON mjsonParams) throws TelewebAppException;
    TelewebJSON deleteRtnAuthGroupUser(TelewebJSON mjsonParams) throws TelewebAppException;
}
