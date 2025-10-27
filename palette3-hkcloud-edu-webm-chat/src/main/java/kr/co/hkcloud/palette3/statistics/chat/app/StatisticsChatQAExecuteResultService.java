package kr.co.hkcloud.palette3.statistics.chat.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * SMS 서비스 인터페이스
 *
 */
public interface StatisticsChatQAExecuteResultService
{
    TelewebJSON selectRtn(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnDetails(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON processRtnEvaluationClose(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON processRtnCancel(TelewebJSON jsonParams) throws TelewebAppException;
}
