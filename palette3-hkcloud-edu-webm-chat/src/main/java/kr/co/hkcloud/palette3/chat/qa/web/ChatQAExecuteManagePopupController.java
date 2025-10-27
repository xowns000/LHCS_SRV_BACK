package kr.co.hkcloud.palette3.chat.qa.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "ChatQAExecuteManagePopupController",
     description = "QA실행 컨트롤러")
public class ChatQAExecuteManagePopupController
{
    /**
     * QA실행 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "QA실행 페이지",
                  notes = "QA실행 페이지로 이동한다")
    @GetMapping("/chat/qa/web/execut-manage/qa-execute-popup")
    public String moveTalkMngQam04() throws TelewebWebException
    {
        log.debug("execute-manage-popup");
        return "chat/qa/chat-qa-execute-manage-popup";
    }
}
