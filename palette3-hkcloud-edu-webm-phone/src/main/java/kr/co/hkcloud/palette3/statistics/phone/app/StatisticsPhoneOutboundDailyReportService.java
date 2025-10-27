package kr.co.hkcloud.palette3.statistics.phone.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface StatisticsPhoneOutboundDailyReportService
{

    /**
     * 아웃바운드일일통계(상세)를 조회한다.
     * 
     * @param  jsonParam
     * @return
     */
    TelewebJSON selectObndAdaySttcRprtList(TelewebJSON jsonParam) throws TelewebAppException;

    /**
     * 아웃바운드일일통계(건수)를 조회한다.
     * 
     * @param  jsonParam
     * @return
     */
    TelewebJSON selectObndAdaySttcRprtCntList(TelewebJSON jsonParam) throws TelewebAppException;

}
