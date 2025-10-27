package kr.co.hkcloud.palette3.omnione.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service("omniOneCertService")
public class OmniOneCertServiceImpl implements OmniOneCertService {

    @Autowired
    Environment environment;

    private final TwbComDAO mobjDao;

    /**
     * OmniOne 간편인증 CP CODE 및 SITE KEY 조회
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON omniGetInfo(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.omnione.dao.OmniOneCertMapper", "omniGetInfo", jsonParams);
    }

    /**
     * OmniOne 간편인증 요청 이력 저장
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON omniOneRequestReg(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.insert("kr.co.hkcloud.palette3.omnione.dao.OmniOneCertMapper", "omniOneRequestReg", jsonParams);
    }

    /**
     * OmniOne 간편인증 결과 이력 저장
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON omniOneResultReg(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.insert("kr.co.hkcloud.palette3.omnione.dao.OmniOneCertMapper", "omniOneResultReg", jsonParams);
    }
}
