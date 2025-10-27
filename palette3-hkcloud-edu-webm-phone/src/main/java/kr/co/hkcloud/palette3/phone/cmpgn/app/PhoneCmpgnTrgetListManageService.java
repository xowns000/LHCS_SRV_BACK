package kr.co.hkcloud.palette3.phone.cmpgn.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneCmpgnTrgetListManageService
{

    /**
     * 불량데이터 조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtn(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtn01(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtnComboBox(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtnComboBox01(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON updateRtn01(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON insertRtn01(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON updateRtn02(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON insertRtn02(TelewebJSON mjsonParams) throws TelewebAppException;
}
