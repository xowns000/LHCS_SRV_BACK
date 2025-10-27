package kr.co.hkcloud.palette3.core.palette.asp.exception;


/**
 * 비즈채널 서비스 URI 불일치 예외
 * 
 * @author Orange
 *
 */
public class AspBizChannelServiceURIMismatchException extends RuntimeException
{
    /**
     * 
     */
    private static final long serialVersionUID = 1737833270267800903L;


    /**
     * 
     */
    public AspBizChannelServiceURIMismatchException() {
        super("ASP Biz Channel Service URI Mismatch Excepiton :::");
    }


    /**
     *
     * @param msg the detail message
     */
    public AspBizChannelServiceURIMismatchException(String msg) {
        super(msg);
    }


    /**
     * 
     * @param msg the detail message
     * @param t   the root cause
     */
    public AspBizChannelServiceURIMismatchException(String msg, Throwable t) {
        super(msg, t);
    }
}
