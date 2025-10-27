package kr.co.hkcloud.palette3.statistics.phone.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "StatisticsPhoneCnsltTypeController",
     description = "상담유형별(유형별)통계 컨트롤러")
public class StatisticsPhoneCnsltTypeController
{
    /**
     * 상담유형별(유형별)통계 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "상담유형별(유형별)통계 페이지",
                  notes = "상담유형별(유형별)통계 이동한다")
    @GetMapping("/statistics/phone/web/cnslt-ty")
    public String moveCnslTypeTypeSttc() throws TelewebWebException
    {
        log.debug("moveCnslTypeTypeSttc");
        return "statistics/phone/statistics-phone-cnslt-ty";
    }
}
