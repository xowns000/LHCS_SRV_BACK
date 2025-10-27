package kr.co.hkcloud.palette3.exception.teleweb;


/**
 * 보안 RuntimeException
 * 
 * @author leeiy
 *
 */
public class TelewebSecurityException extends RuntimeException
{
    /**
     * 
     */
    private static final long serialVersionUID = -284455952284257835L;


    public TelewebSecurityException() {
        super("보안 처리 중 오류가 발생했습니다.");
    }


    public TelewebSecurityException(String message) {
        super(message);
    }


    public TelewebSecurityException(String message, Throwable cause) {
        super(message, cause);
    }


    public TelewebSecurityException(Throwable cause) {
        super(cause);
    }
}
