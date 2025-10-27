package kr.co.hkcloud.palette3.login.exception;

import org.springframework.security.authentication.AccountStatusException;

/**
 * 잘못된 LDAP 자격 증명 예외
 * @author Orange
 *
 */
public class BadLdapCredentialsException extends AccountStatusException
{
    /**
     * 
     */
    private static final long serialVersionUID = -3144240794240557663L;

    /**
     * 
     * @param msg the detail message
     * @param t the root cause
     */
    public BadLdapCredentialsException(String msg, Throwable t)
    {
        super(msg, t);
    }

    /**
     *
     * @param msg the detail message
     */
    public BadLdapCredentialsException(String msg)
    {
        super(msg);
    }
}

