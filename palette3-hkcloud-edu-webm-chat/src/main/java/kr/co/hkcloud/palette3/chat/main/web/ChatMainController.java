package kr.co.hkcloud.palette3.chat.main.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "ChatMainController",
     description = "채팅메인 컨트롤러")
public class ChatMainController
{
    /**
     * 채팅 메인 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "채팅 메인 페이지",
                  notes = "채팅 메인 페이지로 이동한다")
    @GetMapping("/chat/web/main")
    public String moveChatMain() throws TelewebWebException
    {
        log.debug("moveChatMain :::");

        return "chat/main/chat-main";
    }


    /**
     * 텔레톡 메인 페이지 팝업 상담이력 로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "텔레톡 메인 페이지",
                  notes = "텔레톡 메인 페이지로 이동한다")
    @RequestMapping(value = "/main/hkcdv/web/TalkMainHistoryPop",
                    method = RequestMethod.GET)
    public String moveTalkMainHistoryPop() throws TelewebWebException
    {
        String mainTalkPage = "main/hkcdv/TalkMainHistoryPop";
        log.debug("moveTalkMain ::: {}", mainTalkPage);
        return mainTalkPage;
    }
}
