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
@Service("StatisticsPhoneOutboundDailyReportService")
public class StatisticsPhoneOutboundDailyReportServiceImpl implements StatisticsPhoneOutboundDailyReportService
{
    public final TwbComDAO mobjDao;


    /**
     * 아웃바운드일일통계를 조회한다.
     * 
     * @param  jsonParam
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectObndAdaySttcRprtList(TelewebJSON jsonParam) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();

        objParam = mobjDao
            .select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneOutboundDailyReportMapper", "selectObndAdaySttcRprtList", jsonParam);

        return objParam;
    }


    /**
     * 아웃바운드일일통계(건수)를 조회한다.
     * 
     * @param  jsonParam
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectObndAdaySttcRprtCntList(TelewebJSON jsonParam) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();

        objParam = mobjDao
            .select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneOutboundDailyReportMapper", "selectObndAdaySttcRprtCntList", jsonParam);

        return objParam;
    }

}
