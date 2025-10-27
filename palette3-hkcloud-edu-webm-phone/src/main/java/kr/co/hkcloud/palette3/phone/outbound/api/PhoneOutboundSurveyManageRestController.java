package kr.co.hkcloud.palette3.phone.outbound.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.outbound.app.PhoneOutboundSurveyManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneOutboundSurveyManageRestController",
     description = "설문지관리 REST 컨트롤러")
public class PhoneOutboundSurveyManageRestController
{

    private final PhoneOutboundSurveyManageService phoneOutboundSurveyManageService;
    private final InnbCreatCmmnService innbCreatCmmnService;

    /**
     * 
     * @param  mjsonParams
     * @return
     * 
     */
    @ApiOperation(value = "설문지 조회",
                  notes = "설문지 조회")
    @PostMapping("/phone-api/outbound/survey-manage/list")
    public Object selectRtnServayList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneOutboundSurveyManageService.selectRtnServayList(mjsonParams);

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     * 
     */
    @ApiOperation(value = "설문지문항 조회",
                  notes = "설문지문항 조회")
    @PostMapping("/phone-api/outbound/survey-manage/survey-list")
    public Object selectRtnServayQList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneOutboundSurveyManageService.selectRtnServayQList(mjsonParams);

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     * 
     */
    @ApiOperation(value = "설문지답안 조회",
                  notes = "설문지답안 조회")
    @PostMapping("/phone-api/outbound/survey-manage-popup/inqire")
    public Object selectRtnServayAnsList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneOutboundSurveyManageService.selectRtnServayAnsList(mjsonParams);

