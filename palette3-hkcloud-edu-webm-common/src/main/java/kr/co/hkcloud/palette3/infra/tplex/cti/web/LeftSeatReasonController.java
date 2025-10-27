package kr.co.hkcloud.palette3.infra.tplex.cti.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "LeftSeatReasonController",
     description = "전화받기")
public class LeftSeatReasonController
{
    /**
     * 전화걸기로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "전화받기 페이지",
                  notes = "전화받기로 이동한다.")
    @GetMapping("/infra/ics/cti/web/LeftSeatReason")
    public String moveLeftSeatReason() throws TelewebWebException
    {
        log.debug("moveLeftSeatReason");
        return "infra/ics/cti/LeftSeatReason";
    }
}
