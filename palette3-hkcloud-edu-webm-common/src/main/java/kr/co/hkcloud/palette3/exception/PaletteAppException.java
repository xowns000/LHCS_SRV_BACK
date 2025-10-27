package kr.co.hkcloud.palette3.exception;


/**
 * 텔레웹 app RuntimeException
 * 
 * @author leeiy
 *
 */
public class PaletteAppException extends PaletteException
{

    /**
     * 
     */
    private static final long serialVersionUID = -1034946590609930335L;


    public PaletteAppException() {
        super("서비스 처리 중 오류가 발생했습니다.");
    }


    public PaletteAppException(String message) {
        super(message);
    }


    public PaletteAppException(String message, Throwable cause) {
        super(message, cause);
    }


    public PaletteAppException(Throwable cause) {
        super(cause);
    }
}
