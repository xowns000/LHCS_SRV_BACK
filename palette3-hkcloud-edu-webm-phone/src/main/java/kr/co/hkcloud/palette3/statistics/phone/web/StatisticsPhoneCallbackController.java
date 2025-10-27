package kr.co.hkcloud.palette3.statistics.phone.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "statisticsPhoneCallbackController",
     description = "콜백통계 컨트롤러")
public class StatisticsPhoneCallbackController
{
    /**
     * 콜백통계 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "콜백통계 페이지",
                  notes = "콜백통계 이동한다")
    @GetMapping("/statistics/phone/web/callback")
    public String moveStatisticsPhoneCallbackController() throws TelewebWebException
    {
        log.debug("moveStatisticsPhoneCallbackController");
        return "statistics/phone/statistics-phone-callback";
    }
}
