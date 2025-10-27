package kr.co.hkcloud.palette3.common.twb.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 
 * 
 * @author Orange
 *
 */
public interface TwbUserLogBizService
{
    String insertRtn(TelewebJSON jsonParams, String strUserID, String strCase, String strAccessIP, String strLoginID) throws TelewebAppException;
    String insertRtnChangePwdInfo(TelewebJSON jsonParams, String strAccessIP) throws TelewebAppException;
    String insertUserBizLog(TelewebJSON jsonParams, String strAccessIP) throws TelewebAppException;


}
