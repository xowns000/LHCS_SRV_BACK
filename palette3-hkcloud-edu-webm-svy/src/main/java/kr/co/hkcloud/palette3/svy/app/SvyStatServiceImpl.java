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
@Service("svyStatService")
public class SvyStatServiceImpl implements SvyStatService {
    
    private final TwbComDAO mobjDao;
    private String namespace = "kr.co.hkcloud.palette3.svy.dao.SvyStatMapper";
    
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectSendList(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectSendList", mjsonParams);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectSendTypeStat(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectSendTypeStat", mjsonParams);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRspnsStat(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectRspnsStat", mjsonParams);
    }
}
