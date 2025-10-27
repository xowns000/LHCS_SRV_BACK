package kr.co.hkcloud.palette3.phone.script.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneScriptManageService
{
    /**
     * 스크립트 저장
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON insertRtnScriptMng(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 스크립트 업데이트
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON updateRtnScriptMng(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 스크립트정보 삭제
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON deleteRtnScriptMng(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON checkAttachFilCnt(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON requestRtnScriptMng(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON finRequestRtnScriptMng(TelewebJSON jsonParams) throws TelewebAppException;
}
