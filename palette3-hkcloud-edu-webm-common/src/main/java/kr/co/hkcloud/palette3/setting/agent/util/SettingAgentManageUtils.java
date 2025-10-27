package kr.co.hkcloud.palette3.setting.agent.util;


import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.springframework.stereotype.Component;

import kr.co.hkcloud.palette3.common.twb.app.TwbUserBizServiceImpl;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.security.properties.PaletteSecurityProperties;
import kr.co.hkcloud.palette3.core.security.crypto.Hash;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebUtilException;
import lombok.RequiredArgsConstructor;


/**
 * 
 * @author Orange
 *
 */
@RequiredArgsConstructor
@Component
public class SettingAgentManageUtils
{
    private final PaletteSecurityProperties paletteSecurityProperties;
    private final TwbUserBizServiceImpl     twbUserBizServiceImpl;
    
}
