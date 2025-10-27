package kr.co.hkcloud.palette3.chat.script.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "ChatScriptManageController",
     description = "채팅스크립트관리-화면 컨트롤러")
public class ChatScriptManageController
{
    /**
     * @return
     */
    @ApiOperation(value = "채팅스크립트관리-화면",
                  notes = "채팅스크립트관리 화면으로 이동한다")
    @GetMapping("/chat/script/web/manage")
    public String moveTalkMngScript() throws TelewebWebException
    {
        log.debug("moveTalkMngScript");
        return "chat/script/chat-script-manage";
    }
}
