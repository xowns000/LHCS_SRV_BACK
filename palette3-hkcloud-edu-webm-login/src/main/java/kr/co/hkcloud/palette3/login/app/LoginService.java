package kr.co.hkcloud.palette3.login.app;


import java.util.LinkedHashMap;

import javax.security.auth.login.AccountExpiredException;
import javax.servlet.http.HttpServletRequest;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.login.domain.LoginLogDTO;
import kr.co.hkcloud.palette3.login.domain.LoginDTO;


/**
 * 
 * @author Orange
 *
 */
public interface LoginService
{	
	/**
	 * 로그인
	 * @param login
	 * @param req
	 * @return
	 * @throws TelewebAppException
	 * @throws AccountExpiredException 
	 */
    TelewebJSON login(LoginDTO.Login login, HttpServletRequest req) throws TelewebAppException, AccountExpiredException;
    
    LinkedHashMap<String, String> processLogin(LoginLogDTO loginLogDTO) throws TelewebAppException;

}
