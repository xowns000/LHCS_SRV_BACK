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


/**
 * SMS 인터페이스 구현체
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("EvlPaperManageService")
public class PhoneQAEvlPaperManageServiceImpl implements PhoneQAEvlPaperManageService
{
    private final TwbComDAO            mobjDao;
    private final InnbCreatCmmnService innbCreatCmmnService;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnEvSheet(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.qa.dao.PhoneQAEvlPaperManageMapper", "selectRtnEvSheet", jsonParams);
        return objRetParams;
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectChkQASeet(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.qa.dao.PhoneQAEvlPaperManageMapper", "selectChkQASeet", jsonParams);
        return objRetParams;
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnQASheet(TelewebJSON jsonParams) throws TelewebAppException
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
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.phone.qa.dao.PhoneQAEvlPaperManageMapper", "updateRtnQASheet", jsonParams);
        return objRetParams;
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnQASHEET(TelewebJSON jsonParams) throws TelewebAppException
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
        jsonParams.setString("QA_ID", innbCreatCmmnService.getSeqNo("PLT_PHN_QA_EVAL_SHT_SEQ"));
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.qa.dao.PhoneQAEvlPaperManageMapper", "insertRtnQASHEET", jsonParams);
        return objRetParams;
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnCopySheet(TelewebJSON jsonParams) throws TelewebAppException
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
        jsonParams.setString("QA_ID", innbCreatCmmnService.getSeqNo("PLT_PHN_QA_EVAL_SHT_SEQ"));
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.qa.dao.PhoneQAEvlPaperManageMapper", "insertRtnCopySheet", jsonParams);
        return objRetParams;
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteRtnQASheet(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.phone.qa.dao.PhoneQAEvlPaperManageMapper", "deleteRtnQASheet", jsonParams);
        return objRetParams;
    }

}
