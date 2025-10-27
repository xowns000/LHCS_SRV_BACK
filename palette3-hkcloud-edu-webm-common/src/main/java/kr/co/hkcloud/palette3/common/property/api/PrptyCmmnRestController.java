package kr.co.hkcloud.palette3.common.property.api;


import java.util.Optional;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.NoBizLog;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.config.properties.palette.PaletteProperties;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PrptyCmmnRestController",
     description = "프로퍼티공통 REST 컨트롤러")
public class PrptyCmmnRestController
{
    private final PaletteProperties paletteProperties;


    /**
     * 프로퍼티 반환 (보안상 자바스크립트에서 호출하면 안되며, 필요하면 아래 getJsProperty 함수에 정의 후 사용)
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "프로퍼티 반환",
                  notes = "프로퍼티에 정의된 KEY값을 반환한다.")
    @PostMapping("/api/property/common/prpty/inqry")
    public Object getProperty(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        log.debug("TwbCommonRestController.getProperty");

        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        String strKeyName = mjsonParams.getString("KEY_NAME");
        String strDefault = mjsonParams.getString("DEFAULT");

        // Properties 현재 함수를 호출하는 부분에서는 KEY_NAME이 "SERVICE_MODE" 밖에 없으나 추후 확인 필요
        String strValue = paletteProperties.getServiceMode().name();
        if(strValue == null || "".equals(strValue)) {
            strValue = strDefault;
        }

        //반환정보 설정
        objRetParams.setString("RET_VAL", strValue);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 프로퍼티에 정의된 KEY값을 자바스크립트에 반환한다. (보안상 SERVICE_MODE, PARTNER_ID 만 허용)
     * 
     * @param  mjsonParams
     * @return
     */
    @NoBizLog
    @ApiOperation(value = "프로퍼티 자바스크립트 반환",
                  notes = "프로퍼티에 정의된 KEY값을 자바스크립트에 반환한다.")
    @PostMapping("/api/property/common/prpty-js/inqry")
    public Object getJsProperty(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        log.debug("TwbCommonRestController.getJsProperty");

        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        String strKeyName = mjsonParams.getString("KEY_NAME");
        String strDefault = mjsonParams.getString("DEFAULT");
        Optional.ofNullable(strKeyName).ifPresent(key -> {

            String strValue = "";
            switch(key)
            {
                case "SERVICE_MODE":
                {
                    strValue = paletteProperties.getServiceMode().name();
                    break;
                }
                case "PARTNER_ID":
                {
                    strValue = paletteProperties.getPartnerId();
                    break;
                }
                // OPENURL_TTALK / AspCallCenterPhoneNum / FILE_UPLOAD_PATH 삭제 필요
//                case "OPENURL_TTALK":
//                {
//                    strValue = chatProperties.getMessenger().getTtalk().getUrls().getChatOpen().toString();
//                    break;
//                }
//                case "AspCallCenterPhoneNum":
//                {
//                	strValue = paletteProperties.getAsp().getCallCenterPhoneNum();
//                    break;
//                }
//                case "FILE_UPLOAD_PATH":
//                {
//                    strValue = paletteProperties.getRepository().getTrgtTypeCd().name();
//                    break;
//                }
            }
            if(strValue == null || "".equals(strValue)) {
                strValue = strDefault;
            }
            //반환정보 설정
            objRetParams.setString("RET_VAL", strValue);
        });

        //최종결과값 반환
        return objRetParams;
    }
}
