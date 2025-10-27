package kr.co.hkcloud.palette3.phone.history.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneHistoryChangeManageService
{

    /**
     * 상담이력변경관리 조회(공제)
     * 
     * @param  mjsonParams
     * @return
     */
    TelewebJSON selectRtnCnslHistChngMngDe(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 상담이력변경관리 승인
     * 
     * @param  mjsonParams
     * @return
     */
    TelewebJSON updateRtnCnslHistChngMng(TelewebJSON jsonParams) throws TelewebAppException;
}
