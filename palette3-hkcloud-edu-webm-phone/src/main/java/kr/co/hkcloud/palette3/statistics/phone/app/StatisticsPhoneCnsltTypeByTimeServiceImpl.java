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
@Service("statisticsPhoneCounselTypeByTimeService")
public class StatisticsPhoneCnsltTypeByTimeServiceImpl implements StatisticsPhoneCnsltTypeByTimeService
{
    public final TwbComDAO mobjDao;


    /**
     * 상담유형별(유형별)통계를 조회한다.
     * 
     * @param  jsonParam
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectCnslTypeTimeSttcList(TelewebJSON jsonParam) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();

        objParam = mobjDao
            .select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneCnsltTypeByTimeMapper", "selectCnslTypeTimeSttcList", jsonParam);

        return objParam;
    }


    /**
     * 상담유형별(날짜별)통계를 조회한다.
     * 
     * @param  jsonParam
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectCnslTypeDaySttcList(TelewebJSON jsonParam) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();

        objParam = mobjDao
            .select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneCnsltTypeByTimeMapper", "selectCnslTypeDaySttcList", jsonParam);

        return objParam;
    }
}
