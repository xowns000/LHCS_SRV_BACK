package kr.co.hkcloud.palette3.phone.cmpgn.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneCmpgnResultService
{

    /**
     * 목록 조회
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
    public TelewebJSON selectRtnPrj(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtnDetail(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtnStep3(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtnPm(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtnPm01(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtnHappyCallGroup(TelewebJSON mjsonParams) throws TelewebAppException;
}
