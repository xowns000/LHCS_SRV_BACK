package kr.co.hkcloud.palette3.core.chat.redis.dao;


import java.util.Optional;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;

import org.apache.commons.lang3.StringUtils;
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
public class TalkRedisChatInoutRepository
{
    private final TalkRedisChatEscalatingRepository chatEscalatingRepository;
    private final ObjectMapper                      objectMapper;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsEnterInfo;
    
    private String logDevider = "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";

//    @Resource(name = "redisTemplate")
//    private ValueOperations<String, String> valueOps;


    /**
     * 상담중인 userKey 해시 키가 존재하는지 확인
     * 
     * @param  userKey
     * @return
     * @throws TelewebDaoException
     */
    public boolean hasKey(@NotEmpty String userKey) throws TelewebDaoException
    {
        return hashOpsEnterInfo.hasKey(TenantContext.getCurrentTenant() + ":" + RedisCacheKeys.INOUT_ENTER_INFO.getKey(), userKey);
    }


    /**
     * 상담중인 유저가 입장한 userKey 와 stompVO 맵핑 정보 넣기
     * 
     * @param stompVO
     */
    public void setStompVO(@NotNull ChatStompVO stompVO) throws TelewebDaoException
    {
        //ESCALATING:REMOVE:REDIS - 상담 이관중
        chatEscalatingRepository.removeUserKey(stompVO.getUserKey());

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
        log.info(logPrefix + (logNum++) + " ::: 상담중인 유저가 입장한 userKey 와 stompVO 맵핑 정보 넣기 start");
        log.info(logPrefix + (logNum++) + " ::: stompVoToString ::: " + stompVoToString);
        hashOpsEnterInfo.put(TenantContext.getCurrentTenant() + ":" +RedisCacheKeys.INOUT_ENTER_INFO.getKey(), stompVO.getUserKey(), stompVoToString);
    }


    /**
     * 상담중인 userKey로 입장해 있는 stompVO 가져오기
     * 
     * @param  UserKey
     * @return
     */
    public ChatStompVO getStompVO(@NotEmpty String UserKey) throws TelewebDaoException
    {
        String inoutRedisJson = hashOpsEnterInfo.get(TenantContext.getCurrentTenant() + ":" +RedisCacheKeys.INOUT_ENTER_INFO.getKey(), UserKey);

        //ChatStompVO 객채로 맵핑
        ChatStompVO stompVO;
        try {
            stompVO = objectMapper.readValue(inoutRedisJson, ChatStompVO.class);
        }
        catch(JsonProcessingException e) {
            log.error(e.getLocalizedMessage());
            throw new TelewebDaoException(e.getLocalizedMessage());
        }

        return stompVO;
    }


    /**
     * 상담중인 유저 세션정보와 맵핑된 채팅방ID 제거
     * 
     * @param userKey
     */
    public void removeUserKey(@NotEmpty String userKey) throws TelewebDaoException
    {
        hashOpsEnterInfo.delete(TenantContext.getCurrentTenant() + ":" +RedisCacheKeys.INOUT_ENTER_INFO.getKey(), userKey);
    }


    /**
     * 상담중인 전체 사이즈 가져오기
     * 
     * @return Long
     */
    public Long getSize() throws TelewebDaoException
    {
        return Optional.ofNullable(hashOpsEnterInfo.size(TenantContext.getCurrentTenant() + ":" +RedisCacheKeys.INOUT_USER_COUNT.getKey())).orElse(0L);
    }

//    /**
//     * 상담중인 채팅방 유저수 가져오기
//     * @param userKey
//     * @return
//     */
//    public long getCount(@NotEmpty String userKey) throws TelewebDaoException
//    {
//        return Long.valueOf(Optional.ofNullable(valueOps.get(TenantContext.getTenantRedisPrefix() + ":" +RedisCacheKeys.INOUT_USER_COUNT.getKey() + "_" + userKey)).orElse("0"));
//    }
//
//    /**
//     * 상담중인 채팅방에 입장한 유저수 +1
//     * @param userKey
//     * @return
//     */
//    public long plusCount(@NotEmpty String userKey) throws TelewebDaoException
//    {
//        return Optional.ofNullable(valueOps.increment(TenantContext.getTenantRedisPrefix() + ":" +RedisCacheKeys.INOUT_USER_COUNT.getKey() + "_" + userKey)).orElse(0L);
//    }
//
//    /**
//     * 상담중인 채팅방에 입장한 유저수 -1
//     * @param userKey
//     * @return
//     */
//    public long minusCount(@NotEmpty String userKey) throws TelewebDaoException
//    {
//        return Optional.ofNullable(valueOps.decrement(TenantContext.getTenantRedisPrefix() + ":" + RedisCacheKeys.INOUT_USER_COUNT.getKey() + "_" + userKey)).filter(count -> count > 0).orElse(0L);
//    }
}
