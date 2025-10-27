package kr.co.hkcloud.palette3.statistics.phone.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface StatisticsPhoneCnsltTypeByTimeService
{
    /**
     * 상담유형별(시간대별)통계를 조회한다.
     * 
     * @param  jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectCnslTypeTimeSttcList(TelewebJSON jsonParam) throws TelewebAppException;


    /**
     * 상담유형별(날짜별)통계를 조회한다.
     * 
     * @param  jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectCnslTypeDaySttcList(TelewebJSON jsonParam) throws TelewebAppException;
}
