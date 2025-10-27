package kr.co.hkcloud.palette3.phone.cmpgn.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "phoneCmpgnManageController",
     description = "캠페인관리 컨트롤러")
public class PhoneCmpgnManageController
{
    /**
     * 캠페인관리 페이지
     * 
     * @return
     */
    @ApiOperation(value = "캠페인관리 페이지",
                  notes = "캠페인관리 이동")
    @GetMapping("/phone/cmpgn/web/cmpgn-manage")
    public String cmpgnManageMvmn() throws TelewebWebException
    {
        log.debug("cmpgnManageMvmn");
        return "phone/cmpgn/phone-cmpgn-manage";
    }
}
