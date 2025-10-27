package kr.co.hkcloud.palette3.phone.dashboard.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "phoneDashboardCallbackController",
     description = "콜백모니터링 컨트롤러")
public class PhoneDashboardCallbackController
{
    /**
     * 콜백모니터링 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "콜백모니터링 페이지",
                  notes = "콜백모니터링 이동한다")
    @GetMapping("/phone/dashboard/web/callback")
    public String movePhoneDashboardCallback() throws TelewebWebException
    {
        log.debug("movePhoneDashboardCallback");
        return "phone/dashboard/phone-dashboard-callback";
    }
}
