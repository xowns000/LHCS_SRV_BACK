package kr.co.hkcloud.palette3.phone.lm.app;


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
@Service("phoneLmUsRaterManageService")
public class PhoneLmUsRaterManageServiceImpl implements PhoneLmUsRaterManageService
{
    private final TwbComDAO twbComDAO;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnLmUsRater(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmUsRaterManageMapper", "selectRtnLmUsRater", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnMngrUser(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmUsRaterManageMapper", "selectRtnMngrUser", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON processRtnLmUsRater(TelewebJSON jsonParams) throws TelewebAppException
    {
        JSONArray dataArray = jsonParams.getDataObject("raterUser");
        TelewebJSON retParams = new TelewebJSON(jsonParams);
        twbComDAO.insert("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmUsRaterManageMapper", "deleteRtnLmUsRater", jsonParams);
        //삭제 후 재등록
        if(dataArray != null && dataArray.size() > 0) {
            TelewebJSON tempJson = null;
            for(int i = 0; i < dataArray.size(); i++) {
                JSONObject jsonObject = dataArray.getJSONObject(i);
                tempJson = new TelewebJSON();
                tempJson.setString("USER_ID", jsonObject.getString("USER_ID"));
                tempJson.setString("ASP_NEWCUST_KEY", jsonParams.getString("ASP_NEWCUST_KEY"));
                tempJson.setString("USER_NM", jsonObject.getString("USER_NM"));
                tempJson.setString("REG_ID", jsonParams.getString("REG_ID"));
                retParams = twbComDAO.insert("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmUsRaterManageMapper", "insertRtnLmUsRater", tempJson);
            }
        }
        return retParams;
    }
}
