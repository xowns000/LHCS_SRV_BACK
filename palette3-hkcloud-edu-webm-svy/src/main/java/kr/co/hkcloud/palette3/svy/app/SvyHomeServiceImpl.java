package kr.co.hkcloud.palette3.svy.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service("SvyHomeService")
public class SvyHomeServiceImpl implements SvyHomeService {

    public final TwbComDAO mobjDao;
    private final String sqlNameSpace = "kr.co.hkcloud.palette3.svy.dao.SvyHomeMapper";
    
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectStatusStat(TelewebJSON jsonParam) throws TelewebAppException {
        return mobjDao.select(sqlNameSpace, "selectStatusStat", jsonParam);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectBySeStat(TelewebJSON jsonParam) throws TelewebAppException {
        return mobjDao.select(sqlNameSpace, "selectBySeStat", jsonParam);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRspnsRateStat(TelewebJSON jsonParam) throws TelewebAppException {
        return mobjDao.select(sqlNameSpace, "selectRspnsRateStat", jsonParam);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectGoalRateStat(TelewebJSON jsonParam) throws TelewebAppException {
        return mobjDao.select(sqlNameSpace, "selectGoalRateStat", jsonParam);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectBySndngSeStat(TelewebJSON jsonParam) throws TelewebAppException {
        return mobjDao.select(sqlNameSpace, "selectBySndngSeStat", jsonParam);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectApprStat(TelewebJSON jsonParam) throws TelewebAppException {
        return mobjDao.select(sqlNameSpace, "selectApprStat", jsonParam);
    }
    
}
