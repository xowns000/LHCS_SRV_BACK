package kr.co.hkcloud.palette3.exception.teleweb;


/**
 * 텔레웹 Exception
 * 
 * @author leeiy
 *
 */
public class TelewebException extends RuntimeException
{

    /**
     * 
     */
    private static final long serialVersionUID = -514098816025834103L;


    public TelewebException() {
        super("처리 중 오류가 발생했습니다.");
    }


    public TelewebException(String message) {
        super(message);
    }


    public TelewebException(String message, Throwable cause) {
        super(message, cause);
    }


    public TelewebException(Throwable cause) {
        super(cause);
    }
}
