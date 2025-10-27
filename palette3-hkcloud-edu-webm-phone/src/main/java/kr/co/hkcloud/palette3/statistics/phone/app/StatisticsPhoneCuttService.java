package kr.co.hkcloud.palette3.statistics.phone.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

/**
 * 
 * Description : 전화상담 통계
 * package  : kr.co.hkcloud.palette3.statistics.phone.app
 * filename : StatisticsPhoneCuttService.java
 * Date : 2023. 8. 7.
 * History :
 * - 작성자 : ryucease, 날짜 : 2023. 8. 7., 설명 : 최초작성<br>
 *
 * @author ryucease
 * @version 1.0
 */

public interface StatisticsPhoneCuttService
{

	/**
	 * 
	 * 전화상담 종합 - 전화상담 현황(전체)
	 * @Method Name  	: phoneCuttTotalStatistics
	 * @date   			: 2023. 8. 7.
	 * @author   		: ryucease
	 * @version     	: 1.0
	 * ----------------------------------------
	 * @param jsonParam
	 * @return
	 * @throws TelewebAppException
	 */
    TelewebJSON phoneCuttTotalStatistics(TelewebJSON jsonParam) throws TelewebAppException;

    /**
     * 
     * 전화상담 종합 - 콜 유형별 현황
     * @Method Name  	: phoneCuttClTypeStatistics
     * @date   			: 2023. 8. 7.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON phoneCuttClTypeStatistics(TelewebJSON jsonParam) throws TelewebAppException;

    /**
     * 
     * 전화상담 종합 - 통화 시간별 추이
     * @Method Name  	: phoneCuttPhnHrStatistics
     * @date   			: 2023. 8. 7.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON phoneCuttPhnHrStatistics(TelewebJSON jsonParam) throws TelewebAppException;

    /**
     * 
     * 문의 유형별 현황
     * @Method Name  	: phoneCuttIvrStatistics
     * @date   			: 2023. 8. 16.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON phoneCuttIvrStatistics(TelewebJSON jsonParam) throws TelewebAppException;

    /**
     * 
     * 전화상담 종합 - 월간 상담 추이
     * @Method Name  	: phoneCuttMonStatistics
     * @date   			: 2023. 8. 7.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON phoneCuttMonStatistics(TelewebJSON jsonParam) throws TelewebAppException;

    /**
     * 
     * 부서/개인별 통계
     * @Method Name  	: phoneCuttDeptCuslStatistics
     * @date   			: 2023. 8. 8.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON phoneCuttDeptCuslStatistics(TelewebJSON jsonParam) throws TelewebAppException;

    /**
     * 
     * 상담유형별 통계
     * @Method Name  	: phoneCuttTypetatistics
     * @date   			: 2023. 8. 9.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON phoneCuttTypetatistics(TelewebJSON jsonParam) throws TelewebAppException;

    /**
     * 
     * 캠페인 유형별 타겟 통계
     * @Method Name  	: phoneCuttCpiSeStatistics
     * @date   			: 2023. 8. 10.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON phoneCuttCpiSeStatistics(TelewebJSON jsonParam) throws TelewebAppException;

    /**
     * 
     * 캠페인별 진행 결과 현황 목록
     * @Method Name  	: phoneCuttCpiDtlStatistics
     * @date   			: 2023. 8. 10.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON phoneCuttCpiProcRstList(TelewebJSON jsonParam) throws TelewebAppException;

    /**
     * 
     * 콜백 통계 - 기간별 콜백 현황
     * @Method Name  	: phoneClbkStatistics
     * @date   			: 2023. 8. 9.
     * @author   		: njy
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON phoneClbkTermStatistics(TelewebJSON jsonParam) throws TelewebAppException;
    
    /**
     * 
     * 콜백 통계 - 기간별 콜백 현황
     * @Method Name  	: phoneClbkStatistics
     * @date   			: 2023. 8. 11.
     * @author   		: njy
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON phoneClbkDayStatistics(TelewebJSON jsonParam) throws TelewebAppException;
    
    /**
     * 
     * 콜백 통계 - 큐별 현황
     * @Method Name  	: phoneClbkStatistics
     * @date   			: 2023. 8. 11.
     * @author   		: njy
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON phoneClbkQueueStatistics(TelewebJSON jsonParam) throws TelewebAppException;

    /**
     * 
     * 일자별 통계 현황
     * @Method Name  	: phoneCuttDailyStatistics
     * @date   			: 2023. 8. 9.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON phoneCuttDailyStatistics(TelewebJSON jsonParam) throws TelewebAppException;

    /**
     * 
     * QA 유형별 현황 및 QA 평가 건수
     * @Method Name  	: qaStatistics
     * @date   			: 2023. 8. 10.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON qaStatistics(TelewebJSON jsonParam) throws TelewebAppException;

    /**
     * 
     * QA 평가 현황 목록
     * @Method Name  	: selectQaPlan
     * @date   			: 2023. 8. 10.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectQaPlan(TelewebJSON jsonParam) throws TelewebAppException;

    /**
     * 
     * QA 상담평가 차수별 점수 통계
     * @Method Name  	: qaScorStatistics
     * @date   			: 2023. 8. 11.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON qaScorStatistics(TelewebJSON jsonParam) throws TelewebAppException;

    /**
     * 
     * QA 상담평가 차수별 결과 통계
     * @Method Name  	: qaDtlStatistics
     * @date   			: 2023. 8. 11.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON qaDtlStatistics(TelewebJSON jsonParam) throws TelewebAppException;

    /**
     * 
     * 통합 메시지 통계
     * @Method Name  	: itgrtMsgStatistics
     * @date   			: 2023. 8. 16.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON itgrtMsgStatistics(TelewebJSON jsonParam) throws TelewebAppException;
}
