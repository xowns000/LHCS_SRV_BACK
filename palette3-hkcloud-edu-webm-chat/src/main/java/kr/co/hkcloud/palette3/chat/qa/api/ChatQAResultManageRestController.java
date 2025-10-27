package kr.co.hkcloud.palette3.chat.qa.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.chat.qa.app.ChatQAExecuteManageService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "ChatQAResultManageRestController",
     description = "QA결과관리 REST 컨트롤러")
public class ChatQAResultManageRestController
{
    private final ChatQAExecuteManageService chatQAExecuteManageService;


    /**
     * twb.TwbQam03.selectRtnQAIN
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "QA 평가 대상자 현황 (상담톡) 조회 ",
                  notes = "QA 평가 대상자 현황 (상담톡) 조회 ")
    @PostMapping("/chat-api/qa/result-manage/qa-trgter-list/inqire")
    public Object selectRtnQAIN(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatQAExecuteManageService.selectRtnQAIN(jsonParams);

        return objRetParams;
    }


    /**
     * twb.TwbQam03.selectRtnQABul
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "QA 대상자 현황 게시판 조회",
                  notes = "QA 대상자 현황 게시판 조회")
    @PostMapping("/chat-api/qa/result-manage/qa-trgter/inqire")
    public Object selectRtnQABul(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatQAExecuteManageService.selectRtnQABul(jsonParams);

        return objRetParams;
    }


    /**
     * twb.TwbQam03.selectRtnQA
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "QA 대상자 현황 전체 조회",
                  notes = "QA 대상자 현황 전체 조회")
    @PostMapping("/chat-api/qa/result-manage/qa-trgter-all/inqire")
    public Object selectRtnQA(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatQAExecuteManageService.selectRtnQA(jsonParams);

        return objRetParams;
    }

}
