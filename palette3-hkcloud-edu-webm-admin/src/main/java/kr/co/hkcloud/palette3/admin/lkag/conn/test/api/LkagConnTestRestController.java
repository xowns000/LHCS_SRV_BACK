package kr.co.hkcloud.palette3.admin.lkag.conn.test.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.reflect.InvocationTargetException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import kr.co.hkcloud.palette3.admin.lkag.conn.test.app.LkagConnTestService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : kr.co.hkcloud.palette3.admin.lkag.conn.test.api
 * fileName       : LkagConnRestController
 * author         : 연동_연결
 * date           : 2024-03-20
 * description    : << 여기 설명 >>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-03-20        KJD       최초 생성
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class LkagConnTestRestController {

    private final LkagConnTestService LkagConnTestService;

    @PostMapping("/admin-api/lkag/conn/test")
    public Object test(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, JsonProcessingException {
        return LkagConnTestService.executeTest( mjsonParams );
    }



}
