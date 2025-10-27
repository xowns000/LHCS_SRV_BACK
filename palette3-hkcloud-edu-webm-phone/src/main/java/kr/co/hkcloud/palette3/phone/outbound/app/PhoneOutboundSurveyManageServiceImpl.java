package kr.co.hkcloud.palette3.phone.outbound.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Service("phoneOutboundSurveyManageService")
public class PhoneOutboundSurveyManageServiceImpl implements PhoneOutboundSurveyManageService
{
    private final TwbComDAO mobjDao;


    /**
     * 설문지 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnServayList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyManageMapper", "selectRtnServayList", jsonParams);

        return objRetParams;
    }


    /**
     * 설문지 문항 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnServayQList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyManageMapper", "selectRtnServayQList", jsonParams);

        return objRetParams;
    }


    /**
     * 설문지 답안 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnServayAnsList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyManageMapper", "selectRtnServayAnsList", jsonParams);

        return objRetParams;
    }


    /**
     * 설문지 등록
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnServay(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.insert("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyManageMapper", "insertRtnServay", jsonParams);
    }


    /**
     * 설문지 수정
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnServay(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyManageMapper", "updateRtnServay", jsonParams);
    }


    /**
     * 설문지 삭제
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON deleteRtnServay(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        //설문답안 삭제
        mobjDao.delete("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyManageMapper", "deleteRtnServayResult", jsonParams);

        //설문지 다음항목 삭제
        mobjDao.delete("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyManageMapper", "deleteRtnServayData02", jsonParams);

        //설문 항목 삭제
        mobjDao.delete("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyManageMapper", "deleteRtnServayData01", jsonParams);

        //설문지 삭제
        objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyManageMapper", "deleteRtnServay", jsonParams);

        return objRetParams;
    }


    /**
     * 설문지항목 등록
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnServayData(TelewebJSON jsonParams) throws TelewebAppException
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

        QNoReturnParam = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyManageMapper", "selectRtnServayQNo", jsonParams);
        Q_NO = QNoReturnParam.getString("Q_NO");
        jsonParams.setString("Q_NO", Q_NO);

        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyManageMapper", "insertRtnServayData01", jsonParams);

        ansList = jsonParams.getDataObject("ANS_LIST");
        if(ansList != null) {

            ansListSize = ansList.size();
            FLD_SID = jsonParams.getString("FLD_SID");
            USER_ID = jsonParams.getString("USER_ID");

            for(int i = 0; i < ansListSize; i++) {

                ansInsertParam = ansList.getJSONObject(i);
                ansInsertParam.put("FLD_SID", FLD_SID);
                ansInsertParam.put("Q_NO", Q_NO);
                ansInsertParam.put("USER_ID", USER_ID);
                insertParams.setDataObject("DATA", ansInsertParam);
                mobjDao.insert("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyManageMapper", "insertRtnServayData02", insertParams);

            }
        }

        return objRetParams;
    }


    /**
     * 설문지항목 수정
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnServayData(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON insertParams = new TelewebJSON();
        JSONArray ansList = new JSONArray();
        JSONObject ansInsertParam = new JSONObject();
        int ansListSize = 0;
        String Q_NO = "";
        String FLD_SID = "";
        String USER_ID = "";

        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyManageMapper", "updateRtnServayData01", jsonParams);

        ansList = jsonParams.getDataObject("ANS_LIST");
        if(ansList != null) {

            ansListSize = ansList.size();
            Q_NO = jsonParams.getString("Q_NO");
            FLD_SID = jsonParams.getString("FLD_SID");
            USER_ID = jsonParams.getString("USER_ID");

            for(int i = 0; i < ansListSize; i++) {

                ansInsertParam = ansList.getJSONObject(i);
                insertParams.setDataObject("DATA", ansInsertParam);
                ansInsertParam.put("USER_ID", USER_ID);

                // I : insert, U : update, D: delete
                if(insertParams.getString("DATA_FLAG").equals("I")) {
                    insertParams.setString("Q_NO", Q_NO);
                    insertParams.setString("FLD_SID", FLD_SID);
                    mobjDao.insert("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyManageMapper", "insertRtnServayData02", insertParams);
                }
                else if(insertParams.getString("DATA_FLAG").equals("U")) {
                    mobjDao.update("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyManageMapper", "updateRtnServayData02", insertParams);
                }
                else if(insertParams.getString("DATA_FLAG").equals("D")) {
                    mobjDao.delete("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyManageMapper", "deleteRtnServayData02", insertParams);
                }

            }
        }

        return objRetParams;
    }


    /**
     * 설문지문항 삭제
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON deleteRtnServayData(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        //설문답안 삭제
        mobjDao.delete("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyManageMapper", "deleteRtnServayResult", jsonParams);

        //설문지 다음항목 삭제
        mobjDao.delete("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyManageMapper", "deleteRtnServayData02", jsonParams);

        //설문 항목 삭제
        objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyManageMapper", "deleteRtnServayData01", jsonParams);

        return objRetParams;
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnServayNo(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyManageMapper", "selectRtnServayNo", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteRtnServayResult(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.delete("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyManageMapper", "deleteRtnServayResult", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteRtnServayData02(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.delete("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyManageMapper", "deleteRtnServayData02", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteRtnServayData01(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.delete("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyManageMapper", "deleteRtnServayData01", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnServayQNo(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyManageMapper", "selectRtnServayQNo", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnServayData01(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.insert("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyManageMapper", "insertRtnServayData01", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnServayData02(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.insert("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyManageMapper", "insertRtnServayData02", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnServayData01(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyManageMapper", "updateRtnServayData01", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnServayData02(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyManageMapper", "updateRtnServayData02", jsonParams);
    }

}
