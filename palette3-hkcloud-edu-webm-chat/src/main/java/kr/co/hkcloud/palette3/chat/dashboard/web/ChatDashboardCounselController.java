package kr.co.hkcloud.palette3.chat.dashboard.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "ChatDashboardCounselController",
     description = "채팅대시보드상담-화면(구 채팅상담모니터링) 컨트롤러")
public class ChatDashboardCounselController
{
    /**
     * /monitoring/web/TalkNewMonitoring
     * 
     * @return
     */
    @ApiOperation(value = "채팅대시보드상담-화면(구 채팅상담모니터링)",
                  notes = "채팅대시보드상담(구 채팅상담모니터링) 페이지로 이동한다.")
    @GetMapping("/chat/dashboard/web/counsel")
    public String moveChatDashboardCounsel() throws TelewebWebException
    {
        log.debug("moveChatDashboardCounsel");
        return "chat/dashboard/chat-dashboard-counsel";
    }
}
