package kr.co.hkcloud.palette3.chat.history.app;


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
@Service("chatHistoryManageService")
public class ChatHistoryManageServiceImpl implements ChatHistoryManageService
{
    private final TwbComDAO mobjDao;


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnTwbTalkContact(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.chat.history.dao.ChatHistoryManageMapper", "updateRtnTwbTalkContact", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnPageConsHist(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.history.dao.ChatHistoryManageMapper", "selectRtnPageConsHist", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnContent(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.history.dao.ChatHistoryManageMapper", "selectRtnContent", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnPageCnslMgmtByKaom_new(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.history.dao.ChatHistoryManageMapper", "selectRtnPageCnslMgmtByKaom_new", jsonParams);
    }


    /*
     * 저장시 권한 여부 체크 상담사는 당일만 수정 가능
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    @Override
    public boolean checkAuthProcess(TelewebJSON jsonParams) throws TelewebAppException
    {
        boolean blnReturn = false;

        TelewebJSON objTempParams = mobjDao.select("kr.co.hkcloud.palette3.chat.history.dao.ChatHistoryManageMapper", "selectAuthProcess", jsonParams);

        if(objTempParams.getHeaderInt("COUNT") > 0 && objTempParams.getBoolean("IS_AUTH")) {
            blnReturn = true;
        }

        return blnReturn;
    }

}
