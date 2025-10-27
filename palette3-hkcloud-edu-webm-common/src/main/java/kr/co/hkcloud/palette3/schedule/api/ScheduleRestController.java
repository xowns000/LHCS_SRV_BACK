package kr.co.hkcloud.palette3.schedule.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.schedule.app.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "ScheduleRestController",
        description = "일정 컨트롤러")
public class ScheduleRestController {
    private final ScheduleService scheduleService;

    /**
     *
     * 메서드 설명		: 메인홈 일정 상태 조회
     * @Method Name  	: MySchdlStat
     * @date   			: 2023. 09. 07
     * @author   		: 나준영
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return objRetParams
     * @throws TelewebApiException
     */
    @ApiOperation(value = "일정 상태-조회",
            notes = "일정 상태를 조회한다")
    @PostMapping("/api/schedule/MySchdlStat")
    public Object MySchdlStat(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = scheduleService.MySchdlStat(mjsonParams);
        objRetParams.setHeader("ERROR_FLAG", false);
        //최종결과값 반환
        return objRetParams;
    }

    /**
     *
     * 메서드 설명		: 스케쥴 목록 조회
     * @Method Name  	: selectSchedule
     * @date   			: 2023. 09. 07
     * @author   		: 나준영
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return objRetParams
     * @throws TelewebApiException
     */
    @ApiOperation(value = "스케줄-목록-조회",
            notes = "스케줄 목록을 조회한다")
    @PostMapping("/api/schedule/list")
    public Object selectSchedule(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = scheduleService.selectSchedule(mjsonParams);
        objRetParams.setHeader("ERROR_FLAG", false);
        //최종결과값 반환
        return objRetParams;
    }


    /**
     *
     * 메서드 설명		: 스케줄 목록 조회
     * @Method Name  	: selectScheduleRtn
     * @date   			: 2023. 8. 03
     * @author   		: 나준영
     * @version     	: 1.1
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return objRetParams
     * @throws TelewebApiException
     */
    @ApiOperation(value = "스케줄-조회",
            notes = "스케줄을 조회한다")
    @PostMapping("/api/schedule/select")
    public Object selectScheduleRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = scheduleService.selectScheduleRtn(mjsonParams);
        objRetParams.setHeader("ERROR_FLAG", false);
        //최종결과값 반환
        return objRetParams;
    }


    /**
     *
     * 메서드 설명		: 스케줄 등록
     * @Method Name  	: insertScheduleRtn
     * @date   			: 2023. 8. 03
     * @author   		: 나준영
     * @version     	: 1.1
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return objRetParams
     * @throws TelewebApiException
     */
    @ApiOperation(value = "스케줄-목록-등록",
            notes = "스케줄 목록을 등록한다")
    @PostMapping("/api/schedule/insert")
    public Object insertScheduleRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = scheduleService.insertScheduleRtn(mjsonParams);
        objRetParams.setHeader("ERROR_FLAG", false);
        //최종결과값 반환
        return objRetParams;
    }


    /**
     *
     * 메서드 설명		: 스케줄 수정
     * @Method Name  	: updateScheduleRtn
     * @date   			: 2023. 8. 03
     * @author   		: 나준영
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return objRetParams
     * @throws TelewebApiException
     */
    @ApiOperation(value = "스케줄-목록-수정",
            notes = "스케줄 목록을 수정한다")
    @PostMapping("/api/schedule/update")
    public Object updateScheduleRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = scheduleService.updateScheduleRtn(mjsonParams);
        objRetParams.setHeader("ERROR_FLAG", false);
        //최종결과값 반환
        return objRetParams;
    }


    /**
     *
     * 메서드 설명		: 스케줄 삭제
     * @Method Name  	: deleteScheduleRtn
     * @date   			: 2023. 8. 03
     * @author   		: 나준영
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return objRetParams
     * @throws TelewebApiException
     */
    @ApiOperation(value = "스케줄-목록-삭제",
            notes = "스케줄 목록을 삭제한다")
    @PostMapping("/api/schedule/delete")
    public Object deleteScheduleRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = scheduleService.deleteScheduleRtn(mjsonParams);
        objRetParams.setHeader("ERROR_FLAG", false);
        //최종결과값 반환
        return objRetParams;
    }

}
