package kr.co.hkcloud.palette3.core.chat.router.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 
 * @author Orange
 *
 */
public interface RoutingToAgentManagerProhibitService
{
    TelewebJSON selectProhibiteWords(TelewebJSON custcoIdJson) throws TelewebAppException;
}
