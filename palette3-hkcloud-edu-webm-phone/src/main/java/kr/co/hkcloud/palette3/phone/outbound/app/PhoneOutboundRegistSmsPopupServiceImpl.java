package kr.co.hkcloud.palette3.phone.outbound.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("phoneOutboundRegistSmsPopupService")
public class PhoneOutboundRegistSmsPopupServiceImpl implements PhoneOutboundRegistSmsPopupService
{
    private final TwbComDAO mobjDao;


    /**
     * 아웃바운드 공제단말 SMS전송 고객 조회
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectObndSmsTotSend(TelewebJSON mjsonParams) throws TelewebAppException
    {

        TelewebJSON objRetParams = new TelewebJSON();

        // 아웃바운드 공제단말 SMS전송 고객 조회
        objRetParams = mobjDao
            .select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistSmsPopup", "selectObndCustMainList", mjsonParams);

        return objRetParams;

    }


    /**
     * 아웃바운드 업무지원 SMS전송 고객 조회
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectObndSmsTotSendE(TelewebJSON mjsonParams) throws TelewebAppException
    {

        TelewebJSON objRetParams = new TelewebJSON();

        // 아웃바운드 업무지원 SMS전송 고객 조회
        objRetParams = mobjDao
            .select("kr.co.cu.cuics.main.ics.dao.BusiCustObndPerfMapper", "selectObndCustMainList", mjsonParams);

        return objRetParams;

    }
}
