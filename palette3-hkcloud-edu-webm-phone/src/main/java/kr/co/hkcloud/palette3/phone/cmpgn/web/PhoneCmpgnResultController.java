package kr.co.hkcloud.palette3.phone.cmpgn.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "phoneCmpgnResultController",
     description = "불량데이터관리 컨트롤러")
public class PhoneCmpgnResultController
{
    /**
     * 캠페인결과 페이지
     * 
     * @return
     */
    @ApiOperation(value = "캠페인결과 페이지",
                  notes = "캠페인결과 이동")
    @GetMapping("/phone/cmpgn/web/result")
    public String cmpgnResultMvmn() throws TelewebWebException
    {
        log.debug("cmpgnResultMvmn");
        return "phone/cmpgn/phone-cmpgn-badn-result";
    }
}
