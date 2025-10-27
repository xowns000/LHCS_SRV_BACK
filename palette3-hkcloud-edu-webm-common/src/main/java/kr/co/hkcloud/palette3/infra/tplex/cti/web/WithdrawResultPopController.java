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
public class WithdrawResultPopController
{
    /**
     * 내선통화로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "출금동의 페이지",
                  notes = "출금동의로 이동한다.")
    @GetMapping("/infra/ics/cti/web/WithdrawResultPop")
    public String moveWithdrawPop() throws TelewebWebException
    {
        log.debug("moveWithdrawPop");
        return "infra/ics/cti/WithdrawResultPop";
    }
}
