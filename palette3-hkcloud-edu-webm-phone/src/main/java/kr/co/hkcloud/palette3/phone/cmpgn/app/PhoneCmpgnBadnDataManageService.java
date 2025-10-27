package kr.co.hkcloud.palette3.phone.cmpgn.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneCmpgnBadnDataManageService
{

    /**
     * ASIS 목록 조회
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtn(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtnCmpaiNmSet(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtnFileNmSet(TelewebJSON mjsonParams) throws TelewebAppException;
}
