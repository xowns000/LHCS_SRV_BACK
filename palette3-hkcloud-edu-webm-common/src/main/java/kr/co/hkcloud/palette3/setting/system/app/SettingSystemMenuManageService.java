package kr.co.hkcloud.palette3.setting.system.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 메뉴관리 서비스 인터페이스
 *
 * @author R&D
 *
 */
public interface SettingSystemMenuManageService {

    TelewebJSON selectRtnMenuTree(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnMenuGroup(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectDupMenuID(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectMenuIdByMenuPath(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectTwbBas04(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectBoardPath(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectBoardPathDetail(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON processRtnMenu(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON changeMenuOrder(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON deleteRtnMenu(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON deleteRtnBtn(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectLkagLayoutList(TelewebJSON jsonParams) throws TelewebAppException;
    //    TelewebJSON selectRtnFolderInfo(TelewebJSON jsonParams) throws TelewebAppException;
    //    TelewebJSON selectRtnFileList(TelewebJSON jsonParams) throws TelewebAppException;
}
