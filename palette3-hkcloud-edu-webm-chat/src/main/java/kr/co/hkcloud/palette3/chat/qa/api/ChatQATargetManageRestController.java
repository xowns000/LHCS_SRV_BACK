package kr.co.hkcloud.palette3.chat.qa.api;


import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.chat.qa.app.ChatQATargetManageService;
import kr.co.hkcloud.palette3.chat.qa.util.ChatQATargetManageValidator;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "ChatQATargetManageRestController",
     description = "QA대상등록관리 REST 컨트롤러")
public class ChatQATargetManageRestController
{
    private final InnbCreatCmmnService        innbCreatCmmnService;
    private final ChatQATargetManageService   chatQATargetManageService;
    private final ChatQATargetManageValidator chatQATargetManageValidator;


    /**
     * 메인 리스트 조회
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "메인 리스트 조회",
                  notes = "메인 리스트 조회")
    @PostMapping("/chat-api/qa/trget-regist-manage/list")
    public Object selectRtnMainList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        // 반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        // DAO검색 메서드 호출
        objRetParams = chatQATargetManageService.selectRtnMainList(mjsonParams);

        // 최종결과값 반환
        return objRetParams;
    }


    /**
     * 기등록 건수 조회
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "기등록 건수 조회",
                  notes = "기등록 건수 조회")
    @PostMapping("/chat-api/qa/trget-regist-manage/inqire-co/inqire")
    public Object selectRtnPreExtractedCount(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        // 반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        // DAO검색 메서드 호출
        objRetParams = chatQATargetManageService.selectRtnPreExtractedCount(mjsonParams);
        // 최종결과값 반환
        return objRetParams;
    }


    /**
     * 등록
     * 
     * @param  mjsonParams
     * @param  result
     * @return                     TelewebJSON 형식의 처리결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "등록",
                  notes = "등록")
    @PostMapping("/chat-api/qa/trget-regist-manage/regist")
    public Object processRtnExtractTarget(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        // 반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        String QA_YM = mjsonParams.getString("QA_YM");
        String QA_TY_CD = mjsonParams.getString("QA_TY_CD");
        String QA_SEQ = mjsonParams.getString("QA_SEQ");
        String SESSION_ID = mjsonParams.getString("SESSION_ID");

        // 평가 키값 validation 체크
        chatQATargetManageValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        // 등록마감 건이 존재하는지 확인한다.
        if(haveExtractClose(mjsonParams, QA_YM, QA_TY_CD, QA_SEQ)) {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "이미 등록마감 건이 존재합니다.");
            objRetParams.setHeader("ERROR_TYPE", "W");
        }
        else {
            TelewebJSON objRetParams2 = new TelewebJSON(mjsonParams);

            // DAO검색 메서드 호출 ( 목록조회 속도 영향으로 영업시간 추출 별도로직 분리 )
            objRetParams2 = chatQATargetManageService.processRtnExtractTarget(mjsonParams);

            mjsonParams.setString("WORK_ARRAY", objRetParams2.getDataJSON());

            // 추출 타겟 조회
            JSONArray arrExtractRemove = mjsonParams.getDataObject("EXTRACT_REMOVE");

            // 추출 타겟 등록
            objRetParams = insertRtnExtractTarget(mjsonParams, QA_YM, QA_TY_CD, QA_SEQ, SESSION_ID, arrExtractRemove); // 체크한
                                                                                                                      // 행으로
                                                                                                                      // 등록
        }

        // 최종결과값 반환
        return objRetParams;
    }


    /**
     * 등록마감
     * 
     * @param  mjsonParams
     * @param  result
     * @return                     TelewebJSON 형식의 처리결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "등록마감",
                  notes = "등록마감")
    @PostMapping("/chat-api/qa/trget-regist-manage/regist-clos/modify")
    public Object processRtnExtractClose(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        // 반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        String QA_YM = mjsonParams.getString("QA_YM");
        String QA_TY_CD = mjsonParams.getString("QA_TY_CD");
        String QA_SEQ = mjsonParams.getString("QA_SEQ");
        String SESSION_ID = mjsonParams.getString("SESSION_ID");

        // 평가 키값 validation 체크
        chatQATargetManageValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        // 등록마감 건이 존재하는지 확인한다.
        if(haveExtractClose(mjsonParams, QA_YM, QA_TY_CD, QA_SEQ)) {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "이미 등록마감 건이 존재합니다.");
            objRetParams.setHeader("ERROR_TYPE", "W");
        }
        else {
            objRetParams = updateRtnExtractClose(mjsonParams, QA_YM, QA_TY_CD, QA_SEQ, SESSION_ID);
        }

        // 최종결과값 반환
        return objRetParams;
    }


    /**
     * 등록초기화
     * 
     * @param  mjsonParams
     * @param  result
     * @return                     TelewebJSON 형식의 처리결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "등록초기화",
                  notes = "등록초기화")
    @PostMapping("/chat-api/qa/trget-regist-manage/delete")
    public Object deleteRtnExtractReset(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        // 반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        String QA_YM = mjsonParams.getString("QA_YM");
        String QA_TY_CD = mjsonParams.getString("QA_TY_CD");
        String QA_SEQ = mjsonParams.getString("QA_SEQ");

        // 평가 키값 validation 체크
        chatQATargetManageValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        // 평가된 건이 존재하는지 확인한다.
        if(haveEvaluated(mjsonParams, QA_YM, QA_TY_CD, QA_SEQ)) {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "이미 평가된 건이 존재합니다.");
            objRetParams.setHeader("ERROR_TYPE", "W");
        }
        else {
            objRetParams = deleteRtnExtractReset(mjsonParams, QA_YM, QA_TY_CD, QA_SEQ);
        }

        // 최종결과값 반환
        return objRetParams;
    }


    /**
     * 등록제거
     * 
     * @param  mjsonParams
     * @param  result
     * @return                     TelewebJSON 형식의 처리결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "등록제거",
                  notes = "등록제거")
    @PostMapping("/chat-api/qa/trget-regist-manage/regist/delete")
    public Object deleteRtnExtractRemove(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        // 반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        String QA_YM = mjsonParams.getString("QA_YM");
        String QA_TY_CD = mjsonParams.getString("QA_TY_CD");
        String QA_SEQ = mjsonParams.getString("QA_SEQ");

        // 평가 키값 validation 체크
        chatQATargetManageValidator.validate(mjsonParams, result);

        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        // 평가된 건이 존재하는지 확인한다.
        if(haveEvaluated(mjsonParams, QA_YM, QA_TY_CD, QA_SEQ)) {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "이미 평가된 건이 존재합니다.");
            objRetParams.setHeader("ERROR_TYPE", "W");
        }
        else {
            JSONArray arrExtractRemove = mjsonParams.getDataObject("EXTRACT_REMOVE");

            objRetParams = deleteRtnExtractRemove(mjsonParams, arrExtractRemove);
        }

        // 최종결과값 반환
        return objRetParams;
    }


    /**
     * 평가할 대상으로 등록한다.
     * 
     * @param  jsonParams
     * @param  QA_YM
     * @param  QA_TY_CD
     * @param  QA_SEQ
     * @param  SESSION_ID
     * @param  arrExtractTarget
     * @return                  TelewebJSON 형식의 처리결과 데이터
     */
    private TelewebJSON insertRtnExtractTarget(TelewebJSON jsonParams, String QA_YM, String QA_TY_CD, String QA_SEQ, String SESSION_ID, JSONArray arrExtractTarget) throws TelewebApiException
    {
        TelewebJSON objRetJson = new TelewebJSON(jsonParams);

        int index = 0;

        for(Object objTemp : arrExtractTarget) {
            JSONObject objExtractTarget = (JSONObject) objTemp;

            jsonParams.setString("QA_YM", QA_YM);
            jsonParams.setString("QA_TY_CD", QA_TY_CD);
            jsonParams.setString("QA_SEQ", QA_SEQ);
            jsonParams.setString("USER_ID", objExtractTarget.getString("USER_ID"));
            jsonParams.setString("TALK_CONTACT_ID", objExtractTarget.getString("TALK_CONTACT_ID"));
            jsonParams.setString("QA_EXT_CHK", "Y");
            jsonParams.setString("REGR_ID", SESSION_ID);
            jsonParams.setString("AMDR_ID", SESSION_ID);
            jsonParams.setString("SEQNo", innbCreatCmmnService.getSeqNo("PLT_CHT_QA_EVAL_SEQ"));

            index += chatQATargetManageService.insertRtnExtractTarget(jsonParams).getHeaderInt("TOT_COUNT");
        }

        objRetJson.setHeader("TOT_COUNT", index);
        objRetJson.setHeader("COUNT", index);
        objRetJson.setHeader("ERROR_FLAG", false);
        if(index > 0) {
            objRetJson.setHeader("ERROR_MSG", "정상 입력 되었습니다.");
        }
        else {
            objRetJson.setHeader("ERROR_MSG", "입력 데이터가 존재 하지 않습니다.");
        }

        return objRetJson;
    }


