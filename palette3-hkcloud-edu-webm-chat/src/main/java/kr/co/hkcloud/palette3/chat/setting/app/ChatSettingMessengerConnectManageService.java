package kr.co.hkcloud.palette3.chat.setting.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface ChatSettingMessengerConnectManageService
{
    TelewebJSON selectRtnPageAspCustChannelList(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnPageAspCustChannelDetail(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnKaKaoConnStatus(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertRtnAspCustChannel(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateRtnAspCustChannelDetail(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteRtnAspCustChannelItem(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnAspCustChannelInUse(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnAspBizChannelComboData(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON chnStatChange(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON chnChbtStatChange(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON pstChnSet(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectBbsParamStngList(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateBbsParamStng(TelewebJSON jsonParams) throws TelewebAppException;
}
