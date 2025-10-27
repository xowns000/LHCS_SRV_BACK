package kr.co.hkcloud.palette3.chat.status.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "ChatStatusCounselingListController",
     description = "채팅현황상담중현황및3차채팅-화면 컨트롤러")
public class ChatStatusCounselingListController
{
    /**
     * 
     * @return
     */
    @ApiOperation(value = "채팅현황상담중현황및3차채팅-화면",
                  notes = "채팅현황상담중현황및3차채팅 화면으로 이동한다")
    @GetMapping("/chat/status/web/cnslt-list")
    public String moveTalkMngCnsl() throws TelewebWebException
    {
        log.debug("moveTalkMngCnsl");
        return "chat/status/chat-status-counseling-list";
    }
}
