package kr.co.hkcloud.palette3.core.palette.asp.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import kr.co.hkcloud.palette3.common.date.util.DateCmmnUtils;
import kr.co.hkcloud.palette3.core.palette.asp.dao.TalkAspCustJpaRepository;
import kr.co.hkcloud.palette3.core.palette.asp.domain.TwbAspCust;
import kr.co.hkcloud.palette3.core.palette.asp.exception.AspCustServiceIsDisabledException;
import kr.co.hkcloud.palette3.core.palette.asp.exception.AspCustServiceIsExpiredException;
import kr.co.hkcloud.palette3.core.palette.asp.exception.AspCustServiceNotStartedException;
import kr.co.hkcloud.palette3.core.palette.asp.exception.AspDoesNotExistException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 고객사관리 서비스 구현체
 * 
 * @author Orange
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("talkAspCustService")
public class TalkAspCustServiceImpl implements TalkAspCustService
{
//    private final DateUtils                dateUtils;
    private final TalkAspCustJpaRepository aspCustJpaRepository;


    @Override
    @Transactional(readOnly = true)
    public TwbAspCust findByCustcoId(String custcoId) throws TelewebAppException
    {
        TwbAspCust aspCust = aspCustJpaRepository.findByCustcoId(Integer.parseInt(custcoId));
        //존재하지 않음
        if(ObjectUtils.isEmpty(aspCust)) { throw new AspDoesNotExistException(); }
        //비활성
        if("N".equals(aspCust.getCustcoId())) { throw new AspCustServiceIsDisabledException(); }

        Long now = DateCmmnUtils.toEpochMilli();

        //개시 안됨
        //고객사 테이블 변경으로 인한 로직 변경
        String srvcBgngDt = "202304270000";	//aspCust.getSrvcBgngDt();
        Long servStart = DateCmmnUtils.getDateStringToTimestamp(srvcBgngDt);
        if(now < servStart) { throw new AspCustServiceNotStartedException(); }

        //만료됨
        //고객사 테이블 변경으로 인한 로직 변경
        String srvcEndDt = "299912310000";	//aspCust.getSrvcEndDt();
        Long servEnd = DateCmmnUtils.getDateStringToTimestamp(srvcEndDt);
        if(now > servEnd) { throw new AspCustServiceIsExpiredException(); }
        return aspCust;
    }

}
