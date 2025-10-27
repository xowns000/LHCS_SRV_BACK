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
import kr.co.hkcloud.palette3.phone.qa.app.PhoneQATrgetManageService;
import kr.co.hkcloud.palette3.phone.qa.util.PhoneQATrgetManageValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController("TrgetManageRestController")
@Api(value = "TrgetManageRestController",
     description = "QA대상등록관리 REST 컨트롤러")
public class PhoneQATrgetManageRestController
{
    private final PhoneQATrgetManageService   phoneQATrgetManageService;
    private final PhoneQATrgetManageValidator phoneQATrgetManageValidator;


    /**
     * 메인 리스트 조회
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "메인 리스트 조회",
                  notes = "메인 리스트 조회")
    @PostMapping("/phone-api/qa/trget-manage/list")
    public Object selectRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneQATrgetManageService.selectRtn(mjsonParams);
        return objRetParams;
    }


    @ApiOperation(value = "상담이력상세조회",
                  notes = "상담이력상세조회를한다")
    @PostMapping("/phone-api/qa/trget-manage/cnslt-hist/detail")
    public Object selectRtnDetails(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneQATrgetManageService.selectRtnDetails(mjsonParams);
        return objRetParams;
    }


    @ApiOperation(value = "QA등록",
                  notes = "QA등록을한다")
    @PostMapping("/phone-api/qa/trget-manage/regist")
    public Object insertRtn(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
//        Boolean bResult = false;
//       
//        bResult  = QamTargetMngService.selectHaveExtractClose(mjsonParams);
//        
//        if (bResult == false) 
//        {
//        	 objRetParams.setHeader("ERROR_FLAG", true);
//             objRetParams.setHeader("ERROR_MSG", "이미 등록마감 건이 존재합니다.");
//             objRetParams.setHeader("ERROR_TYPE", "W");
//             return objRetParams;
//        }

        // 평가 키값 validation 체크
        phoneQATrgetManageValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        objRetParams = phoneQATrgetManageService.insertRtn(mjsonParams);
        return objRetParams;
    }


    @ApiOperation(value = "QA등록마감",
                  notes = "QA등록마감을한다.")
    @PostMapping("/phone-api/qa/trget-manage/regist-clos/modify")
    public Object updateRtn(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        // 평가 키값 validation 체크
        phoneQATrgetManageValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        objRetParams = phoneQATrgetManageService.updateRtn(mjsonParams);
        return objRetParams;
    }


    @ApiOperation(value = "QA체크여부",
                  notes = "QA체크여부를조회")
    @PostMapping("/phone-api/qa/trget-manage/check-at/inqire")
    public Object selectRtnCheckYN(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneQATrgetManageService.selectRtnCheckYN(mjsonParams);
        return objRetParams;
    }


    @ApiOperation(value = "QA재등록",
                  notes = "QA재등록")
    @PostMapping("/phone-api/qa/trget-manage/rergist")
    public Object updateRtnInsert(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneQATrgetManageService.updateRtnInsert(mjsonParams);
        return objRetParams;
    }


    @ApiOperation(value = "QA번호조회",
                  notes = "QA번호조회")
    @PostMapping("/phone-api/qa/trget-manage/innb/inqire")
    public Object selectRtnQA_ID(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneQATrgetManageService.selectRtnQA_ID(mjsonParams);
        return objRetParams;
    }


    @ApiOperation(value = "QA삭제",
                  notes = "QA삭제")
    @PostMapping("/phone-api/qa/trget-manage/delete")
    public Object deleteRtn(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        // 평가 키값 validation 체크
        phoneQATrgetManageValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        objRetParams = phoneQATrgetManageService.deleteRtn(mjsonParams);
        return objRetParams;
    }


    @ApiOperation(value = "QA마감완료",
                  notes = "QA마감완료")
    @PostMapping("/phone-api/qa/trget-manage/clos-compt/modify")
    public Object processRtnExtractClose(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        // 평가 키값 validation 체크
        phoneQATrgetManageValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        objRetParams = phoneQATrgetManageService.processRtnExtractClose(mjsonParams);
        return objRetParams;
    }

}
