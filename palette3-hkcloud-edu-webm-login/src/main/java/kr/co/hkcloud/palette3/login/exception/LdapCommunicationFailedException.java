package kr.co.hkcloud.palette3.login.exception;

import org.springframework.security.authentication.AccountStatusException;

/**
 * LDAP 통신 실패 예외
 * @author Orange
 *
 */
public class LdapCommunicationFailedException extends AccountStatusException
{
    /**
     * 
     */
    private static final long serialVersionUID = 1083750699756453359L;

    /**
     * 
     * @param msg the detail message
     * @param t the root cause
     */
    public LdapCommunicationFailedException(String msg, Throwable t)
    {
        super(msg, t);
    }

    /**
     *
     * @param msg the detail message
     */
    public LdapCommunicationFailedException(String msg)
    {
        super(msg);
    }
}

