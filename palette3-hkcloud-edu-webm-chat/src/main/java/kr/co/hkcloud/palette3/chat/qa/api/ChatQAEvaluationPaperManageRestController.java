package kr.co.hkcloud.palette3.chat.qa.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.chat.qa.app.ChatQAEvaluationPaperManageService;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "ChatQAEvaluationPaperManageRestController",
     description = "QA평가지관리 서비스콜 REST 컨트롤러")
public class ChatQAEvaluationPaperManageRestController
{
    private final InnbCreatCmmnService               innbCreatCmmnService;
    private final ChatQAEvaluationPaperManageService chatQAEvaluationPaperManageService;


    /**
     * twb.ChatQAEvaluationPaperManage.selectRtnEvSheet
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "평가자관리 목록",
                  notes = "평가자관리 목록")
    @PostMapping("/chat-api/qa/evl-paper-manage/list")
    public Object selectRtnEvSheet(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatQAEvaluationPaperManageService.selectRtnEvSheet(jsonParams);

        return objRetParams;
    }


    /**
     * twb.ChatQAEvaluationPaperManage.selectChkQASeet
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "평가지관리/목록/조회",
                  notes = "평가지관리/목록/조회")
    @PostMapping("/chat-api/qa/evl-paper-manage-list/inqire")
    public Object selectChkQASeet(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatQAEvaluationPaperManageService.selectChkQASeet(jsonParams);

        return objRetParams;
    }


    /**
     * twb.ChatQAEvaluationPaperManage.updateRtnQASheet
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "평가지관리/수정",
                  notes = "평가지관리/수정")
    @PostMapping("/chat-api/qa/evl-paper-manage/modify")
    public Object updateRtnQASheet(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatQAEvaluationPaperManageService.updateRtnQASheet(jsonParams);

        return objRetParams;
    }


    /**
     * twb.ChatQAEvaluationPaperManage.insertRtnQASHEET
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "평가지관리/등록",
                  notes = "평가지관리/등록")
    @PostMapping("/chat-api/qa/evl-paper-manage/regist")
    public Object insertRtnQASHEET(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        jsonParams.setString("SEQNo", innbCreatCmmnService.getSeqNo("PLT_CHT_QA_EVAL_SHT_SEQ"));

        objRetParams = chatQAEvaluationPaperManageService.insertRtnQASHEET(jsonParams);

        return objRetParams;
    }


    /**
     * twb.ChatQAEvaluationPaperManage.insertRtnCopySheet
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "평가지관리/등록복사/등록",
                  notes = "평가지관리/등록복사/등록")
    @PostMapping("/chat-api/qa/evl-paper-manage/regist-copy/regist")
    public Object insertRtnCopySheet(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        jsonParams.setString("SEQNo", innbCreatCmmnService.getSeqNo("PLT_CHT_QA_EVAL_SHT_SEQ"));

        objRetParams = chatQAEvaluationPaperManageService.insertRtnCopySheet(jsonParams);

        return objRetParams;
    }


    /**
     * twb.ChatQAEvaluationPaperManage.deleteRtnQASheet
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "평가지관리/삭제",
                  notes = "평가지관리/삭제")
    @PostMapping("/chat-api/qa/evl-paper-manage/delete")
    public Object deleteRtnQASheet(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatQAEvaluationPaperManageService.deleteRtnQASheet(jsonParams);

        return objRetParams;
    }
}
