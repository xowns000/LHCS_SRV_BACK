package kr.co.hkcloud.palette3.config.redis;

import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

/**
 * Redis-Cache Multi-Tenancy 위해 RedisCacheManager확장.
 */
@Slf4j
public class RedisCustomCacheManager extends RedisCacheManager
{
    public RedisCustomCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
        RedisCacheManager.builder()
                .cacheWriter(cacheWriter)
                .cacheDefaults(defaultCacheConfiguration)
                .build();
    }

    /**
     * @param name
     * @return Prefix the cache store name with the TENANT KEY
     * For SUPER ADMIN no prefix applied
     */
    @Override
    public Cache getCache(String name) {
        String tenantPrefix = TenantContext.getTenantRedisPrefix();
        log.info("Inside getCache:" + tenantPrefix + ":" + name);
        if (StringUtils.isEmpty( tenantPrefix ) ) {
            return super.getCache(name);
        } else if (name.startsWith( tenantPrefix )) {
            return super.getCache(name);
        }
        return super.getCache(tenantPrefix + ":" + name);
    }
}