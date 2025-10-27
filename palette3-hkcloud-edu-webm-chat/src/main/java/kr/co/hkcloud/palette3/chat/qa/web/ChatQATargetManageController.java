package kr.co.hkcloud.palette3.chat.qa.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "ChatQATargetManageController",
     description = "QA대상등록관리 컨트롤러")
public class ChatQATargetManageController
{
    /**
     * QA대상등록관리 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "QA대상등록관리 페이지",
                  notes = "QA대상등록관리 페이지로 이동한다")
    @GetMapping("/chat/qa/web/trget-regist-manage")
    public String moveTalkMngQam02() throws TelewebWebException
    {
        log.debug("chat-qa-target-manage");
        return "chat/qa/chat-qa-target-manage";
    }
}
