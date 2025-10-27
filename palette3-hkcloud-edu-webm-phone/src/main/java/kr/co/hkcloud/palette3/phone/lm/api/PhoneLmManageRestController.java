package kr.co.hkcloud.palette3.phone.lm.api;


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
import kr.co.hkcloud.palette3.phone.lm.app.PhoneLmManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@RestController("PhoneLmManageRestController")
@Api(value = "PhoneLmManageRestController",
     description = "LM평가지관리 서비스콜 REST 컨트롤러")
public class PhoneLmManageRestController
{
    private final PhoneLmManageService phoneLmManageService;
    private final InnbCreatCmmnService innbCreatCmmnService;


    @ApiOperation(value = "LM평가 시험지 리스트 조회",
                  notes = "LM평가 시험지 리스트 조회")
    @PostMapping("/phone-api/lm/evl-paper-manage/list")
    public Object selectRtnLmsEva(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject());

        objRetParams = phoneLmManageService.selectRtnLmsEva(jsonParams);
        return objRetParams;
    }


    @ApiOperation(value = "LM평가 평가지 등록/수정",
                  notes = "LM평가 평가지 등록/수정")
    @PostMapping("/phone-api/lm/evl-paper-manage/regist")
    public Object insertRtnLmEva(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        JSONObject temp = null;
        String LM_EVA_ID = "";

        jsonParams.setDataObject(mjsonParams.getDataObject());

        JSONArray arrExtractRemove = jsonParams.getDataObject("DATA");
        temp = arrExtractRemove.getJSONObject(0);
        LM_EVA_ID = temp.getString("LM_EVA_ID");

        // LM_EVA_ID가 없을경우
        if(LM_EVA_ID.equals("")) {
            LM_EVA_ID = innbCreatCmmnService.getSeqNo("PLT_LM_EVA");
            jsonParams.setString("LM_EVA_ID", LM_EVA_ID);
            objRetParams = phoneLmManageService.insertRtnLmEva(jsonParams);
        }

        // LM_EVA_ID가 있을경우
        else {
            objRetParams = phoneLmManageService.updateRtnLmEva(jsonParams);
        }

        return objRetParams;
    }


    @ApiOperation(value = "LM평가 평가지 복사",
                  notes = "LM평가 평가지 복사")
    @PostMapping("/phone-api/lm/evl-paper-manage/copy/regist")
    public Object insertRtnCopyLmEva(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject());

        objRetParams = phoneLmManageService.insertRtnCopyLmEva(jsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "LM평가 평가지 삭제",
                  notes = "LM평가 평가지 삭제")
    @PostMapping("/phone-api/lm/evl-paper-manage/delete")
    public Object deleteRtnLmEva(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        jsonParams.setDataObject(mjsonParams.getDataObject());
        objRetParams = phoneLmManageService.deleteRtnLmEva(jsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "LM평가 평가문항 리스트 조회",
                  notes = "LM평가 평가문항 리스트 조회")
    @PostMapping("/phone-api/lm/evl-qs-manage/list")
    public Object selectRtnLmQs(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        jsonParams.setDataObject(mjsonParams.getDataObject());
        objRetParams = phoneLmManageService.selectRtnLmQs(jsonParams);

        return objRetParams;

    }


    @ApiOperation(value = "LM평가 평가문항,보기 등록/수정",
                  notes = "LM평가 평가문항,보기 등록/수정")
    @PostMapping("/phone-api/lm/evl-qs-manage/regist")
    public Object insertRtnLmQs(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        JSONObject temp = null;
        String LM_QS_ID = "";

        jsonParams.setDataObject(mjsonParams.getDataObject());

        JSONArray arrExtractRemove = jsonParams.getDataObject("DATA");
        temp = arrExtractRemove.getJSONObject(0);
        LM_QS_ID = temp.getString("LM_QS_ID");

        // QA_EVA_ID가 없을경우
        if(LM_QS_ID.equals("")) {
            LM_QS_ID = innbCreatCmmnService.getSeqNo("PLT_LM_QS");
            mjsonParams.setString("LM_QS_ID", LM_QS_ID);
        }

        objRetParams = phoneLmManageService.insertRtnLmQs(mjsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "LM평가 평가문항 복사",
                  notes = "LM평가 평가문항 복사")
    @PostMapping("/phone-api/lm/evl-qs-manage/copy/regist")
    public Object insertRtnCopyLmQs(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        jsonParams.setDataObject(mjsonParams.getDataObject());

        objRetParams = phoneLmManageService.insertRtnCopyLmQs(jsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "LM평가 평가문항 삭제",
                  notes = "LM평가 평가문항 삭제")
    @PostMapping("/phone-api/lm/evl-qs-manage/delete")
    public Object deleteRtnLmQs(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneLmManageService.deleteRtnLmQs(mjsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "LM평가 평가문항 보기 리스트 조회",
                  notes = "LM평가 평가문항 보기 리스트 조회")
    @PostMapping("/phone-api/lm/evl-qs-manage/ve/list")
    public Object selectRtnLmVe(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        jsonParams.setDataObject(mjsonParams.getDataObject());

        objRetParams = phoneLmManageService.selectRtnLmVe(jsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "LM평가 평가상세 리스트 조회",
                  notes = "LM평가 평가상세 리스트 조회")
    @PostMapping("/phone-api/lm/evl-qa-rst-manage/list")
    public Object selectRtnLmEvaRst(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        jsonParams.setDataObject(mjsonParams.getDataObject());

        objRetParams = phoneLmManageService.selectRtnLmEvaRst(jsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "LM평가 평가상세 등록",
                  notes = "LM평가 평가상세 등록")
    @PostMapping("/phone-api/lm/evl-qa-rst-manage/regist")
    public Object insertRtnLmEvaRst(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneLmManageService.insertRtnLmEvaRst(mjsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "LM평가 미리보기",
                  notes = "LM평가 미리보기")
    @PostMapping("/phone-api/lm/evl-qa-rst-manage/pre/list")
    public Object selectRtnLmPreView(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneLmManageService.selectRtnLmPreView(mjsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "QA평가 미리보기 문항 보기 조회",
                  notes = "QA평가 미리보기 문항 보기 조회")
    @PostMapping("/phone-api/lm/evl-qa-rst-manage/pre/ve/list")
    public Object selectRtnLmVePreView(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        List<String> lmQsIdList = new LinkedList<String>();
        JSONArray lmQsList = mjsonParams.getDataObject("LM_QS_LIST");

        if(lmQsList != null && lmQsList.size() > 0) {
            JSONObject lmQs = null;
            for(int i = 0; i < lmQsList.size(); i++) {
                lmQs = lmQsList.getJSONObject(i);
                lmQsIdList.add(lmQs.getString("LM_QS_ID"));
            }
            jsonParams.setObject("lmQsIdList", 0, lmQsIdList);

            objRetParams = phoneLmManageService.selectRtnLmVePreView(jsonParams);
        }

        return objRetParams;
    }


    @ApiOperation(value = "LM_EVA_ID 기준 pre항목 조회",
                  notes = "LM_EVA_ID 기준 pre항목 조회")
    @PostMapping("/phone-api/lm/evl-lm-rst-manage/pre/all/list")
    Object selectRtnLmVePre(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        TelewebJSON lmQsList = new TelewebJSON();
        TelewebJSON veList = new TelewebJSON();

        List<String> lmQsIdList = new LinkedList<String>();

        lmQsList = phoneLmManageService.selectRtnLmEvaRst(mjsonParams);

        objRetParams.setDataObject("lmQsList", lmQsList);
        if(lmQsList != null && lmQsList.getSize() > 0) {
            for(int i = 0; i < lmQsList.getSize(); i++) {
                lmQsIdList.add(lmQsList.getString("LM_QS_ID", i));
            }
            jsonParams.setObject("lmQsIdList", 0, lmQsIdList);

            veList = phoneLmManageService.selectRtnLmVePreView(jsonParams);

            objRetParams.setDataObject("veList", veList);
        }

        return objRetParams;
    }

}
