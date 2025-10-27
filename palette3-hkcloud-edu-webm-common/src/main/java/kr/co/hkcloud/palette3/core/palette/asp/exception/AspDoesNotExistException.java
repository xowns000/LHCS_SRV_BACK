package kr.co.hkcloud.palette3.core.palette.asp.exception;


/**
 * 존재하지 않음 예외
 * 
 * @author Orange
 *
 */
public class AspDoesNotExistException extends RuntimeException
{
    /**
     * 
     */
    private static final long serialVersionUID = -7265402590459645863L;


    /**
     * 
     */
    public AspDoesNotExistException() {
        super("ASP Does Not Exist Excepiton :::");
    }


    /**
     *
     * @param msg the detail message
     */
    public AspDoesNotExistException(String msg) {
        super(msg);
    }


    /**
     * 
     * @param msg the detail message
     * @param t   the root cause
     */
    public AspDoesNotExistException(String msg, Throwable t) {
        super(msg, t);
    }
}
