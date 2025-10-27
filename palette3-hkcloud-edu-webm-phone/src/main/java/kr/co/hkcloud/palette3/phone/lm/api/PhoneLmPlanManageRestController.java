package kr.co.hkcloud.palette3.phone.lm.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.lm.app.PhoneLmPlanManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController("PhoneLmPlanManageRestController")
@Api(value = "PhoneLmPlanManageRestController",
     description = "LMS기획마법사 REST 컨트롤러")
public class PhoneLmPlanManageRestController
{
    private final PhoneLmPlanManageService phoneLmPlanManageService;


    @ApiOperation(value = "LMS평가기획 목록 조회",
                  notes = "LMS평가기획 목록 조회")
    @PostMapping("/phone-api/plan/lm-manage/list")
    Object selectRtnLm(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneLmPlanManageService.selectRtnLm(mjsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "LMS평가기획 등록",
                  notes = "LMS평가기획 등록")
    @PostMapping("/phone-api/plan/lm-manage/regist")
    Object insertRtnLm(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneLmPlanManageService.insertRtnLm(mjsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "LMS평가기획 평가지 조회",
                  notes = "LMS평가기획 평가지 조회")
    @PostMapping("/phone-api/plan/lm-manage/evl-paper/list")
    Object selectRtnEvlPaper(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneLmPlanManageService.selectRtnEvlPaper(mjsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "LMS평가기획 삭제",
                  notes = "LMS평가기획 삭제")
    @PostMapping("/phone-api/plan/lm-manage/delete")
    Object deleteRtnLm(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParam = new TelewebJSON(mjsonParams);

        objRetParams = phoneLmPlanManageService.deleteRtnLm(mjsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "LMS평가기획 추가정보 등록",
                  notes = "LMS평가기획 추가정보 등록")
    @PostMapping("/phone-api/plan/lm-manage/detail/regist")
    Object updateRtnLm(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneLmPlanManageService.updateRtnLm(mjsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "LMS평가기획 대상자발췌 사용자 목록",
                  notes = "LMS평가기획 대상자발췌 사용자 목록")
    @PostMapping("/phone-api/plan/lm-manage/user/list")
    Object selectRtnLmUser(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        log.debug("in LMS평가기획 대상자발췌 사용자 목록");
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParam = new TelewebJSON(mjsonParams);
        jsonParam.setDataObject(mjsonParams.getDataObject());

        objRetParams = phoneLmPlanManageService.selectRtnLmUser(jsonParam);

        return objRetParams;
    }


    @ApiOperation(value = "LMS평가기획 대상자발췌 대상자 목록",
                  notes = "LMS평가기획 대상자발췌 대상자 목록")
    @PostMapping("/phone-api/plan/lm-manage/data/list")
    Object selectRtnLmData(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParam = new TelewebJSON(mjsonParams);
        jsonParam.setDataObject(mjsonParams.getDataObject());

        objRetParams = phoneLmPlanManageService.selectRtnLmData(jsonParam);

        return objRetParams;
    }

}
