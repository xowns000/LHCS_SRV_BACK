package kr.co.hkcloud.palette3.setting.agent.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface SettingAgentBelongItemManageService
{
    TelewebJSON selectAttrView(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON insertAttr(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON updateAttr(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON deleteAttr(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectChkDeptCode(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectNewDeptData(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON insertNewDeptInfo(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnDeptTreeView(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnNodeDetail(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON updateRtnDeptDisplay(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnDeptListPage(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnUserDeptCdInfo1(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnUserDeptCdInfo2(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnUserDeptCdInfo3(TelewebJSON jsonParams) throws TelewebAppException;
}
