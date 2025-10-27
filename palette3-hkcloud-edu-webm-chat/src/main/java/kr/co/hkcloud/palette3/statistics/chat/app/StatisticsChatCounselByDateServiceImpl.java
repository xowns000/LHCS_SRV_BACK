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
@Service("statisticsChatCounselByDateService")
public class StatisticsChatCounselByDateServiceImpl implements StatisticsChatCounselByDateService
{
    private final TwbComDAO mobjDao;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectStatisticsByDay(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatCounselByDateMapper", "selectStatisticsByDay", jsonParams);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectAlrimSmsList(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatCounselByDateMapper", "selectAlrimSmsList", jsonParams);
    }

}
