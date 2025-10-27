package kr.co.hkcloud.palette3.statistics.chat.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "StatisticsChatQAExecuteResultController",
     description = "QA평가통계 컨트롤러")
public class StatisticsChatQAExecuteResultController
{
    /**
     * QA평가통계 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "QA평가통계 페이지",
                  notes = "QA평가통계 페이지로 이동한다")
    @GetMapping("/statistics/chat/web/qa-evl-result")
    public String moveTalkMngQam05() throws TelewebWebException
    {
        log.debug("statistics-chat-qa-execute-result");
        return "statistics/chat/statistics-chat-qa-execute-result";
    }
}
