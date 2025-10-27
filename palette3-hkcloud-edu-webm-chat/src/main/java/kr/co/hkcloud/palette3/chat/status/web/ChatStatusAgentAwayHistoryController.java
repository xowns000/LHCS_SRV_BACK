package kr.co.hkcloud.palette3.chat.status.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "ChatStatusAgentAwayHistoryController",
     description = "채팅현황상담사이석이력-화면 컨트롤러")
public class ChatStatusAgentAwayHistoryController
{
    /**
     * 
     * @return
     */
    @ApiOperation(value = "채팅현황상담사이석이력-화면",
                  notes = "채팅현황상담사이석이력 페이지로 이동한다")
    @GetMapping("/chat/status/web/agent-away-history")
    public String moveChatStatusAgentAwayHistory() throws TelewebWebException
    {
        log.debug("moveChatStatusAgentAwayHistory");
        return "chat/status/chat-status-agent-away-history";
    }
}
