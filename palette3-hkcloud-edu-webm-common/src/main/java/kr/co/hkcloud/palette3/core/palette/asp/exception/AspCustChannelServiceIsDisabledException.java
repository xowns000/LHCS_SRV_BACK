package kr.co.hkcloud.palette3.core.palette.asp.exception;


/**
 * 고객사채널 서비스 비활성화 예외
 * 
 * @author Orange
 *
 */
public class AspCustChannelServiceIsDisabledException extends RuntimeException
{
    /**
     * 
     */
    private static final long serialVersionUID = -1553974358168717364L;


    /**
     * 
     */
    public AspCustChannelServiceIsDisabledException() {
        super("ASP Customer Channel Service Is Disabled Excepiton :::");
    }


    /**
     *
     * @param msg the detail message
     */
    public AspCustChannelServiceIsDisabledException(String msg) {
        super(msg);
    }


    /**
     * 
     * @param msg the detail message
     * @param t   the root cause
     */
    public AspCustChannelServiceIsDisabledException(String msg, Throwable t) {
        super(msg, t);
    }
}
