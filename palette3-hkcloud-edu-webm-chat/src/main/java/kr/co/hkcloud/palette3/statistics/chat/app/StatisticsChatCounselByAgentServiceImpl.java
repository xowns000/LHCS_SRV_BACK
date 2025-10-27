package kr.co.hkcloud.palette3.statistics.chat.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("statisticsChatCounselByAgentService")
public class StatisticsChatCounselByAgentServiceImpl implements StatisticsChatCounselByAgentService {
    private final TwbComDAO mobjDao;

    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectChatAdviceStatListByInCounsel_new(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatCounselByAgentMapper", "selectChatAdviceStatListByInCounsel_new", jsonParams);
    }

    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectChatAdviceStatListByInCounsel_01(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatCounselByAgentMapper", "selectChatAdviceStatListByInCounsel_01", jsonParams);
    }

    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectChatAdviceStatListByInCounsel_02(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatCounselByAgentMapper", "selectChatAdviceStatListByInCounsel_02", jsonParams);
    }

    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnAuthGroup(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatCounselByAgentMapper", "selectRtnAuthGroup", jsonParams);
    }
}
