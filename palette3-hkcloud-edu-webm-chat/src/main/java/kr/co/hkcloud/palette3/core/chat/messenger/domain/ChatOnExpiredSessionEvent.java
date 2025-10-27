package kr.co.hkcloud.palette3.core.chat.messenger.domain;


import lombok.Builder;
import lombok.Getter;
import net.sf.json.JSONObject;


/**
 * 
 * @author 이동욱
 *
 */
@Getter
@Builder
public class ChatOnExpiredSessionEvent
{
    private final JSONObject expiredSessionJson;
}
