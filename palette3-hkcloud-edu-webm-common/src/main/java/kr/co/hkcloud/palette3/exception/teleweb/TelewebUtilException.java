package kr.co.hkcloud.palette3.exception.teleweb;


/**
 * 텔레웹 util Exception
 * 
 * @author leeiy
 *
 */
public class TelewebUtilException extends TelewebException
{

    /**
     * 
     */
    private static final long serialVersionUID = 2557353017384107417L;


    public TelewebUtilException() {
        super("유틸 처리 중 오류가 발생했습니다.");
    }


    public TelewebUtilException(String message) {
        super(message);
    }


    public TelewebUtilException(String message, Throwable cause) {
        super(message, cause);
    }


    public TelewebUtilException(Throwable cause) {
        super(cause);
    }
}
