package kr.co.hkcloud.palette3.phone.qa.app;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
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
@Service("TrgetManageService")
public class PhoneQATrgetManageServiceImpl implements PhoneQATrgetManageService
{
    private final TwbComDAO            mobjDao;
    private final InnbCreatCmmnService innbCreatCmmnService;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtn(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String strSessionInfo = "";
        String REG_ID = "";

        // 상담사의 정보를 가져오는 루틴
        if(requestAttributes != null) {
            HttpServletRequest objRequest = requestAttributes.getRequest();
            HttpSession objSession = objRequest.getSession();

            strSessionInfo = (String) objSession.getAttribute("TWB_SESSION_INFO");
            if(strSessionInfo != null && !"".equals(strSessionInfo)) {
                REG_ID = strSessionInfo.split("\\|")[0];
            }
        }
        // 상담사의 아이디를 세팅
        jsonParams.setString("REG_ID", REG_ID);
        log.debug("TrgetManageService");
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.qa.dao.PhoneQATrgetManageMapper", "selectRtn", jsonParams);
        return objRetParams;
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnDetails(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.qa.dao.PhoneQATrgetManageMapper", "selectRtnDetails", jsonParams);
        return objRetParams;
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertRtn(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        TelewebJSON objRetParams2 = new TelewebJSON(jsonParams);
        TelewebJSON objRetParams3 = new TelewebJSON(jsonParams);
        TelewebJSON objRetParams4 = new TelewebJSON(jsonParams);
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String strSessionInfo = "";
        String REG_ID = "";
        String QA_ID = "";
        JSONObject temp = null;

        JSONArray arrExtractRemove = jsonParams.getDataObject("EXTRACT_REMOVE");
        JSONArray arrExtractRemove2 = null;

        log.debug("arrExtractRemoveSize(): " + arrExtractRemove.size());
        // 상담사의 정보를 가져오는 루틴 
        if(requestAttributes != null) {
            HttpServletRequest objRequest = requestAttributes.getRequest();
            HttpSession objSession = objRequest.getSession();

            strSessionInfo = (String) objSession.getAttribute("TWB_SESSION_INFO");
            if(strSessionInfo != null && !"".equals(strSessionInfo)) {
                REG_ID = strSessionInfo.split("\\|")[0];
            }
        }

        // 대상 건수 만큼 for문을 돌린다.
        for(int i = 0; i < arrExtractRemove.size(); i++) {
            temp = arrExtractRemove.getJSONObject(i);
            objRetParams4.setString("CNSL_ID", temp.getString("CNSL_ID"));
            // 해당 건수가 있는지 조회
            objRetParams3 = mobjDao.select("kr.co.hkcloud.palette3.phone.qa.dao.PhoneQATrgetManageMapper", "selectRtnQA_ID", objRetParams4);
            // 해당 건수가 있으면 업데이트를 하기 위해 QA_ID를 넣어준다.
            arrExtractRemove2 = objRetParams3.getDataObject("DATA");
            if(arrExtractRemove2.size() > 0) {
                QA_ID = objRetParams3.getString("QA_ID");
            }
            // 각 입력 파라메터 세팅
            objRetParams2.setString("QA_YM", temp.getString("QA_YM"));
            objRetParams2.setString("QA_SEQ", temp.getString("QA_SEQ"));
            objRetParams2.setString("CNSL_ID", temp.getString("CNSL_ID"));
            objRetParams2.setString("USER_ID", temp.getString("USER_ID"));
            objRetParams2.setString("REG_ID", REG_ID);
            objRetParams2.setString("CUSTCO_ID", temp.getString("CUSTCO_ID"));
            objRetParams2.setString("QA_EXT_CHK", "Y");
            objRetParams2.setString("CENT_TY", temp.getString("CENT_TY"));
            // 해당 건수가 없으면 새로등록
            if(QA_ID.equals("")) {
                objRetParams2.setString("QA_ID", innbCreatCmmnService.getSeqNo("PLT_PHN_QA_EVAL_SHT_SEQ"));
                objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.qa.dao.PhoneQATrgetManageMapper", "insertRtn", objRetParams2);
            }
            // 해당 건수가 있으면 업데이트를 한다.
            else {
                objRetParams = mobjDao.update("kr.co.hkcloud.palette3.phone.qa.dao.PhoneQATrgetManageMapper", "updateRtnInsert", objRetParams2);
            }
        }

        return objRetParams;
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateRtn(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String strSessionInfo = "";
        String REG_ID = "";
        JSONObject temp = null;

        JSONArray arrExtractRemove = jsonParams.getDataObject("EXTRACT_REMOVE");

        // 상담사의 정보를 가져오는 루틴
        if(requestAttributes != null) {
            HttpServletRequest objRequest = requestAttributes.getRequest();
            HttpSession objSession = objRequest.getSession();

            strSessionInfo = (String) objSession.getAttribute("TWB_SESSION_INFO");
            if(strSessionInfo != null && !"".equals(strSessionInfo)) {
                REG_ID = strSessionInfo.split("\\|")[0];
            }
        }

        for(int i = 0; i < arrExtractRemove.size(); i++) {
            temp = arrExtractRemove.getJSONObject(i);
            log.debug("CNSL_ID: " + temp.getString("CNSL_ID"));
            // 상담사의 아이디를 세팅
            jsonParams.setString("REG_ID", REG_ID);
            jsonParams.setString("CUSTCO_ID", temp.getString("CUSTCO_ID"));
            jsonParams.setString("QA_ID", temp.getString("QA_ID"));
        }

        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.phone.qa.dao.PhoneQATrgetManageMapper", "updateRtn", jsonParams);
        return objRetParams;
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCheckYN(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.qa.dao.PhoneQATrgetManageMapper", "selectRtnCheckYN", jsonParams);
        return objRetParams;
    }


    @Override
    @Transactional(readOnly = true)
    public Boolean selectHaveExtractClose(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        Boolean bResult = false;
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.qa.dao.PhoneQATrgetManageMapper", "selectHaveExtractClose", jsonParams);

        String result = objRetParams.getString("HAVE_EXTRACT_CLOSE", 0);
        if(result == "N") {
            bResult = false;
        }
        else {
            bResult = true;
        }

        return bResult;
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnInsert(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.phone.qa.dao.PhoneQATrgetManageMapper", "updateRtnInsert", jsonParams);
        return objRetParams;
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnQA_ID(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.qa.dao.PhoneQATrgetManageMapper", "selectRtnQA_ID", jsonParams);
        return objRetParams;
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteRtn(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        TelewebJSON objRetParams2 = new TelewebJSON(jsonParams);
        JSONObject temp = null;
        JSONArray arrExtractRemove = jsonParams.getDataObject("EXTRACT_REMOVE");
        for(int i = 0; i < arrExtractRemove.size(); i++) {

            temp = arrExtractRemove.getJSONObject(i);
            log.debug("CNSL_ID: " + temp.getString("CNSL_ID"));
            objRetParams2.setString("CNSL_ID", temp.getString("CNSL_ID"));
            objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.phone.qa.dao.PhoneQATrgetManageMapper", "deleteRtn", objRetParams2);
        }

        return objRetParams;
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON processRtnExtractClose(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        TelewebJSON objRetParams2 = new TelewebJSON(jsonParams);
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String strSessionInfo = "";
        JSONObject temp = null;
        JSONArray arrExtractRemove = jsonParams.getDataObject("EXTRACT_REMOVE");

        String REG_ID = "";

        // 상담사의 정보를 가져오는 루틴
        if(requestAttributes != null) {
            HttpServletRequest objRequest = requestAttributes.getRequest();
            HttpSession objSession = objRequest.getSession();

            strSessionInfo = (String) objSession.getAttribute("TWB_SESSION_INFO");
            if(strSessionInfo != null && !"".equals(strSessionInfo)) {
                REG_ID = strSessionInfo.split("\\|")[0];
            }
        }

        for(int i = 0; i < arrExtractRemove.size(); i++) {
            temp = arrExtractRemove.getJSONObject(i);
            log.debug("CNSL_ID: " + temp.getString("CNSL_ID"));
            log.info("temp : " + temp);
            objRetParams2.setString("CNSL_ID", temp.getString("CNSL_ID"));
            objRetParams2.setString("QA_ID", temp.getString("QA_ID"));
            objRetParams2.setString("QA_USER_ID", REG_ID);
            objRetParams2.setString("CUSTCO_ID", temp.getString("CUSTCO_ID"));
            objRetParams = mobjDao.update("kr.co.hkcloud.palette3.phone.qa.dao.PhoneQATrgetManageMapper", "processRtnExtractClose", objRetParams2);
        }

        return objRetParams;
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCnslCode(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.qa.dao.PhoneQATrgetManageMapper", "selectRtnCnslCode", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCnslCodeDetail(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.qa.dao.PhoneQATrgetManageMapper", "selectRtnCnslCodeDetail", jsonParams);
    }

}
