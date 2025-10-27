package kr.co.hkcloud.palette3.login.app;

import javax.servlet.http.HttpServletRequest;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

/**
 * packageName    : kr.co.hkcloud.palette3.login.app
 * fileName       :
 * author         : njy
 * date           : 2024-02-01
 * description    : <<여기 설명>>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-02-01           njy            최초 생성
 * </pre>
 */
public interface LoginSecondaryCertService {
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
    TelewebJSON publishNo(TelewebJSON mjsonParam, HttpServletRequest request) throws TelewebAppException;

    /**
     * 2차인증 - 인증번호 검증
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
    TelewebJSON VerificateCode(TelewebJSON mjsonParam) throws TelewebAppException;

}
