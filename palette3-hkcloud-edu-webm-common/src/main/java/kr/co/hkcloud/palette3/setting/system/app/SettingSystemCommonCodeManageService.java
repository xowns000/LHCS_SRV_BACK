package kr.co.hkcloud.palette3.setting.system.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 공통코드 서비스 인터페이스
 * 
 * @author Orange
 *
 */
public interface SettingSystemCommonCodeManageService
{
    TelewebJSON selectRtnCodeType(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnCodeDetail(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON dpcnCheck(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertTwbBas03(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateTwbBse03(TelewebJSON jsonParams) throws TelewebAppException;
    void deleteRtnCodeInfo(TelewebJSON jsonParams) throws TelewebAppException;
}
