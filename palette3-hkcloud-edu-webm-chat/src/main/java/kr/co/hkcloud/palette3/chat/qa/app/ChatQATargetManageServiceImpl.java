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
@Service("chatQATargetManageService")
public class ChatQATargetManageServiceImpl implements ChatQATargetManageService
{
    private final TwbComDAO mobjDao;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnMainList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams2 = new TelewebJSON(jsonParams);

        objRetParams2 = mobjDao.select("kr.co.hkcloud.palette3.chat.qa.dao.ChatQATargetManageMapper", "selectWorkTime", jsonParams);

        jsonParams.setString("WORK_ARRAY", objRetParams2.getDataJSON());

        return mobjDao.select("kr.co.hkcloud.palette3.chat.qa.dao.ChatQATargetManageMapper", "selectRtnMainList", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnPreExtractedCount(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.qa.dao.ChatQATargetManageMapper", "selectRtnPreExtractedCount", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON processRtnExtractTarget(TelewebJSON jsonParams) throws TelewebAppException
    {

        return mobjDao.select("kr.co.hkcloud.palette3.chat.qa.dao.ChatQATargetManageMapper", "selectWorkTime", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnExtractTarget(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.insert("kr.co.hkcloud.palette3.chat.qa.dao.ChatQATargetManageMapper", "insertRtnExtractTarget", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnExtractClose(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.chat.qa.dao.ChatQATargetManageMapper", "updateRtnExtractClose", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteRtnExtractReset(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.delete("kr.co.hkcloud.palette3.chat.qa.dao.ChatQATargetManageMapper", "deleteRtnExtractReset", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnEvaluationClose(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.chat.qa.dao.ChatQATargetManageMapper", "updateRtnEvaluationClose", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteRtnExtractRemove(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.delete("kr.co.hkcloud.palette3.chat.qa.dao.ChatQATargetManageMapper", "deleteRtnExtractRemove", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectHaveExtractClose(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.qa.dao.ChatQATargetManageMapper", "selectHaveExtractClose", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectHaveEvaluatedYN(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.qa.dao.ChatQATargetManageMapper", "selectHaveEvaluatedYN", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectNotEvaluatedCount(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.qa.dao.ChatQATargetManageMapper", "selectNotEvaluatedCount", jsonParams);
    }

}
