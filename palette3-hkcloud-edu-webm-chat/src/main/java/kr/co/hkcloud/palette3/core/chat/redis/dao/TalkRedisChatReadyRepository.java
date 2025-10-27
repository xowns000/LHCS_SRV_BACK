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
public class TalkRedisChatReadyRepository
{
    private final ObjectMapper objectMapper;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsEnterInfo;

    private String logDevider = "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";

    /**
     * 대기중인 userId 해시 키가 존재하는지 확인
     * <p>
     * 해당 상담사가 대기중인 지 확인할 수 있다.
     * 
     * @param  userId
     * @return
     * @throws TelewebDaoException
     */
    public boolean hasKey(@NotEmpty String userId) throws TelewebDaoException
    {
        return hashOpsEnterInfo.hasKey(TenantContext.getCurrentTenant() + ":" +RedisCacheKeys.READY_ENTER_INFO.getKey(), userId);
    }


    /**
     * 대기중인 USER_ID와 stompVO 맵핑 넣기
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
        log.info(logPrefix + (logNum++) + " ::: 대기중인 USER_ID와 stompVO 맵핑 넣기 start");
        log.info(logPrefix + (logNum++) + " ::: stompVoToString ::: " + stompVoToString);
        log.info(logPrefix + (logNum++) + " ::: TenantContext.getTenantRedisPrefix() ::: " + TenantContext.getTenantRedisPrefix());
        
        hashOpsEnterInfo.put(TenantContext.getCurrentTenant() + ":" + RedisCacheKeys.READY_ENTER_INFO.getKey(), stompVO.getAgentId(), stompVoToString);
    }


    /**
     * 대기중인 USER_ID로 stompVO 가져오기
     * 
     * @param  userId
     * @return
     */
    public ChatStompVO getStompVO(@NotEmpty String userId) throws TelewebDaoException
    {
        String readyRedisJson = hashOpsEnterInfo.get(TenantContext.getCurrentTenant() + ":" + RedisCacheKeys.READY_ENTER_INFO.getKey(), userId);

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
     * 대기중인 USER_ID와 맵핑된 유저 세션정보 제거
     * 
     * @param sessionId
     */
    public void removeUserId(@NotEmpty int userId) throws TelewebDaoException
    {
        hashOpsEnterInfo.delete(TenantContext.getCurrentTenant() + ":" +RedisCacheKeys.READY_ENTER_INFO.getKey(), userId);
    }


    /**
     * 대기중인 전체 사이즈 가져오기
     * <p>
     * 대기중인 상담사가 있는 지 확인할 수 있다.
     * 
     * @return Long
     */
    public Long getSize() throws TelewebDaoException
    {
        return Optional.ofNullable(hashOpsEnterInfo.size(TenantContext.getCurrentTenant() + ":" + RedisCacheKeys.READY_ENTER_INFO.getKey())).orElse(0L);
    }
}
