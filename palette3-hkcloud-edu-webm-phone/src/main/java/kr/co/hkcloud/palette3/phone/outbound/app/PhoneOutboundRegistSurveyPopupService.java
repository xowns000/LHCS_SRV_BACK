package kr.co.hkcloud.palette3.phone.outbound.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneOutboundRegistSurveyPopupService
{
//    /**
//     * 설문지 목록(팝업)을 조회한다.
//     * @param jsonParam
//     * @return
//     * @throws TelewebAppException
//     */
    TelewebJSON selectQuestListPop(TelewebJSON jsonParam) throws TelewebAppException;

    /**
     * 설문통계를 조회한다.
     * 
     * @param  jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectQuestSttcList(TelewebJSON jsonParam) throws TelewebAppException;
}
