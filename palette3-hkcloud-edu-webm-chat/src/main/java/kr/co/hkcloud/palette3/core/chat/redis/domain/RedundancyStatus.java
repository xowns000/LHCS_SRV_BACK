package kr.co.hkcloud.palette3.core.chat.redis.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 * @author Orange
 *
 */
@Getter
@AllArgsConstructor
public enum RedundancyStatus
{
    ACTIVE("ACTIVE", "활성 ROUTER"),
    STANDBY("STANDBY", "대기 ROUTER"),
    ;
    private final String key;
    private final String description;
}
