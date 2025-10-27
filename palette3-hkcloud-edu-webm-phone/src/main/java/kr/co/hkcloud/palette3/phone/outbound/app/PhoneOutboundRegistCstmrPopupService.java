package kr.co.hkcloud.palette3.phone.outbound.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneOutboundRegistCstmrPopupService
{
    /**
     * 아웃바운드 조회
     * 
     * @param  mjsonParams
     * @return
     */
    TelewebJSON selectObndCam(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 아웃바운드 단건 등록 팝업
     * 
     * @param  mjsonParams
     * @return
     */
    TelewebJSON insertObndSnglReg(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 아웃바운드 고객 단건 등록 시 중복 체크
     * 
     * @param  mjsonParams
     * @return
     */
    TelewebJSON selectObndSnglRegDubChk(TelewebJSON jsonParams) throws TelewebAppException;

}
