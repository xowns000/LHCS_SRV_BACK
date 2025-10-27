package kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.linebzc.domain;

import lombok.Builder;

import lombok.Getter;
import net.sf.json.JSONObject;

/**
 * 
 * @author 서주희
 *
 */
@Getter
@Builder
public class LinebzcOnExpiredSessionEvent
{
    private final JSONObject expiredSessionJson;
}
