package kr.co.hkcloud.palette3.login.exception;

import org.springframework.security.authentication.AccountStatusException;

/**
 * 최초 로그인 예외
 * @author Orange
 *
 */
public class FirstLoginException extends AccountStatusException
{
    /**
     * 
     */
    private static final long serialVersionUID = 4970930035204387511L;

    /**
     * 
     * @param msg the detail message
     * @param t the root cause
     */
    public FirstLoginException(String msg, Throwable t)
    {
        super(msg, t);
    }

    /**
     *
     * @param msg the detail message
     */
    public FirstLoginException(String msg)
    {
        super(msg);
    }
}

