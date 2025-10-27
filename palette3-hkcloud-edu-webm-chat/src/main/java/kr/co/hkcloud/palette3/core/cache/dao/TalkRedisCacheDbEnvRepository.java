package kr.co.hkcloud.palette3.core.cache.dao;


import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.core.chat.redis.domain.RedisCacheKeys;
import kr.co.hkcloud.palette3.core.chat.router.domain.CustcoId;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.Set;


@Slf4j
@Repository
public class TalkRedisCacheDbEnvRepository
{
    @Resource(name = "redisTemplate")
    private HashOperations<String, CustcoId, String> hashOpsDbEnv;


    public boolean has(@NotNull CustcoId envCustcoId) throws TelewebDaoException
    {
        return hashOpsDbEnv.hasKey( TenantContext.getTenantRedisPrefix() + ":" + RedisCacheKeys.DB_ENV.getKey(), envCustcoId);
    }


    public Set<CustcoId> keys() throws TelewebDaoException
    {
        return hashOpsDbEnv.keys( TenantContext.getTenantRedisPrefix() + ":" + RedisCacheKeys.DB_ENV.getKey());
    }


    public void set(@NotNull CustcoId envCustcoId, String envValue) throws TelewebDaoException
    {
        hashOpsDbEnv.put(TenantContext.getTenantRedisPrefix() + ":" + RedisCacheKeys.DB_ENV.getKey(), envCustcoId, envValue);
    }


    public String get(@NotNull CustcoId envCustcoId) throws TelewebDaoException
    {
        return hashOpsDbEnv.get( TenantContext.getTenantRedisPrefix() + ":" + RedisCacheKeys.DB_ENV.getKey(), envCustcoId);
    }


    public Long remove(@NotNull CustcoId envCustcoId) throws TelewebDaoException
    {
        return hashOpsDbEnv.delete( TenantContext.getTenantRedisPrefix() + ":" + RedisCacheKeys.DB_ENV.getKey(), envCustcoId);
    }


    public Long removeAll() throws TelewebDaoException
    {
        return hashOpsDbEnv.delete( TenantContext.getTenantRedisPrefix() + ":" + RedisCacheKeys.DB_ENV.getKey());
    }


    public Long size() throws TelewebDaoException
    {
        return Optional.ofNullable(hashOpsDbEnv.size( TenantContext.getTenantRedisPrefix() + ":" +  RedisCacheKeys.DB_ENV.getKey())).orElse(0L);
    }

}
