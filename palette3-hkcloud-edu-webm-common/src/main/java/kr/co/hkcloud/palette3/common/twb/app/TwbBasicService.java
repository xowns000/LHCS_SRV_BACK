package kr.co.hkcloud.palette3.common.twb.app;


import javax.servlet.http.HttpServletRequest;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 
 * 
 * @author Orange
 *
 */
public interface TwbBasicService
{
    TelewebJSON excuteCom(TelewebJSON acJson, HttpServletRequest objRequest) throws TelewebAppException;
}
