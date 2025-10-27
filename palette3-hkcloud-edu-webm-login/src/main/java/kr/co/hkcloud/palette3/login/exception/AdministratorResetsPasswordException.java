package kr.co.hkcloud.palette3.login.exception;

import org.springframework.security.authentication.AccountStatusException;

/**
 * 관리자 비밀번호 초기화 예외
 * @author Orange
 *
 */
public class AdministratorResetsPasswordException extends AccountStatusException
{
    /**
     * 
     */
    private static final long serialVersionUID = -6293211254709370819L;

    /**
     * 
     * @param msg the detail message
     * @param t the root cause
     */
    public AdministratorResetsPasswordException(String msg, Throwable t)
    {
        super(msg, t);
    }

    /**
     *
     * @param msg the detail message
     */
    public AdministratorResetsPasswordException(String msg)
    {
        super(msg);
    }

}

