package kr.co.hkcloud.palette3.chat.qa.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "ChatQAResultManageController",
     description = "QA결과관리 컨트롤러")
public class ChatQAResultManageController
{
    /**
     * QA결과관리 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "QA결과관리 페이지",
                  notes = "QA결과관리 페이지로 이동한다")
    @GetMapping("/chat/qa/web/result-manage")
    public String moveTalkMngQam03() throws TelewebWebException
    {
        log.debug("chat-qa-result-manage");
        return "chat/qa/chat-qa-result-manage";
    }
}
