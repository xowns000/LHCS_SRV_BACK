package kr.co.hkcloud.palette3.statistics.phone.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.statistics.phone.app.StatisticsPhoneCallbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "StatisticsPhoneCallbackRestController",
     description = "콜백통계 컨트롤러")
public class StatisticsPhoneCallbackRestController
{
    private final StatisticsPhoneCallbackService statisticsPhoneCallbackService;


    /**
     * 
     * @param  jsonParam
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "콜백통계",
                  notes = "콜백통계를 조회한다.")
    @PostMapping("/api/statistics/phone/callback/list")
    public Object selectClbkSttcList(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {

        TelewebJSON objParam = new TelewebJSON(jsonParam);

        objParam = statisticsPhoneCallbackService.selectClbkSttcList(jsonParam);

        return objParam;
    }
}
