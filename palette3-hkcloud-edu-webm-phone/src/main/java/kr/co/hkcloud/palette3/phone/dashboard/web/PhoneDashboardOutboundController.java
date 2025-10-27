package kr.co.hkcloud.palette3.phone.dashboard.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "phoneDashboardOutboundController",
     description = "아웃바운드모니터링 컨트롤러")
public class PhoneDashboardOutboundController
{
    /**
     * 아웃바운드모니터링 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "아웃바운드모니터링 페이지",
                  notes = "아웃바운드모니터링 이동한다")
    @GetMapping("/phone/dashboard/web/outbound")
    public String movePhoneDashboardOutbound() throws TelewebWebException
    {
        log.debug("movePhoneDashboardOutbound");
        return "phone/dashboard/phone-dashboard-outbound";
    }
}
