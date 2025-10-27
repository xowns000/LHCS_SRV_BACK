package kr.co.hkcloud.palette3.login.exception;

import org.springframework.security.authentication.AccountStatusException;

/**
 * 최초 로그인 예외
 * @author 
 *
 */
public class SignUpServiceKeepException extends AccountStatusException
{
    /**
     * 
     */

    private static final long serialVersionUID = 8382329903682963225L;

    /**
     * 
     * @param msg the detail message
     * @param t the root cause
     */
    public SignUpServiceKeepException(String msg, Throwable t)
    {
        super(msg, t);
    }

    /**
     *
     * @param msg the detail message
     */
    public SignUpServiceKeepException(String msg)
    {
        super(msg);
    }
}

