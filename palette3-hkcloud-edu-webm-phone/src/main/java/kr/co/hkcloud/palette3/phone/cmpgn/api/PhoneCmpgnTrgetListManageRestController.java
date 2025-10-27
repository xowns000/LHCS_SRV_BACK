package kr.co.hkcloud.palette3.phone.cmpgn.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.cmpgn.app.PhoneCmpgnTrgetListManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "phoneCmpgnTrgetListManageRestController",
     description = "캠페인 대상 리스트관리 서비스콜 REST 컨트롤러")
public class PhoneCmpgnTrgetListManageRestController
{
    private final PhoneCmpgnTrgetListManageService phoneCmpgnTrgetListManageService;


    /**
     * 캠페인 대상 리스트 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "캠페인 대상 리스트 조회",
                  notes = "캠페인 대상 리스트 조회")
    @PostMapping("/phone-api/cmpgn/trget-list-manage/list")
    public Object selectRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnTrgetListManageService.selectRtn(mjsonParams);
        return objRetParams;
    }


    /**
     * 할당상담원 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "할당상담원 조회",
                  notes = "할당상담원 조회")
    @PostMapping("/phone-api/cmpgn/trget-list-manage/asgn-cnslr-list")
    public Object selectRtn01(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnTrgetListManageService.selectRtn01(mjsonParams);
        return objRetParams;
    }


    /**
     * 처리결과 콤보
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "처리결과 콤보",
                  notes = "처리결과 콤보")
    @PostMapping("/phone-api/cmpgn/trget-list-manage/process-result-combo")
    public Object selectRtnComboBox(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnTrgetListManageService.selectRtnComboBox(mjsonParams);
        return objRetParams;
    }


    /**
     * 고객데이터파일콤보
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "고객데이터파일콤보",
                  notes = "고객데이터파일콤보")
    @PostMapping("/phone-api/cmpgn/trget-list-manage/cstmr-data-file-combo")
    public Object selectRtnComboBox01(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnTrgetListManageService.selectRtnComboBox01(mjsonParams);
        return objRetParams;
    }


    /**
     * 회수실행
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "회수실행",
                  notes = "회수실행")
    @PostMapping("/phone-api/cmpgn/trget-list-manage/rtrvl-execut")
    public Object updateRtn01(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnTrgetListManageService.updateRtn01(mjsonParams);
        return objRetParams;
    }


    /**
     * 회수실행이력저장
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "회수실행이력저장",
                  notes = "회수실행이력저장")
    @PostMapping("/phone-api/cmpgn/trget-list-manage/rtrvl-execut-hist-stre")
    public Object insertRtn01(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnTrgetListManageService.insertRtn01(mjsonParams);
        return objRetParams;
    }


    /**
     * 할당실행
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "할당실행",
                  notes = "할당실행")
    @PostMapping("/phone-api/cmpgn/trget-list-manage/asgn-execut")
    public Object updateRtn02(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnTrgetListManageService.updateRtn02(mjsonParams);
        return objRetParams;
    }


    /**
     * 할당실행이력저장
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "할당실행이력저장",
                  notes = "할당실행이력저장")
    @PostMapping("/phone-api/cmpgn/trget-list-manage/asgn-execut-hist-stre")
    public Object insertRtn02(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnTrgetListManageService.insertRtn02(mjsonParams);
        return objRetParams;
    }

}
