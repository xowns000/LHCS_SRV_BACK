package kr.co.hkcloud.palette3.statistics.phone.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface StatisticsPhoneQA2Service
{
    TelewebJSON selectRtn(TelewebJSON jsonParam) throws TelewebAppException;
}
