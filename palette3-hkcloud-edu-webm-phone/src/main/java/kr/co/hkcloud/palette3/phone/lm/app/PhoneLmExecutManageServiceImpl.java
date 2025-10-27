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
@Service("phoneLmExecutManageService")
public class PhoneLmExecutManageServiceImpl implements PhoneLmExecutManageService
{
    private final TwbComDAO            twbComDAO;
    private final InnbCreatCmmnService innbCreatCmmnService;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnLmExecut(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmExecutManageMapper", "selectRtnLmExecut", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnLmDetail(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON retJson = new TelewebJSON(jsonParams);
        TelewebJSON evaDetail = new TelewebJSON();
        TelewebJSON rstDetail = new TelewebJSON();

        evaDetail = twbComDAO.select("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmExecutManageMapper", "selectRtnLmEvaRstDetail", jsonParams);
        rstDetail = twbComDAO.select("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmExecutManageMapper", "selectRtnLmDataRstDetail", jsonParams);

        retJson.setDataObject("EVA_DETAIL", evaDetail);
        retJson.setDataObject("RST_DETAIL", rstDetail);

        return retJson;
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnLmEvaPaper(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON checkLm = new TelewebJSON();
        TelewebJSON retParams = new TelewebJSON(jsonParams);
        TelewebJSON lmQsList = new TelewebJSON();
        TelewebJSON lmVeList = new TelewebJSON();
        checkLm = twbComDAO.select("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmExecutManageMapper", "checkLmTime", jsonParams);

        if(checkLm != null && checkLm.getSize() > 0) {
            if(checkLm.getString("LM_DATA_ANS_YN").equals("N")) {

                //시험 시작처리
                twbComDAO.update("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmExecutManageMapper", "updateRtnLmStart", jsonParams);

                lmQsList = twbComDAO.select("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmExecutManageMapper", "selectRtnLmQs", jsonParams);

                if(lmQsList != null) {
                    TelewebJSON veSelectParam = new TelewebJSON();
                    JSONArray qsIdList = new JSONArray();
                    for(int i = 0; i < lmQsList.getSize(); i++) {
                        qsIdList.add(lmQsList.getString("LM_QS_ID", i));
                    }

                    veSelectParam.setObject("qsIdList", 0, qsIdList);
                    veSelectParam.setString("ASP_NEWCUST_KEY", jsonParams.getString("ASP_NEWCUST_KEY"));

                    lmVeList = twbComDAO.select("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmExecutManageMapper", "selectRtnLmVe", veSelectParam);

                    retParams.setDataObject("lmQsList", lmQsList);
                    retParams.setDataObject("lmVeList", lmVeList);
                }

                twbComDAO.update("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmExecutManageMapper", "updateRtnLmExecutStart", jsonParams);
            }
            else {
                //이미 제출
                retParams.setHeader("ERROR_FLAG", true);
                retParams.setHeader("ERROR_MSG", "이미 제출한 시험입니다.");
            }
        }
        else {
            //현재 진행중인 시험 없음
            retParams.setHeader("ERROR_FLAG", true);
            retParams.setHeader("ERROR_MSG", "현재 진행중인 시험이 없습니다.");
        }
        return retParams;
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON processRtnExecut(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON retParams = new TelewebJSON(jsonParams);
        TelewebJSON ansParams = new TelewebJSON();
        ansParams.setDataObject(jsonParams.getDataObject("LM_QS_ANS"));
        JSONArray ansArray = ansParams.getDataObject();

        if(ansArray != null && ansArray.size() > 0) {
            JSONObject ansObj = null;
            TelewebJSON insertJson = null;
            for(int i = 0; i < ansArray.size(); i++) {
                ansObj = ansArray.getJSONObject(i);
                insertJson = new TelewebJSON();
                String lmQsTy = ansObj.getString("LM_QS_TY");
                String lmQsVeLoAns = "";
                if(lmQsTy.equals("20")) {
                    lmQsVeLoAns = ansObj.getString("LM_QS_VE_LO_ANS");
                }

                insertJson.setString("LM_DATA_ID", jsonParams.getString("LM_DATA_ID"));
                insertJson.setString("LM_QS_ID", ansObj.getString("LM_QS_ID"));
                insertJson.setString("LM_QS_VE_LO_ANS", lmQsVeLoAns);
                insertJson.setString("USER_ID", jsonParams.getString("USER_ID"));
                insertJson.setString("ASP_NEWCUST_KEY", jsonParams.getString("ASP_NEWCUST_KEY"));

                String tempVeId = ansObj.getString("LM_QS_VE_ID");
                String[] tempVeIdList = tempVeId.split("\\|");
                log.debug("tempVeIdList :" + tempVeIdList.toString());
                if(tempVeIdList != null && tempVeIdList.length > 0) {
                    for(int j = 0; j < tempVeIdList.length; j++) {
                        String lmDataRstId = innbCreatCmmnService.getSeqNo("PLT_LM_DATA_RST");
                        insertJson.setString("LM_DATA_RST_ID", lmDataRstId);
                        insertJson.setString("LM_QS_VE_ID", tempVeIdList[j]);
                        log.debug("tempVeIdList[j] :" + tempVeIdList[j]);
                        retParams = twbComDAO.insert("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmExecutManageMapper", "insertRtnLmDataRst", insertJson);
                    }
                }
            }
        }

        TelewebJSON updateJson = new TelewebJSON();
        updateJson.setString("LM_DATA_ID", jsonParams.getString("LM_DATA_ID"));
        updateJson.setString("ASP_NEWCUST_KEY", jsonParams.getString("ASP_NEWCUST_KEY"));
        updateJson.setString("USER_ID", jsonParams.getString("USER_ID"));

        retParams = twbComDAO.update("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmExecutManageMapper", "updateRtnLmDataAns", updateJson);
        return retParams;
    }

}
