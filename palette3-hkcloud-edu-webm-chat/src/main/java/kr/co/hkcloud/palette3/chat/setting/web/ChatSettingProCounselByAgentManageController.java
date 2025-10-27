package kr.co.hkcloud.palette3.chat.setting.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.config.properties.palette.PaletteProperties;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Controller
@Api(value = "ChatSettingProCounselByAgentManageController",
     description = "채팅설정사용자별전문상담관리-화면 컨트롤러")
public class ChatSettingProCounselByAgentManageController
{
    private final PaletteProperties paletteProperties;


    /**
     * 
     * @return
     */
    @ApiOperation(value = "채팅설정사용자별전문상담관리-화면",
                  notes = "채팅설정사용자별전문상담관리 페이지로 이동한다")
    @GetMapping("/chat/setting/web/pro-counsel-by-agent-manage")
    public String moveChatSettingProCounselByAgentManage() throws TelewebWebException
    {
        log.debug("moveChatSettingProCounselByAgentManage");
        return "chat/setting/chat-setting-pro-counsel-by-agent-manage";
    }
}
