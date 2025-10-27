package kr.co.hkcloud.palette3.phone.qa.app;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
@Service("EvlExecutManagePopupService")
public class PhoneQAEvlExecutManageExecutEvlPopupServiceImpl implements PhoneQAEvlExecutManageExecutEvlPopupService
{
    private final TwbComDAO twbComDAO;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnEvSheet(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        TelewebJSON objRetParams2 = new TelewebJSON(jsonParams);

        objRetParams2 = twbComDAO.select("kr.co.hkcloud.palette3.phone.qa.dao.PhoneQAEvlExecutManageExecutEvlPopupMapper", "selectRtnSEQ", jsonParams);

        JSONArray arrSeq = objRetParams2.getDataObject();

        if(arrSeq.size() == 0) {
            objRetParams = twbComDAO.select("kr.co.hkcloud.palette3.phone.qa.dao.PhoneQAEvlExecutManageExecutEvlPopupMapper", "selectRtnEvSheet2", jsonParams);
        }
        else {
            objRetParams = twbComDAO.select("kr.co.hkcloud.palette3.phone.qa.dao.PhoneQAEvlExecutManageExecutEvlPopupMapper", "selectRtnEvSheet", jsonParams);
        }

        return objRetParams;
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnDetails(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = twbComDAO.select("kr.co.hkcloud.palette3.phone.qa.dao.PhoneQAEvlExecutManageExecutEvlPopupMapper", "selectRtnDetails", jsonParams);
        return objRetParams;
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON processRtn(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        TelewebJSON objRetParams2 = new TelewebJSON(jsonParams);
        TelewebJSON objRetParams3 = new TelewebJSON(jsonParams);
        TelewebJSON objRetParams4 = new TelewebJSON(jsonParams);

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String strSessionInfo = "";
        String CNSL_ID = "", REG_ID = "";
        JSONArray arrInsertData = jsonParams.getDataObject("DATA");
        JSONObject temp = null, temp2 = null;

        temp2 = arrInsertData.getJSONObject(0);

        // 상담사의 정보를 가져오는 루틴 
        if(requestAttributes != null) {
            HttpServletRequest objRequest = requestAttributes.getRequest();
            HttpSession objSession = objRequest.getSession();

            strSessionInfo = (String) objSession.getAttribute("TWB_SESSION_INFO");
            if(strSessionInfo != null && !"".equals(strSessionInfo)) {
                REG_ID = strSessionInfo.split("\\|")[0];
            }
        }

        CNSL_ID = temp2.getString("CNSL_ID");

        objRetParams3.setString("CNSL_ID", CNSL_ID);
        objRetParams = twbComDAO.delete("kr.co.hkcloud.palette3.phone.qa.dao.PhoneQAEvlExecutManageExecutEvlPopupMapper", "deleteRtn", objRetParams3);

        for(int i = 0; i < arrInsertData.size(); i++) {
            temp = arrInsertData.getJSONObject(i);
            log.debug("QA_ID: " + temp.getString("QA_ID"));
            log.info("temp : " + temp);
            objRetParams2.setString("QA_ID", temp.getString("QA_ID"));
            objRetParams2.setString("QA_YM", temp.getString("QA_YM"));
            objRetParams2.setString("QA_TY_ID", temp.getString("QA_TY_ID"));
            objRetParams2.setString("QA_SEQ", temp.getString("QA_SEQ"));
            objRetParams2.setString("CUSTCO_ID", temp.getString("CUSTCO_ID"));
            objRetParams2.setString("USER_ID", temp.getString("USER_ID"));
            objRetParams2.setString("EVAL_CN", temp.getString("EVAL_CN"));
            objRetParams2.setString("SCORE_CHK", temp.getString("SCORE_CHK"));
            objRetParams2.setString("CENT_TY", jsonParams.getString("CENT_TY"));
            if(temp.getString("SCORE_CHK").equals("11")) {
                objRetParams2.setString("SCORE_CHK", "1");
            }
            else {
                objRetParams2.setString("SCORE_CHK", "0");
            }

            objRetParams2.setString("CNSL_ID", CNSL_ID);
            objRetParams2.setString("REG_ID", REG_ID);

            objRetParams = twbComDAO.insert("kr.co.hkcloud.palette3.phone.qa.dao.PhoneQAEvlExecutManageExecutEvlPopupMapper", "insertRtn", objRetParams2);
        }

        objRetParams4.setString("CNSL_ID", CNSL_ID);
        objRetParams4.setString("QA_NOTIN", temp2.getString("QA_NOTIN"));
        objRetParams4.setString("QA_CN", temp2.getString("QA_CN"));
        objRetParams4.setString("REG_ID", REG_ID);

        objRetParams = twbComDAO.insert("kr.co.hkcloud.palette3.phone.qa.dao.PhoneQAEvlExecutManageExecutEvlPopupMapper", "updateRtn", objRetParams4);

        return objRetParams;
    }

}
