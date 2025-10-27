package kr.co.hkcloud.palette3.chat.setting.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface ChatSettingSystemMessageManageService
{
    TelewebJSON selectSystemMsgType(TelewebJSON jsonParams) throws TelewebAppException;

    int deleteSystemMsg(TelewebJSON objRetParams, TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectSystemMsgList(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectSystemMsgLinkData(TelewebJSON jsonParams) throws TelewebAppException;

    int insertNewSystemMsg(TelewebJSON objRetParams, TelewebJSON jsonParams) throws TelewebAppException;

    void updateSystemMsgByText(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON modifySystemMsgByLink(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON insertSystemLinksMsg(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON updateSystemLinksMsg(TelewebJSON jsonParams) throws TelewebAppException;
    
    TelewebJSON commonSysMsgList(TelewebJSON jsonParams) throws TelewebAppException;
    

}
