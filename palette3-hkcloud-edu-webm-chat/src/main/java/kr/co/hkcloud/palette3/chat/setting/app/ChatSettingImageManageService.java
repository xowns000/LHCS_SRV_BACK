package kr.co.hkcloud.palette3.chat.setting.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface ChatSettingImageManageService
{
    TelewebJSON selectRtnImgMng(TelewebJSON jsonParams) throws TelewebAppException;
}
