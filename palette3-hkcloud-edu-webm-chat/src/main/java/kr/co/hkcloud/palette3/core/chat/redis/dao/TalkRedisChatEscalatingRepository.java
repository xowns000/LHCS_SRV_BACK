package kr.co.hkcloud.palette3.core.chat.redis.dao;


import java.util.Optional;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.hkcloud.palette3.core.chat.redis.domain.RedisCacheKeys;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatStompVO;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Repository
public class TalkRedisChatEscalatingRepository
{
    private final ObjectMapper objectMapper;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsEscalatingInfo;

    private String logDevider = "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";

    /**
     * 이관중인 USER_KEY 해시 키가 존재하는지 확인
     * 
     * @param  userKey
     * @return
     * @throws TelewebDaoException containsKey
     */
    public boolean hasKey(@NotEmpty String userKey) throws TelewebDaoException
    {
        return hashOpsEscalatingInfo.hasKey(TenantContext.getCurrentTenant() + ":" +  RedisCacheKeys.READY_ESCALATING.getKey(), userKey);
    }


    /**
     * 이관중인 USER_KEY와 USER_ID 맵핑 넣기
     * 
     * @param stompVO
     */
    public void setStompVO(@NotNull ChatStompVO stompVO) throws TelewebDaoException
    {
        String stompVoToString;
        try {
            stompVoToString = objectMapper.writeValueAsString(stompVO);
        }
        catch(JsonProcessingException e) {
            log.error(e.getLocalizedMessage());
            throw new TelewebDaoException(e.getLocalizedMessage());
        }
        String logPrefix = logDevider + ".setStompVO" + "___";
        int logNum = 1;
        log.info(logPrefix + (logNum++) + " ::: 이관중인 USER_KEY와 USER_ID 맵핑 넣기 start");
        log.info(logPrefix + (logNum++) + " ::: stompVoToString ::: " + stompVoToString);
//        hashOpsEscalatingInfo.put(TenantContext.getTenantRedisPrefix() + ":" +  RedisCacheKeys.READY_ESCALATING.getKey(), stompVO.getUserKey(), stompVoToString);
        hashOpsEscalatingInfo.put(TenantContext.getCurrentTenant() + ":" + RedisCacheKeys.READY_ESCALATING.getKey(), stompVO.getUserKey(), stompVoToString);
    }


    /**
     * 이관중인 USER_KEY 가져오기
     * 
     * @param  stompVO
     * @return
     */
    public ChatStompVO getStompVO(@NotEmpty String userKey) throws TelewebDaoException
    {
        String readyRedisJson = hashOpsEscalatingInfo.get(TenantContext.getCurrentTenant() + ":" +  RedisCacheKeys.READY_ESCALATING.getKey(), userKey);

        //ChatStompVO 객채로 맵핑
        ChatStompVO stompVO;
        try {
            stompVO = objectMapper.readValue(readyRedisJson, ChatStompVO.class);
        }
        catch(JsonProcessingException e) {
            log.error(e.getLocalizedMessage());
            throw new TelewebDaoException(e.getLocalizedMessage());
        }

        return stompVO;
    }


    /**
     * 이관중인 맵핑된 USER_KEY 제거
     * 
     * @param userKey
     */
    public void removeUserKey(@NotEmpty String userKey) throws TelewebDaoException
    {
        hashOpsEscalatingInfo.delete(TenantContext.getCurrentTenant() + ":" +  RedisCacheKeys.READY_ESCALATING.getKey(), userKey);
    }


    /**
     * 이관중인 전체 사이즈 가져오기
     * 
     * @return Long
     */
    public Long getSize() throws TelewebDaoException
    {
        return Optional.ofNullable(hashOpsEscalatingInfo.size( TenantContext.getCurrentTenant() + ":" + RedisCacheKeys.READY_ESCALATING.getKey())).orElse(0L);
    }
}
