package kr.co.hkcloud.palette3.phone.cmpgn.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "phoneCmpgnBadnDataManageController",
     description = "불량데이터관리 컨트롤러")
public class PhoneCmpgnBadnDataManageController
{
    /**
     * 불량데이터관리 페이지
     * 
     * @return
     */
    @ApiOperation(value = "불량데이터관리 페이지",
                  notes = "불량데이터관리 이동")
    @GetMapping("/phone/cmpgn/web/badn-data-manage")
    public String cmpgnBandDataManageMvmn() throws TelewebWebException
    {
        log.debug("cmpgnBandDataManageMvmn");
        return "phone/cmpgn/phone-cmpgn-badn-data-manage";
    }
}
