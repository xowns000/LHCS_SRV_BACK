package kr.co.hkcloud.palette3.phone.qa2.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.qa2.app.PhoneQAUsRaterManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController("PhoneQAUsRaterManageRestController")
@Api(value = "PhoneQAUsRaterManageRestController",
     description = "QA담당자관리 REST 컨트롤러")
public class PhoneQAUsRaterManageRestController
{

    private final PhoneQAUsRaterManageService phoneQAUsRaterManageService;
    private final InnbCreatCmmnService        innbCreatCmmnService;


    @ApiOperation(value = "QA평가 QAA담당자 리스트 조회",
                  notes = "QA평가 QAA담당자 리스트 조회")
    @PostMapping("/phone-api/qa2/rater-qa-manage/rater/list")
    Object selectRtnQaUsRater(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject());
        objRetParams = phoneQAUsRaterManageService.selectRtnQaUsRater(jsonParams);
        return objRetParams;
    }


    @ApiOperation(value = "QA평가 관리자 리스트 조회",
                  notes = "QA평가 관리자 리스트 조회")
    @PostMapping("/phone-api/qa2/rater-qa-manage/mngr/list")
    Object selectRtnMngrUser(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject());
        objRetParams = phoneQAUsRaterManageService.selectRtnMngrUser(jsonParams);
        return objRetParams;
    }


    @ApiOperation(value = "QA평가 담당자 등록",
                  notes = "QA평가 담당자 등록")
    @PostMapping("/phone-api/qa2/rater-qa-manage/regist")
    Object processRtnQaUsRater(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneQAUsRaterManageService.processRtnQaUsRater(mjsonParams);

        return objRetParams;
    }
}
