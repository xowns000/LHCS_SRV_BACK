package kr.co.hkcloud.palette3.common.code.app;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 연동 코드공통 서비스 인터페이스 구현체
 * 
 * @author Orange
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("lkagCodeCmmnService")
public class LkagCodeCmmnServiceImpl implements LkagCodeCmmnService {

    private final TwbComDAO twbComDao;

    /**
     * 연동 공통코드의 코드,코드명을 검색
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCodeBook(TelewebJSON jsonParams) throws TelewebAppException {

        return twbComDao.select("kr.co.hkcloud.palette3.common.code.dao.LkagCodeCmmnMapper", "selectRtnCodeBook", jsonParams);
    }
}
