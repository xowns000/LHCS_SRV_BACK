package kr.co.hkcloud.palette3.svy.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.svy.app.SvyPlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "SvyPlanRestController", description = "설문조사 계획관리 컨트롤러")
public class SvyPlanRestController {

    private final SvyPlanService svyPlanService;


    /**
     *
     * @param  jsonParam
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "설문조사 계획관리", notes = "설문조사 계획을 조회한다.")
    @PostMapping("/api/svy/plan/selectlistplan")
    public Object selectListPlan(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebAppException {
        return svyPlanService.selectListPlan(jsonParam);
    }

    /**
     *
     * @param  jsonParam
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "COM_SVY-PLAN_PROC", note = "설문조사 계획관리 변경(등록,수정)")
    @ApiOperation(value = "설문조사 계획관리", notes = "설문조사 계획을 저장한다.")
    @PostMapping("/api/svy/plan/upsertlistplan")
    public Object upsertListPlan(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyPlanService.upsertListPlan(jsonParam);
    }

    /**
     *
     * @param  jsonParam
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "COM_SVY-PLAN_DEL", note = "설문조사 계획관리 삭제")
    @ApiOperation(value = "설문조사 계획관리", notes = "설문조사 계획을 삭제한다.")
    @PostMapping("/api/svy/plan/deletelistplan")
    public Object deleteListPlan(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyPlanService.deleteListPlan(jsonParam);
    }

    /**
     *
     * @param  jsonParam
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "설문조사 계획관리", notes = "설문조사 계획을 종료한다.")
    @PostMapping("/api/svy/plan/closelistplan")
    public Object closeListPlan(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyPlanService.closeListPlan(jsonParam);
    }
    
    
    @ApiOperation(value = "설문조사 검토 의견 보기(상태 변경 이력)", notes = "설문조사 검토 의견 보기(상태 변경 이력) 조회")
    @PostMapping("/api/svy/plan/selectSttsHistory")
    public Object selectSttsHistory(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyPlanService.selectSttsHistory(jsonParam);
    }
    
}
