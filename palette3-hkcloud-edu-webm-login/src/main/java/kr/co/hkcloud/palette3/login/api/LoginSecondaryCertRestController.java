package kr.co.hkcloud.palette3.login.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.login.app.LoginSecondaryCertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : kr.co.hkcloud.palette3.login.api
 * fileName       : LoginSecondaryCertRestController
 * author         : njy
 * date           : 2024-02-01
 * description    : 2차 인증 REST CONTROLLER
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-02-01           njy            최초 생성
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@RestController("LoginSecondaryCertRestController")
@Api(value = "LoginSecondaryCertRestController", description = "Palette 로그인 2차인증 컨트롤러")
public class LoginSecondaryCertRestController {

    private final LoginSecondaryCertService loginSecondaryCertService;

    /**
     * 2차인증 - 인증번호 발급
     *
     * @author NJY
     * @since 2024-01-29
     * @version 1.0
     * <pre>
     * ===================================
     * DATE              AUTHOR             NOTE
     * -----------------------------------------------------------
     * 2024-01-29        NJY       최초 생성
     * </pre>
     */
    @ApiOperation(value = "2차 인증 - 인증번호 발급",
        notes = "2차인증 인증번호를 발급한다.")
    @PostMapping("/auth-api/secondaryCert/publishNo")
    public Object publishNo(@TelewebJsonParam TelewebJSON mjsonParam, HttpServletRequest request) throws TelewebApiException {
        TelewebJSON objRetParam = new TelewebJSON(mjsonParam);

        objRetParam = loginSecondaryCertService.publishNo(mjsonParam, request);

        return objRetParam;
    };

    /**
     * 2차인증 - 인증번호 식별
     *
     * @author NJY
     * @since 2024-01-29
     * @version 1.0
     * <pre>
     * ===================================
     * DATE              AUTHOR             NOTE
     * -----------------------------------------------------------
     * 2024-01-29        NJY       최초 생성
     * </pre>
     */
    @ApiOperation(value = "2차 인증 - 인증번호 식별",
        notes = "2차인증 인증번호를 식별한다.")
    @PostMapping("/auth-api/secondaryCert/VerificateCode")
    public Object VerificateCode(@TelewebJsonParam TelewebJSON mjsonParam) throws TelewebApiException{
        TelewebJSON objRetParam = new TelewebJSON(mjsonParam);

        objRetParam = loginSecondaryCertService.VerificateCode(mjsonParam);

        return objRetParam;
    };
}
