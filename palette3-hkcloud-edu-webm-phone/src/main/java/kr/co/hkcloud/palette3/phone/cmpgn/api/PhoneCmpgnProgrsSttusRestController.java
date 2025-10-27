package kr.co.hkcloud.palette3.phone.cmpgn.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.cmpgn.app.PhoneCmpgnProgrsSttusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "phoneCmpgnProgrsSttusRestController",
     description = "캠페인현황 서비스콜 REST 컨트롤러")
public class PhoneCmpgnProgrsSttusRestController
{
    private final PhoneCmpgnProgrsSttusService phoneCmpgnProgrsSttusService;


    /**
     * 캠페인 리스트 콤보
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "캠페인 리스트 콤보",
                  notes = "캠페인 리스트 콤보")
    @PostMapping("/phone-api/cmpgn/progrs-sttus/cmpgn-list-combo")
    public Object selectRtnComboBox(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnProgrsSttusService.selectRtnComboBox(mjsonParams);
        return objRetParams;
    }


    /**
     * 캠페인 진행현황조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "캠페인 현황조회",
                  notes = "캠페인 현황조회")
    @PostMapping("/phone-api/cmpgn/progrs-sttus/list")
    public Object selectRtnStep01(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnProgrsSttusService.selectRtnStep01(mjsonParams);
        return objRetParams;
    }


    /**
     * 담당자별현황리스트
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "담당자별현황리스트",
                  notes = "담당자별현황리스트")
    @PostMapping("/phone-api/cmpgn/progrs-sttus/charger-sttus-list")
    public Object selectRtnStep02(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnProgrsSttusService.selectRtnStep02(mjsonParams);
        return objRetParams;
    }


    /**
     * 캠페인 데이타 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "캠페인 데이타 조회",
                  notes = "캠페인 데이타 조회")
    @PostMapping("/phone-api/cmpgn/progrs-sttus/cmpgn-data-list")
    public Object selectRtnData(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnProgrsSttusService.selectRtnData(mjsonParams);
        return objRetParams;
    }


    /**
     * 캠페인 미할당 상담원 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "캠페인 미할당 상담원 조회",
                  notes = "캠페인 미할당 상담원 조회")
    @PostMapping("/phone-api/cmpgn/progrs-sttus/noasgn-cnslr-list")
    public Object selectRtnUser(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnProgrsSttusService.selectRtnUser(mjsonParams);
        return objRetParams;
    }


    /**
     * 캠페인 할당 상담원 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "캠페인 할당 상담원 조회",
                  notes = "캠페인 할당 상담원 조회")
    @PostMapping("/phone-api/cmpgn/progrs-sttus/asgn-cnslr-list")
    public Object selectRtnAllc(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnProgrsSttusService.selectRtnAllc(mjsonParams);
        return objRetParams;
    }


    /**
     * 캠페인 프로젝트별 데이타 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "캠페인 프로젝트별 데이타 조회",
                  notes = "캠페인 프로젝트별 데이타 조회")
    @PostMapping("/phone-api/cmpgn/progrs-sttus/prjct-data-list")
    public Object selectRtnPrjNo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnProgrsSttusService.selectRtnPrjNo(mjsonParams);
        return objRetParams;
    }


    /**
     * 캠페인 대상_리스트 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "캠페인 대상_리스트  조회",
                  notes = "캠페인 대상_리스트  조회")
    @PostMapping("/phone-api/cmpgn/progrs-sttus/trget-list")
    public Object selectRtnInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnProgrsSttusService.selectRtnInfo(mjsonParams);
        return objRetParams;
    }


    /**
     * 캠페인 할당 데이터 저장 처리
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "캠페인 할당 데이터 저장 처리",
                  notes = "캠페인 할당 데이터 저장 처리")
    @PostMapping("/phone-api/cmpgn/progrs-sttus/asgn-data-stre-process")
    public Object insertRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnProgrsSttusService.insertRtn(mjsonParams);
        return objRetParams;
    }


    /**
     * 캠페인 데이터 삭제 처리
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "캠페인 데이터 삭제 처리",
                  notes = "캠페인 데이터 삭제 처리")
    @PostMapping("/phone-api/cmpgn/progrs-sttus/data-delete-process")
    public Object deleteRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnProgrsSttusService.deleteRtn(mjsonParams);
        return objRetParams;
    }
}