    /**
     * 평가 대상으로 등록된 건을 등록 마감처리 한다.
     * 
     * @param  jsonParams
     * @param  QA_YM
     * @param  QA_TY_CD
     * @param  QA_SEQ
     * @param  SESSION_ID
     * @return            TelewebJSON 형식의 처리결과 데이터
     */
    private TelewebJSON updateRtnExtractClose(TelewebJSON jsonParams, String QA_YM, String QA_TY_CD, String QA_SEQ, String SESSION_ID) throws TelewebApiException
    {
        jsonParams.setString("QA_YM", QA_YM);
        jsonParams.setString("QA_TY_CD", QA_TY_CD);
        jsonParams.setString("QA_SEQ", QA_SEQ);
        jsonParams.setString("AMDR_ID", SESSION_ID);

        return chatQATargetManageService.updateRtnExtractClose(jsonParams);
    }


    /**
     * 평가할 대상으로 등록한 건들을 등록 초기화한다.
     * 
     * @param  jsonParams
     * @param  QA_YM
     * @param  QA_TY_CD
     * @param  QA_SEQ
     * @return            TelewebJSON 형식의 처리결과 데이터
     */
    private TelewebJSON deleteRtnExtractReset(TelewebJSON jsonParams, String QA_YM, String QA_TY_CD, String QA_SEQ) throws TelewebApiException
    {
        jsonParams.setString("QA_YM", QA_YM);
        jsonParams.setString("QA_TY_CD", QA_TY_CD);
        jsonParams.setString("QA_SEQ", QA_SEQ);

        return chatQATargetManageService.deleteRtnExtractReset(jsonParams);
    }


