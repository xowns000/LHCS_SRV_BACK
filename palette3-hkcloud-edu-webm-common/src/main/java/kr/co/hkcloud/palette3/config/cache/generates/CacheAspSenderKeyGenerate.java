package kr.co.hkcloud.palette3.config.cache.generates;


import javax.validation.constraints.NotNull;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import lombok.extern.slf4j.Slf4j;


/**
 * palette:cache:asp-sender-key Key Generate
 * 
 * @author Orange
 *
 */
@Slf4j
public class CacheAspSenderKeyGenerate
{
    /**
     * 키 생성
     * 
     * @param  jsonParams
     * @return
     */
    public static Object generate(@NotNull TelewebJSON jsonParams)
    {
        String custcoId = jsonParams.getString("CUSTCO_ID");
        StringBuffer sbKey = new StringBuffer(custcoId);
        log.debug("**palette:cache:asp-sender-key Key Generate =[{}]", sbKey.toString());
        return sbKey.toString();
    }
}
