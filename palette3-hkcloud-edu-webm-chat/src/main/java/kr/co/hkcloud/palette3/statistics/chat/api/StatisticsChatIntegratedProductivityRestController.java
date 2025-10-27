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
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebValidationException;
import kr.co.hkcloud.palette3.statistics.chat.app.StatisticsChatIntegratedProductivityService;
import kr.co.hkcloud.palette3.statistics.chat.util.StatisticsChatIntegratedProductivityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "StatisticsChatCounselByInquiryTypeRestController",
     description = "통계채팅상담(문의유형별) REST 컨트롤러")
public class StatisticsChatIntegratedProductivityRestController
{
    private final StatisticsChatIntegratedProductivityService   statisticsChatIntegratedProductivityService;
    private final StatisticsChatIntegratedProductivityValidator statisticsChatIntegratedProductivityValidator;


    /**
     * 
     * @param  mjsonParams
     * @param  result
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "통합생산성통계 조회",
                  notes = "통합생산성통계 조회.")
    @PostMapping("/api/statistics/chat/integrated-productivity/inqire")
    public Object selectRtnIntergratedProductivity(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
//        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //validation 체크
        statisticsChatIntegratedProductivityValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        //조회유형(SEARCH_TYPE)
        String SEARCH_TYPE = mjsonParams.getString("SEARCH_TYPE");

        //조회유형(SEARCH_TYPE)이 시간(TIME)일 경우
        if(StringUtils.equals(SEARCH_TYPE, "TIME")) {
            //조회시간 파라메터 배열 생성 후 조회파라메터에 설정
            List<String> arrSearchTime = new LinkedList<>();
            for(int idx = 0, iTimes = 23; idx <= iTimes; ++idx) {
                String SEARCH_TIME = mjsonParams.getString("SEARCH_TIME_" + idx);

                if(StringUtils.isNotEmpty(SEARCH_TIME)) {
                    arrSearchTime.add(SEARCH_TIME);
                }
            }
            mjsonParams.setObject("arrSearchTime", 0, arrSearchTime);
        }

        //조회유형(SEARCH_TYPE)이 요일(DAY_OF_THE_WEEK)일 경우
        if(StringUtils.equals(SEARCH_TYPE, "DAY_OF_THE_WEEK")) {
            //요일 배열 생성 후 조회파라메터에 설정
            List<String> arrDayOfTheWeek = new LinkedList<>();
            String CHK_DAY_OF_THE_WEEK_1 = mjsonParams.getString("CHK_DAY_OF_THE_WEEK_1");
            String CHK_DAY_OF_THE_WEEK_2 = mjsonParams.getString("CHK_DAY_OF_THE_WEEK_2");
            String CHK_DAY_OF_THE_WEEK_3 = mjsonParams.getString("CHK_DAY_OF_THE_WEEK_3");
            String CHK_DAY_OF_THE_WEEK_4 = mjsonParams.getString("CHK_DAY_OF_THE_WEEK_4");
            String CHK_DAY_OF_THE_WEEK_5 = mjsonParams.getString("CHK_DAY_OF_THE_WEEK_5");
            String CHK_DAY_OF_THE_WEEK_6 = mjsonParams.getString("CHK_DAY_OF_THE_WEEK_6");
            String CHK_DAY_OF_THE_WEEK_7 = mjsonParams.getString("CHK_DAY_OF_THE_WEEK_7");
            if(StringUtils.isNotEmpty(CHK_DAY_OF_THE_WEEK_1)) {
                arrDayOfTheWeek.add(CHK_DAY_OF_THE_WEEK_1);
            }
            if(StringUtils.isNotEmpty(CHK_DAY_OF_THE_WEEK_2)) {
                arrDayOfTheWeek.add(CHK_DAY_OF_THE_WEEK_2);
            }
            if(StringUtils.isNotEmpty(CHK_DAY_OF_THE_WEEK_3)) {
                arrDayOfTheWeek.add(CHK_DAY_OF_THE_WEEK_3);
            }
            if(StringUtils.isNotEmpty(CHK_DAY_OF_THE_WEEK_4)) {
                arrDayOfTheWeek.add(CHK_DAY_OF_THE_WEEK_4);
            }
            if(StringUtils.isNotEmpty(CHK_DAY_OF_THE_WEEK_5)) {
                arrDayOfTheWeek.add(CHK_DAY_OF_THE_WEEK_5);
            }
            if(StringUtils.isNotEmpty(CHK_DAY_OF_THE_WEEK_6)) {
                arrDayOfTheWeek.add(CHK_DAY_OF_THE_WEEK_6);
            }
            if(StringUtils.isNotEmpty(CHK_DAY_OF_THE_WEEK_7)) {
                arrDayOfTheWeek.add(CHK_DAY_OF_THE_WEEK_7);
            }
            mjsonParams.setObject("arrDayOfTheWeek", 0, arrDayOfTheWeek);
        }

        objRetParams = statisticsChatIntegratedProductivityService.selectRtnIntergratedProductivity(mjsonParams);

        return objRetParams;
    }
}
