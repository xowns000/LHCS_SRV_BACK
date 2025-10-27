package kr.co.hkcloud.palette3.statistics.phone.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("statisticsPhoneQA2Service")
public class StatisticsPhoneQA2ServiceImpl implements StatisticsPhoneQA2Service
{
    public final TwbComDAO mobjDao;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtn(TelewebJSON jsonParam) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneQA2Mapper", "selectRtn", jsonParam);
    }

}
