package kr.co.hkcloud.palette3.setting.system.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/*
 * ASP고객채널 서비스
 */
public interface SettingSystemCorporateAccountManageService
{
    TelewebJSON selectRtnPageAspCustList(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnAspCustDetail(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnAspCustServiceInfo(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnAspCustComboData(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectAspCustInfo(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertRtnAspCust(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateRtnAspCustDetail(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteRtnAspCust(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectAspQueue(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertAspQueue(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectQueueList(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteQueue(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON botalkProcess(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectCustExist(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON updateCustcoId(TelewebJSON jsonParams) throws TelewebAppException;
}
