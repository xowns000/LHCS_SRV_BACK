package kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.kakaobzc.domain;


import lombok.Builder;
import lombok.Getter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 
 * @author Orange
 *
 */
@Getter
@Builder
public class KakaobzcOnExpiredSessionEvent
{
    private final JSONObject expiredSessionJson;
    private final JSONArray  extraJson;
}
