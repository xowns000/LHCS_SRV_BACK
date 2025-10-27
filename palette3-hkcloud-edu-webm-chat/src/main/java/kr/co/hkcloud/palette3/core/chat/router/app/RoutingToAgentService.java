package kr.co.hkcloud.palette3.core.chat.router.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import net.sf.json.JSONObject;


/**
 * 
 * @author Orange
 *
 */
public interface RoutingToAgentService
{
    TelewebJSON processRoutingToAgent(JSONObject talkInfoMsg) throws TelewebAppException;
    TelewebJSON processRoutingToDesignatedAgent(JSONObject talkInfoMsg) throws TelewebAppException;
}
