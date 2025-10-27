package kr.co.hkcloud.palette3.common.date.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 날짜공통 서비스 인터페이스 구현체
 * 
 * @author Orange
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("dateCmmnService")
public class DateCmmnServiceImpl implements DateCmmnService
{
    private final TwbComDAO twbComDAO;


    /**
     * 서버의 현재 일시를 포맷을 적용하여 검색한다.
     * 
     * @param  jsonParams
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectServerDate(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.common.date.dao.DateCmmnMapper", "selectServerDate", jsonParams);
    }


    /**
     * 기준시간 기준으로 날짜를 더한 결과 일을 검색한다.
     * 
     * @param  jsonParams
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectAddDate(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.common.date.dao.DateCmmnMapper", "selectAddDate", jsonParams);
    }


    /**
     * 두날짜의 차이를 구분에 따라 계산하여 검색한다.
     * 
     * @param  jsonParams
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectDiffDate(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.common.date.dao.DateCmmnMapper", "selectDiffDate", jsonParams);
    }


    /**
     * @param  jsonParams
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectLastDay(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.common.date.dao.DateCmmnMapper", "selectLastDay", jsonParams);
    }


    /**
     * 서버시간 기준으로 오늘날짜, 3일전, 7주일전, 등의 날짜를 검색한다.
     * 
     * @param  jsonParams
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectDatePattern(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.common.date.dao.DateCmmnMapper", "selectDatePattern", jsonParams);
    }


    /**
     * 서버시간 기준으로 오늘날짜, 3일후, 7일후, 1달후, 3달후의 날짜를 검색한다.
     * 
     * @param  jsonParams
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectAfterDatePattern(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.common.date.dao.DateCmmnMapper", "selectAfterDatePattern", jsonParams);
    }

}
