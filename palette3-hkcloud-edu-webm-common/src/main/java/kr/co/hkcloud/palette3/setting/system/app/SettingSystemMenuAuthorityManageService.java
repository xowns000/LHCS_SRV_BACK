package kr.co.hkcloud.palette3.setting.system.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 메뉴권한관리 서비스 인터페이스
 * 
 * @author R&D
 *
 */
public interface SettingSystemMenuAuthorityManageService
{
    TelewebJSON selectRtnAuthGroup(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnAtrtGroupCd(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON processRtnAuth(TelewebJSON mjsonParams) throws TelewebAppException;
    TelewebJSON deleteRtnAuthGroup(TelewebJSON mjsonParams) throws TelewebAppException;
    TelewebJSON deleteRtnAuthGroupUser(TelewebJSON mjsonParams) throws TelewebAppException;
    TelewebJSON selectRtnchkEnableAuth(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnNoAlloc(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnAlloc(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnNoAllocBtn(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnAllocBtn(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnMenuAuthTree(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectDupAuthGroup(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnAuthGroupMng(TelewebJSON jsonParams) throws TelewebAppException;
}
