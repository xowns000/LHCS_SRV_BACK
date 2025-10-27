package kr.co.hkcloud.palette3.phone.cmpgn.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.cmpgn.app.PhoneCmpgnResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "phoneCmpgnResultRestController",
     description = "캠페인결과 서비스콜 REST 컨트롤러")
public class PhoneCmpgnResultRestController
{
    private final PhoneCmpgnResultService phoneCmpgnResultService;


    /**
     * 목록 조회
     * 
     * @param  mjsonParams
     * @return
     */
    /* 실제쿼리없음. 실제 사용되는건지 확인필요 */
    @ApiOperation(value = "목록 조회",
                  notes = "목록 조회")
    @PostMapping("/phone-api/cmpgn/result/selectRtn")
    public Object selectRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnResultService.selectRtn(mjsonParams);
        return objRetParams;
    }


    /**
     * 캠페인결과조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "캠페인결과조회",
                  notes = "캠페인결과조회")
    @PostMapping("/phone-api/cmpgn/result/list")
    public Object selectRtnPrj(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnResultService.selectRtnPrj(mjsonParams);
        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     */
    /* 스크립트내 함수호출분 없음. 실제 사용되는건지 확인필요 */
    @ApiOperation(value = "",
                  notes = "")
    @PostMapping("/phone-api/cmpgn/result/detail")
    public Object selectRtnDetail(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnResultService.selectRtnDetail(mjsonParams);
        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     */
    /* 실제 사용되는건지 확인필요 */
    @ApiOperation(value = "",
                  notes = "")
    @PostMapping("/phone-api/cmpgn/result/RtnStep3")
    public Object selectRtnStep3(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnResultService.selectRtnStep3(mjsonParams);
        return objRetParams;
    }


    /**
     * 상위 협력사 콤보
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "",
                  notes = "")
    @PostMapping("/phone-api/cmpgn/result/")
    public Object selectRtnPm(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnResultService.selectRtnPm(mjsonParams);
        return objRetParams;
    }


    /**
     * 하위 협력사 콤보
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "",
                  notes = "")
    @PostMapping("/phone-api/cmpgn/result/upper-prtn-combo")
    public Object selectRtnPm01(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnResultService.selectRtnPm01(mjsonParams);
        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "",
                  notes = "")
    @PostMapping("/phone-api/cmpgn/result/lwprt-prtn-combo")
    public Object selectRtnHappyCallGroup(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnResultService.selectRtnHappyCallGroup(mjsonParams);
        return objRetParams;
    }

}
