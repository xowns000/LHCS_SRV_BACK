package kr.co.hkcloud.palette3.chat.history.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "ChatHistoryManageController",
     description = "채팅이력관리-화면 컨트롤러")
public class ChatHistoryManageController
{
    /**
     * 
     * @return
     */
    @ApiOperation(value = "채팅이력관리-화면",
                  notes = "채팅이력관리 페이지로 이동한다")
    @GetMapping("/chat/history/web/manage")
    public String moveChatHistoryManage() throws TelewebWebException
    {
        log.debug("moveChatHistoryManage");
        return "chat/history/chat-history-manage";
    }
}
