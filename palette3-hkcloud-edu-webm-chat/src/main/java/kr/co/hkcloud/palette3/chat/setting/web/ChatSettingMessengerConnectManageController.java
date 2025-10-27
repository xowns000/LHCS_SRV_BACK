package kr.co.hkcloud.palette3.chat.setting.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "ChatSettingMessengerConnectManageController",
     description = "채팅설정메신저연동관리-화면(구 채널연동목록) 컨트롤러")
public class ChatSettingMessengerConnectManageController
{
    /**
     * 
     * @return
     */
    @ApiOperation(value = "채팅설정메신저연동관리-화면(구 채널연동목록)",
                  notes = "채팅설정메신저연동관리(구 채널연동목록) 페이지로 이동한다")
    @GetMapping("/chat/setting/web/messenger-connect-manage")
    public String moveChatSettingMessengerConnectManage() throws TelewebWebException
    {
        log.debug("moveChatSettingMessengerConnectManage");
        return "chat/setting/chat-setting-messenger-connect-manage";
    }


    /**
     * 
     * @return
     */
    @ApiOperation(value = "채팅설정메신저연동관리-등록팝업-화면(구 채널연동목록)",
                  notes = "채팅설정메신저연동관리-등록팝업(구 채널연동목록) 페이지로 이동한다")
    @GetMapping("/chat/setting/web/messenger-connect-manage/regist-popup")
    public String moveChatSettingMessengerConnectManageRegistPopup() throws TelewebWebException
    {
        log.debug("moveChatSettingMessengerConnectManageRegistPopup");
        return "chat/setting/chat-setting-messenger-connect-manage-regist-popup";
    }


    /**
     * 
     * @return
     */
    @ApiOperation(value = "채팅설정메신저연동관리-상세팝업-화면(구 채널연동목록)",
                  notes = "채팅설정메신저연동관리-상세팝업(구 채널연동목록) 페이지로 이동한다")
    @GetMapping("/chat/setting/web/messenger-connect-manage/detail-popup")
    public String moveChatSettingMessengerConnectManageDetailPopup() throws TelewebWebException
    {
        log.debug("moveChatSettingMessengerConnectManageDetailPopup");
        return "chat/setting/chat-setting-messenger-connect-manage-detail-popup";
    }
}
