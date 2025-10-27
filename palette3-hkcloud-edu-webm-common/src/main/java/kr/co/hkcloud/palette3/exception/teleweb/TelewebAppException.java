package kr.co.hkcloud.palette3.exception.teleweb;


/**
 * 텔레웹 app Exception
 * 
 * @author leeiy
 *
 */
public class TelewebAppException extends TelewebException
{

    /**
     * 
     */
    private static final long serialVersionUID = -6515897807016930013L;


    public TelewebAppException() {
        super("서비스 처리 중 오류가 발생했습니다.");
    }


    public TelewebAppException(String message) {
        super(message);
    }


    public TelewebAppException(String message, Throwable cause) {
        super(message, cause);
    }


    public TelewebAppException(Throwable cause) {
        super(cause);
    }
}
