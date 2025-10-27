package kr.co.hkcloud.palette3.chat.status.app;


import org.springframework.stereotype.Service;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("ChatStatusAgentAwayHistoryService")
public class ChatStatusAgentAwayHistoryServiceImpl implements ChatStatusAgentAwayHistoryService
{
    private final TwbComDAO mobjDao;


    @Override
    public TelewebJSON selectUserReadyOffHistory(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.chat.status.dao.ChatStatusAgentAwayHistoryMapper", "selectUserReadyOffHistory", jsonParams);
    }
}
