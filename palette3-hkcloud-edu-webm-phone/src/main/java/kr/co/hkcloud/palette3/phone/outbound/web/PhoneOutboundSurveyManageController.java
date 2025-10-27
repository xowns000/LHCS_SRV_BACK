package kr.co.hkcloud.palette3.phone.outbound.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "phoneOutboundSurveyManageController",
     description = "설문지관리 컨트롤러")
public class PhoneOutboundSurveyManageController
{
    /**
     * 설문지관리 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "설문지관리 페이지",
                  notes = "설문지관리 이동한다")
    @GetMapping("/phone/outbound/web/survey-manage")
    public String movePhoneOutboundSurveyManageController() throws TelewebWebException
    {
        log.debug("movePhoneOutboundSurveyManageController");
        return "phone/outbound/phone-outbound-survey-manage";
    }


    /**
     * 설문지문항등록 팝업 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "설문지문항등록 팝업 페이지",
                  notes = "설문지문항등록 팝업 페이지로 이동한다")
    @GetMapping("/phone/outbound/web/survey-manage/popup")
    public String movePhoneOutboundSurveyManagePopup() throws TelewebWebException
    {
        log.debug("movePhoneOutboundSurveyManagePopup");
        return "phone/outbound/phone-outbound-survey-manage-popup";
    }


    /**
     * 설문지조회 팝업 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "설문지조회 팝업 페이지",
                  notes = "설문지조회 팝업 페이지로 이동한다")
    @GetMapping("/phone/outbound/web/survey-manage/qestnr-inqire-popup")
    public String movePhoneOutboundSurveyManageQestnrInqirePopup() throws TelewebWebException
    {
        log.debug("movePhoneOutboundSurveyManageQestnrInqirePopup");
        return "phone/outbound/phone-outbound-survey-manage-qestnr-inqire-popup";
    }
}
