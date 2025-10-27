package kr.co.hkcloud.palette3.login.app;


import javax.security.auth.login.AccountExpiredException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.login.domain.LoginLogDTO;
import kr.co.hkcloud.palette3.login.domain.LoginDTO;


/**
 * 
 * @author Orange
 *
 */
public interface LogoutService
{
    void processLogout(LoginLogDTO loginLogDTO, HttpSession session) throws TelewebAppException;
    
    /**
     * 로그 아웃
     * @param logout
     * @param req
     * @return
     * @throws TelewebAppException
     * @throws AccountExpiredException
     */
    TelewebJSON logout(LoginDTO.Logout logout, HttpServletRequest req) throws TelewebAppException, AccountExpiredException;
}