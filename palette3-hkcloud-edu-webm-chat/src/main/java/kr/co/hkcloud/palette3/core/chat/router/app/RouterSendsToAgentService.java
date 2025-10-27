package kr.co.hkcloud.palette3.core.chat.router.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 
 * @author jangh
 *
 */
public interface RouterSendsToAgentService
{
    void readyAlram(TelewebJSON routeJson, TelewebJSON inJson) throws TelewebAppException;
    void transReadyAlram(String chkUserId) throws TelewebAppException;
}
