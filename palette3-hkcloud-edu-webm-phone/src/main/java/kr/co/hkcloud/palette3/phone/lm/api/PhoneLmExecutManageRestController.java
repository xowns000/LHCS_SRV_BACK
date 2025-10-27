package kr.co.hkcloud.palette3.phone.lm.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.lm.app.PhoneLmExecutManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController("PhoneLmExecutManageRestController")
@Api(value = "PhoneLmExecutManageRestController",
     description = "LM시험실행/결과 서비스콜 REST 컨트롤러")
public class PhoneLmExecutManageRestController
{
    private final PhoneLmExecutManageService phoneLmExecutManageService;


    @ApiOperation(value = "LM실형결과 목록 조회",
                  notes = "LM실행결과 목록 조회")
    @PostMapping("/phone-api/lm/execut/list")
    Object selectRtnExecut(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneLmExecutManageService.selectRtnLmExecut(mjsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "LM실행결과 시험 상세 조회",
                  notes = "LM실행결과 시험 상세 조회")
    @PostMapping("/phone-api/lm/execut/detail/inqire")
    Object selectRtnLmDetail(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneLmExecutManageService.selectRtnLmDetail(mjsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "LM실행결과 시험지 조회",
                  notes = "LM실행결과 시험지 조회")
    @PostMapping("/phone-api/lm/execut/eva-paper/inqire")
    Object selectRtnLmEvaPaper(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneLmExecutManageService.selectRtnLmEvaPaper(mjsonParams);

        return objRetParams;

    }


    @ApiOperation(value = "LM실행결과 시험 제출",
                  notes = "LM실행결과 시험 제출")
    @PostMapping("/phone-api/lm/execut/regist")
    Object processRtnExecut(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneLmExecutManageService.processRtnExecut(mjsonParams);

        return objRetParams;
    }
}
