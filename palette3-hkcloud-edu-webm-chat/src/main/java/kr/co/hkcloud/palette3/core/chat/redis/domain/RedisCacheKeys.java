package kr.co.hkcloud.palette3.core.chat.redis.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RedisCacheKeys
{
    READY_ESCALATING("palette:messenger:ready-escalating", "대기에서 상담중으로 이관중"),
    READY_ENTER_INFO("palette:messenger:ready-enter-info", "대기중인 클라이언트의 sessionId와 userId를 맵핑한 정보 저장"),
    INOUT_ENTER_INFO("palette:messenger:inout-enter-info", "채팅룸에 입장한 클라이언트의 sessionId와 채팅룸 id를 맵핑한 정보 저장"),
    INOUT_USER_COUNT("palette:messenger:inout-user-count", "채팅룸에 입장한 클라이언트수 저장"),
    DB_ENV("palette:cache:db-env", "디비 설정 정보를 저장"),
    SYSTEM_MESSAGE("palette:cache:system-message", "시스템메세지를 저장"),
    CHAT_ROOMS("palette:messenger:chat-room", "채팅룸 저장"),
    REDUNDANCY_STATUS("palette:router:redundancy-status", "ROUTER 활성/대기 정보 저장")
    ;
    
    private final String key;
    private final String description;
}
