package kr.co.hkcloud.palette3.svy.api;

import java.util.List;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.svy.app.SvyStatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "SvyStatRestController",
description = "설문조사 통계 REST 컨트롤러")
public class SvyStatRestController {
    private final SvyStatService        svyStatService;
    
    
    @ApiOperation(value = "설문조사 발송 내역 조회", notes = "설문조사 발송 내역을 조회한다")
    @PostMapping("/api/svy/stat/selectSendList")
    public Object selectSendList(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        
        return svyStatService.selectSendList(mjsonParams);
    }
    
    
    @ApiOperation(value = "설문 발송 유형별 현황 통계 조회", notes = "설문 발송 유형별 현황 통계 조회를 조회한다")
    @PostMapping("/api/svy/stat/selectSendTypeStat")
    public Object selectSendStat(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {

        return svyStatService.selectSendTypeStat(mjsonParams);
    }
    
    @ApiOperation(value = "설문 참여율 현황 통계 조회", notes = "설문 참여율 현황 통계 조회를 조회한다")
    @PostMapping("/api/svy/stat/selectRspnsStat")
    public Object selectRspnsStat(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {

        return svyStatService.selectRspnsStat(mjsonParams);
    }
}
