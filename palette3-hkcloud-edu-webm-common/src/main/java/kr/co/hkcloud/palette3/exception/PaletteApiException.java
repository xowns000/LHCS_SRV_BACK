package kr.co.hkcloud.palette3.exception;


/**
 * 텔레웹 api RuntimeException
 * 
 * @author leeiy
 *
 */
public class PaletteApiException extends PaletteException
{
    /**
     * 
     */
    private static final long serialVersionUID = 826592072374245801L;


    public PaletteApiException() {
        super("컨트롤러 처리 중 오류가 발생했습니다.");
    }


    public PaletteApiException(String message) {
        super(message);
    }


    public PaletteApiException(String message, Throwable cause) {
        super(message, cause);
    }


    public PaletteApiException(Throwable cause) {
        super(cause);
    }
}
