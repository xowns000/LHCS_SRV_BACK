package kr.co.hkcloud.palette3.common.log.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.log.app.SystemEventLogService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.NoBizLog;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "SystemEventLogRestController",
     description = "시스템 로그 REST 컨트롤러")
public class SystemEventLogRestController {
    private final SystemEventLogService systemEventLogService;
    
    
    @NoBizLog
    @ApiOperation(value = "시스템 로그 목록 조회")
    @PostMapping("/api/common/log/selectSysLogList")
    public Object selectSysLogList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        
        return systemEventLogService.selectSysLogList(mjsonParams);
    }
    
    
    @ApiOperation(value = "시스템 로그 구분 목록 조회")
    @PostMapping("/api/common/log/selectSeList")
    public Object selectSeList(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return systemEventLogService.selectSeList(jsonParam);
    }
    
}
