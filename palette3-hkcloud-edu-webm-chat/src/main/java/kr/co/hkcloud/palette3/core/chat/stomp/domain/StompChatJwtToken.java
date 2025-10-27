package kr.co.hkcloud.palette3.core.chat.stomp.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StompChatJwtToken
{
    private String name;
    private String token;
    private String id;
    private String error;
}
