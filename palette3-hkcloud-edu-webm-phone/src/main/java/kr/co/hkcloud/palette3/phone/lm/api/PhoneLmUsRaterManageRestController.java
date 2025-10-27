package kr.co.hkcloud.palette3.phone.lm.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.lm.app.PhoneLmUsRaterManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController("PhoneLmUsRaterManageRestController")
@Api(value = "PhoneLmUsRaterManageRestController",
     description = "LMS담당자관리 REST 컨트롤러")
public class PhoneLmUsRaterManageRestController
{
    private final PhoneLmUsRaterManageService phoneLmUsRaterManageService;


    @ApiOperation(value = "LMS평가 LMS담당자 리스트 조회",
                  notes = "LMS평가 LMS담당자 리스트 조회")
    @PostMapping("/phone-api/lm/rater-lm-manage/rater/list")
    Object selectRtnLmUsRater(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject());
        objRetParams = phoneLmUsRaterManageService.selectRtnLmUsRater(jsonParams);
        return objRetParams;
    }


    @ApiOperation(value = "LMS평가 관리자 리스트 조회",
                  notes = "LMS평가 관리자 리스트 조회")
    @PostMapping("/phone-api/lm/rater-lm-manage/mngr/list")
    Object selectRtnMngrUser(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject());
        objRetParams = phoneLmUsRaterManageService.selectRtnMngrUser(jsonParams);
        return objRetParams;
    }


    @ApiOperation(value = "LMS평가 담당자 등록",
                  notes = "LMS평가 담당자 등록")
    @PostMapping("/phone-api/lm/rater-lm-manage/regist")
    Object processRtnLmUsRater(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneLmUsRaterManageService.processRtnLmUsRater(mjsonParams);

        return objRetParams;
    }
}
