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
@Service("StatisticsChatCuttService")
public class StatisticsChatCuttServiceImpl implements StatisticsChatCuttService
{
    private final TwbComDAO mobjDao;

    /**
     * 채팅상담 종합 - 채팅상담 현황(전체)
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON chatCuttTotalStatistics(TelewebJSON jsonParam) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();

        objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatCuttMapper", "chatCuttTotalStatistics", jsonParam);

        return objParam;
    }

    /**
     * 채팅상담 종합 - SNS채널별 현황
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON chatCuttChnTypeStatistics(TelewebJSON jsonParam) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();

        objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatCuttMapper", "chatCuttChnTypeStatistics", jsonParam);

        return objParam;
    }

    /**
     * 채팅상담 종합 - 평균 상담시간 현황
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON chatCuttHrStatistics(TelewebJSON jsonParam) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();

        objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatCuttMapper", "chatCuttHrStatistics", jsonParam);

        return objParam;
    }

    /**
     * 채팅상담 종합 - 상담 유형별 현황
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON chatCuttTypeStatistics(TelewebJSON jsonParam) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();

        objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatCuttMapper", "chatCuttTypeStatistics", jsonParam);

        return objParam;
    }

    /**
     * 채팅상담 종합 - 원간 상담 추이
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON chatCuttMonStatistics(TelewebJSON jsonParam) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();

        objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatCuttMapper", "chatCuttMonStatistics", jsonParam);

        return objParam;
    }

    /**
     * 채팅상담 종합 - 통합 생산성 현황
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON chatCuttPrdctnStatistics(TelewebJSON jsonParam) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();

        objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatCuttMapper", "chatCuttPrdctnStatistics", jsonParam);

        return objParam;
    }
    /**
     * 채팅상담 상담 직원별 통계 - 상단 건수
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON chatCuttCuslStatistics(TelewebJSON jsonParam) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();

        objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatCuttMapper", "chatCuttCuslStatistics", jsonParam);

        return objParam;
    }

    /**
     * 채팅상담 직원별 통계 - 그리드 데이터
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON chatCuttCuslStatisticsList(TelewebJSON jsonParam) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();

        objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatCuttMapper", "chatCuttCuslStatisticsList", jsonParam);

        return objParam;
    }

    /**
     * 채팅상담 문의유형 통계 - 그리드 데이터
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectStatisticsByInqryType(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatCuttMapper", "selectStatisticsByInqryType", jsonParams);
    }

    /**
     * 채팅상담 문의유형 통계 - 문의유형 데이터
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectInqryTypeTree(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatCuttMapper", "selectInqryTypeTree", jsonParams);
    }

    /**
     * 채팅상담 유형별 데이터
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectCuttTypeStatistics(TelewebJSON jsonParam) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();

        objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatCuttMapper", "selectCuttTypeStatistics", jsonParam);

        return objParam;
    }

    /**
     * 채팅상담 일자별 통계 - 선 그래프
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON chatCuttDateStatistics(TelewebJSON jsonParam) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();

        objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatCuttMapper", "chatCuttDateStatistics", jsonParam);

        return objParam;
    }

    /**
     * 채팅상담 일자별 통계 - 그리드 데이터
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON chatCuttDateStatisticsList(TelewebJSON jsonParam) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();

        objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatCuttMapper", "chatCuttDateStatisticsList", jsonParam);

        return objParam;
    }


    /**
     * 채팅상담 통합생산성 통계 - 선 그래프
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectCuttPrdctnStatistics(TelewebJSON jsonParam) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();

        objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatCuttMapper", "selectCuttPrdctnStatistics", jsonParam);

        return objParam;
    }

    /**
     * 채팅상담 통합생산성 통계 - 그리드 데이터
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectCuttPrdctnStatisticsList(TelewebJSON jsonParam) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();

        objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatCuttMapper", "selectCuttPrdctnStatisticsList", jsonParam);

        return objParam;
    }

    /**
     * 채팅통계 상담건수 포함항목 설정값 조회
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON getStatStng(TelewebJSON jsonParam) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();

        objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatCuttMapper", "getStatStng", jsonParam);

        return objParam;
    }

}
