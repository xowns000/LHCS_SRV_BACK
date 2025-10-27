package kr.co.hkcloud.palette3.exception;


/**
 * 팔레트 RuntimeException
 * 
 * @author leeiy
 *
 */
public class PaletteException extends RuntimeException
{
    /**
     * 
     */
    private static final long serialVersionUID = 4180824550438176930L;


    public PaletteException() {
        super("팔레트 처리 중 오류가 발생했습니다.");
    }


    public PaletteException(String message) {
        super(message);
    }


    public PaletteException(String message, Throwable cause) {
        super(message, cause);
    }


    public PaletteException(Throwable cause) {
        super(cause);
    }

}
