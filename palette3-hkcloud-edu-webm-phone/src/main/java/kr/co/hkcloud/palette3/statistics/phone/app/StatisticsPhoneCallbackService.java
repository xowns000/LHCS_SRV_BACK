package kr.co.hkcloud.palette3.statistics.phone.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface StatisticsPhoneCallbackService
{
    /**
     * 콜백통계를 조회한다.
     * 
     * @param  jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectClbkSttcList(TelewebJSON jsonParam) throws TelewebAppException;

}
