package kr.co.hkcloud.palette3.core.chat.redis.dao;


import java.util.List;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;

import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

import kr.co.hkcloud.palette3.core.chat.redis.domain.RedisCacheKeys;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatRoom;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Repository
public class TalkRedisChatRepository
{
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, ChatRoom> hashOpsChatRoom;


    /**
     * 모든 채팅방 찾기
     * 
     * @return
     */
    public List<ChatRoom> findAllRoom() throws TelewebDaoException
    {
        return hashOpsChatRoom.values(TenantContext.getTenantRedisPrefix() + ":" + RedisCacheKeys.CHAT_ROOMS.getKey());
    }


    /**
     * userKey로 채팅방 찾기
     * 
     * @param  userKey
     * @return
     */
    public ChatRoom finByUserKey(@NotEmpty String userKey) throws TelewebDaoException
    {
        return hashOpsChatRoom.get(TenantContext.getTenantRedisPrefix() + ":" + RedisCacheKeys.CHAT_ROOMS.getKey(), userKey);
    }


    /**
     * 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
     * 
     * @param  userKey
     * @return
     */
    public ChatRoom createChatRoom(@NotEmpty String userKey) throws TelewebDaoException
    {
        log.trace("createChatRoom ::: {}", userKey);
        ChatRoom chatRoom = ChatRoom.builder().owner(userKey).build();
        hashOpsChatRoom.put(RedisCacheKeys.CHAT_ROOMS.getKey(), userKey, chatRoom);
        return chatRoom;
    }
}
