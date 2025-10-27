package kr.co.hkcloud.palette3.statistics.phone.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface StatisticsPhoneQAExecuteResultService
{
    TelewebJSON selectRtnLNm(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON selectRtnMNm(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON selectRtnSts(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON selectRtnHeaderLNm(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON selectRtnQaStsc(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON selectRtnQaTy(TelewebJSON jsonParam) throws TelewebAppException;
}
