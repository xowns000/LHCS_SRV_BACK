package kr.co.hkcloud.palette3.v3;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : kr.co.hkcloud.palette3.v3
 * fileName       : WelcomeController
 * author         : KJD
 * date           : 2023-12-19
 * description    : << 여기 설명 >>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-12-19        KJD       최초 생성
 */
@Slf4j
@RestController
@Api(value = "PaletteV3WelcomeController", description = "팔레트 WEB ROOT 컨트롤러")
public class PaletteV3WelcomeController {

    @Value("${k8s.pod.name}")
    private String k8sPodName;

    @RequestMapping(value = "/v3-api/welcome", method = {RequestMethod.GET, RequestMethod.POST})
    public Object welcome() {
        log.debug("[v3-api] Welcome :: " + k8sPodName);
        return "[v3-api] Welcome :: " + k8sPodName;
    }
}
