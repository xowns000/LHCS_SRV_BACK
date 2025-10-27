package kr.co.hkcloud.palette3.statistics.phone.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface StatisticsPhoneOutboundService
{

    /**
     * 아웃바운드 목록(팝업) 조회한다.
     * 
     * @param  jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectObndListPop(TelewebJSON jsonParam) throws TelewebAppException;

    /**
     * 아웃바운드통계를 조회한다.
     * 
     * @param  jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectObndSttcList(TelewebJSON jsonParam) throws TelewebAppException;
}
