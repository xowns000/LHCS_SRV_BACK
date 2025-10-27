package kr.co.hkcloud.palette3.common.twb.util;


import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.security.crypto.Hash;
import kr.co.hkcloud.palette3.core.util.PaletteUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Component
public class TwbCheckParameterUtils
{
    private final PaletteUtils       paletteCmmnUtils;
    private final TwbCmmnLoggerUtils twbCmmnLoggerUtils;


    /**
     * 전송된 파라메터를 체크한다.
     * 
     * @param  objJsonParams
     * @param  objRequest
     * @return
     * @throws TelewebAppException
     */
    public String checkParameter(TelewebJSON objJsonParams, HttpServletRequest objRequest) throws TelewebAppException
    {
        String strResult = "";
        return strResult;
    }
}