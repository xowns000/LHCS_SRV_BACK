package kr.co.hkcloud.palette3.phone.sendInfo.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.message.app.MessageService;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.sendInfo.app.PhoneSendPlaceContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneSendPlaceContactRestController", description = "위치정보, 주소록정보 전송 REST 컨트롤러")
public class PhoneSendPlaceContactRestController {

    private final PhoneSendPlaceContactService phoneSendPlaceContactService;
    private final MessageService messageService;


    /**
     * 메서드 설명		: 위치정보, 연락처정보 조회
     */
    @ApiOperation(value = "위치정보, 연락처정보 조회", notes = "위치정보, 연락처정보를 조회한다")
    @PostMapping("/phone-api/placeContact/selectplaceContact")
    public Object selectPlace(@TelewebJsonParam TelewebJSON jsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        objRetParams = phoneSendPlaceContactService.selectPlace(jsonParams);

        return objRetParams;
    }


    /**
     * 메서드 설명		: 문자,알림톡 단건 전송
     */
    @ApiOperation(value = "문자,알림톡 단건 전송", notes = "문자,알림톡을 단건 전송한다")
    @PostMapping("/phone-api/placeContact/sendInfo")
    public Object sendInfo(
        @TelewebJsonParam TelewebJSON jsonParams) throws TelewebApiException, URISyntaxException, UnsupportedEncodingException, JsonProcessingException {
        TelewebJSON objRetParams = new TelewebJSON();

        //기존 phone-api에서 common api로 변경
//        objRetParams = phoneSendPlaceContactService.sendInfo(jsonParams);
        objRetParams = messageService.sendInfo(jsonParams);

        return objRetParams;
    }

    /**
     * 메서드 설명		: 위치정보,연락처정보 전용 템플릿 조회
     */
    @ApiOperation(value = "위치정보,연락처정보 전용 템플릿 조회", notes = "위치정보,연락처정보 전용 템플릿을 조회한다")
    @PostMapping("/phone-api/placeContact/selectTempleteInfo")
    public Object selectTempleteInfo(@TelewebJsonParam TelewebJSON jsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        objRetParams = phoneSendPlaceContactService.selectTempleteInfo(jsonParams);

        return objRetParams;
    }

    /**
     * 메서드 설명		: 알림톡 단건 전송 템플릿 조회
     */
    @ApiOperation(value = "알림톡 단건 전송 템플릿 조회", notes = "알림톡 단건 전송 템플릿을 조회한다")
    @PostMapping("/phone-api/singleAtalk/selectAtalkTemplete")
    public Object selectAtalkTemplete(@TelewebJsonParam TelewebJSON jsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        objRetParams = phoneSendPlaceContactService.selectAtalkTemplete(jsonParams);

        return objRetParams;
    }


}
