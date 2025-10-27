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
@Service("phoneLmGradingManageService")
public class PhoneLmGradingManageServiceImpl implements PhoneLmGradingManageService
{

    private final TwbComDAO            twbComDAO;
    private final InnbCreatCmmnService innbCreatCmmnService;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnLmGrading(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmGradingManageMapper", "selectRtnLmGrading", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnLmGradingDetail(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON retParams = new TelewebJSON(jsonParams);
        TelewebJSON loCount = new TelewebJSON();
        retParams = twbComDAO.select("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmGradingManageMapper", "selectRtnLmGradingDetail", jsonParams);
        loCount = twbComDAO.select("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmGradingManageMapper", "selectRtnLmLoCount", jsonParams);
        retParams.setDataObject("LO_COUNT", loCount);
        return retParams;
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnLmGradingMultiChoice(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON retParams = new TelewebJSON(jsonParams);

        TelewebJSON loCount = new TelewebJSON();
        //객관식(단식,복식),ox형 채점 진행

        retParams = twbComDAO.update("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmGradingManageMapper", "updateRtnLmGrading", jsonParams);
        //서술형 문항 보유 여부 체크
        loCount = twbComDAO.select("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmGradingManageMapper", "selectRtnLmLoCount", jsonParams);
        if(loCount.getInt("LO_COUNT") == 0) {
            //서술형 미보유 시 채점 여부 완료처리
            twbComDAO.update("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmGradingManageMapper", "updateRtnLmGradingDone", jsonParams);
        }

        return retParams;
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnLmUserAns(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON retParams = new TelewebJSON(jsonParams);
        TelewebJSON userAns = new TelewebJSON();
        TelewebJSON userAnsDtl = new TelewebJSON();

        userAns = twbComDAO.select("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmGradingManageMapper", "selectRtnLmUserAns", jsonParams);

        userAnsDtl = twbComDAO.select("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmGradingManageMapper", "selectRtnLmUserAnsDetail", jsonParams);

        retParams.setDataObject("USER_ANS", userAns);
        retParams.setDataObject("USER_ANS_DTL", userAnsDtl);

        return retParams;
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON processRtnLmGrading(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON retParams = new TelewebJSON(jsonParams);

        JSONArray loScList = jsonParams.getDataObject("LO_SC_DATA");

        //객관식(단식,복식),ox형 채점 진행
        twbComDAO.update("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmGradingManageMapper", "updateRtnLmGrading", jsonParams);

        //서술형 채점 진행
        if(loScList != null && loScList.size() > 0) {
            JSONObject loScObj = null;
            TelewebJSON loJson = null;
            for(int i = 0; i < loScList.size(); i++) {
                loScObj = loScList.getJSONObject(i);
                loJson = new TelewebJSON();
                loJson.setString("LM_DATA_ID", jsonParams.getString("LM_DATA_ID"));
                loJson.setString("LM_DATA_RST_SUM", loScObj.getString("LM_DATA_RST_SUM"));
                loJson.setString("LM_QS_ID", loScObj.getString("LM_QS_ID"));
                twbComDAO.update("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmGradingManageMapper", "updateRtnLmGradingLo", loJson);
            }
        }

        //채점 여부 완료처리
        retParams = twbComDAO.update("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmGradingManageMapper", "updateRtnLmGradingDone", jsonParams);

        return retParams;
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnAdminYn(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmGradingManageMapper", "selectRtnAdminYn", jsonParams);

    }

}
