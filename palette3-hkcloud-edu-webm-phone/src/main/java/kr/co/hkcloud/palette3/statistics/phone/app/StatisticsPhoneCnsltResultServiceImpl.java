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
@Service("statisticsPhoneCnsltResultService")
public class StatisticsPhoneCnsltResultServiceImpl implements StatisticsPhoneCnsltResultService
{

    public final TwbComDAO mobjDao;


    /**
     * 상담결과통계를 조회한다.
     * 
     * @param  jsonParam
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectCnslResultSttc(TelewebJSON jsonParam) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();

        objParam = mobjDao
            .select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneCnsltResultMapper", "selectCnslResultSttc", jsonParam);

        return objParam;
    }

}
