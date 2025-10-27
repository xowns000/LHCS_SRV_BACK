package kr.co.hkcloud.palette3.phone.qa.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * SMS 인터페이스 구현체
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("ResultManageService")
public class PhoneQAResultManageServiceImpl implements PhoneQAResultManageService
{
    private final TwbComDAO mobjDao;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtn(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.qa.dao.PhoneQAResultManageMapper", "selectRtn", jsonParams);
        return objRetParams;
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnEvSheet(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.qa.dao.PhoneQAResultManageMapper", "selectRtnEvSheet", jsonParams);
        return objRetParams;
    }

}
