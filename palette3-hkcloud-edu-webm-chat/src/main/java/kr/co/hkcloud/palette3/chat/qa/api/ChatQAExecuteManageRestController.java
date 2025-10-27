package kr.co.hkcloud.palette3.chat.qa.api;


import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.chat.qa.app.ChatQAExecuteManageService;
import kr.co.hkcloud.palette3.chat.qa.app.ChatQATargetManageService;
import kr.co.hkcloud.palette3.chat.qa.util.ChatQAExecuteManageValidator;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "ChatQAExecuteManageRestController",
     description = "QA평가실행관리 서비스콜 REST 컨트롤러")
public class ChatQAExecuteManageRestController
{
    private final ChatQATargetManageService    chatQATargetManageService;
    private final ChatQAExecuteManageService   chatQAExecuteManageService;
    private final ChatQAExecuteManageValidator chatQAExecuteManageValidator;


    /**
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "QA 대상자 현황 게시판 조회",
                  notes = "QA 대상자 현황 게시판 조회")
    @PostMapping("/chat-api/qa/execut-manage/qa-trgter/inqire")
    public Object selectRtnQABul(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatQAExecuteManageService.selectRtnQABul(jsonParams);

        return objRetParams;
    }


    /**
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "QA 대상자 현황 전체 조회",
                  notes = "QA 대상자 현황 전체 조회")
    @PostMapping("/chat-api/qa/execut-manage/qa-trgter-all/inqire")
    public Object selectRtnQA(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatQAExecuteManageService.selectRtnQA(jsonParams);

        return objRetParams;
    }


    /**
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "QA평가 여부 갯수",
                  notes = "QA평가 여부 갯수")
    @PostMapping("/chat-api/qa/execut-manage/evl-col/inqire")
    public Object selectRtnQaEvalCnt(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatQAExecuteManageService.selectRtnQaEvalCnt(jsonParams);

        return objRetParams;
    }


    /**
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "QA 평가 대상자 현황 (상담톡) 조회 ",
                  notes = "QA 평가 대상자 현황 (상담톡) 조회 ")
    @PostMapping("/chat-api/qa/execut-manage/qa-trgter-list/inqire")
    public Object selectRtnQAIN(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatQAExecuteManageService.selectRtnQAIN(jsonParams);

        return objRetParams;
    }


    /**
     * 평가마감
     * 
     * @param  mjsonParams
     * @param  result
     * @return                     TelewebJSON 형식의 처리결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "평가마감",
                  notes = "평가마감")
    @PostMapping("/chat-api/qa/execut-manage/evl-clos/inqire")
    public Object processRtnEvaluationClose(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        // 반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        String QA_YM = mjsonParams.getString("QA_YM");
        String QA_TY_CD = mjsonParams.getString("QA_TY_CD");
        String QA_SEQ = mjsonParams.getString("QA_SEQ");
        String SESSION_ID = mjsonParams.getString("SESSION_ID");

        // 평가 키값 validation 체크
        chatQAExecuteManageValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        // 평가되지 않은 건 수를 반환한다.
        if(getNotEvaluatedCount(mjsonParams, QA_YM, QA_TY_CD, QA_SEQ) > 0) {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "평가되지 않은 건이 존재합니다.");
            objRetParams.setHeader("ERROR_TYPE", "W");
        }
        else {
            objRetParams = updateRtnEvaluationClose(mjsonParams, QA_YM, QA_TY_CD, QA_SEQ, SESSION_ID);
        }

        // 최종결과값 반환
        return objRetParams;
    }


    @ApiOperation(value = "QA평가마감취소",
                  notes = "QA평가마감취소")
    @PostMapping("/chat-api/qa/evl-execut-manage/clos-cancl/modify")
    public Object processRtnCancel(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject());
        objRetParams = chatQAExecuteManageService.processRtnCancel(mjsonParams);

        return objRetParams;
    }


    /**
     * 평가되지 않은 건수를 반환한다.
     * 
     * @param  jsonParams
     * @param  QA_YM
     * @param  QA_TY_CD
     * @param  QA_SEQ
     * @return            int(평가된 건수)
     */
    private int getNotEvaluatedCount(TelewebJSON jsonParams, String QA_YM, String QA_TY_CD, String QA_SEQ) throws TelewebApiException
    {
        jsonParams.setString("QA_YM", QA_YM);
        jsonParams.setString("QA_TY_CD", QA_TY_CD);
        jsonParams.setString("QA_SEQ", QA_SEQ);

        return chatQATargetManageService.selectNotEvaluatedCount(jsonParams).getInt("NOT_EVALUATED_COUNT");
    }


    /**
     * 평가된 건을 마감처리 한다.
     * 
     * @param  jsonParams
     * @param  QA_YM
     * @param  QA_TY_CD
     * @param  QA_SEQ
     * @return            int(평가된 건수)
     */
    private TelewebJSON updateRtnEvaluationClose(TelewebJSON jsonParams, String QA_YM, String QA_TY_CD, String QA_SEQ, String SESSION_ID) throws TelewebApiException
    {

        jsonParams.setString("QA_YM", QA_YM);
        jsonParams.setString("QA_TY_CD", QA_TY_CD);
        jsonParams.setString("QA_SEQ", QA_SEQ);
        jsonParams.setString("AMDR_ID", SESSION_ID);

        return chatQATargetManageService.updateRtnEvaluationClose(jsonParams);
    }

}
