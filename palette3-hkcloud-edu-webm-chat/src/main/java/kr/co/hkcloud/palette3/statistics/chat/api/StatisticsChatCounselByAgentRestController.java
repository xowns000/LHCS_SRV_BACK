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
import kr.co.hkcloud.palette3.statistics.chat.app.StatisticsChatCounselByAgentService;
import kr.co.hkcloud.palette3.statistics.chat.util.StatisticsChatCounselByAgentValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "StatisticsChatCounselByAgentRestController",
     description = "통계채팅상담(상담사별) REST 컨트롤러")
public class StatisticsChatCounselByAgentRestController
{
    private final StatisticsChatCounselByAgentService   statisticsChatCounselByAgentService;
    private final StatisticsChatCounselByAgentValidator statisticsChatCounselByAgentValidator;


    /**
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     * 
     *                                  통계채팅에서 statistics-chat-common.js 내에서 사용
     */
    @ApiOperation(value = "업무 시작/종료 시간 조회",
                  notes = "서버에서 업무 시작/종료 시간을 가져온다.")
    @PostMapping("/api/statistics/chat/common/job-time/inqire")
    public Object getServerWorkStartEndTime(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
//        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams.setString("WORK_START_TIME", HcTeletalkDbEnvironment.getInstance().getString(((JSONObject) (mjsonParams.getDataObject().get(0))).getString("ASP_NEWCUST_KEY"), "WORK_START_TIME"));
        objRetParams.setString("WORK_END_TIME", HcTeletalkDbEnvironment.getInstance().getString(((JSONObject) (mjsonParams.getDataObject().get(0))).getString("ASP_NEWCUST_KEY"), "WORK_END_TIME"));

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @param  result
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     * 
     */
    @ApiOperation(value = "통계채팅상담(상담사별)-조회",
                  notes = "통계채팅상담(상담사별) 정보를 조회한다.")
    @PostMapping("/api/statistics/chat/counsel-by-agent/inqire")
    public Object selectStatisticsByCounsel(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
//        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //조회유형(SEARCH_TYPE)
        String SEARCH_TYPE = mjsonParams.getString("SEARCH_TYPE");

        //validation 체크
        statisticsChatCounselByAgentValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

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

        //20190517 ojw: 해당 쿼리가 join과 union all이 먹지 않아 따로 조회 후 자바단에서 합침
        objRetParams = statisticsChatCounselByAgentService.selectChatAdviceStatListByInCounsel_new(mjsonParams);
        /*
         * objRetParams = statisticsChatCounselByAgentService.selectChatAdviceStatListByInCounsel_01(mjsonParams); TelewebJSON objRetParams02 =
         * statisticsChatCounselByAgentService.selectChatAdviceStatListByInCounsel_02(mjsonParams);
         * 
         * int TOT_COUNT = objRetParams.getHeaderInt("TOT_COUNT"); int TOT_COUNT_02 = objRetParams02.getHeaderInt("TOT_COUNT");
         * 
         * if(TOT_COUNT != TOT_COUNT_02) { throw new IllegalStateException("The total count is different."); }
         * 
         * for(int idx = 0, iTimes = TOT_COUNT; idx < iTimes; ++idx) { if(! StringUtils.equals(objRetParams.getString("USER_ID", idx) , objRetParams02.getString("USER_ID", idx)) ) { throw new
         * IllegalStateException("The user id is different"); }
         * 
         * objRetParams.setString("NUMBER_ASSIGNMENT", idx, objRetParams02.getString("NUMBER_ASSIGNMENT", idx)); objRetParams.setString("NUMBER_PROGRESS", idx, objRetParams02.getString("NUMBER_PROGRESS", idx));
         * objRetParams.setString("NUMBER_COMPLETE", idx, objRetParams02.getString("NUMBER_COMPLETE", idx)); objRetParams.setString("NUMBER_ESCALATION", idx, objRetParams02.getString("NUMBER_ESCALATION", idx));
         * objRetParams.setString("NUMBER_RELAY", idx, objRetParams02.getString("NUMBER_RELAY", idx)); objRetParams.setString("NUMBER_TAKE_DELIVERY", idx, objRetParams02.getString("NUMBER_TAKE_DELIVERY", idx));
         * objRetParams.setString("NUMBER_CUSTOMER_GIVE_UP_92", idx, objRetParams02.getString("NUMBER_CUSTOMER_GIVE_UP_92", idx)); objRetParams.setString("RATE_RESPONSE", idx, objRetParams02.getString("RATE_RESPONSE",
         * idx)); objRetParams.setString("AVERAGE_CHATTING", idx, objRetParams02.getString("AVERAGE_CHATTING", idx)); objRetParams.setString("AVERAGE_AFTER_TREATMENT", idx,
         * objRetParams02.getString("AVERAGE_AFTER_TREATMENT", idx)); objRetParams.setString("AVERAGE_CHATTING_AFTER_TREATMENT", idx, objRetParams02.getString("AVERAGE_CHATTING_AFTER_TREATMENT", idx));
         * objRetParams.setString("AVERAGE_CUST_MSG_TIME_SUM", idx, objRetParams02.getString("AVERAGE_CUST_MSG_TIME_SUM", idx)); }
         */

        return objRetParams;
    }


    /**
     * 콤보박스데이터 로드(권한그룹명)
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     * 
     */
    @ApiOperation(value = "콤보박스데이터 로드(권한그룹명)",
                  notes = "콤보박스데이터 로드(권한그룹명)")
    @PostMapping("/api/statistics/chat/counsel-by-agent/combobox/inqire")
    public Object selectRtnAuthGroup(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //client에서 전송받은 파라메터 생성
        //        TelewebJSON mjsonParams     = (TelewebJSON)inHashMap.get("mjsonParams");
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON();
        //본부 트리뷰 데이터 검색
        objRetParams = statisticsChatCounselByAgentService.selectRtnAuthGroup(mjsonParams);
        //최종결과값 반환
        return objRetParams;
    }
}
