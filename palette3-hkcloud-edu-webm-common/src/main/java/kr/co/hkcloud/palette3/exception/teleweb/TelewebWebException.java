package kr.co.hkcloud.palette3.exception.teleweb;


/**
 * 텔레웹 web Exception
 * 
 * @author leeiy
 *
 */
public class TelewebWebException extends TelewebException
{

    /**
     * 
     */
    private static final long serialVersionUID = 1062880381797482734L;


    public TelewebWebException() {
        super("컨트롤러 처리 중 오류가 발생했습니다.");
    }


    public TelewebWebException(String message) {
        super(message);
    }


    public TelewebWebException(String message, Throwable cause) {
        super(message, cause);
    }


    public TelewebWebException(Throwable cause) {
        super(cause);
    }
}
