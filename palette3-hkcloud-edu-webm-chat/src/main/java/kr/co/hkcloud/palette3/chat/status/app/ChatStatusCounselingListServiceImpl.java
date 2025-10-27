package kr.co.hkcloud.palette3.chat.status.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("chatStatusCounselingListService")
public class ChatStatusCounselingListServiceImpl implements ChatStatusCounselingListService
{
    private final TwbComDAO mobjDao;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnPageCnslMgmtByKaom_new(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.chat.status.dao.ChatStatusCounselingListMapper", "selectRtnPageCnslMgmtByKaom_new", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectCorrectedDate(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.chat.status.dao.ChatStatusCounselingListMapper", "selectCorrectedDate", jsonParams);
    }
}
