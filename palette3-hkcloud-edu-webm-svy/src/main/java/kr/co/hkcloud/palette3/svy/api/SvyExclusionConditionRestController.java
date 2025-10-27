package kr.co.hkcloud.palette3.svy.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.svy.app.SvyExclusionConditionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SvyExclusionConditionRestController {
    private final SvyExclusionConditionService svyExclusionConditionService;
    
    @ApiOperation(value = "설문조사 제외 조건 목록 조회")
    @PostMapping("/api/svy/exclusion/selectConditionList")
    public Object selectConditionList(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyExclusionConditionService.selectConditionList(jsonParam);
    }
    
    @SystemEventLogAspectAnotation(value = "COM_SVY-EXCL-TRGT_PROC", note = "설문조사 제외 대상 변경(등록,수정)")
    @ApiOperation(value = "설문조사 제외 조건 등록/수정")
    @PostMapping("/api/svy/exclusion/mergeCondition")
    public Object mergeCondition(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyExclusionConditionService.mergeCondition(jsonParam);
    }
    
    @SystemEventLogAspectAnotation(value = "COM_SVY-EXCL-TRGT_DEL", note = "설문조사 제외 대상 삭제")
    @ApiOperation(value = "설문조사 제외 조건 삭제")
    @PostMapping("/api/svy/exclusion/deleteCondition")
    public Object deleteCondition(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyExclusionConditionService.deleteCondition(jsonParam);
    }
}
