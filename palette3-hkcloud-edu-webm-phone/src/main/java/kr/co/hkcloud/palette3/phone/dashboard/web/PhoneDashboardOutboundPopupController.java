package kr.co.hkcloud.palette3.phone.dashboard.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api
public class PhoneDashboardOutboundPopupController
{
    /**
     * 아웃바운드 진행 현황 팝업 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "아웃바운드 진행 현황 팝업 페이지",
                  notes = "아웃바운드 진행 현황 페이지로 이동한다")
    @GetMapping("/phone/dashboard/web/outbound/progrs-sttus-popup")
    public String moveObndIngPop() throws TelewebWebException
    {
        log.debug("moveObndIngPop");
        return "phone/dashboard/phone-dashboard-outbound-popup";
    }

}
