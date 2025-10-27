package kr.co.hkcloud.palette3.phone.script.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneScriptInqireService
{
    /**
     * 스크립트트리 조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectRtnScriptTree(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 스크립트리스트 조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectMainScriptList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 스크립트트리 리스트 조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectRtnScriptList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 스크립트상세조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectRtnScriptDetail(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 스크립트하위리스트조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectRtnLowScript(TelewebJSON jsonParams) throws TelewebAppException;

}
