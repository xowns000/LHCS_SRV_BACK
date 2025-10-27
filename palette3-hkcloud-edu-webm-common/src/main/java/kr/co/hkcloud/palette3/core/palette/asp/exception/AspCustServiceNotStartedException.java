package kr.co.hkcloud.palette3.core.palette.asp.exception;


/**
 * 고객사 서비스 시작 안됨 예외
 * 
 * @author Orange
 *
 */
public class AspCustServiceNotStartedException extends RuntimeException
{
    /**
     * 
     */
    private static final long serialVersionUID = -6370761344932364804L;


    /**
     * 
     */
    public AspCustServiceNotStartedException() {
        super("ASP Customer Service Not Started Excepiton :::");
    }


    /**
     *
     * @param msg the detail message
     */
    public AspCustServiceNotStartedException(String msg) {
        super(msg);
    }


    /**
     * 
     * @param msg the detail message
     * @param t   the root cause
     */
    public AspCustServiceNotStartedException(String msg, Throwable t) {
        super(msg, t);
    }
}
