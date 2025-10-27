package kr.co.hkcloud.palette3.core.chat.stomp.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import net.sf.json.JSONObject;


/**
 * 
 * @author Orange
 *
 */
public interface TalkStompSendToKakaoService
{
    JSONObject sendToKakao(String userKey, TelewebJSON sendJson, String CallTypCd) throws TelewebAppException;
}
