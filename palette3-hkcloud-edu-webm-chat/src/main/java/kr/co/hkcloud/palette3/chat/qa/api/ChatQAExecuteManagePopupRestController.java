package kr.co.hkcloud.palette3.chat.qa.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.chat.main.app.ChatMainService;
import kr.co.hkcloud.palette3.chat.qa.app.ChatQAExecuteManagePopupService;
import kr.co.hkcloud.palette3.chat.setting.util.ChatSettingBannedWordUtils;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.core.support.PaletteUserContextSupport;
import kr.co.hkcloud.palette3.core.util.PaletteDataFormatUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "ChatQAExecuteManagePopupRestController",
     description = "QA실행 REST 컨트롤러")
public class ChatQAExecuteManagePopupRestController
{
    private final InnbCreatCmmnService            innbCreatCmmnService;
    private final PaletteDataFormatUtils          paletteDataFormatUtils;
    private final ChatSettingBannedWordUtils      chatSettingBannedWordUtils;
    private final ChatQAExecuteManagePopupService chatQAExecuteManagePopupService;
    private final ChatMainService                 chatMainService;


    /**
     * QA 결과 저장
     * 
     * @param  Object HashMap. Key List : (HttpServletRequest)mobjRequest, (HttpServletResponse)mobjResponse, (TelewebJSON)mjsonParams, (String)strDBMS
     * @return        TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "QA 결과 등록",
                  notes = "QA 결과 등록")
    @PostMapping("/chat-api/qa/execut-manage/qa-execute-popup/result/regist")
    public Object processRtnQaRslt(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {

        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();

        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //DB Access 파라메터 생성
        TelewebJSON objParams = new TelewebJSON(mjsonParams);

        JSONArray objArry = mjsonParams.getDataObject();
        JSONObject objJson = new JSONObject();

        if(!objArry.isEmpty()) {
            boolean delYn = true;
            for(int i = 0; i < objArry.size(); i++) {
                objJson = objArry.getJSONObject(i);
                if(!objJson.isEmpty() && !objJson.isNullObject()) {
                    objParams.setDataObject(JSONArray.fromObject(objJson));
                    objParams.setString("CUSTCO_ID", custcoId);
                    objParams.setString("SEQNo", innbCreatCmmnService.getSeqNo("PLT_CHT_QA_EVAL_RST_SEQ"));

                    if(delYn) {
                        objRetParams = chatQAExecuteManagePopupService.deleteRtnQaRslt(objParams);
                        delYn = false;
                    }

                    objRetParams = chatQAExecuteManagePopupService.insertRtnQaRslt(objParams);
                }
            }
        }
        //최종결과값 반환
        return objRetParams;
    }


    /**
     * QA 결과 삭제
     * 
     * @param  Object HashMap. Key List : (HttpServletRequest)mobjRequest, (HttpServletResponse)mobjResponse, (TelewebJSON)mjsonParams, (String)strDBMS
     * @return        TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "QA 결과 삭제",
                  notes = "QA 결과 삭제")
    @PostMapping("/chat-api/qa/execut-manage/qa-execute-popup/result/delete")
    public Object deleteRtnQaRslt(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        mjsonParams.setString("CUSTCO_ID", custcoId);

        objRetParams = chatQAExecuteManagePopupService.deleteRtnQaRslt(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "QA 대상 평가 조회",
                  notes = "QA 대상 평가 조회")
    @PostMapping("/chat-api/qa/execut-manage/qa-execute-popup/list/inqire")
    public Object selectRtnQaEval(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {

        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        jsonParams.setString("CUSTCO_ID", custcoId);

        objRetParams = chatQAExecuteManagePopupService.selectRtnQaEval(jsonParams);

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "QA 평가 결과 마스터 존재여부 조회",
                  notes = "QA 평가 결과 마스터 존재여부 조회")
    @PostMapping("/chat-api/qa/execut-manage/qa-execute-popup/master-result/inqire")
    public Object selectChkQaRsltMst(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        jsonParams.setString("CUSTCO_ID", custcoId);

        objRetParams = chatQAExecuteManagePopupService.selectChkQaRsltMst(jsonParams);

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "QA 평가 결과 존재여부 조회",
                  notes = "QA 평가 결과 존재여부 조회")
    @PostMapping("/chat-api/qa/execut-manage/qa-execute-popup/result/inqire")
    public Object selectChkQaRslt(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        jsonParams.setString("CUSTCO_ID", custcoId);

        objRetParams = chatQAExecuteManagePopupService.selectChkQaRslt(jsonParams);

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "QA 대상 평가 수정",
                  notes = "QA 대상 평가 수정")
    @PostMapping("/chat-api/qa/execut-manage/qa-execute-popup/modify")
    public Object updateRtnQaRsltMst(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        jsonParams.setString("CUSTCO_ID", custcoId);

        objRetParams = chatQAExecuteManagePopupService.updateRtnQaRsltMst(jsonParams);

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "QA 대상 평가 등록",
                  notes = "QA 대상 평가 등록")
    @PostMapping("/chat-api/qa/execut-manage/qa-execute-popup/regist")
    public Object insertRtnQaRsltMst(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        jsonParams.setString("CUSTCO_ID", custcoId);

        objRetParams = chatQAExecuteManagePopupService.insertRtnQaRsltMst(jsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "상담내용 조회",
                  notes = "상담내용 조회")
    //@PostMapping("/chat-api/qa/execut-manage/qa-execute-popup/cnslt-cn/inqire")
    @PostMapping("/chat-api/qa/execut-manage/qa-execute-popup/inqire")
    public Object selectRtnChatContent(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        jsonParams.setString("CUSTCO_ID", custcoId);

        objRetParams = chatMainService.selectRtnChatContent(jsonParams);

        //고객명 마스킹처리
        int jsonSize = objRetParams.getHeaderInt("COUNT");
        if(objRetParams != null && jsonSize > 0) {
            for(int i = 0; i < jsonSize; i++) {
                String strCustNm = paletteDataFormatUtils.getFormatData("class_nameEnc", objRetParams.getString("CUSTOMER_NM", i));
                objRetParams.setString("CUSTOMER_NM", i, strCustNm);

                String strCustNm3 = paletteDataFormatUtils.getFormatData("class_nameEnc", objRetParams.getString("CUSTOMER_NM3", i));
                objRetParams.setString("CUSTOMER_NM3", i, strCustNm3);
            }
        }

        //금칙어
        JSONArray talkJsonArray = chatSettingBannedWordUtils.parseContents(objRetParams, jsonParams.getString("ADMIN_CHK"), jsonParams.getString("CUSTCO_ID"));

        objRetParams.remove("DATA");
        objRetParams.setDataObject("DATA", talkJsonArray);

        return objRetParams;
    }


    /**
     * 기존 == twb.TwbTalk.selectRtnUserNm
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "상담원 및 관리자 정보",
                  notes = "상담원 및 관리자 정보를 가져온다.")
    @PostMapping("/api/twb/TwbTalk/selectRtnUserNm")
    public Object selectRtnUserNm(@TelewebJsonParam TelewebJSON mjsonParams) throws Exception
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        jsonParams.setString("CUSTCO_ID", custcoId);

        objRetParams = chatMainService.selectRtnUserNm(jsonParams);

        //금칙어
        JSONArray talkJsonArray = chatSettingBannedWordUtils.parseContents(objRetParams, jsonParams.getString("ADMIN_CHK"), jsonParams.getString("CUSTCO_ID"));

        objRetParams.remove("DATA");
        objRetParams.setDataObject("DATA", talkJsonArray);

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "QA 평가 대상자 현황 (상담톡) 조회 ",
                  notes = "QA 평가 대상자 현황 (상담톡) 조회 ")
    @PostMapping("/chat-api/qa/execut-manage/qa-execute-popup/lngt-text/inqire")
    public Object selectRtnQAIN(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        objRetParams = chatMainService.getAllTextByDB(jsonParams);

        return objRetParams;
    }

}
