package kr.co.hkcloud.palette3.config.cache.generates;


import javax.validation.constraints.NotNull;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import lombok.extern.slf4j.Slf4j;


/**
 * palette:cache:code-book Key Generate
 * 
 * @author Orange
 *
 */
@Slf4j
public class CacheCodeBookKeyGenerate
{
    /**
     * 키 생성
     * 
     * @param  jsonParams
     * @return
     */
    public static Object generate(@NotNull TelewebJSON jsonParams)
    {
        String groupCd = jsonParams.getString("GROUP_CD_ID");
        StringBuffer sbKey = new StringBuffer(groupCd.trim());
        log.debug("**palette:cache:code-book Key Generate =[{}]", sbKey.toString());
        return sbKey.toString();
    }


    public static String get(@NotNull TelewebJSON jsonParams, String key)
    {
        return jsonParams.getString(key);
    }
}
