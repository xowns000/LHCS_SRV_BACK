package kr.co.hkcloud.palette3.infra.tplex.cti.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "phoneCallController",
     description = "전화걸기")
public class PhoneCallController
{
    /**
     * 전화걸기로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "전화걸기 페이지",
                  notes = "전화걸기로 이동한다.")
    @GetMapping("/infra/ics/cti/web/PhoneCall")
    public String movePhoneCall() throws TelewebWebException
    {
        log.debug("movePhoneCall");
        return "infra/ics/cti/PhoneCall";
    }
}
