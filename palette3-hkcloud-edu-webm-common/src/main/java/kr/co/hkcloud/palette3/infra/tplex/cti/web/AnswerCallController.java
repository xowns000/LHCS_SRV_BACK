package kr.co.hkcloud.palette3.infra.tplex.cti.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "AnswerCallController",
     description = "전화받기")
public class AnswerCallController
{
    /**
     * 전화걸기로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "전화받기 페이지",
                  notes = "전화받기로 이동한다.")
    @GetMapping("/infra/ics/cti/web/AnswerCall")
    public String movePhoneCall() throws TelewebWebException
    {
        log.debug("moveAnswerCall");
        return "infra/ics/cti/AnswerCall";
    }


    /**
     * 전화걸기로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "전화받기 페이지",
                  notes = "전화받기로 이동한다.")
    @GetMapping("/infra/ics/cti/web/AnswerCallTest")
    public String movePhoneCallTest() throws TelewebWebException
    {
        log.debug("moveAnswerCallTest");
        return "infra/ics/cti/AnswerCallTest";
    }


    /**
     * 호전환전화받기 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "호전환전화받기 페이지",
                  notes = "호전환전화받기로 이동한다.")
    @GetMapping("/infra/ics/cti/web/AnswerTransfer")
    public String movePhoneCallAnswerTransfer() throws TelewebWebException
    {
        log.debug("moveAnswerAnswerTransfer");
        return "infra/ics/cti/AnswerTransfer";
    }
}
