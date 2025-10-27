package kr.co.hkcloud.palette3.phone.campaign.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.phone.campaign.app.PhoneCampaignPlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneCampaignPlanRestController", description = "캠페인 계획관리 컨트롤러")
public class PhoneCampaignPlanRestController {

    private final PhoneCampaignPlanService phoneCampaignPlanService;

    @ApiOperation(value = "캠페인 계획 조회", notes = "캠페인 계획을 조회한다.")
    @PostMapping("/phone-api/campaign/plan/selectcpiplan")
    public Object selectCpiPlan(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebAppException {
        return phoneCampaignPlanService.selectCpiPlan(jsonParam);
    }

    @SystemEventLogAspectAnotation(value = "PHN_CPI-PLAN_PROC", note = "캠페인 계획 변경(등록,수정)")
    @ApiOperation(value = "캠페인 계획 저장", notes = "캠페인 계획을 저장한다.")
    @PostMapping("/phone-api/campaign/plan/upsertcpiplan")
    public Object upsertCpiPlan(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return phoneCampaignPlanService.upsertCpiPlan(jsonParam);
    }

    @SystemEventLogAspectAnotation(value = "PHN_CPI-PLAN_DEL", note = "캠페인 계획 삭제")
    @ApiOperation(value = "캠페인 계획 삭제", notes = "캠페인 계획을 삭제한다.")
    @PostMapping("/phone-api/campaign/plan/deletecpiplan")
    public Object deleteCpiPlan(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return phoneCampaignPlanService.deleteCpiPlan(jsonParam);
    }

    @SystemEventLogAspectAnotation(value = "PHN_CPI-PLAN_CLOSE", note = "캠페인 계획 종료")
    @ApiOperation(value = "캠페인 계획 종료", notes = "캠페인 계획을 종료한다.")
    @PostMapping("/phone-api/campaign/plan/closecpiplan")
    public Object closeCpiPlan(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return phoneCampaignPlanService.closeCpiPlan(jsonParam);
    }

    @SystemEventLogAspectAnotation(value = "PHN_CPI-PLAN_STARTNOW", note = "캠페인 계획 즉시진행")
    @ApiOperation(value = "캠페인 즉시진행", notes = "캠페인 계획을 즉시 진행한다.")
    @PostMapping("/phone-api/campaign/plan/strtNow")
    public Object cpiPlanStrtNow(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return phoneCampaignPlanService.cpiPlanStrtNow(jsonParam);
    }
}
