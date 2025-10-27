package kr.co.hkcloud.palette3.statistics.phone.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.statistics.phone.app.StatisticsPhoneCnsltTypeByTimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "StatisticsPhoneCnsltTypeByTimeRestController",
     description = "상담유형별(시간대별)통계 컨트롤러")
public class StatisticsPhoneCnsltTypeByTimeRestController
{
    private final StatisticsPhoneCnsltTypeByTimeService statisticsPhoneCnsltTypeByTimeService;


    /**
     * 
     * @param  jsonParam
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "상담유형별(시간대별)통계",
                  notes = "상담유형별(시간대별)통계를 조회한다.")
    @PostMapping("/api/statistics/phone/cnslt-type-by-time/inqire")
    public Object selectCnslTypeTimeSttcList(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {
        TelewebJSON objParam = new TelewebJSON(jsonParam);

        objParam = statisticsPhoneCnsltTypeByTimeService.selectCnslTypeTimeSttcList(jsonParam);

        return objParam;
    }
    

    /**
     * 
     * @param  jsonParam
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "상담유형별(날짜별)통계",
                  notes = "상담유형별(날짜별)통계를 조회한다.")
    @PostMapping("/api/statistics/phone/cnslt-type-by-date/inqire")
    public Object selectCnslTypeDaySttcList(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {
        TelewebJSON objParam = new TelewebJSON(jsonParam);

        objParam = statisticsPhoneCnsltTypeByTimeService.selectCnslTypeDaySttcList(jsonParam);

        return objParam;
    }

}
