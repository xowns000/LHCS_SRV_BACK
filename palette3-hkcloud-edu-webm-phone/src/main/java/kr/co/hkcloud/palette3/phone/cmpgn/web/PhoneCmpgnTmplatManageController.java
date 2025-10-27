package kr.co.hkcloud.palette3.phone.cmpgn.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "phoneCmpgnTmplatManageController",
     description = "캠페인 템플릿관리컨트롤러")
public class PhoneCmpgnTmplatManageController
{
    /**
     * 캠페인템플릿관리 페이지
     * 
     * @return
     */
    @ApiOperation(value = "캠페인템플릿관리 페이지",
                  notes = "캠페인템플릿관리 이동")
    @GetMapping("/phone/cmpgn/web/tmplat-manage")
    public String cmpgnTmplatManageMvmn() throws TelewebWebException
    {
        log.debug("cmpgnTmplatManageMvmn");
        return "phone/cmpgn/phone-cmpgn-tmplat-manage";
    }
}
