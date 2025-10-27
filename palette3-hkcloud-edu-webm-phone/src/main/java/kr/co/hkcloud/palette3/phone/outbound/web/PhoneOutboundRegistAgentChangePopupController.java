package kr.co.hkcloud.palette3.phone.outbound.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api
public class PhoneOutboundRegistAgentChangePopupController
{

    /**
     * 아웃바운드 배분변경 상담원 팝업 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "아웃바운드 배분변경 상담원 팝업 페이지",
                  notes = "아웃바운드 배분변경 상담원 페이지로 이동한다")
    @GetMapping("/phone/outbound/web/manage/agent-change-inqire-popup")
    public String moveObndDivChangePop() throws TelewebWebException
    {
        log.debug("moveObndDivChangePop");
        return "phone/outbound/phone-outbound-manage-agent-change-popup";
    }

}
