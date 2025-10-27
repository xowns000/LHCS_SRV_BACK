package kr.co.hkcloud.palette3.phone.dashboard.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneDashboardOutboundPopupService
{
    /**
     * 아웃바운드 진행 현황
     * 
     * @param  mjsonParams
     * @return
     */
    TelewebJSON selectObndIngList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 아웃바운드 진행 현황 처리결과 변경
     * 
     * @param  mjsonParams
     * @return
     */
    TelewebJSON updateEotTyChange(TelewebJSON jsonParams) throws TelewebAppException;

}
