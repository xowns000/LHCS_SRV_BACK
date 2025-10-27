package kr.co.hkcloud.palette3.v3.gwm.service;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

/**
 * packageName    : kr.co.hkcloud.palette3.v3.gwm.service
 * fileName       : V3GwmService
 * author         : njy
 * date           : 2024-03-13
 * description    : Gwm전용 if Service
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-03-13           njy            최초 생성
 * </pre>
 */
public interface V3GwmService {

    TelewebJSON mergeCustInfo(
        TelewebJSON jsonParam) throws TelewebAppException, InvalidAlgorithmParameterException, UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;
    TelewebJSON selectCustomer(
        TelewebJSON jsonParam) throws TelewebAppException, InvalidAlgorithmParameterException, UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;
}

