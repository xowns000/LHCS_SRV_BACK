package kr.co.hkcloud.palette3.chat.setting.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "ChatSettingBannedWordController",
     description = "채팅설정금칙어관리-화면 컨트롤러")
public class ChatSettingBannedWordController
{
    /**
     * 
     * @return
     */
    @ApiOperation(value = "채팅설정금칙어관리-화면",
                  notes = "채팅설정금칙어관리 페이지로 이동한다")
    @GetMapping("/chat/setting/web/banned-word")
    public String moveChatSettingBannedWord() throws TelewebWebException
    {
        log.debug("moveChatSettingBannedWord");
        return "chat/setting/chat-setting-banned-word";
    }
}
