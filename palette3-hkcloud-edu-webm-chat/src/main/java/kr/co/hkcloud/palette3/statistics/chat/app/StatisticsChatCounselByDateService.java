package kr.co.hkcloud.palette3.statistics.chat.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface StatisticsChatCounselByDateService
{
    TelewebJSON selectStatisticsByDay(TelewebJSON jsonParams) throws TelewebAppException;
    
    TelewebJSON selectAlrimSmsList(TelewebJSON jsonParams) throws TelewebAppException;
}
