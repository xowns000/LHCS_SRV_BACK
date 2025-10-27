package kr.co.hkcloud.palette3.exception.teleweb;


/**
 * 텔레웹 api Exception
 * 
 * @author leeiy
 *
 */
public class TelewebApiException extends TelewebException
{

    /**
     * 
     */
    private static final long serialVersionUID = -1728191556253626985L;


    public TelewebApiException() {
        super("컨트롤러 처리 중 오류가 발생했습니다.");
    }


    public TelewebApiException(String message) {
        super(message);
    }


    public TelewebApiException(String message, Throwable cause) {
        super(message, cause);
    }


    public TelewebApiException(Throwable cause) {
        super(cause);
    }
}
