package kr.co.hkcloud.palette3.setting.env.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

/**
 * packageName : kr.co.hkcloud.palette3.setting.customer.app
 * fileName : SettingEnvService
 * author : USER
 * date : 2023-11-01
 * description : << 여기 설명 >>
 * ===========================================================
 * DATE AUTHOR NOTE
 * -----------------------------------------------------------
 * 2023-11-01 USER 최초 생성
 */
public interface SettingEnvService {

    TelewebJSON custcoSettingList(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectSettingEnv(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON updateSettingEnv(TelewebJSON jsonParams) throws TelewebAppException;

}
