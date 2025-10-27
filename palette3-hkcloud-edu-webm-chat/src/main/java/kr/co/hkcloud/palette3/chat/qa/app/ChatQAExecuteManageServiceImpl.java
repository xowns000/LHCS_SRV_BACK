package kr.co.hkcloud.palette3.chat.qa.app;


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
@Service("chatQAExecuteManageService")
public class ChatQAExecuteManageServiceImpl implements ChatQAExecuteManageService
{
    private final TwbComDAO mobjDao;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnQABul(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.qa.dao.ChatQAExecuteManageMapper", "selectRtnQABul", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnQA(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.qa.dao.ChatQAExecuteManageMapper", "selectRtnQA", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnQaEvalCnt(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.qa.dao.ChatQAExecuteManageMapper", "selectRtnQaEvalCnt", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnQAIN(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.qa.dao.ChatQAExecuteManageMapper", "selectRtnQAIN", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON processRtnCancel(TelewebJSON jsonParams) throws TelewebAppException
    {

        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        TelewebJSON objRetParams2 = new TelewebJSON(jsonParams);
        JSONArray arrExtractRemove = jsonParams.getDataObject("EXTRACT_REMOVE");
        JSONObject temp = null;

        for(int i = 0; i < arrExtractRemove.size(); i++) {
            temp = arrExtractRemove.getJSONObject(i);

            objRetParams2.setString("TALK_CONTACT_ID", temp.getString("TALK_CONTACT_ID"));
            objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.qa.dao.ChatQAExecuteManageMapper", "processRtnCancel", objRetParams2);
        }

        return objRetParams;
    }
}
