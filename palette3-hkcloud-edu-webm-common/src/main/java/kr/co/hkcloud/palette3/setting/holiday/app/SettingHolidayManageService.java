package kr.co.hkcloud.palette3.setting.holiday.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 휴일관리 서비스
 *
 */
public interface SettingHolidayManageService
{
    TelewebJSON selectRtnPageHoliday(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnHolidayDupChk(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertRtnHoliday(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON insertRtnHoliday(TelewebJSON jsonParam, String holidayDate, String holidayName, String holidayGbCd) throws TelewebAppException;
    TelewebJSON updateRtnHoliday(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON deleteRtnHoliday(TelewebJSON jsonParam) throws TelewebAppException;
}
