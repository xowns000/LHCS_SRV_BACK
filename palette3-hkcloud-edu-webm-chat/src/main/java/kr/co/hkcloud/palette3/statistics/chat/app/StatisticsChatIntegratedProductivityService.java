package kr.co.hkcloud.palette3.statistics.chat.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface StatisticsChatIntegratedProductivityService
{
    TelewebJSON selectRtnIntergratedProductivity(TelewebJSON jsonParams) throws TelewebAppException;
}
