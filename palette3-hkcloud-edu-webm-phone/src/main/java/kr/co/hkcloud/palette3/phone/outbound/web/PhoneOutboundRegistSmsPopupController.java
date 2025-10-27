package kr.co.hkcloud.palette3.phone.outbound.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "PhoneOutboundRegistSmsPopupController",
     description = "아웃바운드 SMS일괄전송 팝업컨트폴러")
public class PhoneOutboundRegistSmsPopupController
{
    /**
     * 아웃바운드 SMS일괄전송 팝업 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "아웃바운드 SMS일괄전송 팝업 페이지",
                  notes = "아웃바운드 SMS일괄전송 팝업 페이지로 이동한다")
    @GetMapping("/phone/outbound/web/manage/sms-process-popup")
    public String moveObndSmsTotSendPop() throws TelewebWebException
    {
        log.debug("moveObndSmsTotSendPop");
        return "phone/outbound/phone-outbound-manage-sms-popup";
    }

}
