package kr.co.hkcloud.palette3.common.twb.app;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 
 * 
 * @author Orange
 *
 */
public interface TwbBizService
{
    TelewebJSON callService(HttpServletRequest req, HttpServletResponse res, HttpSession session, TelewebJSON acJson) throws TelewebAppException;
}
