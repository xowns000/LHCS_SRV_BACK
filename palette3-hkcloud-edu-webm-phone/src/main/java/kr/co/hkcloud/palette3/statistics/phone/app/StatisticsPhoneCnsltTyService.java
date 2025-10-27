package kr.co.hkcloud.palette3.statistics.phone.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface StatisticsPhoneCnsltTyService
{

    /**
     * 상담유형별(유형별)통계를 조회한다.
     * 
     * @param  jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectCnslTypeTypeSttcList(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON SELECT_TWB_BTCB01_MNG(TelewebJSON jsonParam) throws TelewebAppException;
}
