package kr.co.hkcloud.palette3.core.chat.router.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import net.sf.json.JSONObject;


/**
 * 
 * @author jangh
 *
 */
public interface RouterInformationMessageService
{
    TelewebJSON selectInfoMsg() throws TelewebAppException;
    void processInfoMsg(JSONObject talkInfoMsg) throws TelewebAppException;
}
