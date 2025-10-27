package kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.ttalkbzc.domain;

import lombok.Builder;

import lombok.Getter;
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

/**
 * 
 * @author Orange
 *
 */
@Getter
@Builder
public class TtalkOnReferenceEvent
{
    private final JSONObject referenceJson;
    private final JSONArray extraJson;
}
