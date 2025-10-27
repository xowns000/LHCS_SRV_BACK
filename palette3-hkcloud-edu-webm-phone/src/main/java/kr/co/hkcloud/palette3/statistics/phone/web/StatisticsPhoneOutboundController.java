package kr.co.hkcloud.palette3.statistics.phone.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "statisticsPhoneOutboundController",
     description = "아웃바운드통계 컨트롤러")
public class StatisticsPhoneOutboundController
{
    /**
     * 아웃바운드통계 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "아웃바운드통계 페이지",
                  notes = "아웃바운드통계 이동한다")
    @GetMapping("/statistics/phone/web/outbnd")
    public String moveStatisticsPhoneOutbound() throws TelewebWebException
    {
        log.debug("moveStatisticsPhoneOutbound");
        return "statistics/phone/statistics-phone-outbound";
    }


    /**
     * 아웃바운드 조회 팝업 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "아웃바운드 조회 팝업 페이지",
                  notes = "아웃바운드 조회 팝업 페이지로 이동한다")
    @GetMapping("/statistics/phone/web/outbnd/inqire-popup")
    public String moveObndFindPop() throws TelewebWebException
    {
        log.debug("moveObndFindPop");
        return "statistics/phone/statistics-phone-outboundpopup";
    }
}
