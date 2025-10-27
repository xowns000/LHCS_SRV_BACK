package kr.co.hkcloud.palette3.chat.setting.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface ChatSettingManageService
{
    TelewebJSON selectRtnCnslProp(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnHistoryOfWorkTime(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnHistoryOfWorkTimeLast(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnHistoryOfWorkTimeBefore(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnYesterday(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertRtnHistoryOfWorkTime(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateRtnCnslProp(TelewebJSON objRetParams, TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateRtnHistoryOfWorkTime(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteRtnMsg(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnCnsltPermCnt(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateMaxAgentAndViewBaseScriptYnByUserId(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON updateChtLmtCnt(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateUserStat(TelewebJSON jsonParams) throws TelewebAppException;
}
