package kr.co.hkcloud.palette3.svy.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface SvyPlanService
{
    TelewebJSON selectListPlan(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON upsertListPlan(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON deleteListPlan(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON closeListPlan(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON insertSttsHstry(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectSttsHistory(TelewebJSON jsonParams) throws TelewebAppException;

}
