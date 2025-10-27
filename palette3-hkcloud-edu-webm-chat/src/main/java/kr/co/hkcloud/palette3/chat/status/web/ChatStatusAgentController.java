package kr.co.hkcloud.palette3.chat.status.web;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.date.util.DateCmmnUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "ChatStatusAgentController",
     description = "채팅현황상담사-화면 컨트롤러")
public class ChatStatusAgentController
{
    /**
     * /monitoring/web/TalkAgentMonitoring
     * 
     * @return
     */
    @ApiOperation(value = "채팅현황상담사-화면",
                  notes = "채팅현황상담사 페이지로 이동한다")
    @GetMapping("/chat/status/web/agent")
    public String moveChatStatusAgent(Model model) throws TelewebWebException
    {
        log.debug("moveChatStatusAgent");

        model.addAttribute("timestamp", DateCmmnUtils.toEpochMilli());
        return "chat/status/chat-status-agent";
    }
}
