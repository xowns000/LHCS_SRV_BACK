package kr.co.hkcloud.palette3.core.security.exception;


/**
 * 
 * @author Orange
 *
 */
public class SecurityInvalidSessionException extends Exception
{
    /**
     * 
     */
    private static final long serialVersionUID = -3996315683639889316L;


    /**
     * 
     */
    public SecurityInvalidSessionException() {
        super("Spring Security Invalid Session Exception :::");
    }


    /**
     * 
     * @param msg the detail message
     * @param t   the root cause
     */
    public SecurityInvalidSessionException(String msg, Throwable t) {
        super(msg, t);
    }


    /**
     *
     * @param msg the detail message
     */
    public SecurityInvalidSessionException(String msg) {
        super(msg);
    }
}
