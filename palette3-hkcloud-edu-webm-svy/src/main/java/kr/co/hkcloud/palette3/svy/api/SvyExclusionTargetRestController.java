package kr.co.hkcloud.palette3.svy.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.svy.app.SvyExclusionTargetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SvyExclusionTargetRestController {
    private final SvyExclusionTargetService svyExclusionTargetService;
    
    @ApiOperation(value = "설문조사 제외 대상 목록 조회")
    @PostMapping("/api/svy/exclusion/selectTargetList")
    public Object selectTargetList(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyExclusionTargetService.selectTargetList(jsonParam);
    }
    
    @ApiOperation(value = "설문조사 제외 대상 등록 - 전화번호 중복 체크")
    @PostMapping("/api/svy/exclusion/checkCustPhnNo")
    public Object checkCustPhnNo(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyExclusionTargetService.checkCustPhnNo(jsonParam);
    }
    
    @SystemEventLogAspectAnotation(value = "COM_SVY-EXCL-TRGT_PROC", note = "설문조사 제외 대상 변경(등록,수정)")
    @ApiOperation(value = "설문조사 제외 대상 등록/수정")
    @PostMapping("/api/svy/exclusion/mergeTarget")
    public Object mergeTarget(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyExclusionTargetService.mergeTarget(jsonParam);
    }
    
    @SystemEventLogAspectAnotation(value = "COM_SVY-EXCL-TRGT_DEL", note = "설문조사 제외 대상 삭제")
    @ApiOperation(value = "설문조사 제외 대상 삭제")
    @PostMapping("/api/svy/exclusion/deleteTarget")
    public Object deleteTarget(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyExclusionTargetService.deleteTarget(jsonParam);
    }
}
