package kr.co.hkcloud.palette3.setting.consulttype.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface SettingConsulttypeManageService
{

    TelewebJSON selectCnslTypLevelByTopLevel(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectCnslTypByNotTopLevel(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON processRtnTwbTalkCnslTyp(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON deleteRtnCnslTyp(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnNodeDetail(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectCompanyName(TelewebJSON jsonParams) throws TelewebAppException;

}
