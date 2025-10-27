package kr.co.hkcloud.palette3.chat.qa.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface ChatQATargetManageService
{
    TelewebJSON selectRtnMainList(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnPreExtractedCount(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON processRtnExtractTarget(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertRtnExtractTarget(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateRtnExtractClose(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteRtnExtractReset(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateRtnEvaluationClose(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteRtnExtractRemove(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectHaveExtractClose(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectHaveEvaluatedYN(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectNotEvaluatedCount(TelewebJSON jsonParams) throws TelewebAppException;
}