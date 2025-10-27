package kr.co.hkcloud.palette3.statistics.phone.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.statistics.phone.app.StatisticsPhoneLMService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "StatisticsPhoneLMRestController",
     description = "LMS 통계 Rest 컨트롤러")
public class StatisticsPhoneLMRestController
{
    private final StatisticsPhoneLMService statisticsPhoneLMService;


    @ApiOperation(value = "LMS 통계 평가 리스트",
                  notes = "LMS 통계 평가 리스트")
    @PostMapping("/api/statistics/phone/lm/list")
    public Object selectRtnLm(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON rtnJson = new TelewebJSON(mjsonParams);

        rtnJson = statisticsPhoneLMService.selectRtnLm(mjsonParams);

        return rtnJson;
    }


    @ApiOperation(value = "LMS 통계 평가 상세 조회",
                  notes = "LMS 통계 평가 상세 조회")
    @PostMapping("/api/statistics/phone/lm/detail/inqire")
    public Object selectRtnDetail(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON rtnJson = new TelewebJSON(mjsonParams);

        rtnJson.setDataObject("DETAIL", statisticsPhoneLMService.selectRtnLmDetail(mjsonParams));

        rtnJson.setDataObject("LIST", statisticsPhoneLMService.selectRtnLmDataRst(mjsonParams));

        return rtnJson;
    }


    @ApiOperation(value = "LMS 통계 문항 상세 조회",
                  notes = "LMS 통계 문항 상세 조회")
    @PostMapping("/api/statistics/phone/lm/detail/qs/inqire")
    public Object selectRtnDetailQs(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON rtnJson = new TelewebJSON(mjsonParams);

        rtnJson.setDataObject("DETAIL", statisticsPhoneLMService.selectRtnLmDetail(mjsonParams));
        rtnJson.setDataObject("qsList", statisticsPhoneLMService.selectRtnLmQs(mjsonParams));
        rtnJson.setDataObject("veList", statisticsPhoneLMService.selectRtnLmVe(mjsonParams));

        return rtnJson;
    }
}
