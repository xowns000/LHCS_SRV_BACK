package kr.co.hkcloud.palette3.core.palette.asp.exception;


/**
 * 고객사 서비스 만료 예외
 * 
 * @author Orange
 *
 */
public class AspCustServiceIsExpiredException extends RuntimeException
{
    /**
     * 
     */
    private static final long serialVersionUID = -545940957758729718L;


    /**
     * 
     */
    public AspCustServiceIsExpiredException() {
        super("ASP Customer Service Is Expired Excepiton :::");
    }


    /**
     *
     * @param msg the detail message
     */
    public AspCustServiceIsExpiredException(String msg) {
        super(msg);
    }


    /**
     * 
     * @param msg the detail message
     * @param t   the root cause
     */
    public AspCustServiceIsExpiredException(String msg, Throwable t) {
        super(msg, t);
    }
}
