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
@Service("statisticsPhoneOutboundService")
public class StatisticsPhoneOutboundServiceImpl implements StatisticsPhoneOutboundService
{

    public final TwbComDAO mobjDao;


    /**
     * 아웃바운드 목록(팝업)을 조회한다.
     * 
     * @param  jsonParam
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectObndListPop(TelewebJSON jsonParam) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneOutboundMapper", "selectObndListPop", jsonParam);
    }


    /**
     * 아웃바운드통계를 조회한다.
     * 
     * @param  jsonParam
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectObndSttcList(TelewebJSON jsonParam) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();

        String searchTy = jsonParam.getString("SEARCH_TY");
        
        if(searchTy.equals("CM")) {
            // 조회구분: 캠페인별
            objParam = mobjDao
                .select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneOutboundMapper", "selectObndSttcCampList", jsonParam);

        }
        else if(searchTy.equals("BD")) {
            // 조회구분: 업무구분별
            objParam = mobjDao
                .select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneOutboundMapper", "selectObndSttcBusiList", jsonParam);

        }
        else if(searchTy.equals("DV")) {
            // 조회구분: 배분상담원별
            objParam = mobjDao
                .select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneOutboundMapper", "selectObndSttcCsltList", jsonParam);
        }
        
        return objParam;
    }
}
