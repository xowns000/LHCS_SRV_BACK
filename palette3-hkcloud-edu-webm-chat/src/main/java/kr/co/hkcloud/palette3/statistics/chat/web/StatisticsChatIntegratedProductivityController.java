package kr.co.hkcloud.palette3.statistics.chat.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Controller
@Api(value = "StatisticsChatIntegratedProductivityController",
     description = "통계채팅통합생산성-화면 컨트롤러")
public class StatisticsChatIntegratedProductivityController
{
    /**
     * 
     * @return
     */
    @ApiOperation(value = "통계채팅통합생산성-화면",
                  notes = "통계채팅통합생산성 페이지로 이동한다")
    @GetMapping("/statistics/chat/web/integrated-productivity")
    public String moveStatisticsChatIntegratedProductivity() throws TelewebWebException
    {
        log.debug("moveStatisticsChatIntegratedProductivity");
        return "statistics/chat/statistics-chat-integrated-productivity";
    }
}
