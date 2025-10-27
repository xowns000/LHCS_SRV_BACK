package kr.co.hkcloud.palette3.svy.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface SvyResultService
{
    TelewebJSON selectSummyList(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON selectSummyItem(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON selectDetailList(TelewebJSON jsonParam) throws TelewebAppException;
}
