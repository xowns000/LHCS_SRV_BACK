package kr.co.hkcloud.palette3.phone.outbound.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneOutboundSurveyManageService
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
     * 설문지항목 조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectRtnServayAnsList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 설문지 등록
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON insertRtnServay(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 설문지 수정
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON updateRtnServay(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 설문지 삭제
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON deleteRtnServay(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 설문지항목 등록
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON insertRtnServayData(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 설문지항목 수정
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON updateRtnServayData(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 설문지문항 삭제
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON deleteRtnServayData(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnServayNo(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON deleteRtnServayResult(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON deleteRtnServayData02(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON deleteRtnServayData01(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnServayQNo(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON insertRtnServayData01(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON insertRtnServayData02(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON updateRtnServayData01(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON updateRtnServayData02(TelewebJSON jsonParams) throws TelewebAppException;

}
