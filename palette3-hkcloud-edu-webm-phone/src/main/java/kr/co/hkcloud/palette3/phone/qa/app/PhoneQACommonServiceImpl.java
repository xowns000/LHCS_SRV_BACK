package kr.co.hkcloud.palette3.phone.qa.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("phoneQACommonService")
public class PhoneQACommonServiceImpl implements PhoneQACommonService
{
    private final TwbComDAO twbComDAO;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnBeforeMonthLastDay(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.palette3.phone.qa.dao.PhoneQACommonMapper", "selectRtnBeforeMonthLastDay", jsonParams);
    }

}
