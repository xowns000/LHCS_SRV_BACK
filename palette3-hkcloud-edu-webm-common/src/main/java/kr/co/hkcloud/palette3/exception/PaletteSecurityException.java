package kr.co.hkcloud.palette3.exception;


/**
 * 보안 RuntimeException
 * 
 * @author leeiy
 *
 */
public class PaletteSecurityException extends RuntimeException
{
    /**
     * 
     */
    private static final long serialVersionUID = 2922620118522371350L;


    public PaletteSecurityException() {
        super("보안 처리 중 오류가 발생했습니다.");
    }


    public PaletteSecurityException(String message) {
        super(message);
    }


    public PaletteSecurityException(String message, Throwable cause) {
        super(message, cause);
    }


    public PaletteSecurityException(Throwable cause) {
        super(cause);
    }
}
