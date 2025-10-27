package kr.co.hkcloud.palette3.phone.cmpgn.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneCmpgnTmplatManageService
{

    /**
     * 캠페인 조회
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtn(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 캠페인 템플릿 항목 조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtn2(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 캠페인 템플릿 파일 조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtn3(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 캠페인 템플릿 컬럼 저장
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON processRtn(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 캠페인 템플릿 저장
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON processRtnMain(TelewebJSON mjsonParams) throws TelewebAppException;
}
