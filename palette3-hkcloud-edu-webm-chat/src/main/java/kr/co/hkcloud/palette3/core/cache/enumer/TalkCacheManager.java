package kr.co.hkcloud.palette3.core.cache.enumer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TalkCacheManager
{
    MAP("텔레톡 자체 MAP에 캐싱"),
    REDIS("REDIS 메모리 DB에 저장")
    ;
    
    private final String description;
}
