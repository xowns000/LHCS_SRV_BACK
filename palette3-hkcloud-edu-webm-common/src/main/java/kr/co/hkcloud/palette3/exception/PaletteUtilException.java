package kr.co.hkcloud.palette3.exception;


/**
 * 팔레트 util RuntimeException
 * 
 * @author leeiy
 *
 */
public class PaletteUtilException extends PaletteException
{

    /**
     * 
     */
    private static final long serialVersionUID = -6431936359469662175L;


    public PaletteUtilException() {
        super("유틸리티 처리 중 오류가 발생했습니다.");
    }


    public PaletteUtilException(String message) {
        super(message);
    }


    public PaletteUtilException(String message, Throwable cause) {
        super(message, cause);
    }


    public PaletteUtilException(Throwable cause) {
        super(cause);
    }
}
