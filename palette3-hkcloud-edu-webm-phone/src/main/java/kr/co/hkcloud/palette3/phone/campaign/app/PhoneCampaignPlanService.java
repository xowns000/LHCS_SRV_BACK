package kr.co.hkcloud.palette3.phone.campaign.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneCampaignPlanService
{
    TelewebJSON selectCpiPlan(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON upsertCpiPlan(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON deleteCpiPlan(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON closeCpiPlan(TelewebJSON jsonParam) throws TelewebAppException;

    TelewebJSON cpiPlanStrtNow(TelewebJSON jsonParam) throws TelewebAppException;
}
