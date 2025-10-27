package kr.co.hkcloud.palette3.common.prvc.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Description : 개인정보 조회 Impl
 * package  : kr.co.hkcloud.palette3.common.prvc.app
 * filename : PrvcServiceImpl.java
 * Date : 2023. 9. 7.
 * History :
 * - 작성자 : ryucease, 날짜 : 2023. 9. 7., 설명 : 최초작성<br>
 *
 * @author ryucease
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service("prvcService")
public class PrvcServiceImpl implements PrvcService
{
	private final InnbCreatCmmnService innbCreatCmmnService;
    private final TwbComDAO mobjDao;


    /**
     * 개인정보 조회-목록
     */
    @Transactional(readOnly = true)
    public TelewebJSON prvcInqHistList(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.common.voc.dao.PrvcMapper", "prvcInqHistList", mjsonParams);
    }

    /**
     * 개인정보 조회 이력 저장 처리
     */
    @Transactional(readOnly = false)
    public TelewebJSON insertPrvcInqLog(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.insert("kr.co.hkcloud.palette3.common.voc.dao.PrvcMapper", "INSERT_PRVC_INQ_LOG", jsonParams);
    }
}
