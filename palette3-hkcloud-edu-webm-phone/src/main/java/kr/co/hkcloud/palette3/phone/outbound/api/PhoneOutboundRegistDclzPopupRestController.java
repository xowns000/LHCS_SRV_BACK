package kr.co.hkcloud.palette3.phone.outbound.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.outbound.app.PhoneOutboundRegistDclzPopupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneOutboundRegistDclzPopupRestController",
     description = "상담원근태팝업 REST 컨트롤러")
public class PhoneOutboundRegistDclzPopupRestController
{
    private final PhoneOutboundRegistDclzPopupService phoneOutboundRegistDclzPopupService;

    /**
     * 상담원근태 조회한다.
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "상담원근태 조회",
                  notes = "상담원근태 조회")
    @PostMapping("/phone-api/outbound/manage/agent-dclz-inqire-popup/inqire")
    public Object selectObndDilceList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        objRetParams = phoneOutboundRegistDclzPopupService.selectObndDilceList(jsonParams);

        return objRetParams;
    }


    /**
     * 상담원근태 변경한다.
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "상담원근태 변경",
                  notes = "상담원근태 변경")
    @PostMapping("/phone-api/outbound/manage/agent-dclz-inqire-popup/regist")
    public Object mergeObndDilce(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        JSONArray obndDilceArr = mjsonParams.getDataObject("OBND_DILCE_ARR");
        jsonParams.setDataObject("OBND_DILCE_ARR", obndDilceArr);
        jsonParams.setString("BASE_DATE", mjsonParams.getString("BASE_DATE"));
        jsonParams.setString("REG_MAN", mjsonParams.getString("REG_MAN"));
        jsonParams.setString("CHNG_MAN", mjsonParams.getString("CHNG_MAN"));

        objRetParams = phoneOutboundRegistDclzPopupService.mergeObndDilce(jsonParams);

        return objRetParams;
    }
}
