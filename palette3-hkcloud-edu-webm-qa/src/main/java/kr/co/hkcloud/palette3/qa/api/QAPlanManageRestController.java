package kr.co.hkcloud.palette3.qa.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.qa.app.QAPlanManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController("QAPlanManageRestController")
@Api(value = "QAPlanManageRestController", description = "QA시행계획관리 REST 컨트롤러")
public class QAPlanManageRestController {

    private final QAPlanManageService qaPlanManageService;

    @ApiOperation(value = "QA시행계획관리", notes = "QA시행계획조회")
    @PostMapping("/api/qa/plan/list")
    Object selectQAPlan(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = qaPlanManageService.selectQaPlanList(mjsonParams);
        return objRetParams;
    }


    @ApiOperation(value = "QA평가시행관리", notes = "QA평가시행계획 등록")
    @PostMapping("/api/qa/plan/regist")
    Object insertQAPlan(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = qaPlanManageService.insertQaPlan(mjsonParams);
        return objRetParams;
    }

	/*
	@ApiOperation(value = "QA평가시행관리",
				  notes = "QA평가시행계획 수정")
	@PostMapping("/api/qa/plan/modify")
	Object updateQAPlan(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
	{
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		objRetParams = qaPlanManageService.updateQaPlan(mjsonParams);
		return objRetParams;
	}
	*/

    @SystemEventLogAspectAnotation(value = "COM_QA-PLAN_DEL", note = "QA평가시행관리 삭제")
    @ApiOperation(value = "QA평가시행관리", notes = "QA평가시행계획 삭제")
    @PostMapping("/api/qa/plan/remove")
    Object deleteQAPlan(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = qaPlanManageService.deleteQaPlan(mjsonParams);
        return objRetParams;
    }

    @SystemEventLogAspectAnotation(value = "COM_QA-PLAN_STTS_BATH", note = "QA평가시행관리 상태변경(일괄 업데이트)")
    @ApiOperation(value = "QA평가시행관리", notes = "QA평가시행계획 상태변경-진행/종료처리")
    @PostMapping("/api/qa/plan/updatesttsbath")
    Object updateQAPlanSttsBatch(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return qaPlanManageService.updateQaPlanSttsBatch(mjsonParams);
    }

    @SystemEventLogAspectAnotation(value = "COM_QA-PLAN_STTS_TERMIAT", note = "QA평가시행관리 상태변경(종료)")
    @ApiOperation(value = "QA평가시행관리", notes = "QA평가시행계획 상태변경-종료처리")
    @PostMapping("/api/qa/plan/termiat")
    Object updateQAPlanSttsTermiat(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        mjsonParams.setString("PRGRS_STTS_CD", "TERMIAT");
        return updateQAPlanStts(mjsonParams);
    }

    @SystemEventLogAspectAnotation(value = "COM_QA-PLAN_STTS_UPDATE", note = "QA평가시행관리 상태변경")
    @ApiOperation(value = "QA평가시행관리", notes = "QA평가시행계획 상태변경")
    @PostMapping("/api/qa/plan/updatestts")
    Object updateQAPlanStts(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return qaPlanManageService.updateQaPlanStts(mjsonParams);
    }

    @SystemEventLogAspectAnotation(value = "COM_QA-PLAN_STRTNOW", note = "QA평가시행관리 즉시시행")
    @ApiOperation(value = "QA평가시행관리", notes = "QA평가시행계획 즉시진행")
    @PostMapping("/api/qa/plan/strtNow")
    Object qaPlanStrtNow(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return qaPlanManageService.qaPlanStrtNow(mjsonParams);
    }

}

