package kr.co.hkcloud.palette3.chat.status.api;


import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.chat.status.app.ChatStatusAgentAwayHistoryService;
import kr.co.hkcloud.palette3.chat.status.util.ChatStatusAgentAwayHistoryValidator;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "ChatStatusAgentAwayHistoryRestController",
     description = "채팅현황상담사이석이력 REST 컨트롤러")
public class ChatStatusAgentAwayHistoryRestController
{
    private final ChatStatusAgentAwayHistoryService   chatStatusAgentAwayHistoryService;
    private final ChatStatusAgentAwayHistoryValidator chatStatusAgentAwayHistoryValidator;


    /**
     * 
     * @param  mjsonParams
     * @return
     * 
     */
    @ApiOperation(value = "채팅현황상담사이석이력/목록",
                  notes = "채팅현황상담사이석이력/목록")
    @PostMapping("/chat-api/status/agent-away-history/list")
    public Object selectUserReadyOffHistory(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        //validation 체크
        chatStatusAgentAwayHistoryValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatStatusAgentAwayHistoryService.selectUserReadyOffHistory(jsonParams);

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     * 
     */
    @ApiOperation(value = "채팅현황상담사이석이력/채팅/조회",
                  notes = "채팅현황상담사이석이력/채팅/조회")
    @PostMapping("/chat-api/status/agent-away-history/chtt/inqire")
    public Object selectUserReadyOffHistory(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatStatusAgentAwayHistoryService.selectUserReadyOffHistory(jsonParams);

        return objRetParams;
    }
}
