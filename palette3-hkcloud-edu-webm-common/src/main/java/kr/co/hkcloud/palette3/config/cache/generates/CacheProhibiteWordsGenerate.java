package kr.co.hkcloud.palette3.config.cache.generates;


import javax.validation.constraints.NotNull;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import lombok.extern.slf4j.Slf4j;


/**
 * palette:cache:ProhibiteWords Key Generate
 * 
 * @author Orange
 *
 */
@Slf4j
public class CacheProhibiteWordsGenerate
{
    /**
     * 키 생성
     * 
     * @param  custcoIdJson
     * @return
     * @see                   kr.co.hkcloud.palette.core.chat.router.app.RoutingToAgentManagerProhibitImpl
     * @see                   kr.co.hkcloud.palette.mng.prohibiteword.app.TwbProhibiteWordServiceImpl
     */
    public static Object generate(@NotNull TelewebJSON custcoIdJson)
    {
        String custcoId = custcoIdJson.getString("CUSTCO_ID");
        StringBuffer sbKey = new StringBuffer(custcoId);
        log.debug("**palette:cache:prohibite-words Key Generate =[{}]", sbKey.toString());
        return sbKey.toString();
    }
}
