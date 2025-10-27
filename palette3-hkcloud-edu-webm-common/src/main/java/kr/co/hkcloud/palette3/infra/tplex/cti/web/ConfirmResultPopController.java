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
     description = "출금동의")
public class ConfirmResultPopController
{
    /**
     * 내선통화로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "인증결과",
                  notes = "인증결과로 이동한다.")
    @GetMapping("/infra/ics/cti/web/ConfirmResultPop")
    public String moveWithdrawPop() throws TelewebWebException
    {
        log.debug("moveConfirmResultPop");
        return "infra/ics/cti/ConfirmResultPop";
    }
}
