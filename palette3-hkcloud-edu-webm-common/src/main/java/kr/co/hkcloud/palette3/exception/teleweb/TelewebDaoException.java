package kr.co.hkcloud.palette3.exception.teleweb;


/**
 * 텔레웹 dao Exception
 * 
 * @author leeiy
 *
 */
public class TelewebDaoException extends TelewebException
{

    /**
     * 
     */
    private static final long serialVersionUID = 7317201160452552161L;


    public TelewebDaoException() {
        super("DB 처리 중 오류가 발생했습니다.");
    }


    public TelewebDaoException(String message) {
        super(message);
    }


    public TelewebDaoException(String message, Throwable cause) {
        super(message, cause);
    }


    public TelewebDaoException(Throwable cause) {
        super(cause);
    }
}
