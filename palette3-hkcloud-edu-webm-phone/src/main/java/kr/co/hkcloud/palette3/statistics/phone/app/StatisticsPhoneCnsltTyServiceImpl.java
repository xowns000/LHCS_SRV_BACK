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
@Service("statisticsPhoneCnsltTypeService")
public class StatisticsPhoneCnsltTyServiceImpl implements StatisticsPhoneCnsltTyService
{
    public final TwbComDAO mobjDao;


    /**
     * 상담유형별(유형별) 통계를 조회한다.
     * 
     * @param  jsonParam
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectCnslTypeTypeSttcList(TelewebJSON jsonParam) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();

        String dataVal = jsonParam.getString("DATA_VAL");

        if(dataVal.equals("CNT")) {

            objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneCnsltTyMapper", "selectCnslTypeCntSttcList", jsonParam);
        }
        else {

            objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneCnsltTyMapper", "selectCnslTypeCallSttcList", jsonParam);
        }
        return objParam;
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON SELECT_TWB_BTCB01_MNG(TelewebJSON jsonParam) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();
        objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneCnsltTyMapper", "SELECT_TWB_BTCB01_MNG", jsonParam);
        return objParam;
    }

}
