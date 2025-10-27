package kr.co.hkcloud.palette3.phone.reservationcall.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneReservationcallListService
{
    /**
     * 예약콜정보 조회
     * 
     * @param  mjsonParams
     * @return
     */
    TelewebJSON selectRtnResvCallInqInfo(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 예약콜 완료처리
     * 
     * @param  mjsonParams
     * @return
     */
    TelewebJSON updateRtnResvCallInqInfo(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 예약콜 삭제
     * 
     * @param  mjsonParams
     * @return
     */
    TelewebJSON deleteRtnResvCallInqInfo(TelewebJSON jsonParams) throws TelewebAppException;
}
