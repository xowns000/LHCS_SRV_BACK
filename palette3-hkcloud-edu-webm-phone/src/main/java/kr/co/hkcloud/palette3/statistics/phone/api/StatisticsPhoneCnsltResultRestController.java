package kr.co.hkcloud.palette3.statistics.phone.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.statistics.phone.app.StatisticsPhoneCnsltResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "StatisticsPhoneCnsltResultRestController",
     description = "상담결과통계 컨트롤러")
public class StatisticsPhoneCnsltResultRestController
{
    private final StatisticsPhoneCnsltResultService statisticsPhoneCnsltResultService;


    /**
     * 
     * @param  jsonParam
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "상담결과통계",
                  notes = "상담결과통계를 조회한다.")
    @PostMapping("/api/statistics/phone/cnslt-result/inqire")
    public Object selectCnslResultSttc(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {
        //client에서 전송받은 파라메터 생성
        //TelewebJSON mjsonParams     = (TelewebJSON)inHashMap.get("mjsonParams");

        //반환 파라메터 생성
        TelewebJSON objParam = new TelewebJSON(jsonParam);

        //Service 호출
        objParam = statisticsPhoneCnsltResultService.selectCnslResultSttc(jsonParam);

        return objParam;
    }

}
