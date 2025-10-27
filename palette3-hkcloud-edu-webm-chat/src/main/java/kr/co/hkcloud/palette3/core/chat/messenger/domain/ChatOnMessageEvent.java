package kr.co.hkcloud.palette3.core.chat.messenger.domain;

import lombok.Builder;
import lombok.Getter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Getter
@Builder
public class ChatOnMessageEvent {
    private final JSONObject messageJson;
    private final JSONArray  extraJson;
}
