package kr.co.hkcloud.palette3.core.palette.asp.exception;


/**
 * 고객사채널 서비스 비활성화 예외
 * 
 * @author Orange
 *
 */
public class AspCustChannelFailLineSignatureException extends RuntimeException
{
    /**
     * 
     */
    private static final long serialVersionUID = -1553974358168717364L;


    /**
     * 
     */
    public AspCustChannelFailLineSignatureException() {
        super("ASP Customer Channel Service failed line Signature Excepiton :::");
    }


    /**
     *
     * @param msg the detail message
     */
    public AspCustChannelFailLineSignatureException(String msg) {
        super(msg);
    }


    /**
     * 
     * @param msg the detail message
     * @param t   the root cause
     */
    public AspCustChannelFailLineSignatureException(String msg, Throwable t) {
        super(msg, t);
    }
}
