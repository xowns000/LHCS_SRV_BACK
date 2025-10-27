package kr.co.hkcloud.palette3.phone.dashboard.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneDashboardOutboundService
{

    /**
     * 아웃바운드모니터링 조회한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectObndCallList(TelewebJSON jsonParams) throws TelewebAppException;

}
