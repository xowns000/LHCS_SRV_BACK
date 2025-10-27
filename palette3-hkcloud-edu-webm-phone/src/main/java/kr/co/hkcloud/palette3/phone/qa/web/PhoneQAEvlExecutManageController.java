package kr.co.hkcloud.palette3.phone.qa.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "EvlExecutManageController",
     description = "QA평가실행관리 컨트롤러")
public class PhoneQAEvlExecutManageController
{
    /**
     * QA평가실행관리 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "QA평가실행관리 페이지",
                  notes = "QA평가실행관리 페이지로 이동한다")
    @GetMapping("/phone/qa/web/evl-execut-manage")
    public String moveEvlExecutManage() throws TelewebWebException
    {
        log.debug("moveEvlExecutManage");
        return "phone/qa/phone-qa-evl-execut-manage";
    }
}
