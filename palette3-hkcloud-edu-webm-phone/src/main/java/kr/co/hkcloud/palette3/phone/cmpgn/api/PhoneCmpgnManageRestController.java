package kr.co.hkcloud.palette3.phone.cmpgn.api;


import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.cmpgn.app.PhoneCmpgnManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "phoneCmpgnManageRestController",
     description = "캠페인관리 서비스콜 REST 컨트롤러")
public class PhoneCmpgnManageRestController
{
    private final PhoneCmpgnManageService phoneCmpgnManageService;


    /**
     * 캠페인 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "캠페인 조회",
                  notes = "캠페인 조회")
    @PostMapping("/phone-api/cmpgn/manage/list")
    public Object selectRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnManageService.selectRtn(mjsonParams);
        return objRetParams;
    }


    /**
     * 상담원 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "상담원 조회",
                  notes = "상담원 조회")
    @PostMapping("/phone-api/cmpgn/manage/cnslr-list")
    public Object selectRtn2(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnManageService.selectRtn2(mjsonParams);
        return objRetParams;
    }


    /**
     * 캠페인데이터 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "캠페인데이터 조회",
                  notes = "캠페인데이터 조회")
    @PostMapping("/phone-api/cmpgn/manage/data-list")
    public Object selectRtn3(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnManageService.selectRtn3(mjsonParams);
        return objRetParams;
    }


    /**
     * 고객데이터 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "고객데이터 조회",
                  notes = "고객데이터 조회")
    @PostMapping("/phone-api/cmpgn/manage/cstmr-data-list")
    public Object selectRtn4(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnManageService.selectRtn4(mjsonParams);
        return objRetParams;
    }


    /**
     * 캠페인 불량데이터 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "캠페인 불량데이터 조회",
                  notes = "캠페인 불량데이터 조회")
    @PostMapping("/phone-api/cmpgn/manage/badn-data-list")
    public Object selectRtn5(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnManageService.selectRtn5(mjsonParams);
        return objRetParams;
    }


    /**
     * 캠페인콤보 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "캠페인콤보 조회",
                  notes = "캠페인콤보 조회")
    @PostMapping("/phone-api/cmpgn/manage/combo-list")
    public Object selectRtnCombo01(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnManageService.selectRtnCombo01(mjsonParams);
        return objRetParams;
    }


    /**
     * 파일템플릿콤보 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "파일템플릿콤보 조회",
                  notes = "파일템플릿콤보 조회")
    @PostMapping("/phone-api/cmpgn/manage/file-tmplat-combo-list")
    public Object selectRtnCombo02(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnManageService.selectRtnCombo02(mjsonParams);
        return objRetParams;
    }


    /**
     * 켐페인 템플릿 컬럼 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "켐페인 템플릿 컬럼 조회",
                  notes = " 켐페인 템플릿 컬럼 조회")
    @PostMapping("/phone-api/cmpgn/manage/tmplat-column-list")
    public Object selectRtnCTC(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnManageService.selectRtnCTC(mjsonParams);
        return objRetParams;
    }


    /**
     * 켐페인 시퀀스 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "켐페인 시퀀스 조회",
                  notes = " 켐페인 시퀀스 조회")
    @PostMapping("/phone-api/cmpgn/manage/seq-list")
    public Object selectRtnGetSEQ(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnManageService.selectRtnGetSEQ(mjsonParams);
        return objRetParams;
    }


    /**
     * 캠페인 등록
     * 
     * @param  mjsonParams
     * @return             TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "캠페인 등록",
                  notes = "캠페인 등록")
    @PostMapping("/phone-api/cmpgn/manage/regist")
    public Object insertRtn(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        return phoneCmpgnManageService.insertRtn(mjsonParams);
    }


    /**
     * 캠페인 리스트 등록
     * 
     * @param  mjsonParams
     * @return             TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "캠페인 리스트 등록",
                  notes = "캠페인 리스트 등록")
    @PostMapping("/phone-api/cmpgn/manage/list-regist")
    public Object insertRtnOBJLIST(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        return phoneCmpgnManageService.insertRtnOBJLIST(mjsonParams);
    }


    /**
     * 캠페인 불량 데이터 등록
     * 
     * @param  mjsonParams
     * @return             TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "캠페인 불량 데이터 등록",
                  notes = "캠페인 불량 데이터 등록")
    @PostMapping("/phone-api/cmpgn/manage/badn-data-regist")
    public Object insertRtnErrorData(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        return phoneCmpgnManageService.insertRtnErrorData(mjsonParams);
    }


    /**
     * 캠페인 수정
     * 
     * @param  mjsonParams
     * @return             TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "캠페인 수정",
                  notes = "캠페인 수정")
    @PostMapping("/phone-api/cmpgn/manage/updt")
    public Object upDataRtn(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        return phoneCmpgnManageService.upDataRtn(mjsonParams);
    }


    /**
     * 캠페인 데이터 등록
     * 
     * @param  mjsonParams
     * @return             TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "캠페인 데이터 등록",
                  notes = "캠페인 데이터 등록")
    @PostMapping("/phone-api/cmpgn/manage/data-regist")
    public Object insertRtnCmpData(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        return phoneCmpgnManageService.insertRtnCmpData(mjsonParams);
    }


    /**
     * 캠페인 삭제
     * 
     * @param  mjsonParams
     * @return             TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "캠페인 삭제",
                  notes = "캠페인 삭제")
    @PostMapping("/phone-api/cmpgn/manage/delete")
    public Object deleteRtn(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        return phoneCmpgnManageService.deleteRtn(mjsonParams);
    }

}
