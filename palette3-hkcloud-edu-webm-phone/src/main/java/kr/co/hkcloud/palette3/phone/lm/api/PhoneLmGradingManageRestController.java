package kr.co.hkcloud.palette3.phone.lm.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.lm.app.PhoneLmGradingManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController("PhoneLmGradingManageRestController")
@Api(value = "PhoneLmGradingManageRestController",
     description = "LM시험채점 서비스콜 REST 컨트롤러")
public class PhoneLmGradingManageRestController
{
    private final PhoneLmGradingManageService phoneLmGradingManageService;


    @ApiOperation(value = "LM시험 목록 조회",
                  notes = "LM시험 목록 조회")
    @PostMapping("/phone-api/lm/grading/list")
    Object selectRtnLmGrading(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON adminYn = new TelewebJSON();

        adminYn = phoneLmGradingManageService.selectRtnAdminYn(mjsonParams);

        if(adminYn != null && adminYn.getString("ADMIN_YN") != null) {
            mjsonParams.setString("ADMIN_YN", adminYn.getString("ADMIN_YN"));
        }
        else {
            mjsonParams.setString("ADMIN_YN", "N");
        }
        objRetParams = phoneLmGradingManageService.selectRtnLmGrading(mjsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "LM시험 채점 목록 조회",
                  notes = "LM시험 채점 목록 조회")
    @PostMapping("/phone-api/lm/grading/detail/inqire")
    Object selectRtnLmGradingDetail(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneLmGradingManageService.selectRtnLmGradingDetail(mjsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "LM시험 객관식 채점",
                  notes = "LM시험 객관식 채점")
    @PostMapping("/phone-api/lm/grading/multi-choice/regist")
    Object updateRtnLmGradingMultiChoice(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneLmGradingManageService.updateRtnLmGradingMultiChoice(mjsonParams);

        return objRetParams;

    }


    @ApiOperation(value = "LM시험 사용자 답변정보 조회",
                  notes = "LM시험 사용자 답변정보 조회")
    @PostMapping("/phone-api/lm/grading/user-ans/inqire")
    Object selectRtnLmUserAns(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneLmGradingManageService.selectRtnLmUserAns(mjsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "LM시험 채점완료",
                  notes = "LM시험 채점완료")
    @PostMapping("/phone-api/lm/grading/regist")
    Object processRtnLmGrading(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneLmGradingManageService.processRtnLmGrading(mjsonParams);

        return objRetParams;
    }
}
