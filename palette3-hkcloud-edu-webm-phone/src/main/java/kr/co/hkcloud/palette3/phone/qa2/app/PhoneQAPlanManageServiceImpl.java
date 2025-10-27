package kr.co.hkcloud.palette3.phone.qa2.app;


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
@Service("phoneQAPlanManageService")
public class PhoneQAPlanManageServiceImpl implements PhoneQAPlanManageService
{
    private final TwbComDAO twbComDAO;

    private final InnbCreatCmmnService innbCreatCmmnService;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnQa(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAPlanManageMapper", "selectRtnQa", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnQa(TelewebJSON jsonParams) throws TelewebAppException
    {
        String qaId = jsonParams.getString("QA_ID");
        if(qaId == null || "".equals(qaId)) {
            jsonParams.setString("QA_ID", innbCreatCmmnService.getSeqNo("PLT_QA"));
        }
        return twbComDAO.insert("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAPlanManageMapper", "insertRtnQa", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnEvlPaper(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAPlanManageMapper", "selectRtnEvlPaper", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteRtnQa(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.delete("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAPlanManageMapper", "deleteRtnQa", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnQa(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = twbComDAO.update("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAPlanManageMapper", "updateRtnQa", jsonParams);

        if(jsonParams.getString("STEP").equals("3")) {
            //대상발췌
            if(jsonParams.getString("QA_TG_TY").equals("10")) {
                //콜
                objRetParams = twbComDAO.insert("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAPlanManageMapper", "insertRtnPhoneQaData", jsonParams);
            }
            else if(jsonParams.getString("QA_TG_TY").equals("20")) {
                //채팅
                objRetParams = twbComDAO.insert("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAPlanManageMapper", "insertRtnChatQaData", jsonParams);
            }
        }

        return objRetParams;
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnPhoneCnslHist(TelewebJSON jsonParams) throws TelewebAppException
    {
        String qaTgCnslTime = jsonParams.getString("QA_TG_CNSL_TIME");

        if(qaTgCnslTime != null && !qaTgCnslTime.equals("")) {
            jsonParams.setInt("QA_TG_CNSL_TIME", Integer.valueOf(qaTgCnslTime) * 60);
        }

        return twbComDAO.select("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAPlanManageMapper", "selectRtnPhoneCnslHist", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnChatCnslHist(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAPlanManageMapper", "selectRtnChatCnslHist", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnQaDivCnt(TelewebJSON jsonParams) throws TelewebAppException
    {

        return twbComDAO.select("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAPlanManageMapper", "selectRtnQaDivCnt", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnQaDiv(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAPlanManageMapper", "selectRtnQaDiv", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON processRtnQaDiv(TelewebJSON jsonParams) throws TelewebAppException
    {
        JSONArray qaJson = new JSONArray();
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        qaJson = jsonParams.getDataObject("QA_LIST");

        if(qaJson != null && qaJson.size() > 0) {
            TelewebJSON tempJson = null;
            JSONObject qaObj = null;
            for(int i = 0; i < qaJson.size(); i++) {
                qaObj = qaJson.getJSONObject(i);
                tempJson = new TelewebJSON();
                tempJson.setString("QA_ID", jsonParams.getString("QA_ID"));
                tempJson.setString("ASP_NEWCUST_KEY", jsonParams.getString("ASP_NEWCUST_KEY"));
                tempJson.setString("DIV_USER_ID", qaObj.getString("DIV_USER_ID"));
                tempJson.setString("DIV_USER_NM", qaObj.getString("DIV_USER_NM"));
                tempJson.setString("QA_DATA", qaObj.getString("QA_DATA"));  //건수 (분배, 회수)

                if(jsonParams.getString("DIV_FLAG").equals("R")) {
                    //회수
                    objRetParams = twbComDAO.update("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAPlanManageMapper", "updateRtnQaDivRecall", tempJson);
                }
                else if(jsonParams.getString("DIV_FLAG").equals("D")) {
                    //분배
                    objRetParams = twbComDAO.update("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAPlanManageMapper", "updateRtnQaDiv", tempJson);
                }
            }
        }
        return objRetParams;
    }

}
