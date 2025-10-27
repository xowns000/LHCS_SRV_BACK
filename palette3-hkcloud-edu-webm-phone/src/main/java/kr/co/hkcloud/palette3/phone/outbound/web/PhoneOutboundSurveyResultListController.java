package kr.co.hkcloud.palette3.phone.outbound.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "phoneOutboundSurveyResultListController",
     description = "설문지결과조회 컨트롤러")
public class PhoneOutboundSurveyResultListController
{
    /**
     * 설문지결과조회 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "설문지결과조회 페이지",
                  notes = "설문지결과조회 이동한다")
    @GetMapping("/phone/outbound/web/survey-result-list")
    public String movePhoneOutboundSurveyResultList() throws TelewebWebException
    {
        log.debug("movePhoneOutboundSurveyResultList");
        return "phone/outbound/phone-outbound-survey-result-list";
    }

}
