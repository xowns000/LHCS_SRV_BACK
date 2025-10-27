package kr.co.hkcloud.palette3.svy.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface SvyPreviewService
{
    TelewebJSON selectMainList(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON upsertTerms(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON insertItem(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON aesUrlDecrypt(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON deleteGroupRspns(TelewebJSON jsonParam) throws TelewebAppException;
}
