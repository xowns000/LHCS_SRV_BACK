package kr.co.hkcloud.palette3.phone.lm.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Service("phoneLmPlanManageService")
public class PhoneLmPlanManageServiceImpl implements PhoneLmPlanManageService
{
    private final TwbComDAO            twbComDAO;
    private final InnbCreatCmmnService innbCreatCmmnService;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnLm(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmPlanManageMapper", "selectRtnLm", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnLm(TelewebJSON jsonParams) throws TelewebAppException
    {
        String lmId = jsonParams.getString("LM_ID");
        if(lmId == null || "".equals(lmId)) {
            jsonParams.setString("LM_ID", innbCreatCmmnService.getSeqNo("PLT_LM"));
        }

        String lmStDttm = "";
        if(!jsonParams.getString("LM_ST_DATE").equals("") && !jsonParams.getString("LM_ST_TIME").equals("")) {
            lmStDttm = jsonParams.getString("LM_ST_DATE").replaceAll("-", "") + jsonParams.getString("LM_ST_TIME").replaceAll(":", "") + "00";
            jsonParams.setString("LM_ST_DTTM", lmStDttm);
        }

        String lmEnDttm = "";
        if(!jsonParams.getString("LM_EN_DATE").equals("") && !jsonParams.getString("LM_EN_TIME").equals("")) {
            lmEnDttm = jsonParams.getString("LM_EN_DATE").replaceAll("-", "") + jsonParams.getString("LM_EN_TIME").replaceAll(":", "") + "00";
            jsonParams.setString("LM_EN_DTTM", lmEnDttm);
        }

        return twbComDAO.insert("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmPlanManageMapper", "insertRtnLm", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnEvlPaper(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmPlanManageMapper", "selectRtnEvlPaper", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteRtnLm(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.delete("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmPlanManageMapper", "deleteRtnLm", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnLm(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        objRetParams = twbComDAO.update("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmPlanManageMapper", "updateRtnLm", jsonParams);

        if(jsonParams.getString("STEP").equals("2")) {
            JSONArray lmDataList = jsonParams.getDataObject("LM_DATA_LIST");
            JSONArray delDataList = jsonParams.getDataObject("DEL_LM_DATA_ID");

            if(delDataList != null && delDataList.size() > 0) {
                JSONObject dataObj = null;
                TelewebJSON deleteJson = null;
                for(int i = 0; i < delDataList.size(); i++) {
                    deleteJson = new TelewebJSON();
                    dataObj = delDataList.getJSONObject(i);

                    if(dataObj.getString("LM_DATA_ID") != null && !dataObj.getString("LM_DATA_ID").equals("")) {
                        deleteJson.setString("LM_DATA_ID", dataObj.getString("LM_DATA_ID"));
                        objRetParams = twbComDAO.delete("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmPlanManageMapper", "deleteRtnLmData", deleteJson);
                    }

                }
            }

            if(lmDataList != null && lmDataList.size() > 0) {
                JSONObject lmData = null;
                TelewebJSON lmDataJson = null;
                String LM_DATA_ID = "";
                for(int i = 0; i < lmDataList.size(); i++) {
                    lmDataJson = new TelewebJSON();
                    lmData = lmDataList.getJSONObject(i);

                    LM_DATA_ID = innbCreatCmmnService.getSeqNo("PLT_LM_DATA");

                    lmDataJson.setString("ASP_NEWCUST_KEY", jsonParams.getString("ASP_NEWCUST_KEY"));
                    lmDataJson.setString("LM_ID", jsonParams.getString("LM_ID"));
                    lmDataJson.setString("LM_EVA_ID", jsonParams.getString("LM_EVA_ID"));
                    lmDataJson.setString("LM_DATA_ID", LM_DATA_ID);
                    lmDataJson.setString("LM_DATA_US_ID", lmData.getString("LM_DATA_US_ID"));
                    lmDataJson.setString("LM_DATA_US_NM", lmData.getString("LM_DATA_US_NM"));
                    lmDataJson.setString("REG_ID", jsonParams.getString("REG_ID"));

                    objRetParams = twbComDAO.insert("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmPlanManageMapper", "insertRtnLmData", lmDataJson);
                }
            }

        }

        return objRetParams;
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnLmUser(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmPlanManageMapper", "selectRtnLmUser", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnLmData(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmPlanManageMapper", "selectRtnLmData", jsonParams);
    }

}
