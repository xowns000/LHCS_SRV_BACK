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
@Api(value = "StatisticsChatCounselByCounselTypeController",
     description = "통계채팅상담(상담유형별)-화면 컨트롤러")
public class StatisticsChatCounselByCounselTypeController
{
    /**
     * 
     * @return
     */
    @ApiOperation(value = "통계채팅상담(상담유형별)-화면",
                  notes = "통계채팅상담(상담유형별) 페이지로 이동한다")
    @GetMapping("/statistics/chat/web/counsel-by-counsel-type")
    public String moveStatisticsChatCounselByCounselType() throws TelewebWebException
    {
        log.debug("moveStatisticsChatCounselByCounselType");
        return "statistics/chat/statistics-chat-counsel-by-counsel-type";
    }
}