        return objRetParams;
    }


    /**
     * 설문지 등록
     * 
     * @param  mjsonParams
     * @return
     * 
     */
    @ApiOperation(value = "설문지 등록",
                  notes = "설문지 등록")
    @PostMapping("/phone-api/outbound/survey-manage/insert")
    public Object insertRtnServay(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON QNoReturnParam = new TelewebJSON();

        QNoReturnParam = phoneOutboundSurveyManageService.selectRtnServayNo(mjsonParams);
        String questId = QNoReturnParam.getString("QUEST_ID");
        mjsonParams.setString("FLD_SID", questId);

        objRetParams = phoneOutboundSurveyManageService.insertRtnServay(mjsonParams);

        return objRetParams;
    }


    /**
     * 설문지 수정
     * 
     * @param  mjsonParams
     * @return
     * 
     */
    @ApiOperation(value = "설문지 수정",
                  notes = "설문지 수정")
    @PostMapping("/phone-api/outbound/survey-manage/modify")
    public Object updateRtnServay(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = phoneOutboundSurveyManageService.updateRtnServay(mjsonParams);

        return objRetParams;
    }


    /**
     * 설문지 삭제
     * 
     * @param  mjsonParams
     * @return
     * 
     */
    @ApiOperation(value = "설문지 삭제",
                  notes = "설문지 삭제")
    @PostMapping("/phone-api/outbound/survey-manage/delete")
    public Object deleteRtnServay(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        //설문답안 삭제
        phoneOutboundSurveyManageService.deleteRtnServayResult(mjsonParams);

        //설문지 다음항목 삭제
        phoneOutboundSurveyManageService.deleteRtnServayData02(mjsonParams);

        //설문 항목 삭제
        phoneOutboundSurveyManageService.deleteRtnServayData01(mjsonParams);

        //설문지 삭제
        objRetParams = phoneOutboundSurveyManageService.deleteRtnServay(mjsonParams);

        return objRetParams;
    }


    /**
     * 설문지항목 등록
     * 
     * @param  mjsonParams
     * @return
     * 
     */
    @ApiOperation(value = "설문지항목 등록",
                  notes = "설문지항목 등록")
    @PostMapping("/phone-api/outbound/survey-manage-popup/regist")
    public Object insertRtnServayData(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON insertParams = new TelewebJSON();
        TelewebJSON QNoReturnParam = new TelewebJSON();
        JSONArray ansList = new JSONArray();
        JSONObject ansInsertParam = new JSONObject();
        int ansListSize = 0;
        String Q_NO = "";
        String FLD_SID = "";
        String USER_ID = "";

        QNoReturnParam = phoneOutboundSurveyManageService.selectRtnServayQNo(mjsonParams);
        Q_NO = QNoReturnParam.getString("Q_NO");
        mjsonParams.setString("Q_NO", Q_NO);

        objRetParams = phoneOutboundSurveyManageService.insertRtnServayData01(mjsonParams);

        ansList = mjsonParams.getDataObject("ANS_LIST");
        if(ansList != null) {

            ansListSize = ansList.size();
            FLD_SID = mjsonParams.getString("FLD_SID");
            USER_ID = mjsonParams.getString("USER_ID");

            for(int i = 0; i < ansListSize; i++) {

                ansInsertParam = ansList.getJSONObject(i);
                ansInsertParam.put("FLD_SID", FLD_SID);
                ansInsertParam.put("Q_NO", Q_NO);
                ansInsertParam.put("USER_ID", USER_ID);
                insertParams.setDataObject("DATA", ansInsertParam);
                phoneOutboundSurveyManageService.insertRtnServayData02(insertParams);

            }
        }

        return objRetParams;
    }


    /**
     * 설문지항목 수정
     * 
     * @param  mjsonParams
     * @return
     * 
     */
    @ApiOperation(value = "설문지항목 수정",
                  notes = "설문지항목 수정")
    @PostMapping("/phone-api/outbound/survey-manage-popup/modify")
    public Object updateRtnServayData(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON insertParams = new TelewebJSON();
        JSONArray ansList = new JSONArray();
        JSONObject ansInsertParam = new JSONObject();
        int ansListSize = 0;
        String Q_NO = "";
        String FLD_SID = "";
        String USER_ID = "";

        objRetParams = phoneOutboundSurveyManageService.updateRtnServayData01(mjsonParams);

        ansList = mjsonParams.getDataObject("ANS_LIST");
        if(ansList != null) {

            ansListSize = ansList.size();
            Q_NO = mjsonParams.getString("Q_NO");
            FLD_SID = mjsonParams.getString("FLD_SID");
            USER_ID = mjsonParams.getString("USER_ID");

            for(int i = 0; i < ansListSize; i++) {

                ansInsertParam = ansList.getJSONObject(i);
                ansInsertParam.put("USER_ID", USER_ID);
                insertParams.setDataObject("DATA", ansInsertParam);

                // I : insert, U : update, D: delete
                if(insertParams.getString("DATA_FLAG").equals("I")) {
                    insertParams.setString("Q_NO", Q_NO);
                    insertParams.setString("FLD_SID", FLD_SID);
                    phoneOutboundSurveyManageService.insertRtnServayData02(mjsonParams);
                }
                else if(insertParams.getString("DATA_FLAG").equals("U")) {
                    phoneOutboundSurveyManageService.updateRtnServayData02(mjsonParams);
                }
                else if(insertParams.getString("DATA_FLAG").equals("D")) {
                    phoneOutboundSurveyManageService.deleteRtnServayData02(mjsonParams);
                }

            }
        }

        return objRetParams;
    }


    /**
     * 설문지문항 삭제
     * 
     * @param  mjsonParams
     * @return
     * 
     */
    @ApiOperation(value = "설문지문항 삭제",
                  notes = "설문지문항 삭제")
    @PostMapping("/phone-api/outbound/survey-manage-popup/delete")
    public Object deleteRtnServayData(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        //설문답안 삭제
        phoneOutboundSurveyManageService.deleteRtnServayResult(mjsonParams);

        //설문지 다음항목 삭제
        phoneOutboundSurveyManageService.deleteRtnServayData02(mjsonParams);

        //설문 항목 삭제
        objRetParams = phoneOutboundSurveyManageService.deleteRtnServayData01(mjsonParams);

        return objRetParams;
    }

}
