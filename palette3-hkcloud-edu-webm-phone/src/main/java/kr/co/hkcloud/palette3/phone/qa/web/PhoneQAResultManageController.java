package kr.co.hkcloud.palette3.phone.qa.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "ResultManageController",
     description = "QA결과관리 컨트롤러")
public class PhoneQAResultManageController
{
    /**
     * QA결과관리 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "QA결과관리 페이지",
                  notes = "QA결과관리 페이지로 이동한다")
    @GetMapping("/phone/qa/web/result-manage")
    public String moveResultManage() throws TelewebWebException
    {
        log.debug("moveResultManage");
        return "phone/qa/phone-qa-result-manage";
    }
}
