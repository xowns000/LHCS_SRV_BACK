package kr.co.hkcloud.palette3.core.chat.transfer.domain;

import javax.validation.constraints.NotEmpty;

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
public class TransToEndTalkEvent
{
    @NotEmpty
    private final String active;
    
    @NotEmpty
    private final String callTypCd;
    
    private final JSONObject writeData;
}
