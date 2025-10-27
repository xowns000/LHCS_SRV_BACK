package kr.co.hkcloud.palette3.statistics.phone.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface StatisticsPhoneCnsltResultService
{
    /**
     * 상담결과통계를 조회한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectCnslResultSttc(TelewebJSON jsonParam) throws TelewebAppException;

}
