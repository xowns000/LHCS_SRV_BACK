package kr.co.hkcloud.palette3.phone.cmpgn.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.cmpgn.app.PhoneCmpgnBadnDataManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "phoneCmpgnBadnDataManageRestController",
     description = "불량데이터관리 서비스콜 REST 컨트롤러")
public class PhoneCmpgnBadnDataManageRestController
{
    private final PhoneCmpgnBadnDataManageService phoneCmpgnBadnDataManageService;


    /**
     * 불량데이터 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "불량데이터 조회",
                  notes = "불량데이터 조회")
    @PostMapping("/phone-api/cmpgn/badn-data-manage/list")
    public Object selectRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnBadnDataManageService.selectRtn(mjsonParams);
        return objRetParams;
    }


    /**
     * 캠페인 콤보 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "캠페인 콤보 조회",
                  notes = "캠페인 콤보 조회")
    @PostMapping("/phone-api/cmpgn/badn-data-manage/cmpgn-combo")
    public Object selectRtnCmpaiNmSet(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnBadnDataManageService.selectRtnCmpaiNmSet(mjsonParams);
        return objRetParams;
    }


    /**
     * 고객데이터 콤보 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "고객데이터 콤보 조회",
                  notes = "고객데이터 콤보 조회")
    @PostMapping("/phone-api/cmpgn/badn-data-manage/cstmr-data-combo")
    public Object selectRtnFileNmSet(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnBadnDataManageService.selectRtnFileNmSet(mjsonParams);
        return objRetParams;
    }

}
