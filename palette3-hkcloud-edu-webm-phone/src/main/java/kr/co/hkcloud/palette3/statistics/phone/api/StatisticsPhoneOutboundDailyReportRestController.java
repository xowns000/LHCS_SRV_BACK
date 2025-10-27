package kr.co.hkcloud.palette3.statistics.phone.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.statistics.phone.app.StatisticsPhoneOutboundDailyReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "StatisticsPhoneOutboundDailyReportRestController",
     description = "아웃바운드일일통계보고 컨트롤러")
public class StatisticsPhoneOutboundDailyReportRestController
{
    private final StatisticsPhoneOutboundDailyReportService statisticsPhoneOutboundDailyReportService;


    /**
     * 
     * @param  jsonParam
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "아웃바운드일일통계",
                  notes = "아웃바운드일일통계 현황을 조회한다.")
    @PostMapping("/api/statistics/phone/outbnd-dail-report/inqire")
    public Object selectObndAdaySttcRprtList(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {

        TelewebJSON objParam = new TelewebJSON();

        objParam = statisticsPhoneOutboundDailyReportService.selectObndAdaySttcRprtList(jsonParam);

        return objParam;
    }


    /**
     * 
     * @param  jsonParam
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "아웃바운드일일통계(건수)",
                  notes = "아웃바운드일일통계(건수) 현황을 조회한다.")
    @PostMapping("/api/statistics/phone/outbnd-dail-report-detail/inqire")
    public Object selectObndAdaySttcRprtCntList(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {

        TelewebJSON objParam = new TelewebJSON();

        objParam = statisticsPhoneOutboundDailyReportService.selectObndAdaySttcRprtCntList(jsonParam);

        return objParam;
    }

}
