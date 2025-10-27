package kr.co.hkcloud.palette3.statistics.phone.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("StatisticsPhoneCuttService")
public class StatisticsPhoneCuttServiceImpl implements StatisticsPhoneCuttService
{
    public final TwbComDAO mobjDao;

    /**
     * 전화상담 종합 - 전화상담 현황(전체)
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON phoneCuttTotalStatistics(TelewebJSON jsonParam) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();

        objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneCuttMapper", "phoneCuttTotalStatistics", jsonParam);

        return objParam;
    }

    /**
     * 전화상담 종합 - 콜 유형별 현황
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON phoneCuttClTypeStatistics(TelewebJSON jsonParam) throws TelewebAppException
    {
    	TelewebJSON objParam = new TelewebJSON();
    	
    	objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneCuttMapper", "phoneCuttClTypeStatistics", jsonParam);
    	
    	return objParam;
    }

    /**
     * 전화상담 종합 - 통화 시간별 추이
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON phoneCuttPhnHrStatistics(TelewebJSON jsonParam) throws TelewebAppException
    {
    	TelewebJSON objParam = new TelewebJSON();
    	
    	objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneCuttMapper", "phoneCuttPhnHrStatistics", jsonParam);
    	
    	return objParam;
    }

    
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON phoneCuttIvrStatistics(TelewebJSON jsonParam) throws TelewebAppException
    {
    	TelewebJSON objParam = new TelewebJSON();
    	
    	objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneCuttMapper", "phoneCuttIvrStatistics", jsonParam);
    	
    	return objParam;
    }

    /**
     * 전화상담 종합 - 월간 상담 추이
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON phoneCuttMonStatistics(TelewebJSON jsonParam) throws TelewebAppException
    {
    	TelewebJSON objParam = new TelewebJSON();
    	
    	objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneCuttMapper", "phoneCuttMonStatistics", jsonParam);
    	
    	return objParam;
    }

    /**
     * 부서/개인별 통계
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON phoneCuttDeptCuslStatistics(TelewebJSON jsonParam) throws TelewebAppException
    {
    	TelewebJSON objParam = new TelewebJSON();
    	
    	objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneCuttMapper", "phoneCuttDeptCuslStatistics", jsonParam);
    	
    	return objParam;
    }

    /**
     * 상담유형별 통계
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON phoneCuttTypetatistics(TelewebJSON jsonParam) throws TelewebAppException
    {
    	TelewebJSON objParam = new TelewebJSON();
    	
    	objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneCuttMapper", "phoneCuttTypetatistics", jsonParam);
    	
    	return objParam;
    }

    /**
     * 일자별 통계 현황
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON phoneCuttDailyStatistics(TelewebJSON jsonParam) throws TelewebAppException
    {
    	TelewebJSON objParam = new TelewebJSON();
    	
    	objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneCuttMapper", "phoneCuttDailyStatistics", jsonParam);
    	
    	return objParam;
    }

    /**
     * 캠페인 유형별 타겟 통계
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON phoneCuttCpiSeStatistics(TelewebJSON jsonParam) throws TelewebAppException
    {
    	TelewebJSON objParam = new TelewebJSON();
    	
    	objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneCuttMapper", "phoneCuttCpiSeStatistics", jsonParam);
    	
    	return objParam;
    }

    /**
     * 캠페인별 진행 결과 현황 목록
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON phoneCuttCpiProcRstList(TelewebJSON jsonParam) throws TelewebAppException
    {
    	TelewebJSON objParam = new TelewebJSON();
    	
    	objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneCuttMapper", "phoneCuttCpiProcRstList", jsonParam);
    	
    	return objParam;
    }
    
    /**
     * QA 평가 현황 목록
     */
    @Override
	@Transactional(readOnly = true)
	public TelewebJSON selectQaPlan(TelewebJSON jsonParams) throws TelewebAppException
    {
    	TelewebJSON objParam = new TelewebJSON();
    	
    	objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneCuttMapper", "selectQaPlan", jsonParams);
    	
    	return objParam;
	}

    /**
     * QA 유형별 현황 및 QA 평가 건수
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON qaStatistics(TelewebJSON jsonParam) throws TelewebAppException
    {
    	TelewebJSON objParam = new TelewebJSON();
    	
    	objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneCuttMapper", "qaStatistics", jsonParam);
    	
    	return objParam;
    }

    /**
     * QA 상담평가 차수별 결과 통계
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON qaDtlStatistics(TelewebJSON jsonParam) throws TelewebAppException
    {
    	TelewebJSON objParam = new TelewebJSON();
    	
    	objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneCuttMapper", "qaDtlStatistics", jsonParam);
    	
    	return objParam;
    }

    /**
     * QA 상담평가 차수별 점수 통계
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON qaScorStatistics(TelewebJSON jsonParam) throws TelewebAppException
    {
    	TelewebJSON objParam = new TelewebJSON();
    	
    	objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneCuttMapper", "qaScorStatistics", jsonParam);
    	
    	return objParam;
    }
    
    
	 /**
     * 전화상담 콜백 통계 - 기간별 콜백통계
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON phoneClbkTermStatistics(TelewebJSON jsonParam) throws TelewebAppException
    {
    	TelewebJSON objParam = new TelewebJSON();
    	
    	
    	objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneCuttMapper", "phoneClbkTermStatistics", jsonParam);
    	
    	return objParam;
    }
	
	 /**
     * 전화상담 콜백 통계 - 요일별 콜백통계
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON phoneClbkDayStatistics(TelewebJSON jsonParam) throws TelewebAppException
    {
    	TelewebJSON objParam = new TelewebJSON();
    	
    	
    	objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneCuttMapper", "phoneClbkDayStatistics", jsonParam);
    	
    	return objParam;
    }
    /**
     * 전화상담 콜백 통계 - 큐별 콜백 통계
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON phoneClbkQueueStatistics(TelewebJSON jsonParam) throws TelewebAppException
    {
    	TelewebJSON objParam = new TelewebJSON();
    	
    	
    	objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneCuttMapper", "phoneClbkQueueStatistics", jsonParam);
    	
    	return objParam;
    }

    /**
     * 통합 메시지 통계
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON itgrtMsgStatistics(TelewebJSON jsonParam) throws TelewebAppException
    {
    	TelewebJSON objParam = new TelewebJSON();
    	
    	
    	objParam = mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.dao.StatisticsPhoneCuttMapper", "itgrtMsgStatistics", jsonParam);
    	
    	return objParam;
    }
}
