package kr.co.hkcloud.palette3.phone.qa.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.qa.app.PhoneQAResultManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController("ResultManageRestController")
@Api(value = "ResultManageRestController",
     description = "QA결과관리 REST 컨트롤러")
public class PhoneQAResultManageRestController
{
    private final PhoneQAResultManageService ResultManageService;


    /**
     * twb.ResultManage.selectRtnQaEvalResult
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "QA평가지 조회",
                  notes = "QA평가지 조회")
    @PostMapping("/phone-api/qa/result-manage/evl-paper/inqire")
    public Object selectRtnEvSheet(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject());
        objRetParams = ResultManageService.selectRtnEvSheet(jsonParams);
        return objRetParams;
    }


    /**
     * twb.ResultManage.selectRtnQaEvalResult
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "전화QA결과관리/QA결과/조회",
                  notes = "전화QA결과관리/QA결과/조회")
    @PostMapping("/phone-api/qa/result-manage/qa-result/inqire")
    public Object selectRtnQaEvalResult(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject());
        objRetParams = ResultManageService.selectRtn(jsonParams);
        return objRetParams;
    }
}
