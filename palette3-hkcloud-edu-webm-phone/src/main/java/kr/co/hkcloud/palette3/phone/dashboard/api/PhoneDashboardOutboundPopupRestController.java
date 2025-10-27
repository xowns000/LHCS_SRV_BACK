package kr.co.hkcloud.palette3.phone.dashboard.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.dashboard.app.PhoneDashboardOutboundPopupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api
public class PhoneDashboardOutboundPopupRestController
{
    private final PhoneDashboardOutboundPopupService phoneDashboardOutboundPopupService;

    /**
     * 아웃바운드 진행 현황 처리결과 변경한다.
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "아웃바운드 진행 현황 처리결과 변경",
                  notes = "아웃바운드 진행 현황 처리결과 변경")
    @PostMapping("/phone-api/dashboard/outbound/progrs-sttus-popup/modify")
    public Object updateEotTyChange(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = phoneDashboardOutboundPopupService.updateEotTyChange(jsonParams);

        return objRetParams;
    }

}
