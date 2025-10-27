package kr.co.hkcloud.palette3.chat.setting.dao;


import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Repository
public class ChatSettingSystemMessageManageDAO
{

    private final TwbComDAO mobjDao;


    @Transactional(readOnly = false)
    public void insertNewSystemMsg(TelewebJSON jsonParams) throws TelewebDaoException
    {
        mobjDao
            .insert("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingSystemMessageManageMapper", "insertNewSystemMsg", jsonParams);
    }


    @Transactional(readOnly = false)
    public void insertNewSystemLinksMsg(TelewebJSON jsonParams) throws TelewebDaoException
    {
        mobjDao
            .insert("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingSystemMessageManageMapper", "insertNewSystemLinksMsg", jsonParams);
    }


    @Transactional(readOnly = false)
    public void deleteSystemMsg(TelewebJSON jsonParams) throws TelewebDaoException
    {
        mobjDao
            .delete("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingSystemMessageManageMapper", "deleteSystemMsg", jsonParams);
    }


    @Transactional(readOnly = false)
    public void deleteSystemMsgLinks(TelewebJSON jsonParams) throws TelewebDaoException
    {
        mobjDao
            .delete("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingSystemMessageManageMapper", "deleteSystemMsgLinks", jsonParams);
    }
}
