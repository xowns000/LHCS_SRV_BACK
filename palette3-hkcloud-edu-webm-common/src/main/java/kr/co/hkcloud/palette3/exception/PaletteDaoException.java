package kr.co.hkcloud.palette3.exception;


/**
 * 텔레웹 dao RuntimeException
 * 
 * @author leeiy
 *
 */
public class PaletteDaoException extends PaletteException
{

    /**
     * 
     */
    private static final long serialVersionUID = 7651436180936303361L;


    public PaletteDaoException() {
        super("디비 처리 중 오류가 발생했습니다.");
    }


    public PaletteDaoException(String message) {
        super(message);
    }


    public PaletteDaoException(String message, Throwable cause) {
        super(message, cause);
    }


    public PaletteDaoException(Throwable cause) {
        super(cause);
    }
}
