package kr.co.hkcloud.palette3.phone.qa.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.qa.app.PhoneQAEvlExecutManageExecutEvlPopupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController("EvlExecutManagePopupRestController")
@Api(value = "EvlExecutManagePopupRestController",
     description = "QA실행 REST 컨트롤러")
public class PhoneQAEvlExecutManageExecutEvlPopupRestController
{
    private final PhoneQAEvlExecutManageExecutEvlPopupService EvlExecutManagePopupService;


    @ApiOperation(value = "QA평가지조회",
                  notes = "QA평가지조회")
    @PostMapping("/phone-api/qa/evl-execut-manage/execut-evl-popup/evl-paper/inqire")
    public Object selectRtnEvSheet(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject());
        objRetParams = EvlExecutManagePopupService.selectRtnEvSheet(jsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "QA상담이력상세조회",
                  notes = "QA상담이력상세조회")
    @PostMapping("/phone-api/qa/evl-execut-manage/execut-evl-popup/cnslt-hist/detail")
    public Object selectRtnDetails(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject());
        objRetParams = EvlExecutManagePopupService.selectRtnDetails(jsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "QA결과등록",
                  notes = "QA결과등록")
    @PostMapping("/phone-api/qa/evl-execut-manage/execut-evl-popup/qa-result/regist")
    public Object processRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject());
        objRetParams = EvlExecutManagePopupService.processRtn(jsonParams);

        return objRetParams;
    }

}
