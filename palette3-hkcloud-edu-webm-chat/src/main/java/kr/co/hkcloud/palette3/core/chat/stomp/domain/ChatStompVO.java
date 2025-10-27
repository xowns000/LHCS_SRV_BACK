package kr.co.hkcloud.palette3.core.chat.stomp.domain;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Orange
 * 
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatStompVO implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 8484550411952567282L;
    @NotEmpty private String sessionId;
    @NotEmpty private String agentId;
    private String agentName;
    private String agentNickName;
    private String userKey;
    private String roomId;
    private String sndrKey;
    private String talkContactId;
    private String custcoId;
}
