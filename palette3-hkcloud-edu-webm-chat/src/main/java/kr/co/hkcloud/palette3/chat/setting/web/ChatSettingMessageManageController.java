package kr.co.hkcloud.palette3.chat.setting.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "ChatSettingMessageManageController",
     description = "채팅설정메시지관리-화면 컨트롤러")
public class ChatSettingMessageManageController
{
    /**
     * 메시지설정 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "채팅설정메시지관리-화면",
                  notes = "채팅설정메시지관리-화면으로 이동한다")
    @GetMapping("/chat/setting/web/message-manage")
    public String moveTalkMngCnslMsg() throws TelewebWebException
    {
        log.debug("moveTalkMngCnslMsg");
        return "chat/setting/chat-setting-message-manage";
    }
}
