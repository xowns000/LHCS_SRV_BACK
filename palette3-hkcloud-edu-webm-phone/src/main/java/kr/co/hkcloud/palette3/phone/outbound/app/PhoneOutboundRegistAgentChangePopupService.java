package kr.co.hkcloud.palette3.phone.outbound.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneOutboundRegistAgentChangePopupService
{
    /**
     * 배분변경 상담원 조회
     * 
     * @param  mjsonParams
     * @return
     */
    TelewebJSON selectObndDivChangeList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 배분 상담원 변경
     * 
     * @param  mjsonParams
     * @return
     */
    TelewebJSON updateDivChange(TelewebJSON jsonParams) throws TelewebAppException;

}
