package kr.co.hkcloud.palette3.statistics.phone.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface StatisticsPhoneLMService
{
    TelewebJSON selectRtnLm(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON selectRtnLmDetail(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON selectRtnLmDataRst(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON selectRtnLmVe(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON selectRtnLmQs(TelewebJSON jsonParam) throws TelewebAppException;
}
