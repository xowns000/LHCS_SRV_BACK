package kr.co.hkcloud.palette3.phone.outbound.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.outbound.app.PhoneOutboundRegistAgentChangePopupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneOutboundRegistAgentChangePopupRestController",
     description = "상담원변경팝업REST 컨트롤러")
public class PhoneOutboundRegistAgentChangePopupRestController
{
    private final PhoneOutboundRegistAgentChangePopupService phoneOutboundRegistAgentChangePopupService;

    /**
     * 배분변경 상담원 조회한다.
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "배분변경 상담원 조회",
                  notes = "배분변경 상담원 조회")
    @PostMapping("/phone-api/outbound/manage/agent-change-inqire-popup/inqire")
    public Object selectObndDivChangeList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        objRetParams = phoneOutboundRegistAgentChangePopupService.selectObndDivChangeList(jsonParams);

        return objRetParams;
    }


    /**
     * 배분 상담원 변경한다.
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "배분변경 상담원 조회",
                  notes = "배분변경 상담원 조회")
    @PostMapping("/phone-api/outbound/manage/agent-change-inqire-popup/modify")
    public Object updateDivChange(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        objRetParams = phoneOutboundRegistAgentChangePopupService.updateDivChange(jsonParams);

        return objRetParams;
    }

}
