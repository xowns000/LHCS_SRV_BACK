package kr.co.hkcloud.palette3.phone.qa.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.qa.app.PhoneQAEvlPaperManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@RestController("EvlPaperManageRestController")
@Api(value = "EvlPaperManageRestController",
     description = "QA평가지관리 서비스콜 REST 컨트롤러")
public class PhoneQAEvlPaperManageRestController
{
    private final PhoneQAEvlPaperManageService EvlPaperManageService;
    private final InnbCreatCmmnService         innbCreatCmmnService;


    /**
     * twb.EvlPaperManage.selectRtnEvSheet
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "Qa평가지조회",
                  notes = "Qa평가지조회")
    @PostMapping("/phone-api/qa/evl-paper-manage/list")
    public Object selectRtnEvSheet(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject());
        objRetParams = EvlPaperManageService.selectRtnEvSheet(jsonParams);

        return objRetParams;
    }


    /**
     * twb.EvlPaperManage.selectChkQASeet
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "Qa평가지체크",
                  notes = "Qa평가지체크")
    @PostMapping("/phone-api/qa/evl-paper-manage/check/inqire")
    public Object selectChkQASeet(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject());
        objRetParams = EvlPaperManageService.selectChkQASeet(jsonParams);

        return objRetParams;
    }


    /**
     * twb.EvlPaperManage.updateRtnQASheet
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "QA평가지저장",
                  notes = "QA평가지저장")
    @PostMapping("/phone-api/qa/evl-paper-manage/regist")
    public Object insertRtnQASHEET(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        JSONObject temp = null;
        String QA_ID = "";

        jsonParams.setDataObject(mjsonParams.getDataObject());

        JSONArray arrExtractRemove = jsonParams.getDataObject("DATA");
        temp = arrExtractRemove.getJSONObject(0);
        QA_ID = temp.getString("QA_ID");

        // QA_ID가 없을경우
        if(QA_ID.equals("")) {
            QA_ID = innbCreatCmmnService.getSeqNo(jsonParams, "QA");
            jsonParams.setString("QA_ID", QA_ID);
            objRetParams = EvlPaperManageService.insertRtnQASHEET(jsonParams);
        }

        // QA_ID가 있을경우
        else {
            objRetParams = EvlPaperManageService.updateRtnQASheet(jsonParams);
        }

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "Qa평가지복사등록",
                  notes = "Qa평가지복사등록")
    @PostMapping("/phone-api/qa/evl-paper-manage/copy/regist")
    public Object insertRtnCopySheet(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject());

        objRetParams = EvlPaperManageService.insertRtnCopySheet(jsonParams);

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "Qa평가지삭제",
                  notes = "Qa평가지삭제")
    @PostMapping("/phone-api/qa/evl-paper-manage/delete")
    public Object deleteRtnQASheet(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject());
        objRetParams = EvlPaperManageService.deleteRtnQASheet(jsonParams);

        return objRetParams;
    }
}
