package kr.co.hkcloud.palette3.phone.outbound.api;


import io.swagger.annotations.Api;
import kr.co.hkcloud.palette3.phone.outbound.app.PhoneOutboundRegistSmsPopupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneOutboundRegistSmsPopupRestController",
     description = "sms팝업 REST 컨트롤러")
public class PhoneOutboundRegistSmsPopupRestController
{
    private final PhoneOutboundRegistSmsPopupService phoneOutboundRegistSmsPopupService;

}
