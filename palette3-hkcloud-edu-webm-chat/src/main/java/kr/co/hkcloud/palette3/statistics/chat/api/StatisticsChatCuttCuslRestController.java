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
import kr.co.hkcloud.palette3.statistics.chat.app.StatisticsChatCuttCuslService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "StatisticsChatCuttCuslRestController",
     description = "통계채팅상담(상담사별) REST 컨트롤러")
public class StatisticsChatCuttCuslRestController
{
    private final StatisticsChatCuttCuslService   statisticsChatCuttCuslService;


//    /**
//     *
//     * 채팅상담 직원별 통계
//     * @Method Name  	: selectObndAdaySttcRprtList
//     * @date   			: 2023. 12. 14.
//     * @author   		: ktj
//     * @version     	: 1.0
//     * ----------------------------------------
//     * @param jsonParam
//     * @return
//     * @throws TelewebApiException
//     */
//    @ApiOperation(value = "채팅상담 상담직원별 통계",
//                  notes = "채팅상담 상담직원별 통계를 조회한다.")
//    @PostMapping("/api/statistics/chat/chatCuttCuslStatistics")
//    public Object selectCuslStatistics(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
//    {
//        TelewebJSON objParam = new TelewebJSON();
//
//        objParam = statisticsChatCuttCuslService.chatCuttCuslStatistics(jsonParam); //상담직원별 현황(상단 모니터링 건수)
//
//        objParam.setDataObject("CUSL_LIST", statisticsChatCuttCuslService.chatCuttCuslStatisticsList(jsonParam).getDataObject("DATA")); //상담 직원별 현황 (그리드 데이터)
//
//        return objParam;
//    }
    
//    /**
//     *
//     * 채팅상담 일자별 통계
//     * @Method Name  	: selectObndAdaySttcRprtList
//     * @date   			: 2023. 12. 14.
//     * @author   		: ktj
//     * @version     	: 1.0
//     * ----------------------------------------
//     * @param jsonParam
//     * @return
//     * @throws TelewebApiException
//     */
//    @ApiOperation(value = "채팅상담 일자별 통계",
//                  notes = "채팅상담 일자별 통계를 조회한다.")
//    @PostMapping("/api/statistics/chat/chatCuttDateStatistics")
//    public Object selectDateStatistics(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
//    {
//        TelewebJSON objParam = new TelewebJSON();
//
//        objParam = statisticsChatCuttCuslService.chatCuttDateStatistics(jsonParam); //상담직원별 현황(상단 모니터링 건수)
//
//        objParam.setDataObject("DATE_LIST", statisticsChatCuttCuslService.chatCuttDateStatisticsList(jsonParam).getDataObject("DATA")); //상담 직원별 현황 (그리드 데이터)
//
//        return objParam;
//    }
    
//    /**
//     *
//     * 채팅상담 통합생산성 통계
//     * @Method Name  	: selectObndAdaySttcRprtList
//     * @date   			: 2023. 12. 14.
//     * @author   		: ktj
//     * @version     	: 1.0
//     * ----------------------------------------
//     * @param jsonParam
//     * @return
//     * @throws TelewebApiException
//     */
//    @ApiOperation(value = "채팅상담 통합생산성 통계",
//                  notes = "채팅상담 통합생산성 통계를 조회한다.")
//    @PostMapping("/api/statistics/chat/chatCuttPrdctnStatistics")
//    public Object selectPrdctnStatistics(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
//    {
//        TelewebJSON objParam = new TelewebJSON();
//
//        objParam = statisticsChatCuttCuslService.chatCuttPrdctnStatistics(jsonParam); //상담직원별 현황(상단 모니터링 건수)
//
//        objParam.setDataObject("PRDCTN_LIST", statisticsChatCuttCuslService.chatCuttPrdctnStatisticsList(jsonParam).getDataObject("DATA")); //상담 직원별 현황 (그리드 데이터)
//
//        return objParam;
//    }
    
//    /**
//     *
//     * 채팅상담 유형별 통계
//     * @Method Name  	: selectObndAdaySttcRprtList
//     * @date   			: 2023. 12. 18.
//     * @author   		: ktj
//     * @version     	: 1.0
//     * ----------------------------------------
//     * @param jsonParam
//     * @return
//     * @throws TelewebApiException
//     */
//    @ApiOperation(value = "채팅상담 유형별 통계",
//                  notes = "채팅상담 유형별 통계를 조회한다.")
//    @PostMapping("/api/statistics/chat/chatCuttTypetatistics")
//    public Object selectCuttTypeStatistics(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
//    {
//        TelewebJSON objParam = new TelewebJSON();
//
//        objParam = statisticsChatCuttCuslService.chatCuttTypeStatistics(jsonParam); //상담직원별 현황(상단 모니터링 건수)
//
//        return objParam;
//    }
}
