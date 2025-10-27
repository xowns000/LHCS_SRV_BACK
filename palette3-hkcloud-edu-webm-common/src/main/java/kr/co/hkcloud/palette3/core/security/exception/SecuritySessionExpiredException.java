package kr.co.hkcloud.palette3.core.security.exception;


/**
 * 
 * @author Orange
 *
 */
public class SecuritySessionExpiredException extends Exception
{

    /**
     * 
     */
    private static final long serialVersionUID = 6354577781770674919L;


    /**
     * 
     */
    public SecuritySessionExpiredException() {
        super("Spring Security Session Expired Exception :::");
    }


    /**
     * 
     * @param msg the detail message
     * @param t   the root cause
     */
    public SecuritySessionExpiredException(String msg, Throwable t) {
        super(msg, t);
    }


    /**
     *
     * @param msg the detail message
     */
    public SecuritySessionExpiredException(String msg) {
        super(msg);
    }
}
