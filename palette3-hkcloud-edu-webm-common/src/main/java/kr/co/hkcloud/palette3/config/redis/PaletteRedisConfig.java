package kr.co.hkcloud.palette3.config.redis;


import io.lettuce.core.ReadFrom;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.core.profile.enumer.PaletteProfiles;
import kr.co.hkcloud.palette3.core.profile.util.PaletteProfileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;


@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableCaching
public class PaletteRedisConfig
{
    private final PaletteProfileUtils paletteProfileUtils;
    private final RedisProperties redisProperties;


    @Bean
    public RedisConnectionFactory redisConnectionFactory()
    {
        PaletteProfiles profile = paletteProfileUtils.getActiveProfile();
        log.info("*palette-web: Reids - connection profile={}", profile);
        switch(profile)
        {
            case productioncloud:
            case productioncloudgwm:
            case productionlhcs:
            case production:
            {
                try
                {
                    log.info("*palette-web-common: Redis - RedisClusterConfiguration and LettuceClientConfiguration!!\n\n\t[cluster-nodes={}]\n", redisProperties.getCluster().getNodes());
                    RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(redisProperties.getCluster().getNodes());
                    LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder().readFrom(ReadFrom.REPLICA_PREFERRED).build();
                    LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisClusterConfiguration, lettuceClientConfiguration);
                    return lettuceConnectionFactory;
                } catch(NullPointerException ne)
                {
                    log.info("*palette-web-common: Redis - RedisStandaloneConfiguration!!\n\n\t[host={}:{}]\n", redisProperties.getHost(), redisProperties.getPort());
                    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
                    redisStandaloneConfiguration.setHostName(redisProperties.getHost());
                    redisStandaloneConfiguration.setPort(redisProperties.getPort());
                    redisStandaloneConfiguration.setPassword(redisProperties.getPassword());
                    LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
                    return lettuceConnectionFactory;
                }
            }
            // case uat: case production:
            case local:
            case dev:
            default:
            {
                log.info("*palette-web-common: Redis - RedisStandaloneConfiguration!!\n\n\t[host={}:{}]\n", redisProperties.getHost(), redisProperties.getPort());
                RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
                redisStandaloneConfiguration.setHostName(redisProperties.getHost());
                redisStandaloneConfiguration.setPort(redisProperties.getPort());
                redisStandaloneConfiguration.setPassword(redisProperties.getPassword());
                LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
                return lettuceConnectionFactory;
            }
        }
    }

    /**
     * 아래 Redis-Cache Multi-Tenancy 관련. 추가.
     *
     * @return
     */
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration()
    {
        return RedisCacheConfiguration
                .defaultCacheConfig(Thread.currentThread().getContextClassLoader())
            ;
    }

    @Bean
    public RedisCacheWriter redisCacheWriter()
    {
        return RedisCacheWriter.lockingRedisCacheWriter(redisConnectionFactory());
    }

    @Bean
    public RedisCacheManager redisCacheManager()
    {
        return new RedisCustomCacheManager(redisCacheWriter(), redisCacheConfiguration());
    }

}
