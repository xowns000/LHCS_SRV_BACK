package kr.co.hkcloud.palette3.phone.cmpgn.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "phoneCmpgnAsgnController",
     description = "캠페인할당 컨트롤러")
public class PhoneCmpgnAsgnController
{
    /**
     * 캠페인할당 페이지
     * 
     * @return
     */
    @ApiOperation(value = "캠페인할당 페이지",
                  notes = "캠페인할당 이동")
    @GetMapping("/phone/cmpgn/web/asgn")
    public String cmpgnBandAsgnMvmn() throws TelewebWebException
    {
        log.debug("cmpgnBandAsgnMvmn");
        return "phone/cmpgn/phone-cmpgn-asgn";
    }
}
