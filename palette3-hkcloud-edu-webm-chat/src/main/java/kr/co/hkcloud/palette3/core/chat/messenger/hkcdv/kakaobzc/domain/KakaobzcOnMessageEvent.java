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
public class KakaobzcOnMessageEvent
{
    private final JSONObject messageJson;
    private final JSONArray  extraJson;
}