    private TelewebJSON deleteRtnExtractRemove(TelewebJSON jsonParams, JSONArray arrExtractRemove) throws TelewebApiException
    {
        TelewebJSON objRetJson = new TelewebJSON(jsonParams);

        int index = 0;

        for(Object objTemp : arrExtractRemove) {
            JSONObject objExtractRemove = (JSONObject) objTemp;

            // TelewebJSON jsonDelete = new TelewebJSON(jsonParams);
            jsonParams.setString("QA_YM", objExtractRemove.getString("QM_QA_YM"));
            jsonParams.setString("QA_TY_CD", objExtractRemove.getString("QM_QA_TY_CD"));
            jsonParams.setString("QA_SEQ", objExtractRemove.getString("QM_QA_SEQ"));
            jsonParams.setString("TALK_CONTACT_ID", objExtractRemove.getString("QM_TALK_CONTACT_ID"));
            jsonParams.setString("USER_ID", objExtractRemove.getString("QM_USER_ID"));

            index += chatQATargetManageService.deleteRtnExtractRemove(jsonParams).getHeaderInt("TOT_COUNT");
        }

        objRetJson.setHeader("TOT_COUNT", index);
        objRetJson.setHeader("COUNT", index);
        objRetJson.setHeader("ERROR_FLAG", false);
        if(index > 0) {
            objRetJson.setHeader("ERROR_MSG", "정상 삭제 되었습니다.");
        }
        else {
            objRetJson.setHeader("ERROR_MSG", "삭제 데이터가 존재 하지 않습니다.");
        }

        return objRetJson;
    }


    /**
     * 등록마감 건이 존재하는지 확인한다.
     * 
     * @param  jsonParams
     * @param  QA_YM
     * @param  QA_TY_CD
     * @param  QA_SEQ
     * @return            true/false(true: 평가된 건이 존재함)
     */
    private boolean haveExtractClose(TelewebJSON jsonParams, String QA_YM, String QA_TY_CD, String QA_SEQ) throws TelewebApiException
    {
        jsonParams.setString("QA_YM", QA_YM);
        jsonParams.setString("QA_TY_CD", QA_TY_CD);
        jsonParams.setString("QA_SEQ", QA_SEQ);

        String strHaveExtractClose = chatQATargetManageService.selectHaveExtractClose(jsonParams).getString("HAVE_EXTRACT_CLOSE");
        if(StringUtils.equals(strHaveExtractClose, "Y")) {
            return true;
        }
        else {
            return false;
        }
    }


    /**
     * 평가된 건이 존재하는지 확인한다.
     * 
     * @param  jsonParams
     * @param  QA_YM
     * @param  QA_TY_CD
     * @param  QA_SEQ
     * @return            true/false(true: 평가된 건이 존재함)
     */
    private boolean haveEvaluated(TelewebJSON jsonParams, String QA_YM, String QA_TY_CD, String QA_SEQ) throws TelewebApiException
    {
        jsonParams.setString("QA_YM", QA_YM);
        jsonParams.setString("QA_TY_CD", QA_TY_CD);
        jsonParams.setString("QA_SEQ", QA_SEQ);

        String strHaveEvaluatedYN = chatQATargetManageService.selectHaveEvaluatedYN(jsonParams).getString("HAVE_EVALUATED_YN");
        if(StringUtils.equals(strHaveEvaluatedYN, "Y")) {
            return true;
        }
        else {
            return false;
        }
    }

}
