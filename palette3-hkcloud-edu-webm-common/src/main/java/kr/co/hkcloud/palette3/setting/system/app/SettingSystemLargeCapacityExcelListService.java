package kr.co.hkcloud.palette3.setting.system.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface SettingSystemLargeCapacityExcelListService
{
    TelewebJSON selectRtnReqList(TelewebJSON jsonParams) throws TelewebAppException;
}
