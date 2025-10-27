package kr.co.hkcloud.palette3.phone.cmpgn.api;


import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.cmpgn.app.PhoneCmpgnTmplatManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneCmpgnTmplatManageRestController",
     description = "캠페인 템플릿관리 서비스콜 REST 컨트롤러")
public class PhoneCmpgnTmplatManageRestController
{
    private final PhoneCmpgnTmplatManageService phoneCmpgnTmplatManageService;


    /**
     * 캠페인 템플릿 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "캠페인 템플릿 조회",
                  notes = "캠페인 템플릿 조회")
    @PostMapping("/phone-api/cmpgn/tmplat-manage/cmpgn-tmplat-list")
    public Object selectRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnTmplatManageService.selectRtn(mjsonParams);
        return objRetParams;
    }


    /**
     * 캠페인 템플릿 항목 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "캠페인 템플릿 항목 조회",
                  notes = "캠페인 템플릿 항목 조회")
    @PostMapping("/phone-api/cmpgn/tmplat-manage/cmpgn-tmplat-iem-list")
    public Object selectRtn2(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnTmplatManageService.selectRtn2(mjsonParams);
        return objRetParams;
    }


    /**
     * 캠페인 템플릿 파일 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "캠페인 템플릿 파일 조회",
                  notes = "캠페인 템플릿 파일 조회")
    @PostMapping("/phone-api/cmpgn/tmplat-manage/cmpgn-tmplat-file-list")
    public Object selectRtn3(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnTmplatManageService.selectRtn3(mjsonParams);
        return objRetParams;
    }


    /**
     * 캠페인 템플릿 컬럼 저장
     * 
     * @param  mjsonParams
     * @return             TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "캠페인 템플릿 컬럼 저장",
                  notes = "캠페인 템플릿 컬럼 저장")
    @PostMapping("/phone-api/cmpgn/tmplat-manage/cmpgn-tmplat-column-regist")
    public Object processRtn(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        return phoneCmpgnTmplatManageService.processRtn(mjsonParams);
    }


    /**
     * 캠페인 템플릿 저장
     * 
     * @param  mjsonParams
     * @return             TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "캠페인 템플릿 저장",
                  notes = "캠페인 템플릿 저장")
    @PostMapping("/phone-api/cmpgn/tmplat-manage/cmpgn-tmplat-regist")
    public Object processRtnMain(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        return phoneCmpgnTmplatManageService.processRtnMain(mjsonParams);
    }

}
