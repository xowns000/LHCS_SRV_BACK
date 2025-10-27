package kr.co.hkcloud.palette3.statistics.phone.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.statistics.phone.app.StatisticsPhoneQA2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "StatisticsPhoneQA2RestController",
     description = "QA평가통계 REST 컨트롤러")
public class StatisticsPhoneQA2RestController
{
    private final StatisticsPhoneQA2Service statisticsPhoneQA2Service;


    @ApiOperation(value = "QA현황 조회",
                  notes = "QA현황 조회")
    @PostMapping("/api/statistics/phone/qa2/list")
    Object selectRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON rtnJson = new TelewebJSON(mjsonParams);

        rtnJson = statisticsPhoneQA2Service.selectRtn(mjsonParams);

        return rtnJson;
    }
}
