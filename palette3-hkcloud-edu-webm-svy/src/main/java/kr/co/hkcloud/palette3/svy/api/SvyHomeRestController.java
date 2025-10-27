package kr.co.hkcloud.palette3.svy.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.svy.app.SvyHomeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SvyHomeRestController {
private final SvyHomeService svyHomeService;
    
    
    @ApiOperation(value = "0000년 00월 설문조사 현황 통계 조회")
    @PostMapping("/api/svy/home/selectStatusStat")
    public Object selectStatusStat(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyHomeService.selectStatusStat(jsonParam);
    }
    
    @ApiOperation(value = "설문 구분별 통계 조회")
    @PostMapping("/api/svy/home/selectBySeStat")
    public Object selectBySeStat(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyHomeService.selectBySeStat(jsonParam);
    }
    
    @ApiOperation(value = "진행중인 설문조사별 참여율(%) 현황 통계 조회")
    @PostMapping("/api/svy/home/selectRspnsRateStat")
    public Object selectRspnsRateStat(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyHomeService.selectRspnsRateStat(jsonParam);
    }
    
    @ApiOperation(value = "목표인원 설문조사별 달성율(%) 현황 통계 조회")
    @PostMapping("/api/svy/home/selectGoalRateStat")
    public Object selectGoalRateStat(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyHomeService.selectGoalRateStat(jsonParam);
    }
    
    @ApiOperation(value = "발송 유형별 현황 통계 조회")
    @PostMapping("/api/svy/home/selectBySndngSeStat")
    public Object selectBySndngSeStat(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyHomeService.selectBySndngSeStat(jsonParam);
    }
    
    @ApiOperation(value = "설문조사 승인 현황 통계 조회")
    @PostMapping("/api/svy/home/selectApprStat")
    public Object selectApprStat(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyHomeService.selectApprStat(jsonParam);
    }
}
