package kr.co.hkcloud.palette3.phone.dashboard.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("phoneDashboardOutboundService")
public class PhoneDashboardOutboundServiceImpl implements PhoneDashboardOutboundService
{
    private final TwbComDAO mobjDao;


    /**
     * 아웃바운드모니터링 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectObndCallList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        //콜백 목록을 조회한다.
        objRetParams = mobjDao
            .select("kr.co.hkcloud.palette3.phone.dashboard.dao.PhoneDashboardOutboundMapper", "selectObndCallList", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }

}
