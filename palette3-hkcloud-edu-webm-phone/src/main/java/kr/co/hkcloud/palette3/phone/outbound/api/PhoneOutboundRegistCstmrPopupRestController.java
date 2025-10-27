package kr.co.hkcloud.palette3.phone.outbound.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.outbound.app.PhoneOutboundRegistCstmrPopupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneOutboundRegistCstmrPopupRestController",
     description = "고객단건등록 REST 컨트롤러")
public class PhoneOutboundRegistCstmrPopupRestController
{
    private final PhoneOutboundRegistCstmrPopupService phoneOutboundRegistCstmrPopupService;

    /**
     * 아웃바운드 팝업 조회한다.
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "아웃바운드 조회",
                  notes = "아웃바운드 조회")
    @PostMapping("/phone-api/outbound/manage/single-cstmr-regist-popup/inqire")
    public Object selectObndCam(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        objRetParams = phoneOutboundRegistCstmrPopupService.selectObndCam(jsonParams);

        return objRetParams;
    }


    /**
     * 아웃바운드 단건 등록한다.
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "아웃바운드 단건 등록",
                  notes = "아웃바운드 단건 등록")
    @PostMapping("/phone-api/outbound/manage/single-cstmr-regist-popup/regist")
    public Object insertObndSnglReg(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {

        System.out.println("single-cstmr-regist-popup/regist==>" + mjsonParams);
        TelewebJSON objRetKey = new TelewebJSON(mjsonParams);
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);   //반환 파라메터 생성
        TelewebJSON obndCustParams = new TelewebJSON(mjsonParams);

        // FORM(DATA)데이터
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        // 아웃바운드 고객 중복 체크
        objRetKey = phoneOutboundRegistCstmrPopupService.selectObndSnglRegDubChk(mjsonParams);

        if(objRetKey.getInt("CNT") == 0) {

            //아웃바운드 고객 단건 등록
            objRetParams = phoneOutboundRegistCstmrPopupService.insertObndSnglReg(mjsonParams);

        }
        else {

            // 헤더값 세팅
            objRetParams.setHeader("TOT_COUNT", 0);
            objRetParams.setHeader("COUNT", 0);
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "중복된 건수가 있습니다.");
        }

        //최종결과값 반환
        return objRetParams;
    }

}
