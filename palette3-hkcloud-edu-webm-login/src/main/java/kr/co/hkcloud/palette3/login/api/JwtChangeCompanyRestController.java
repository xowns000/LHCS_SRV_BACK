package kr.co.hkcloud.palette3.login.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController("JwtChangeCompanyRestController")
@Api(value = "JwtChangeCompanyRestController", description = "Compony 변경시 인증토큰 변경 컨트롤러")
public class JwtChangeCompanyRestController {

    @ApiOperation(value = "Compony 변경시 인증토큰 변경", notes = "Compony 변경시 인증토큰 변경")
    @PostMapping("/auth-api/changeComponyToken")
    public Object changeComponyToken(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebAppException {
        // JwtAuthenticationFilter에서 처리함.
        return new TelewebJSON(mjsonParams);
    }
}
