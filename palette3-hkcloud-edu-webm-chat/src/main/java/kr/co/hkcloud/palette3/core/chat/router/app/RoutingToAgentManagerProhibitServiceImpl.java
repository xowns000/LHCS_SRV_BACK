package kr.co.hkcloud.palette3.core.chat.router.app;


import javax.validation.constraints.NotNull;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.chat.router.dao.RoutingToAgentDAO;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("routingToAgentManagerProhibitService")
public class RoutingToAgentManagerProhibitServiceImpl implements RoutingToAgentManagerProhibitService
{
    private final RoutingToAgentDAO routingToAgentDAO;


    /**
     * 금칙어 추출 (캐시 적용)
     * 
     * @param   jsonParams
     * @return             TelewebJSON 형식의 처리 결과 데이터
     * @version            5.0
     * @see                kr.co.hkcloud.palette.mng.prohibiteword.util.ProhibiteUtils
     */
    @Cacheable(value = "palette:cache:prohibite-words",
               key = "T(kr.co.hkcloud.palette3.config.cache.generates.CacheProhibiteWordsGenerate).generate(#custcoIdJson)")
    @Transactional(readOnly = true)
    public TelewebJSON selectProhibiteWords(@NotNull TelewebJSON custcoIdJson) throws TelewebAppException
    {
    	log.info("<<<<<<<<<<",custcoIdJson.getString("CUSTCO_ID"));
        return routingToAgentDAO.selectProhibiteWords(custcoIdJson.getString("CUSTCO_ID"));
    }
}
