package kr.co.hkcloud.palette3.core.chat.stomp.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatRoom implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -5985837782497853132L;
    
    @Builder.Default
    private String roomId = UUID.randomUUID().toString();
    
    //owner (userKey)
    @NotEmpty(message = "USER_KEY가 있어야 합니다.")
    private String owner;
    
    //채팅방 인원수
    @PositiveOrZero(message = "채팅방 인원수는 0 이상이어야 합니다.")
    private long userCount;
}
