package kr.co.hkcloud.palette3.chat.qa.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("chatQAEvaluationPaperManageService")
public class ChatQAEvaluationPaperManageServiceImpl implements ChatQAEvaluationPaperManageService
{
    private final TwbComDAO mobjDao;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnEvSheet(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.qa.dao.ChatQAEvaluationPaperManageMapper", "selectRtnEvSheet", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectChkQASeet(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.qa.dao.ChatQAEvaluationPaperManageMapper", "selectChkQASeet", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnQASheet(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.chat.qa.dao.ChatQAEvaluationPaperManageMapper", "updateRtnQASheet", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnQASHEET(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.insert("kr.co.hkcloud.palette3.chat.qa.dao.ChatQAEvaluationPaperManageMapper", "insertRtnQASHEET", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnCopySheet(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.insert("kr.co.hkcloud.palette3.chat.qa.dao.ChatQAEvaluationPaperManageMapper", "insertRtnCopySheet", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteRtnQASheet(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.delete("kr.co.hkcloud.palette3.chat.qa.dao.ChatQAEvaluationPaperManageMapper", "deleteRtnQASheet", jsonParams);
    }

}
