package kr.co.hkcloud.palette3.chat.qa.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "ChatQAEvaluationPaperManagController",
     description = "QA평가지관리 컨트롤러")
public class ChatQAEvaluationPaperManageController
{
    /**
     * QA평가지관리 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "QA평가지관리 페이지",
                  notes = "QA평가지관리 페이지로 이동한다")
    @GetMapping("/chat/qa/web/evl-paper-manage")
    public String moveTalkMngQam01() throws TelewebWebException
    {
        log.debug("evaluation-paper-manage");
        return "chat/qa/chat-qa-evaluation-paper-manage";
    }
}
