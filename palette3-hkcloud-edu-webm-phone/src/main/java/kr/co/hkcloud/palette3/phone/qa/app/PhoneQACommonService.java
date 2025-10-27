package kr.co.hkcloud.palette3.phone.qa.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneQACommonService
{
    TelewebJSON selectRtnBeforeMonthLastDay(TelewebJSON jsonParams) throws TelewebAppException;
}
