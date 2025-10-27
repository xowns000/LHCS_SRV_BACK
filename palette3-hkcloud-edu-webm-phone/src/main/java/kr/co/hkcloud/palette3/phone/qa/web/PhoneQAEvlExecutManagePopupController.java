package kr.co.hkcloud.palette3.phone.qa.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "EvlExecutManagePopupController",
     description = "QA실행 컨트롤러")
public class PhoneQAEvlExecutManagePopupController
{
    /**
     * QA실행 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "QA실행 페이지",
                  notes = "QA실행 페이지로 이동한다")
    @GetMapping("/phone/qa/web/evl-execut-manage/execut-evl-popup")
    public String moveEvlExecutManagePopup() throws TelewebWebException
    {
        log.debug("moveevl-execut-manage-popup");
        return "phone/qa/phone-qa-evl-execut-manage-execut-evl-popup";
    }
}
