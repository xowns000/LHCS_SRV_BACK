package kr.co.hkcloud.palette3.integration.service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import net.sf.json.JSONObject;

/**
 * packageName    : kr.co.hkcloud.palette3.external.commerce_api.app
 * fileName       : CommerceService
 * author         : KJD
 * date           : 2024-04-09
 * description    : << 여기 설명 >>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-04-09        KJD       최초 생성
 * </pre>
 */
public interface CommerceApiService {

    TelewebJSON oauthCode( TelewebJSON jsonParams) throws TelewebAppException;  // 인증
    TelewebJSON sample_authentication( TelewebJSON jsonParams) throws TelewebAppException;  // 인증
    TelewebJSON authentication( TelewebJSON jsonParams, JSONObject baseDataObj) throws TelewebAppException;  // 인증
    TelewebJSON call_api(TelewebJSON jsonParams) throws TelewebAppException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, ParseException, UnsupportedEncodingException;
    TelewebJSON call_batch_api(TelewebJSON jsonParams) throws TelewebAppException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, ParseException, UnsupportedEncodingException;

}
