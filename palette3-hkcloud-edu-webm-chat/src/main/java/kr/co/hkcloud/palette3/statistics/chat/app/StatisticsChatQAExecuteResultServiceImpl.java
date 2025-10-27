package kr.co.hkcloud.palette3.statistics.chat.app;


import org.springframework.stereotype.Service;

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
@Service("StatisticsChatQAExecuteResultService")
public class StatisticsChatQAExecuteResultServiceImpl implements StatisticsChatQAExecuteResultService
{
    private final TwbComDAO mobjDao;


    @Override
    public TelewebJSON selectRtn(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = mobjDao
            .select("com.hcteleweb.teleweb.mng.qam.dao.StatisticsChatQAExecuteResultMapper", "selectRtn", jsonParams);
        return objRetParams;
    }


    @Override
    public TelewebJSON selectRtnDetails(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = mobjDao
            .select("com.hcteleweb.teleweb.mng.qam.dao.StatisticsChatQAExecuteResultMapper", "selectRtnDetails", jsonParams);
        return objRetParams;
    }


    @Override
    public TelewebJSON processRtnEvaluationClose(TelewebJSON jsonParams) throws TelewebAppException
    {

        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        TelewebJSON objRetParams2 = new TelewebJSON(jsonParams);
        JSONArray arrExtractRemove = jsonParams.getDataObject("EXTRACT_REMOVE");
        JSONObject temp = null;

        for(int i = 0; i < arrExtractRemove.size(); i++) {

            temp = arrExtractRemove.getJSONObject(i);
            objRetParams2.setString("CNSL_ID", temp.getString("CNSL_ID"));
            objRetParams = mobjDao
                .update("com.hcteleweb.teleweb.mng.qam.dao.StatisticsChatQAExecuteResultMapper", "processRtnEvaluationClose", objRetParams2);
        }

        return objRetParams;
    }


    @Override
    public TelewebJSON processRtnCancel(TelewebJSON jsonParams) throws TelewebAppException
    {

        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        TelewebJSON objRetParams2 = new TelewebJSON(jsonParams);
        JSONArray arrExtractRemove = jsonParams.getDataObject("EXTRACT_REMOVE");
        JSONObject temp = null;

        for(int i = 0; i < arrExtractRemove.size(); i++) {
            temp = arrExtractRemove.getJSONObject(i);
            objRetParams2.setString("CNSL_ID", temp.getString("CNSL_ID"));
            objRetParams = mobjDao
                .update("com.hcteleweb.teleweb.mng.qam.dao.StatisticsChatQAExecuteResultMapper", "processRtnCancel", objRetParams2);
        }

        return objRetParams;
    }

}
