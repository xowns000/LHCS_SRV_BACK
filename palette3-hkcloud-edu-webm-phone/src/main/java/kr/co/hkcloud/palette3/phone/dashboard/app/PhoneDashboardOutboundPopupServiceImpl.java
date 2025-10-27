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
@Service("phoneDashboardOutboundPopupService")
public class PhoneDashboardOutboundPopupServiceImpl implements PhoneDashboardOutboundPopupService
{
    private final TwbComDAO mobjDao;


    /**
     * 아웃바운드 진행 현황
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectObndIngList(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        // 아웃바운드 진행 현황
        objRetParams = mobjDao
            .select("kr.co.hkcloud.palette3.phone.dashboard.dao.PhoneDashboardOutboundPopupMapper", "selectObndIngList", mjsonParams);

        return objRetParams;

    }


    /**
     * 아웃바운드 진행 현황 처리결과 변경
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateEotTyChange(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        // 아웃바운드 진행 현황 처리결과 변경
        objRetParams = mobjDao
            .update("kr.co.hkcloud.palette3.phone.dashboard.dao.PhoneDashboardOutboundPopupMapper", "updateEotTyChange", mjsonParams);

        return objRetParams;

    }

}
