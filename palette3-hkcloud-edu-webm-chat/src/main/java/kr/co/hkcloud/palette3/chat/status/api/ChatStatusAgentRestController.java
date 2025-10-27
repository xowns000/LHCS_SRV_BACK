package kr.co.hkcloud.palette3.chat.status.api;


import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.chat.status.app.ChatStatusAgentService;
import kr.co.hkcloud.palette3.chat.status.util.ChatStatusAgentValidator;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "ChatStatusAgentRestController",
     description = "채팅현황상담사 REST 컨트롤러")
public class ChatStatusAgentRestController
{
    private final ChatStatusAgentService   chatStatusAgentService;
    private final ChatStatusAgentValidator chatStatusAgentValidator;


    /**
     * 
     * @param  telewebJSON
     * @param  result
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "채팅현황상담사-대시보드조회",
                  notes = "채팅현황상담사 대시보드조회")
    @PostMapping("/chat-api/status/agent/dashboard/inqire")
    public Object selectRtnAgentDashboard(@TelewebJsonParam TelewebJSON telewebJSON, BindingResult result) throws TelewebApiException
    {
        //Validation 체크
        chatStatusAgentValidator.validate(telewebJSON, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        TelewebJSON rtnAgentMonitoring = chatStatusAgentService.selectRtnAgentDashboard(telewebJSON);

        return rtnAgentMonitoring;
    }


    /**
     * 
     * @param  telewebJSON
     * @param  result
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "채팅현황상담사-현황조회",
                  notes = "채팅현황상담사 현황조회")
    @PostMapping("/chat-api/status/agent/sttus/inqire")
    public Object selectRtnAgentMonitoringStatus(@TelewebJsonParam TelewebJSON telewebJSON, BindingResult result) throws TelewebApiException
    {
        //Validation 체크 
//        chatStatusAgentValidator.validate(telewebJSON, result);
//        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        TelewebJSON rtnAgentMonitoring = chatStatusAgentService.selectRtnAgentMonitoringStatus(telewebJSON);

        return rtnAgentMonitoring;
    }


    /**
     * 
     * @param  telewebJSON
     * @param  result
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "홈화면 sns상담 현황",
                  notes = "홈화면 sns상담 현황")
    @PostMapping("/chat-api/status/chnnal/sttus/inqire")
    public Object selectChnStts(@TelewebJsonParam TelewebJSON telewebJSON, BindingResult result) throws TelewebApiException
    {
        //Validation 체크 
//        chatStatusAgentValidator.validate(telewebJSON, result);
//        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        TelewebJSON rtnAgentMonitoring = chatStatusAgentService.selectChnStts(telewebJSON);

        return rtnAgentMonitoring;
    }


    /**
     * 
     * @param  telewebJSON
     * @param  result
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "오늘의 상담 현황 / 전일대비",
                  notes = "오늘의 상담 현황 / 전일대비")
    @PostMapping("/chat-api/status/agent/today/inqire")
    public Object selectSttsToday(@TelewebJsonParam TelewebJSON telewebJSON, BindingResult result) throws TelewebApiException
    {
        //Validation 체크 
//        chatStatusAgentValidator.validate(telewebJSON, result);
//        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        TelewebJSON rtnAgentMonitoring = chatStatusAgentService.selectSttsToday(telewebJSON);

        return rtnAgentMonitoring;
    }
}
