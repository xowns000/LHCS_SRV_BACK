package kr.co.hkcloud.palette3.statistics.phone.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.statistics.phone.app.StatisticsPhoneQAExecuteResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "StatisticsPhoneQAExecuteResultRestController",
     description = "QA평가통계 REST 컨트롤러")
public class StatisticsPhoneQAExecuteResultRestController
{
    private final StatisticsPhoneQAExecuteResultService statisticsPhoneQAExecuteResultService;


    /**
     * QA통계 조회
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "QA통계 조회",
                  notes = "QA통계 조회")
    @PostMapping("/api/statistics/phone/qa-execute-result/stats/inqire")
    public Object selectRtnQaStsc(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                     //반환 파라메터 생성

        objRetParams = statisticsPhoneQAExecuteResultService.selectRtnQaStsc(mjsonParams);

        return objRetParams;    //최종 결과값 반환
    }


    /**
     * QA 유형 조회
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "QA 유형 조회",
                  notes = "QA 유형 조회")
    @PostMapping("/api/statistics/phone/qa-execute-result/ty/inqire")
    public Object selectRtnQaTy(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                     //반환 파라메터 생성

        objRetParams = statisticsPhoneQAExecuteResultService.selectRtnQaTy(mjsonParams);

        return objRetParams;    //최종 결과값 반환
    }
}
