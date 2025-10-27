package kr.co.hkcloud.palette3.statistics.phone.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.campaign.app.PhoneCampaignPlanService;
import kr.co.hkcloud.palette3.statistics.phone.app.StatisticsPhoneCuttService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Description : 전화상담 통계
 * package  : kr.co.hkcloud.palette3.statistics.phone.api
 * filename : StatisticsPhoneCuttRestController.java
 * Date : 2023. 8. 7.
 * History :
 * - 작성자 : ryucease, 날짜 : 2023. 8. 7., 설명 : 최초작성<br>
 *
 * @author ryucease
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "StatisticsPhoneCuttRestController",
     description = "전화상담 통계 컨트롤러")
public class StatisticsPhoneCuttRestController
{
    private final StatisticsPhoneCuttService statisticsPhoneCuttService;
    private final PhoneCampaignPlanService phoneCampaignPlanService;

    /**
     * 
     * 전화상담 종합 통계
     * @Method Name  	: selectObndAdaySttcRprtList
     * @date   			: 2023. 8. 7.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "전화상담 종합 통계",
                  notes = "전화상담 종합 통계를 조회한다.")
    @PostMapping("/api/statistics/phone/phoneCuttTotalStatistics")
    public Object selectObndAdaySttcRprtList(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {

        TelewebJSON objParam = new TelewebJSON();

        objParam = statisticsPhoneCuttService.phoneCuttTotalStatistics(jsonParam); //전화상담 현황(전체)
        
        objParam.setDataObject("CL_TYPE_DATA", statisticsPhoneCuttService.phoneCuttClTypeStatistics(jsonParam).getDataObject("DATA")); //콜 유형별 현황

        objParam.setDataObject("PHN_HR_DATA", statisticsPhoneCuttService.phoneCuttPhnHrStatistics(jsonParam).getDataObject("DATA")); //통화 시간별 추이

        objParam.setDataObject("IVR_DATA", statisticsPhoneCuttService.phoneCuttIvrStatistics(jsonParam).getDataObject("DATA")); //문의 유형별 현황

        objParam.setDataObject("MON_DATA", statisticsPhoneCuttService.phoneCuttMonStatistics(jsonParam).getDataObject("DATA")); //월간 상담 추이

        return objParam;
    }

    /**
     * 
     * 전화상담 부서/개인별 통계
     * @Method Name  	: phoneCuttDeptCuslStatistics
     * @date   			: 2023. 8. 8.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "전화상담 부서/개인별 통계",
    		notes = "전화상담 부서/개인별 통계를 조회한다.")
    @PostMapping("/api/statistics/phone/phoneCuttDeptCuslStatistics")
    public Object phoneCuttDeptCuslStatistics(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {
    	
    	TelewebJSON objParam = new TelewebJSON();
    	
    	objParam = statisticsPhoneCuttService.phoneCuttDeptCuslStatistics(jsonParam); //부서/개인별 통계
    	
    	return objParam;
    }

    /**
     * 
     * 전화상담 상담유형별 통계
     * @Method Name  	: phoneCuttTypetatistics
     * @date   			: 2023. 8. 9.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "전화상담 상담유형별 통계",
    		notes = "전화상담 상담유형별 통계를 조회한다.")
    @PostMapping("/api/statistics/phone/phoneCuttTypetatistics")
    public Object phoneCuttTypetatistics(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {
    	
    	TelewebJSON objParam = new TelewebJSON();
    	
    	objParam = statisticsPhoneCuttService.phoneCuttTypetatistics(jsonParam); //상담유형별 통계
    	
    	return objParam;
    }
    
    
    /**
     * 
     * 콜백 통계
     * @Method Name  	: phoneClbkStatistics
     * @date   			: 2023. 8. 9.
     * @author   		: njy
     * @version     	: 1.1
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "콜백 통계",
    		notes = "콜백 통계를 조회한다.")
    @PostMapping("/api/statistics/phone/phoneClbkStatistics")
    public Object phoneClbkStatistics(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {
    	
    	TelewebJSON objParam = new TelewebJSON();
    	
    	 objParam.setDataObject("TERM_DATA", statisticsPhoneCuttService.phoneClbkTermStatistics(jsonParam).getDataObject("DATA")); // 기간별 콜백 현황
    	 objParam.setDataObject("DAY_DATA", statisticsPhoneCuttService.phoneClbkDayStatistics(jsonParam).getDataObject("DATA")); // 요일별 콜백 현황
    	 objParam.setDataObject("QUEUE_DATA", statisticsPhoneCuttService.phoneClbkQueueStatistics(jsonParam).getDataObject("DATA")); // 큐별 콜백 현황
    	
    	return objParam;
    }

    /**
     * 
     * 전화상담 일자별 통계
     * @Method Name  	: phoneCuttDailyStatistics
     * @date   			: 2023. 8. 9.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "전화상담 일자별 통계",
	            notes = "전화상담 일자별 통계를 조회한다.")
	@PostMapping("/api/statistics/phone/phoneCuttDailyStatistics")
	public Object phoneCuttDailyStatistics(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
	{
	
	  TelewebJSON objParam = new TelewebJSON();
	
	  objParam = statisticsPhoneCuttService.phoneCuttTotalStatistics(jsonParam); //전화상담 현황(전체)
	  
	  objParam.setDataObject("CL_TYPE_DATA", statisticsPhoneCuttService.phoneCuttClTypeStatistics(jsonParam).getDataObject("DATA")); //콜 유형별 현황
	
	  objParam.setDataObject("DAILY_DATA", statisticsPhoneCuttService.phoneCuttDailyStatistics(jsonParam).getDataObject("DATA")); //일자별 통계 현황
	
	  return objParam;
	}

    /**
     * 
     * 전화상담 캠페인 통계
     * @Method Name  	: phoneCuttCpiStatistics
     * @date   			: 2023. 8. 10.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "전화상담 캠페인 통계",
    		notes = "전화상담 캠페인 통계를 조회한다.")
    @PostMapping("/api/statistics/phone/phoneCuttCpiStatistics")
    public Object phoneCuttCpiStatistics(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {
    	
    	TelewebJSON objParam = new TelewebJSON();
    	
    	objParam = phoneCampaignPlanService.selectCpiPlan(jsonParam); //캠페인 현황(전체)
    	
    	objParam.setDataObject("CPI_SE_DATA", statisticsPhoneCuttService.phoneCuttCpiSeStatistics(jsonParam).getDataObject("DATA")); //캠페인 유형별 타겟 현황
    	
    	return objParam;
    }

    /**
     * 
     * 전화상담 캠페인별 통계
     * @Method Name  	: phoneCuttCpiDtlStatistics
     * @date   			: 2023. 8. 10.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "전화상담 캠페인별 통계",
    		notes = "전화상담 캠페인별 통계를 조회한다.")
    @PostMapping("/api/statistics/phone/phoneCuttCpiDtlStatistics")
    public Object phoneCuttCpiDtlStatistics(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {
    	
    	TelewebJSON objParam = new TelewebJSON();
    	
    	objParam = statisticsPhoneCuttService.phoneCuttCpiProcRstList(jsonParam); //캠페인별 진행 결과 현황 목록
    	
    	return objParam;
    }
    
    /**
     * 
     * QA 상담평가 통계
     * @Method Name  	: qaStatistics
     * @date   			: 2023. 8. 10.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "QA 상담평가 통계",
    		notes = "QA 상담평가 통계를 조회한다.")
    @PostMapping("/api/statistics/phone/qaStatistics")
    public Object qaStatistics(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {
    	
    	TelewebJSON objParam = new TelewebJSON();
    	
    	objParam = statisticsPhoneCuttService.selectQaPlan(jsonParam); //QA 평가 현황(전체)
    	
    	objParam.setDataObject("QA_SE_DATA", statisticsPhoneCuttService.qaStatistics(jsonParam).getDataObject("DATA")); //QA 유형별 현황 및 QA 평가 건수
    	
    	return objParam;
    }

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
     * @throws TelewebApiException
     */
    @ApiOperation(value = "QA 상담평가 차수별 결과 통계",
    		notes = "QA 상담평가 차수별 결과 통계를 조회한다.")
    @PostMapping("/api/statistics/phone/qaDtlStatistics")
    public Object qaDtlStatistics(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {
    	
    	TelewebJSON objParam = new TelewebJSON();
    	
    	objParam = statisticsPhoneCuttService.qaDtlStatistics(jsonParam);
    	
    	objParam.setDataObject("QA_SCOR_DATA", statisticsPhoneCuttService.qaScorStatistics(jsonParam).getDataObject("DATA")); //QA 상담평가 차수별 점수 통계
    	
    	return objParam;
    }

    /**
     * 
     * 통합 메시지
     * @Method Name  	: itgrtMsgStatistics
     * @date   			: 2023. 8. 16.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "통합 메시지 통계",
    		notes = "통합 메시지 통계를 조회한다.")
    @PostMapping("/api/statistics/phone/itgrtMsgStatistics")
    public Object itgrtMsgStatistics(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {
    	
    	TelewebJSON objParam = new TelewebJSON();
    	
    	objParam = statisticsPhoneCuttService.itgrtMsgStatistics(jsonParam);
    	
    	return objParam;
    }
}
