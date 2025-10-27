package kr.co.hkcloud.palette3.phone.cmpgn.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.cmpgn.app.PhoneCmpgnAsgnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "phoneCmpgnAsgnRestController",
     description = "캠페인할당 서비스콜 REST 컨트롤러")
public class PhoneCmpgnAsgnRestController
{
    private final PhoneCmpgnAsgnService phoneCmpgnAsgnService;


    /**
     * 캠페인 기본 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "캠페인 기본 조회",
                  notes = "캠페인 기본 조회")
    @PostMapping("/phone-api/cmpgn/asgn/list")
    public Object selectRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnAsgnService.selectRtn(mjsonParams);
        return objRetParams;
    }


    /**
     * 캠페인 데이타 조회
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "캠페인 데이타 조회",
                  notes = "캠페인 데이타 조회")
    @PostMapping("/phone-api/cmpgn/asgn/data-list")
    public Object selectRtnData(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnAsgnService.selectRtnData(mjsonParams);
        return objRetParams;
    }


    /**
     * 캠페인 미할당 상담원 조회
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "캠페인 미할당 상담원 조회",
                  notes = "캠페인 미할당 상담원 조회")
    @PostMapping("/phone-api/cmpgn/asgn/noasgn-cnslr-list")
    public Object selectRtnUser(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnAsgnService.selectRtnUser(mjsonParams);
        return objRetParams;
    }


    /**
     * 캠페인 할당 상담원 조회
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "캠페인 할당 상담원 조회",
                  notes = "캠페인 할당 상담원 조회")
    @PostMapping("/phone-api/cmpgn/asgn/asgn-cnslr-list")
    public Object selectRtnAllc(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnAsgnService.selectRtnAllc(mjsonParams);
        return objRetParams;
    }


    /**
     * 캠페인 프로젝트별 데이타 조회
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "캠페인 프로젝트별 데이타 조회",
                  notes = "캠페인 프로젝트별 데이타 조회")
    @PostMapping("/phone-api/cmpgn/asgn/prjct-data-list")
    public Object selectRtnPrjNo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnAsgnService.selectRtnPrjNo(mjsonParams);
        return objRetParams;
    }


    /**
     * 캠페인 대상_리스트 조회
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "캠페인 대상_리스트  조회",
                  notes = "캠페인 대상_리스트  조회")
    @PostMapping("/phone-api/cmpgn/asgn/trget-list")
    public Object selectRtnInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnAsgnService.selectRtnInfo(mjsonParams);
        return objRetParams;
    }


    /**
     * 캠페인 할당 데이터 저장 처리
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "캠페인 할당 데이터 저장 처리",
                  notes = "캠페인 할당 데이터 저장 처리")
    @PostMapping("/phone-api/cmpgn/asgn/asgn-data-stre-process")
    public Object insertRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnAsgnService.insertRtn(mjsonParams);
        return objRetParams;
    }


    /**
     * 캠페인할당 실행
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "캠페인할당 실행",
                  notes = "캠페인할당 실행")
    @PostMapping("/phone-api/cmpgn/asgn/asgn-execut")
    public Object updateRtn1(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnAsgnService.updateRtn1(mjsonParams);
        return objRetParams;
    }


    /**
     * 캠페인할당 실행 프로젝트별
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "캠페인할당 실행 프로젝트별",
                  notes = "캠페인할당 실행 프로젝트별")
    @PostMapping("/phone-api/cmpgn/asgn/prjct-asgn-execut")
    public Object updateRtn2(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnAsgnService.updateRtn2(mjsonParams);
        return objRetParams;
    }


    /**
     * 캠페인할당 회수
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "캠페인할당 회수",
                  notes = "캠페인할당 회수")
    @PostMapping("/phone-api/cmpgn/asgn/asgn-rtrvl")
    public Object updateRtn3(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnAsgnService.updateRtn3(mjsonParams);
        return objRetParams;
    }


    /**
     * 캠페인 데이터 삭제 처리
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "캠페인 데이터 삭제 처리",
                  notes = "캠페인 데이터 삭제 처리")
    @PostMapping("/phone-api/cmpgn/asgn/data-delete-process")
    public Object deleteRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCmpgnAsgnService.deleteRtn(mjsonParams);
        return objRetParams;
    }
}
