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
public class PhoneOutboundRegistDclzPopupController
{

    /**
     * 아웃바운드 상담원근태 팝업 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "아웃바운드 상담원근태 팝업 페이지",
                  notes = "아웃바운드 상담원근태 페이지로 이동한다")
    @GetMapping("/phone/outbound/web/manage/agent-dclz-inqire-popup")
    public String moveObndDilcePop() throws TelewebWebException
    {
        log.debug("moveObndDilcePop");
        return "phone/outbound/phone-outbound-manage-dclz-popup";
    }

}
