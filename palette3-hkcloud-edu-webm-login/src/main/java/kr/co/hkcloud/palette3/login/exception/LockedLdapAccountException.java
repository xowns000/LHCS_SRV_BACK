package kr.co.hkcloud.palette3.login.exception;

import org.springframework.security.authentication.AccountStatusException;

/**
 * 잠긴 LDAP 계정 예외
 * @author Orange
 *
 */
public class LockedLdapAccountException extends AccountStatusException
{
    /**
     * 
     */
    private static final long serialVersionUID = -4285495866282191984L;

    /**
     * 
     * @param msg the detail message
     * @param t the root cause
     */
    public LockedLdapAccountException(String msg, Throwable t)
    {
        super(msg, t);
    }

    /**
     *
     * @param msg the detail message
     */
    public LockedLdapAccountException(String msg)
    {
        super(msg);
    }
}

