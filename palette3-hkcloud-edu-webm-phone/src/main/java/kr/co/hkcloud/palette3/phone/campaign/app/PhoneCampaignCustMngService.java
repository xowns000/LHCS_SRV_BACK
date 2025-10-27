package kr.co.hkcloud.palette3.phone.campaign.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneCampaignCustMngService
{
    TelewebJSON selectComboCpiCustMng(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON selectTopCpiCustMng(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON selectAttrCustMng(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON reOrderAttrCustMng(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON upsertAttrCustMng(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON deleteAttrCustMng(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON selectCustCustMng(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON custExpsnInfo(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON uploadExcelCustMng(TelewebJSON jsonParam) throws TelewebAppException;
}
