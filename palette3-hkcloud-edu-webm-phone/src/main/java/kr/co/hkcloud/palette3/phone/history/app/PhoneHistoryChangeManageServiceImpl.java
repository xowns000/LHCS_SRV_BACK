package kr.co.hkcloud.palette3.phone.history.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("phoneHistoryChangeManageService")
public class PhoneHistoryChangeManageServiceImpl implements PhoneHistoryChangeManageService
{
    private final TwbComDAO mobjDao;


    /**
     * 상담이력변경관리 조회(지원)
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON selectRtnCnslHistChngMngDe(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.history.dao.PhoneHistoryChangeManageMapper", "selectRtnCnslHistChngMngDe", mjsonParams);
    }


    /**
     * 상담이력변경관리 승인
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnCnslHistChngMng(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.phone.history.dao.PhoneHistoryChangeManageMapper", "updateRtnCnslHistChngMng", mjsonParams);
    }
}
