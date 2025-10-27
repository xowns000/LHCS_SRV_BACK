package kr.co.hkcloud.palette3.chat.qa.app;


import java.sql.SQLException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("chatQAExecuteManagePopupService")
public class ChatQAExecuteManagePopupServiceImpl implements ChatQAExecuteManagePopupService
{
    private final TwbComDAO mobjDao;


    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON deleteRtnQaRslt(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.delete("kr.co.hkcloud.palette3.chat.qa.dao.ChatQAExecuteManagePopupMapper", "deleteRtnQaRslt", jsonParams);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON insertRtnQaRslt(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.insert("kr.co.hkcloud.palette3.chat.qa.dao.ChatQAExecuteManagePopupMapper", "insertRtnQaRslt", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnQaEval(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.qa.dao.ChatQAExecuteManagePopupMapper", "selectRtnQaEval", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectChkQaRsltMst(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.qa.dao.ChatQAExecuteManagePopupMapper", "selectChkQaRsltMst", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectChkQaRslt(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.qa.dao.ChatQAExecuteManagePopupMapper", "selectChkQaRslt", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnQaRsltMst(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.chat.qa.dao.ChatQAExecuteManagePopupMapper", "updateRtnQaRsltMst", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON insertRtnQaRsltMst(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.insert("kr.co.hkcloud.palette3.chat.qa.dao.ChatQAExecuteManagePopupMapper", "insertRtnQaRsltMst", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnQaRslt(TelewebJSON jsonParams) throws TelewebAppException
    {
        return null;
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteRtnQaRsltMst(TelewebJSON jsonParams) throws TelewebAppException
    {
        return null;
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnID(TelewebJSON jsonParams) throws TelewebAppException
    {
        return null;
    }

}
