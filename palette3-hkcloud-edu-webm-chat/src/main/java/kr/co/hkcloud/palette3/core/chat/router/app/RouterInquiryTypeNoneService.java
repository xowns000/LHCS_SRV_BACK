package kr.co.hkcloud.palette3.core.chat.router.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import net.sf.json.JSONObject;


/**
 * 
 * @author jangh
 *
 */
public interface RouterInquiryTypeNoneService
{
    TelewebJSON selectInqryTypeNoContact() throws TelewebAppException;
    void processInqryTypeNoContact(JSONObject talkInfoMsg) throws TelewebAppException;
}
