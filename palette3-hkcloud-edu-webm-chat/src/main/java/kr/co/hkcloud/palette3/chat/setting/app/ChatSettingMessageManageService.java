package kr.co.hkcloud.palette3.chat.setting.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface ChatSettingMessageManageService
{
    TelewebJSON selectRtnCnslProp(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnSystemMsgByMsgID(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnCustMsg(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnCustMsgDuplicatedCnt(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnCustcoMsgDuplicatedCnt(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertRtnCnslMsg(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteRtnCnslMsg(TelewebJSON jsonParams) throws TelewebAppException;
    //TelewebJSON updateRtnCnslProp(TelewebJSON objRetParams, TelewebJSON jsonParams) throws TelewebAppException;
}
