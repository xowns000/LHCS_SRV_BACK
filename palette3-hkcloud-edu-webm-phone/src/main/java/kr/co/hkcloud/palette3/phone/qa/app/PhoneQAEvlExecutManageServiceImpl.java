package kr.co.hkcloud.palette3.phone.qa.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * SMS 인터페이스 구현체
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("EvlExecutManageService")
public class PhoneQAEvlExecutManageServiceImpl implements PhoneQAEvlExecutManageService
{
    private final TwbComDAO twbComDAO;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtn(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = twbComDAO.select("kr.co.hkcloud.palette3.dao.PhoneQAEvlExecutManageMapper", "selectRtn", jsonParams);
        return objRetParams;
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnDetails(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = twbComDAO.select("kr.co.hkcloud.palette3.dao.PhoneQAEvlExecutManageMapper", "selectRtnDetails", jsonParams);
        return objRetParams;
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON processRtnEvaluationClose(TelewebJSON jsonParams) throws TelewebAppException
    {

        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        TelewebJSON objRetParams2 = new TelewebJSON(jsonParams);
        JSONArray arrExtractRemove = jsonParams.getDataObject("EXTRACT_REMOVE");
        JSONObject temp = null;

        for(int i = 0; i < arrExtractRemove.size(); i++) {

            temp = arrExtractRemove.getJSONObject(i);
            objRetParams2.setString("CNSL_ID", temp.getString("CNSL_ID"));
            objRetParams = twbComDAO.update("kr.co.hkcloud.palette3.dao.PhoneQAEvlExecutManageMapper", "processRtnEvaluationClose", objRetParams2);
        }

        return objRetParams;
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON processRtnCancel(TelewebJSON jsonParams) throws TelewebAppException
    {

        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        TelewebJSON objRetParams2 = new TelewebJSON(jsonParams);
        JSONArray arrExtractRemove = jsonParams.getDataObject("EXTRACT_REMOVE");
        JSONObject temp = null;

        for(int i = 0; i < arrExtractRemove.size(); i++) {
            temp = arrExtractRemove.getJSONObject(i);
            objRetParams2.setString("CNSL_ID", temp.getString("CNSL_ID"));
            objRetParams = twbComDAO.update("kr.co.hkcloud.palette3.dao.PhoneQAEvlExecutManageMapper", "processRtnCancel", objRetParams2);
        }

        return objRetParams;
    }

}
