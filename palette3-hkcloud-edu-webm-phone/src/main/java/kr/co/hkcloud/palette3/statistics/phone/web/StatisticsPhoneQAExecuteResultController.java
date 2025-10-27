package kr.co.hkcloud.palette3.statistics.phone.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "StatisticsPhoneQAExecuteResultController",
     description = "QA평가통계 컨트롤러")
public class StatisticsPhoneQAExecuteResultController
{
    /**
     * QA평가통계 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "QA평가통계 페이지",
                  notes = "QA평가통계 페이지로 이동한다")
    @GetMapping("/statistics/phone/web/qa-execute-result")
    public String moveQamStats() throws TelewebWebException
    {
        log.debug("moveStatisticsPhoneQAExecuteResult");
        return "statistics/phone/statistics-phone-qa-execute-result";
    }
}
