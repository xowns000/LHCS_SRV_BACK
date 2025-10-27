package kr.co.hkcloud.palette3.phone.script.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneScriptChangeManageService
{
    /**
     * 스크립트변경이력 조회한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectScrtChngList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 스크립트변경이력 상세조회한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectScrtChngDetail(TelewebJSON jsonParams) throws TelewebAppException;

}
