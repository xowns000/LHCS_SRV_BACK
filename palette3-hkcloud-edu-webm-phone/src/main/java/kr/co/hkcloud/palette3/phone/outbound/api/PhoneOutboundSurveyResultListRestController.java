package kr.co.hkcloud.palette3.phone.outbound.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.outbound.app.PhoneOutboundSurveyResultListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneOutboundSurveyResultListRestController",
     description = "설문지관리 REST 컨트롤러")
public class PhoneOutboundSurveyResultListRestController
{

    private final PhoneOutboundSurveyResultListService phoneOutboundSurveyResultListService;


    /**
     * 설문지 조회
     * 
     * @param  mjsonParams
     * @return
     * 
     */
    @ApiOperation(value = "설문지 조회",
                  notes = "설문지 조회")
    @PostMapping("/phone-api/outbound/survey-result-list/list")
    public Object selectRtnServayList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        objRetParams = phoneOutboundSurveyResultListService.selectRtnServayList(mjsonParams);

        return objRetParams;
    }


    /**
     * 설문지문항 조회
     * 
     * @param  mjsonParams
     * @return
     * 
     */
    @ApiOperation(value = "설문지문항 조회",
                  notes = "설문지문항 조회")
    @PostMapping("/phone-api/outbound/survey-result-list/detail")
    public Object selectRtnServayQList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = phoneOutboundSurveyResultListService.selectRtnServayQList(mjsonParams);

        return objRetParams;
    }


    /**
     * 설문지결과 조회
     * 
     * @param  mjsonParams
     * @return
     * 
     */
    @ApiOperation(value = "설문지결과 조회",
                  notes = "설문지결과 조회")
    @PostMapping("/phone-api/outbound/survey-result-list-result/inqire")
    public Object selectRtnResult(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON retTotCntJson = new TelewebJSON();
        int totCnt = 0;
        String ansType = "";

        retTotCntJson = phoneOutboundSurveyResultListService.selectRtnResultQAnsTotCnt(mjsonParams);
        totCnt = retTotCntJson.getInt("TOT_CNT");

        ansType = mjsonParams.getString("ANS_TYPE");
        if(ansType.equals("01")) {
            objRetParams = phoneOutboundSurveyResultListService.selectRtnResult_multi(mjsonParams);
        }
        else if(ansType.equals("02")) {
            objRetParams = phoneOutboundSurveyResultListService.selectRtnResult_short(mjsonParams);
        }

        objRetParams.setInt("TOT_CNT", totCnt);

        return objRetParams;
    }

}
