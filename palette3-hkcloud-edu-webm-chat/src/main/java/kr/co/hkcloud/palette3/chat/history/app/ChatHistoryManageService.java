package kr.co.hkcloud.palette3.chat.history.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface ChatHistoryManageService
{
    TelewebJSON updateRtnTwbTalkContact(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnPageConsHist(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnContent(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnPageCnslMgmtByKaom_new(TelewebJSON jsonParams) throws TelewebAppException;
    boolean checkAuthProcess(TelewebJSON jsonParams) throws TelewebAppException;
}
