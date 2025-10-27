package kr.co.hkcloud.palette3.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : kr.co.hkcloud.palette3.integration
 * fileName       : PaletteIntgrWelcomeController
 * author         : KJD
 * date           : 2024-04-12
 * description    : << 여기 설명 >>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-04-12        KJD       최초 생성
 * </pre>
 */
@Slf4j
@RestController
public class PaletteIntgrWelcomeController {
    @Value("${k8s.pod.name}")
    private String k8sPodName;

    @RequestMapping(value = "/intgr-api/welcome", method = {RequestMethod.GET, RequestMethod.POST})
    public Object welcome() {
        log.debug("[intgr-api] Welcome :: " + k8sPodName);
        return "[intgr-api] Welcome :: " + k8sPodName;
    }
}
