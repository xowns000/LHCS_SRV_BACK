package kr.co.hkcloud.palette3.core.palette.asp.exception;


/**
 * 고객사 서비스 비활성화 예외
 * 
 * @author Orange
 *
 */
public class AspCustServiceIsDisabledException extends RuntimeException
{
    /**
     * 
     */
    private static final long serialVersionUID = -5854087742079481399L;


    /**
     * 
     */
    public AspCustServiceIsDisabledException() {
        super("ASP Customer Service Is Disabled Excepiton :::");
    }


    /**
     *
     * @param msg the detail message
     */
    public AspCustServiceIsDisabledException(String msg) {
        super(msg);
    }


    /**
     * 
     * @param msg the detail message
     * @param t   the root cause
     */
    public AspCustServiceIsDisabledException(String msg, Throwable t) {
        super(msg, t);
    }
}
