package kr.co.hkcloud.palette3.chat.script.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface ChatScriptManageService
{
    TelewebJSON selectRtnScriptMngList(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertRtnScriptMng(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateRtnScriptMng(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteRtnScriptMng(TelewebJSON jsonParams) throws TelewebAppException;
    //chat-script-manage.js와 매핑되지 않은 함수들
    TelewebJSON selectRtnScriptMngByBaseScript(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnScriptMngByUserScript(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnScriptMngByCounseler(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnScriptMngByAdmin(TelewebJSON jsonParams) throws TelewebAppException;
}