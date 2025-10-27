package kr.co.hkcloud.palette3.infra.tplex.cti.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "CallTransferController",
     description = "내선전화")
public class CallTransferController
{
    /**
     * 내선통화로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "내선전화 페이지",
                  notes = "내선전화로 이동한다.")
    @GetMapping("/infra/ics/cti/web/CallTransfer")
    public String moveCallTransfer() throws TelewebWebException
    {
        log.debug("moveTransferCall");
        return "infra/ics/cti/CallTransfer";
    }
}
