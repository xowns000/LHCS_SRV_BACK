package kr.co.hkcloud.palette3;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.core.util.PaletteMailUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 팔레트 WEB ROOT 컨트롤러
 * 
 * @author Orange
 *
 */
@Slf4j
@RestController
@Api(value = "PaletteWebRootController", description = "팔레트 WEB ROOT 컨트롤러")
public class PaletteWebRootController {

    @Value("${k8s.pod.name}")
    private String k8sPodName;

    @Autowired
    public PaletteMailUtils paletteMailUtils;

    /**
     * @return
     */
    @ApiOperation(value = "루트 시작 페이지", notes = "루트 시작 페이지로 이동한다")
    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> root() {
        return new ResponseEntity<>("## Welcome / " + k8sPodName, HttpStatus.OK);
    }

    /**
     * LB HealthCheck 용도.
     * @param response
     * @return
     */
    @RequestMapping(value = "/healthz", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> healthz(HttpServletResponse response) {
        return new ResponseEntity<>("## healthz " + k8sPodName, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/welcome", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> welcome() {
        log.debug("## Welcome:04 /api/welcome :: " + k8sPodName);
        return new ResponseEntity<>("## Welcome /api/welcome :: " + k8sPodName, HttpStatus.OK);
    }

    @RequestMapping(value = "/auth-api/welcome", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> authapi() {
        log.debug("## Welcome /auth-api/welcome :: " + k8sPodName);
        return new ResponseEntity<>("## Welcome /auth-api/welcome :: " + k8sPodName, HttpStatus.OK);
    }

    @RequestMapping(value = "/chat-api/welcome", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> chatapi() {
        log.debug("## Welcome /chat-api/welcome :: " + k8sPodName);
        return new ResponseEntity<>("## Welcome /chat-api/welcome :: " + k8sPodName, HttpStatus.OK);
    }

    @RequestMapping(value = "/phone-api/welcome", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> phoneapi() {
        log.debug("## Welcome /phone-api/welcome :: " + k8sPodName);
        return new ResponseEntity<>("## Welcome /phone-api/welcome :: " + k8sPodName, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/test/sendmail", method = {RequestMethod.GET, RequestMethod.POST})
    public Object sendmail(@TelewebJsonParam TelewebJSON mjsonParams) {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);	//반환 파라메터 생성

        String emlTo = "kjd4717@nate.com";		// 수신자
        String subject = "이메일 발송";								// eml 제목
        String emalFrom = "kjd4717@hkcloud.co.kr";					// 송신자
        String EML_CNTN = "메일내용.";	// eml 내용
        String sendContents = EML_CNTN;

        try {
            paletteMailUtils.sendMimeMessage(emlTo, subject, sendContents, emalFrom, objRetParams);
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new TelewebAppException(e.getLocalizedMessage());
        }

        return "전송??";
    }

}
