package kr.co.hkcloud.palette3.phone.qa.api;


import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebValidationException;
import kr.co.hkcloud.palette3.phone.qa.app.PhoneQAEvlExecutManageService;
import kr.co.hkcloud.palette3.phone.qa.util.PhoneQAEvlExecutManageValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController("EvlExecutManageRestController")
@Api(value = "EvlExecutManageRestController",
     description = "QA평가실행관리 REST 컨트롤러")
public class PhoneQAEvlExecutManageRestController
{
    private final PhoneQAEvlExecutManageService   EvlExecutManageService;
    private final PhoneQAEvlExecutManageValidator phoneQAEvlExecutManageValidator;


    /**
     * QA 결과 조회
     * 
     * @param  mjsonParams HashMap. Key List : (HttpServletRequest)mobjRequest, (HttpServletResponse)mobjResponse, (TelewebJSON)mjsonParams, (String)strDBMS
     * @return             TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "QA결과조회",
                  notes = "QA결과조회")
    @PostMapping("/phone-api/qa/evl-execut-manage/result/list")
    public Object selectRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        // 반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = EvlExecutManageService.selectRtn(mjsonParams);
        // 최종결과값 반환
        return objRetParams;
    }


    /**
     * QA 결과 조회
     * 
     * @param  mjsonParams HashMap. Key List : (HttpServletRequest)mobjRequest, (HttpServletResponse)mobjResponse, (TelewebJSON)mjsonParams, (String)strDBMS
     * @return             TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "QA결과상세조회",
                  notes = "QA결과상세조회")
    @PostMapping("/phone-api/qa/evl-execut-manage/result/detail")
    public Object selectRtnDetails(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        // 반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = EvlExecutManageService.selectRtnDetails(mjsonParams);
        // 최종결과값 반환
        return objRetParams;
    }


    @ApiOperation(value = "QA평가마감",
                  notes = "QA평가마감")
    @PostMapping("/phone-api/qa/evl-execut-manage/evl-clos/modify")
    public Object processRtnEvaluationClose(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject());

        // 평가 키값 validation 체크
        phoneQAEvlExecutManageValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        objRetParams = EvlExecutManageService.processRtnEvaluationClose(mjsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "QA평가마감취소",
                  notes = "QA평가마감취소")
    @PostMapping("/phone-api/qa/evl-execut-manage/clos-cancl/modify")
    public Object processRtnCancel(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject());
        objRetParams = EvlExecutManageService.processRtnCancel(mjsonParams);

        return objRetParams;
    }

}
