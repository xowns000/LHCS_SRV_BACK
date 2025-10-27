package kr.co.hkcloud.palette3.statistics.phone.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "StatisticsPhoneOutboundDailyReportController",
     description = "아웃바운드일일통계보고 컨트롤러")
public class StatisticsPhoneOutboundDailyReportController
{
    /**
     * 아웃바운드일일통계보고 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "아웃바운드일일통계보고 페이지",
                  notes = "아웃바운드일일통계보고 이동한다")
    @GetMapping("/statistics/phone/web/outbnd-dail-report")
    public String moveStatisticsPhoneOutboundDailyReport() throws TelewebWebException
    {
        log.debug("moveStatisticsPhoneOutboundDailyReport");
        return "statistics/phone/statistics-phone-outbound-daily-report";
    }
}
