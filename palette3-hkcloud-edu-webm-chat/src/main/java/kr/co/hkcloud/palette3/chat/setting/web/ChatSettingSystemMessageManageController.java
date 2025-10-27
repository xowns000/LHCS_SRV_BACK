package kr.co.hkcloud.palette3.chat.setting.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "ChatSettingSystemMessageManageController",
     description = "채팅설정시스테메시지관리-화면 컨트롤러")
public class ChatSettingSystemMessageManageController
{
    /**
     * @return
     */
    @ApiOperation(value = "채팅설정시스테메시지관리-화면",
                  notes = "채팅설정시스테메시지관리 화면으로 이동한다")
    @GetMapping("/chat/setting/web/system-mssage-manage")
    public String moveTalkMngSystemMsg() throws TelewebWebException
    {
        log.debug("moveTalkMngSystemMsg");
        return "chat/setting/chat-setting-system-message-manage";
    }
}
