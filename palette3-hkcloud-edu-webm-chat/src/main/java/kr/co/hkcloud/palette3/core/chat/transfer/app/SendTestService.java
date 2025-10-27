package kr.co.hkcloud.palette3.core.chat.transfer.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;


public interface SendTestService
{
    TelewebJSON selectMessage(String custcoId, String sysMsgId, int userId);
}
