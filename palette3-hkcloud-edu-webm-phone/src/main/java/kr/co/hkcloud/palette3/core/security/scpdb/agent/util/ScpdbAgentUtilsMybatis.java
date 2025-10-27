package kr.co.hkcloud.palette3.core.security.scpdb.agent.util;


import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.penta.scpdb.ScpDbAgent;

import kr.co.hkcloud.palette3.core.profile.enumer.PaletteProfiles;
import kr.co.hkcloud.palette3.core.profile.util.PaletteProfileUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebUtilException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * SpcDb Agent Utils ㄴ 고객사에 맞게 수정하여 사용
 * 
 * @author RND
 *
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ScpdbAgentUtilsMybatis
{
    private final PaletteProfileUtils paletteProfileUtils;

    private String iniFilePath;


    /**
     * 부분암호화
     * 
     * @param  inputString
     * @return                      String
     * @throws TelewebUtilException
     */
    public String partialEncryption(String inputString) throws TelewebUtilException
    {
        String resultValue = inputString;
        ScpDbAgent scpDbAgent = new ScpDbAgent();

        if(StringUtils.isEmpty(resultValue)) { return resultValue; }

        PaletteProfiles profile = paletteProfileUtils.getActiveProfile();
        if(profile == PaletteProfiles.local || profile == PaletteProfiles.dev || profile == PaletteProfiles.uat || profile == PaletteProfiles.production) {
            resultValue = scpDbAgent.ScpEncB64(iniFilePath, "KEY1", inputString);
        }
        else {
            switch(resultValue.length())
            {
                // 고객번호
                case 13:
                    resultValue = "8009011024lwzsi7df7YuoUK9o1UzTMg==";
                    break;
                // 전화번호
                case 9:
                case 10:
                case 11:
                    resultValue = "sSm83RVI3Mjt+huaSySTKA==";
                    break;
                // 카드번호
                case 16:
                    resultValue = "rh/wTpsiax+IyRVhCtQITE3clGJlq0cRVHDDt0UP3O4=";
                    break;
                // 계좌번호
                default:
                    resultValue = "N5Q36dDERd/qtkOg8e+S6g==";
                    break;
            }
        }

        return resultValue;
    }


    /**
     * 전체암호화
     * 
     * @param  inputString
     * @return                      String
     * @throws TelewebUtilException
     */
    public String encryption(String inputString) throws TelewebUtilException
    {
        String resultValue = inputString;
        ScpDbAgent scpDbAgent = new ScpDbAgent();

        if(StringUtils.isEmpty(resultValue)) { return resultValue; }

        PaletteProfiles profile = paletteProfileUtils.getActiveProfile();
        if(profile == PaletteProfiles.local || profile == PaletteProfiles.dev || profile == PaletteProfiles.uat || profile == PaletteProfiles.production) {
            resultValue = scpDbAgent.ScpEncB64(iniFilePath, "KEY2", inputString);
        }
        else {
            switch(resultValue.length())
            {
                // 고객번호
                case 13:
                    resultValue = "8009011024lwzsi7df7YuoUK9o1UzTMg==";
                    break;
                // 전화번호
                case 9:
                case 10:
                case 11:
                    resultValue = "sSm83RVI3Mjt+huaSySTKA==";
                    break;
                // 카드번호
                case 16:
                    resultValue = "rh/wTpsiax+IyRVhCtQITE3clGJlq0cRVHDDt0UP3O4=";
                    break;
                // 계좌번호
                default:
                    resultValue = "N5Q36dDERd/qtkOg8e+S6g==";
                    break;
            }
        }

        return resultValue;
    }


    /**
     * 부분복호화
     * 
     * @param  inputString
     * @return                      String
     * @throws TelewebUtilException
     */
    public String partialDecryption(String inputString) throws TelewebUtilException
    {
        String resultValue = inputString;
        ScpDbAgent scpDbAgent = new ScpDbAgent();

        if(StringUtils.isEmpty(resultValue)) { return resultValue; }

        PaletteProfiles profile = paletteProfileUtils.getActiveProfile();
        if(profile == PaletteProfiles.local || profile == PaletteProfiles.dev || profile == PaletteProfiles.uat || profile == PaletteProfiles.production) {
            resultValue = scpDbAgent.ScpDecB64(iniFilePath, "KEY1", inputString);
        }
        else {
            switch(resultValue.length())
            {
                // 고객번호
//                case "8009011024lwzsi7df7YuoUK9o1UzTMg==":resultValue = "8009011403111";
                case 34:
                    resultValue = "8009011403111";
                    break;
                // 전화번호
//                case "sSm83RVI3Mjt+huaSySTKA==":resultValue = "01074157899";
                case 24:
                    resultValue = "01074157899";
                    break;
                // 카드번호
//                case "rh/wTpsiax+IyRVhCtQITE3clGJlq0cRVHDDt0UP3O4=":resultValue = "5570420206031234";
                case 44:
                    resultValue = "5570420206031234";
                    break;
                // 계좌번호
                default:
                    resultValue = "01143278997807";
                    break;
            }
        }

        return resultValue;
    }


    /**
     * 전체복호화
     * 
     * @param  inputString
     * @return                      String
     * @throws TelewebUtilException
     */
    public String decryption(String inputString) throws TelewebUtilException
    {
        String resultValue = inputString;
        ScpDbAgent scpDbAgent = new ScpDbAgent();

        if(StringUtils.isEmpty(resultValue)) { return resultValue; }

        PaletteProfiles profile = paletteProfileUtils.getActiveProfile();
        if(profile == PaletteProfiles.local || profile == PaletteProfiles.dev || profile == PaletteProfiles.uat || profile == PaletteProfiles.production) {
            resultValue = scpDbAgent.ScpDecB64(iniFilePath, "KEY2", inputString);
        }
        else {
            switch(resultValue.length())
            {
                // 고객번호
//                case "8009011024lwzsi7df7YuoUK9o1UzTMg==":resultValue = "8009011403111";
                case 34:
                    resultValue = "8009011403111";
                    break;
                // 전화번호
//                case "sSm83RVI3Mjt+huaSySTKA==":resultValue = "01074157899";
                case 24:
                    resultValue = "01074157899";
                    break;
                // 카드번호
//                case "rh/wTpsiax+IyRVhCtQITE3clGJlq0cRVHDDt0UP3O4=":resultValue = "5570420206031234";
                case 44:
                    resultValue = "5570420206031234";
                    break;
                // 계좌번호
                default:
                    resultValue = "01143278997807";
                    break;
            }
        }

        return resultValue;
    }
}
