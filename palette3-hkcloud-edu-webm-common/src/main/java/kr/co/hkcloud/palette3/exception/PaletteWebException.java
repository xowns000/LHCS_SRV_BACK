package kr.co.hkcloud.palette3.exception;


/**
 * 팔레트 web Runtime Exception
 * 
 * @author leeiy
 *
 */
public class PaletteWebException extends PaletteException
{

    /**
     * 
     */
    private static final long serialVersionUID = 4819042790431835299L;


    public PaletteWebException() {
        super("웹컨트롤러 처리 중 오류가 발생했습니다.");
    }


    public PaletteWebException(String message) {
        super(message);
    }


    public PaletteWebException(String message, Throwable cause) {
        super(message, cause);
    }


    public PaletteWebException(Throwable cause) {
        super(cause);
    }
}
