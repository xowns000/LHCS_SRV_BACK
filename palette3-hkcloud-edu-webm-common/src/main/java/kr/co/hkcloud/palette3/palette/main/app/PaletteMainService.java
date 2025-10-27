package kr.co.hkcloud.palette3.palette.main.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PaletteMainService
{
    TelewebJSON selectRtnMenu(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON getAllMenuListWithAuth(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON getRtNotice(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON getPopRtNotice(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectGetMenuBaseInfo(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectAlimtalkTmplat(TelewebJSON mjsonParams) throws TelewebAppException;
    TelewebJSON insertAlimtalkHist(TelewebJSON mjsonParams) throws TelewebAppException;
    TelewebJSON selectCstmrId(TelewebJSON mjsonParams) throws TelewebAppException;
    TelewebJSON selectSmsTmplat(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertEmailHist(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnScript(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnScriptDetail(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnChatScript(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnChatScriptDetail(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectCustcoId(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectAuth(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectCountCNSL(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON getUserAuth(TelewebJSON jsonParams) throws TelewebAppException;
    
    TelewebJSON setBFOmniAuthUser(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON setAFOmniAuthUser(TelewebJSON jsonParams) throws TelewebAppException;
}
