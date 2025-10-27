package kr.co.hkcloud.palette3.statistics.phone.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.statistics.phone.app.StatisticsPhoneOutboundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "StatisticsPhoneOutboundRestController",
     description = "아웃바운드통계 컨트롤러")
public class StatisticsPhoneOutboundRestController
{
    private final StatisticsPhoneOutboundService statisticsPhoneOutboundService;


    /**
     * 
     * @param  jsonParam
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "아웃바운드 목록(팝업)",
                  notes = "아웃바운드 목록(팝업)을 조회한다.")
    @PostMapping("/api/statistics/phone/outbnd/inqire-popup/inqire")
    public Object selectObndListPop(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {
        TelewebJSON objParam = new TelewebJSON(jsonParam);

        objParam = statisticsPhoneOutboundService.selectObndListPop(jsonParam);

        return objParam;
    }


    @ApiOperation(value = "아웃바운드통계",
                  notes = "아웃바운드통계를 조회한다.")
    @PostMapping("/api/statistics/phone/outbnd/inqire")
    public Object selectObndSttcList(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {
        TelewebJSON objParam = new TelewebJSON(jsonParam);

        objParam = statisticsPhoneOutboundService.selectObndSttcList(jsonParam);

        return objParam;
    }

}
