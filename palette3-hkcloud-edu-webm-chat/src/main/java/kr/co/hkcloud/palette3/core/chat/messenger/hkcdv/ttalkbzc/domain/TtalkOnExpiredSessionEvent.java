package kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.ttalkbzc.domain;

import lombok.Builder;
import lombok.Getter;
import net.sf.json.JSONObject;

/**
 * 
 * @author Orange
 *
 */
@Getter
@Builder
public class TtalkOnExpiredSessionEvent
{
    private final JSONObject expiredSessionJson;
}
