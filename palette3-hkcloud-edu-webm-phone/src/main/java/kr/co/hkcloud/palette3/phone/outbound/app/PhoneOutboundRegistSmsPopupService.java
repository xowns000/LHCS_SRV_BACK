package kr.co.hkcloud.palette3.phone.outbound.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneOutboundRegistSmsPopupService
{
    /**
     * 아웃바운드 공제단말 SMS전송 고객 조회
     * 
     * @param  mjsonParams
     * @return
     */
    TelewebJSON selectObndSmsTotSend(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 아웃바운드 업무지원 SMS전송 고객 조회
     * 
     * @param  mjsonParams
     * @return
     */
    TelewebJSON selectObndSmsTotSendE(TelewebJSON jsonParams) throws TelewebAppException;

}
