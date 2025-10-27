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
@Service("statisticsPhoneLMService")
public class StatisticsPhoneLMServiceImpl implements StatisticsPhoneLMService
{
    public final TwbComDAO mobjDao;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnLm(TelewebJSON jsonParam) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneLMMapper", "selectRtnLm", jsonParam);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnLmDetail(TelewebJSON jsonParam) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneLMMapper", "selectRtnLmDetail", jsonParam);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnLmDataRst(TelewebJSON jsonParam) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneLMMapper", "selectRtnLmDataRst", jsonParam);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnLmVe(TelewebJSON jsonParam) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneLMMapper", "selectRtnLmVe", jsonParam);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnLmQs(TelewebJSON jsonParam) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneLMMapper", "selectRtnLmQs", jsonParam);
    }
}
