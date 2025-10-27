package kr.co.hkcloud.palette3.statistics.chat.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface StatisticsChatCounselByAgentService
{
    TelewebJSON selectChatAdviceStatListByInCounsel_new(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectChatAdviceStatListByInCounsel_01(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectChatAdviceStatListByInCounsel_02(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnAuthGroup(TelewebJSON jsonParams) throws TelewebAppException;
}
