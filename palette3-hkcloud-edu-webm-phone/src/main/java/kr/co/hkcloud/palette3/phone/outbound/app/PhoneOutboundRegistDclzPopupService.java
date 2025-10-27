package kr.co.hkcloud.palette3.phone.outbound.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneOutboundRegistDclzPopupService
{

    /**
     * 상담원근태 조회
     * 
     * @param  mjsonParams
     * @return
     */
    TelewebJSON selectObndDilceList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 상담원근태 변경
     * 
     * @param  mjsonParams
     * @return
     */
    TelewebJSON mergeObndDilce(TelewebJSON jsonParams) throws TelewebAppException;

}
