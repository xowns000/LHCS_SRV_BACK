package kr.co.hkcloud.palette3.phone.dashboard.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneDashboardCallbackService
{

    /**
     * 콜백 모니터링을 조회한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectClbkMntrList(TelewebJSON jsonParams) throws TelewebAppException;

}
