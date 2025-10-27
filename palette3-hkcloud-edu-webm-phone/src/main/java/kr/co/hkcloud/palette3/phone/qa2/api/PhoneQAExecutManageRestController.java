package kr.co.hkcloud.palette3.phone.qa2.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.qa2.app.PhoneQAExecutManageService;
import kr.co.hkcloud.palette3.phone.qa2.app.PhoneQAManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController("PhoneQAExecutManageRestController")
@Api(value = "PhoneQAExecutManageRestController",
     description = "QA평가실행/결과 REST 컨트롤러")
public class PhoneQAExecutManageRestController
{

    private final PhoneQAExecutManageService phoneQAExecutManageService;
    private final PhoneQAManageService       phoneQAManageService;


    @ApiOperation(value = "QA평가 실행 목록",
                  notes = "QA평가 실행 목록")
    @PostMapping("/phone-api/qa2/qa-execut/list")
    Object selectRtnExecutList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON manageYn = new TelewebJSON(mjsonParams);
        TelewebJSON adminYn = new TelewebJSON(mjsonParams);
        manageYn = phoneQAExecutManageService.selectRtnQaManageYn(mjsonParams);
        mjsonParams.setString("QA_MANAGE_YN", manageYn.getString("QA_MANAGE_YN"));
        adminYn = phoneQAExecutManageService.selectRtnAdminYn(mjsonParams);
        mjsonParams.setString("ADMIN_YN", adminYn.getString("ADMIN_YN"));
        objRetParams = phoneQAExecutManageService.selectRtnExecutList(mjsonParams);
        if(adminYn.getString("ADMIN_YN").equals("Y")) {
            objRetParams.setString("QA_MANAGE_YN", "Y");
        }
        else {
            objRetParams.setString("QA_MANAGE_YN", manageYn.getString("QA_MANAGE_YN"));

        }
        return objRetParams;
    }


    @ApiOperation(value = "QA평가 평가표 조회",
                  notes = "QA평가 평가표 조회")
    @PostMapping("/phone-api/qa2/qa-execut/qa-qs/list")
    Object selectRtnExecutQaQs(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneQAExecutManageService.selectRtnQaDataRst(mjsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "QA평가 평가결과 등록",
                  notes = "QA평가 평가결과 등록")
    @PostMapping("/phone-api/qa2/qa-execut/regist")
    Object processRtnExecut(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneQAExecutManageService.processRtnQaDataRst(mjsonParams);

        return objRetParams;
    }

}
