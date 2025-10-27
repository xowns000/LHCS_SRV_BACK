package kr.co.hkcloud.palette3.common.code.app;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 코드공통 서비스 인터페이스 구현체
 * 
 * @author Orange
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("codeCmmnService")
public class CodeCmmnServiceImpl implements CodeCmmnService {

    private final CacheManager cacheManager;  //using debug
    private final TwbComDAO twbComDao;

    /**
     * 공통코드의 코드,코드명을 검색 (캐싱 적용) - jsonParams -> CD_EXPLN 값이 null or empty(SpEL) 일 때만 캐싱한다.
     * 
     * @param jsonParams
     * @return
     */
    @Override
    @Cacheable(value = "palette:cache:code-book", key = "T(kr.co.hkcloud.palette3.config.cache.generates.CacheCodeBookKeyGenerate).generate(#jsonParams)")
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCachingCodeBook(TelewebJSON jsonParams) throws TelewebAppException {
        return twbComDao.select("kr.co.hkcloud.palette3.common.code.dao.CodeCmmnMapper", "selectRtnCodeBook", jsonParams);
    }

    /**
     * 공통코드의 코드,코드명을 검색
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCodeBook(TelewebJSON jsonParams) throws TelewebAppException {

        return twbComDao.select("kr.co.hkcloud.palette3.common.code.dao.CodeCmmnMapper", "selectRtnCodeBook", jsonParams);
    }

    /**
     * 공통회사의,회사명을 검색 (캐싱 적용) - jsonParams -> CD_EXPLN 값이 null or empty(SpEL) 일 때만 캐싱한다.
     * 
     * @param jsonParams
     * @author 정성현
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCachingAspBook(TelewebJSON jsonParams) throws TelewebAppException {

        return twbComDao.select("kr.co.hkcloud.palette3.common.code.dao.CodeCmmnMapper", "selectRtnAspBook", jsonParams);
    }

    /**
     * 채팅채널이 있는 공통회사의,회사명을 검색 (캐싱 적용)
     * 
     * @param jsonParams
     * @author ..
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCachingSenderAspBook(TelewebJSON jsonParams) throws TelewebAppException {

        return twbComDao.select("kr.co.hkcloud.palette3.common.code.dao.CodeCmmnMapper", "selectRtnSenderAspBook", jsonParams);
    }

    /*
     * 시스템관리자가 갖게될 콤보박스, 테이블내에 존재하는 모든 회사를 콤보박스로
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCachingAllAspBook(TelewebJSON jsonParams) throws TelewebAppException {

        return twbComDao.select("kr.co.hkcloud.palette3.common.code.dao.CodeCmmnMapper", "selectRtnCachingAllAspBook", jsonParams);
    }

    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectTranCommCode(TelewebJSON jsonParams) throws TelewebAppException {
        return twbComDao.select("kr.co.hkcloud.palette3.common.code.dao.CodeCmmnMapper", "selectTranCommCode", jsonParams);
    }

}
