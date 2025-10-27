package kr.co.hkcloud.palette3.phone.outbound.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneOutboundSurveyResultListService
{
    /**
     * 설문지 조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectRtnServayList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 설문지문항 조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectRtnServayQList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 설문지결과 조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectRtnResult(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnResultQAnsTotCnt(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnResult_multi(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnResult_short(TelewebJSON jsonParams) throws TelewebAppException;

}
