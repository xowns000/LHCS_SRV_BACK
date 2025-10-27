package kr.co.hkcloud.palette3.statistics.chat.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("StatisticsChatCuttCuslService")
public class StatisticsChatCuttCuslServiceImpl implements StatisticsChatCuttCuslService
{
    private final TwbComDAO mobjDao;

//    /**
//     * 채팅상담 상담 직원별 통계 - 상단 건수
//     */
//    @Override
//    @Transactional(readOnly = true)
//    public TelewebJSON chatCuttCuslStatistics(TelewebJSON jsonParam) throws TelewebAppException
//    {
//        TelewebJSON objParam = new TelewebJSON();
//
//        objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatCuttCuslMapper", "chatCuttCuslStatistics", jsonParam);
//
//        return objParam;
//    }
//
//    /**
//     * 채팅상담 직원별 통계 - 그리드 데이터
//     */
//    @Override
//    @Transactional(readOnly = true)
//    public TelewebJSON chatCuttCuslStatisticsList(TelewebJSON jsonParam) throws TelewebAppException
//    {
//        TelewebJSON objParam = new TelewebJSON();
//
//        objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatCuttCuslMapper", "chatCuttCuslStatisticsList", jsonParam);
//
//        return objParam;
//    }
    

//    /**
//     * 채팅상담 일자별 통계 - 선 그래프
//     */
//    @Override
//    @Transactional(readOnly = true)
//    public TelewebJSON chatCuttDateStatistics(TelewebJSON jsonParam) throws TelewebAppException
//    {
//        TelewebJSON objParam = new TelewebJSON();
//
//        objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatCuttCuslMapper", "chatCuttDateStatistics", jsonParam);
//
//        return objParam;
//    }
//
//    /**
//     * 채팅상담 일자별 통계 - 그리드 데이터
//     */
//    @Override
//    @Transactional(readOnly = true)
//    public TelewebJSON chatCuttDateStatisticsList(TelewebJSON jsonParam) throws TelewebAppException
//    {
//        TelewebJSON objParam = new TelewebJSON();
//
//        objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatCuttCuslMapper", "chatCuttDateStatisticsList", jsonParam);
//
//        return objParam;
//    }
    
//
//    /**
//     * 채팅상담 통합생산성 통계 - 선 그래프
//     */
//    @Override
//    @Transactional(readOnly = true)
//    public TelewebJSON chatCuttPrdctnStatistics(TelewebJSON jsonParam) throws TelewebAppException
//    {
//        TelewebJSON objParam = new TelewebJSON();
//
//        objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatCuttCuslMapper", "chatCuttPrdctnStatistics", jsonParam);
//
//        return objParam;
//    }
//
//    /**
//     * 채팅상담 통합생산성 통계 - 그리드 데이터
//     */
//    @Override
//    @Transactional(readOnly = true)
//    public TelewebJSON chatCuttPrdctnStatisticsList(TelewebJSON jsonParam) throws TelewebAppException
//    {
//        TelewebJSON objParam = new TelewebJSON();
//
//        objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatCuttCuslMapper", "chatCuttPrdctnStatisticsList", jsonParam);
//
//        return objParam;
//    }

//    /**
//     * 채팅상담 유형별 데이터
//     */
//    @Override
//    @Transactional(readOnly = true)
//    public TelewebJSON chatCuttTypeStatistics(TelewebJSON jsonParam) throws TelewebAppException
//    {
//        TelewebJSON objParam = new TelewebJSON();
//
//        objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatCuttCuslMapper", "chatCuttTypeStatistics", jsonParam);
//
//        return objParam;
//    }
//
    
}
