package kr.co.hkcloud.palette3.statistics.chat.api;


import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbEnvironment;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebValidationException;
import kr.co.hkcloud.palette3.statistics.chat.app.StatisticsChatCuttService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "StatisticsChatCuttRestController",
     description = "통계채팅상담(상담사별) REST 컨트롤러")
public class StatisticsChatCuttRestController
{
    private final StatisticsChatCuttService   statisticsChatCuttService;


    /**
     * 
     * 전화상담 종합 통계
     * @Method Name  	: selectObndAdaySttcRprtList
     * @date   			: 2023. 12. 12.
     * @author   		: ktj
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "채팅상담 종합 통계",
                  notes = "채팅상담 종합 통계를 조회한다.")
    @PostMapping("/api/statistics/chat/chatCuttTotalStatistics")
    public Object selectTotalStatistics(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {
        TelewebJSON objParam = new TelewebJSON();

        objParam = statisticsChatCuttService.chatCuttTotalStatistics(jsonParam); //채팅상담 현황(전체)
        
        objParam.setDataObject("CHN_DATA", statisticsChatCuttService.chatCuttChnTypeStatistics(jsonParam).getDataObject("DATA")); //SNS 채널별 현황

        objParam.setDataObject("CHT_HR_DATA", statisticsChatCuttService.chatCuttHrStatistics(jsonParam).getDataObject("DATA")); //평균상담시간 현황

        objParam.setDataObject("TYPE_DATA", statisticsChatCuttService.chatCuttTypeStatistics(jsonParam).getDataObject("DATA")); //상담유형별 현황

        objParam.setDataObject("MON_DATA", statisticsChatCuttService.chatCuttMonStatistics(jsonParam).getDataObject("DATA")); //월간 상담 추이

        objParam.setDataObject("PRDCTN_DATA", statisticsChatCuttService.chatCuttPrdctnStatistics(jsonParam).getDataObject("DATA")); //통합생산성 현황

        return objParam;
    }


    /**
     *
     * 채팅상담 직원별 통계
     * @Method Name  	: selectObndAdaySttcRprtList
     * @date   			: 2023. 12. 14.
     * @author   		: ktj
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "채팅상담 상담직원별 통계",
        notes = "채팅상담 상담직원별 통계를 조회한다.")
    @PostMapping("/api/statistics/chat/chatCuttCuslStatistics")
    public Object selectCuslStatistics(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {
        TelewebJSON objParam = new TelewebJSON();

        objParam = statisticsChatCuttService.chatCuttCuslStatistics(jsonParam); //상담직원별 현황(상단 모니터링 건수)

        objParam.setDataObject("CUSL_LIST", statisticsChatCuttService.chatCuttCuslStatisticsList(jsonParam).getDataObject("DATA")); //상담 직원별 현황 (그리드 데이터)

        return objParam;
    }

    /**
     *
     * 채팅상담 직원별 통계
     * @Method Name  	: selectStatisticsByInqryType
     * @date   			: 2023. 12. 14.
     * @author   		: njy
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "통계채팅상담(문의유형별)-조회",
        notes = "통계채팅상담(문의유형별) 정보를 조회한다.")
    @PostMapping("/api/statistics/chat/counsel-by-inqire-type/selectByInqryType")
    public Object selectStatisticsByInqryType(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams.setDataObject("CNT_DATA",statisticsChatCuttService.selectStatisticsByInqryType(mjsonParams).getDataObject("DATA"));
        objRetParams.setDataObject("QSTN_DATA",statisticsChatCuttService.selectInqryTypeTree(mjsonParams).getDataObject("DATA"));

        log.info("objRetParams ================>" + objRetParams);
        return objRetParams;
    }

    /**
     *
     * 채팅상담 유형별 통계
     * @Method Name  	: selectObndAdaySttcRprtList
     * @date   			: 2023. 12. 18.
     * @author   		: ktj
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "채팅상담 유형별 통계",
        notes = "채팅상담 유형별 통계를 조회한다.")
    @PostMapping("/api/statistics/chat/chatCuttTypetatistics")
    public Object selectCuttTypeStatistics(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {
        TelewebJSON objParam = new TelewebJSON();

        objParam = statisticsChatCuttService.selectCuttTypeStatistics(jsonParam); //상담직원별 현황(상단 모니터링 건수)

        return objParam;
    }

    /**
     *
     * 채팅상담 일자별 통계
     * @Method Name  	: selectObndAdaySttcRprtList
     * @date   			: 2023. 12. 14.
     * @author   		: ktj
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "채팅상담 일자별 통계",
        notes = "채팅상담 일자별 통계를 조회한다.")
    @PostMapping("/api/statistics/chat/chatCuttDateStatistics")
    public Object selectDateStatistics(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {
        TelewebJSON objParam = new TelewebJSON();

        objParam = statisticsChatCuttService.chatCuttDateStatistics(jsonParam); //상담직원별 현황(상단 모니터링 건수)

        objParam.setDataObject("DATE_LIST", statisticsChatCuttService.chatCuttDateStatisticsList(jsonParam).getDataObject("DATA")); //상담 직원별 현황 (그리드 데이터)

        return objParam;
    }

    /**
     *
     * 채팅상담 통합생산성 통계
     * @Method Name  	: selectObndAdaySttcRprtList
     * @date   			: 2023. 12. 14.
     * @author   		: ktj
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "채팅상담 통합생산성 통계",
        notes = "채팅상담 통합생산성 통계를 조회한다.")
    @PostMapping("/api/statistics/chat/chatCuttPrdctnStatistics")
    public Object selectPrdctnStatistics(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {
        TelewebJSON objParam = new TelewebJSON();

        objParam = statisticsChatCuttService.selectCuttPrdctnStatistics(jsonParam); //상담직원별 현황(상단 모니터링 건수)

        objParam.setDataObject("PRDCTN_LIST", statisticsChatCuttService.selectCuttPrdctnStatisticsList(jsonParam).getDataObject("DATA")); //상담 직원별 현황 (그리드 데이터)

        return objParam;
    }


    /**
     * 
     * 채팅통계 상담건수 포함항목 설정값 조회
     * @Method Name  	: getStatStng
     * @date   			: 2023. 12. 12.
     * @author   		: ktj
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "채팅통계 상담건수 포함항목 설정값 조회",
                  notes = "채팅통계 상담건수 포함항목 설정값 조회")
    @PostMapping("/api/statistics/chat/getStatStng")
    public Object getStatStng(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {
        TelewebJSON objParam = new TelewebJSON();

        objParam = statisticsChatCuttService.getStatStng(jsonParam); //채팅상담 현황(전체)

        return objParam;
    }


}
