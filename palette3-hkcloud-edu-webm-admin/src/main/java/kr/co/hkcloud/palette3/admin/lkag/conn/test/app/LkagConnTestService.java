package kr.co.hkcloud.palette3.admin.lkag.conn.test.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.reflect.InvocationTargetException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

/**
 * packageName    : kr.co.hkcloud.palette3.admin.lkag.conn.app
 * fileName       : LkagConnService
 * author         : KJD
 * date           : 2024-03-20
 * description    : << 여기 설명 >>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-03-20        KJD       최초 생성
 * </pre>
 */
public interface LkagConnTestService {
    TelewebJSON executeTest(TelewebJSON jsonParams) throws TelewebAppException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, JsonProcessingException;
}
