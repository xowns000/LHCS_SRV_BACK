package kr.co.hkcloud.palette3.chat.status.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface ChatStatusAgentService
{
    TelewebJSON selectRtnAgentDashboard(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectRtnAgentMonitoringStatus(TelewebJSON telewebJSON) throws TelewebAppException;

    TelewebJSON selectChnStts(TelewebJSON telewebJSON) throws TelewebAppException;
    TelewebJSON selectSttsToday(TelewebJSON telewebJSON) throws TelewebAppException;
    
}
