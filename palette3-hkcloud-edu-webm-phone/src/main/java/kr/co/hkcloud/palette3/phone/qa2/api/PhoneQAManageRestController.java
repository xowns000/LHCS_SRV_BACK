package kr.co.hkcloud.palette3.phone.qa2.api;


import java.util.LinkedList;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.qa2.app.PhoneQAManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@RestController("PhoneQAManageRestController")
@Api(value = "PhoneQAManageRestController",
     description = "QA평가관리 REST 컨트롤러")
public class PhoneQAManageRestController
{

    private final PhoneQAManageService phoneQAManageService;
    private final InnbCreatCmmnService innbCreatCmmnService;


    @ApiOperation(value = "Qa평가 시험지 리스트 조회",
                  notes = "Qa평가 시험지 리스트 조회")
    @PostMapping("/phone-api/qa2/evl-paper-manage/list")
    public Object selectRtnQaEva(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject());

        objRetParams = phoneQAManageService.selectRtnQaEva(jsonParams);
        return objRetParams;
    }


    @ApiOperation(value = "QA평가 평가지 등록/수정",
                  notes = "QA평가 평가지 등록/수정")
    @PostMapping("/phone-api/qa2/evl-paper-manage/regist")
    public Object insertRtnQaEva(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        JSONObject temp = null;
        String QA_EVA_ID = "";

        jsonParams.setDataObject(mjsonParams.getDataObject());

        JSONArray arrExtractRemove = jsonParams.getDataObject("DATA");
        temp = arrExtractRemove.getJSONObject(0);
        QA_EVA_ID = temp.getString("QA_EVA_ID");

        // QA_EVA_ID가 없을경우
        if(QA_EVA_ID.equals("")) {
            QA_EVA_ID = innbCreatCmmnService.getSeqNo("PLT_QA_EVA");
            jsonParams.setString("QA_EVA_ID", QA_EVA_ID);
            objRetParams = phoneQAManageService.insertRtnQaEva(jsonParams);
        }

        // QA_EVA_ID가 있을경우
        else {
            objRetParams = phoneQAManageService.updateRtnQaEva(jsonParams);
        }

        return objRetParams;
    }


    @ApiOperation(value = "QA평가 평가지 복사",
                  notes = "QA평가 평가지 복사")
    @PostMapping("/phone-api/qa2/evl-paper-manage/copy/regist")
    public Object insertRtnCopyQaEva(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject());

        objRetParams = phoneQAManageService.insertRtnCopyQaEva(jsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "QA평가 평가지 삭제",
                  notes = "QA평가 평가지 삭제")
    @PostMapping("/phone-api/qa2/evl-paper-manage/delete")
    public Object deleteRtnQaEva(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        jsonParams.setDataObject(mjsonParams.getDataObject());
        objRetParams = phoneQAManageService.deleteRtnQaEva(jsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "QA평가 평가문항 리스트 조회",
                  notes = "QA평가 평가문항 리스트 조회")
    @PostMapping("/phone-api/qa2/evl-qs-manage/list")
    public Object selectRtnQaQs(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        jsonParams.setDataObject(mjsonParams.getDataObject());
        objRetParams = phoneQAManageService.selectRtnQaQs(jsonParams);

        return objRetParams;

    }


    @ApiOperation(value = "QA평가 평가문항,보기 등록/수정",
                  notes = "QA평가 평가문항,보기 등록/수정")
    @PostMapping("/phone-api/qa2/evl-qs-manage/regist")
    public Object insertRtnQaQs(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        JSONObject temp = null;
        String QA_QS_ID = "";

        jsonParams.setDataObject(mjsonParams.getDataObject());

        JSONArray arrExtractRemove = jsonParams.getDataObject("DATA");
        temp = arrExtractRemove.getJSONObject(0);
        QA_QS_ID = temp.getString("QA_QS_ID");

        // QA_EVA_ID가 없을경우
        if(QA_QS_ID.equals("")) {
            QA_QS_ID = innbCreatCmmnService.getSeqNo("PLT_QA_QS");
            mjsonParams.setString("QA_QS_ID", QA_QS_ID);
        }

        objRetParams = phoneQAManageService.insertRtnQaQs(mjsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "QA평가 평가문항 복사",
                  notes = "QA평가 평가문항 복사")
    @PostMapping("/phone-api/qa2/evl-qs-manage/copy/regist")
    public Object insertRtnCopyQaQs(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        jsonParams.setDataObject(mjsonParams.getDataObject());

        objRetParams = phoneQAManageService.insertRtnCopyQaQs(jsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "Qa평가 평가문항 삭제",
                  notes = "QA평가 평가문항 삭제")
    @PostMapping("/phone-api/qa2/evl-qs-manage/delete")
    public Object deleteRtnQaQs(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneQAManageService.deleteRtnQaQs(mjsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "QA평가 평가문항 보기 리스트 조회",
                  notes = "QA평가 평가문항 보기 리스트 조회")
    @PostMapping("/phone-api/qa2/evl-qs-manage/ve/list")
    public Object selectRtnQaVe(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        jsonParams.setDataObject(mjsonParams.getDataObject());

        objRetParams = phoneQAManageService.selectRtnQaVe(jsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "QA평가 평가상세 리스트 조회",
                  notes = "QA평가 평가상세 리스트 조회")
    @PostMapping("/phone-api/qa2/evl-qa-rst-manage/list")
    public Object selectRtnQaEvaRst(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        jsonParams.setDataObject(mjsonParams.getDataObject());

        objRetParams = phoneQAManageService.selectRtnQaEvaRst(jsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "QA평가 평가상세 등록",
                  notes = "QA평가 평가상세 등록")
    @PostMapping("/phone-api/qa2/evl-qa-rst-manage/regist")
    public Object insertRtnQaEvaRst(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneQAManageService.insertRtnQaEvaRst(mjsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "QA평가 미리보기",
                  notes = "QA평가 미리보기")
    @PostMapping("/phone-api/qa2/evl-qa-rst-manage/pre/list")
    public Object selectRtnQaPreView(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneQAManageService.selectRtnQaPreView(mjsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "QA평가 미리보기 문항 보기 조회",
                  notes = "QA평가 미리보기 문항 보기 조회")
    @PostMapping("/phone-api/qa2/evl-qa-rst-manage/pre/ve/list")
    public Object selectRtnQaVePreView(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        List<String> qaQsIdList = new LinkedList<String>();
        JSONArray qaQsList = mjsonParams.getDataObject("QA_QS_LIST");

        if(qaQsList != null && qaQsList.size() > 0) {
            JSONObject qaQs = null;
            for(int i = 0; i < qaQsList.size(); i++) {
                qaQs = qaQsList.getJSONObject(i);
                qaQsIdList.add(qaQs.getString("QA_QS_ID"));
            }
            jsonParams.setObject("qaQsIdList", 0, qaQsIdList);

            objRetParams = phoneQAManageService.selectRtnQaVePreView(jsonParams);
        }

        return objRetParams;
    }


    @ApiOperation(value = "QA_EVA_ID 기준 pre항목 조회",
                  notes = "QA_EVA_ID 기준 pre항목 조회")
    @PostMapping("/phone-api/qa2/evl-qa-rst-manage/pre/all/list")
    Object selectRtnQaVePre(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        TelewebJSON qaQsList = new TelewebJSON();
        TelewebJSON veList = new TelewebJSON();

        List<String> qaQsIdList = new LinkedList<String>();

        qaQsList = phoneQAManageService.selectRtnQaEvaRst(mjsonParams);

        objRetParams.setDataObject("qaQsList", qaQsList);
        if(qaQsList != null && qaQsList.getSize() > 0) {
            for(int i = 0; i < qaQsList.getSize(); i++) {
                qaQsIdList.add(qaQsList.getString("QA_QS_ID", i));
            }
            jsonParams.setObject("qaQsIdList", 0, qaQsIdList);

            veList = phoneQAManageService.selectRtnQaVePreView(jsonParams);

            objRetParams.setDataObject("veList", veList);
        }

        return objRetParams;
    }

}
