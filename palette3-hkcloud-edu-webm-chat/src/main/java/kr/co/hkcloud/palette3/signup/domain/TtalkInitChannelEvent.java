package kr.co.hkcloud.palette3.signup.domain;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import lombok.Builder;
import lombok.Getter;

/**
 * 
 * @author Orange
 *
 */
@Getter
@Builder
public class TtalkInitChannelEvent
{
    private final TelewebJSON jsonParams;
}
