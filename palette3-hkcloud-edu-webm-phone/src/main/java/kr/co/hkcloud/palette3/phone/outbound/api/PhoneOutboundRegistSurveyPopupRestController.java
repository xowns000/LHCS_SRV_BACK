package kr.co.hkcloud.palette3.phone.outbound.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.outbound.app.PhoneOutboundRegistSurveyPopupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneOutboundRegistSurveyPopupRestController",
     description = "설문통계 컨트롤러")
public class PhoneOutboundRegistSurveyPopupRestController

{
    private final PhoneOutboundRegistSurveyPopupService phoneOutboundRegistSurveyPopupService;


    @ApiOperation(value = "설문 목록 조회",
                  notes = "설문 목록(팝업)을 조회한다.")
    @PostMapping("/phone-api/outbound/survey-manage/qestnr-inqire-popup/inqire")
    public Object selectQuestList(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {
        TelewebJSON objParam = new TelewebJSON(jsonParam);
        objParam = phoneOutboundRegistSurveyPopupService.selectQuestListPop(jsonParam);
        return objParam;
    }

}
