package kr.co.hkcloud.palette3.core.cache.dao;


import java.util.Optional;
import java.util.Set;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

import kr.co.hkcloud.palette3.core.chat.redis.domain.RedisCacheKeys;
import kr.co.hkcloud.palette3.core.chat.router.domain.CustcoId;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


@Slf4j
@Repository
public class TalkRedisCacheSystemMessageRepository
{
    @Resource(name = "redisTemplate")
    private HashOperations<String, CustcoId, JSONObject> hashOpsSystemMessage;


    public boolean has(@NotNull CustcoId custcoId) throws TelewebDaoException
    {
        return hashOpsSystemMessage.hasKey( TenantContext.getTenantRedisPrefix() + ":" +  RedisCacheKeys.SYSTEM_MESSAGE.getKey(), custcoId);
    }


    public Set<CustcoId> keys() throws TelewebDaoException
    {
        return hashOpsSystemMessage.keys( TenantContext.getTenantRedisPrefix() + ":" +  RedisCacheKeys.SYSTEM_MESSAGE.getKey());
    }


    public void set(@NotNull CustcoId custcoId, JSONObject sysMsgJSON) throws TelewebDaoException
    {
        hashOpsSystemMessage.put( TenantContext.getTenantRedisPrefix() + ":" +  RedisCacheKeys.SYSTEM_MESSAGE.getKey(), custcoId, sysMsgJSON);
    }


    public JSONObject get(@NotNull CustcoId custcoId) throws TelewebDaoException
    {
        return hashOpsSystemMessage.get( TenantContext.getTenantRedisPrefix() + ":" +  RedisCacheKeys.SYSTEM_MESSAGE.getKey(), custcoId);
    }


    public Long remove(@NotNull CustcoId custcoId) throws TelewebDaoException
    {
        return hashOpsSystemMessage.delete( TenantContext.getTenantRedisPrefix() + ":" +  RedisCacheKeys.SYSTEM_MESSAGE.getKey(), custcoId);
    }


    public Long removeAll() throws TelewebDaoException
    {
        return hashOpsSystemMessage.delete( TenantContext.getTenantRedisPrefix() + ":" +  RedisCacheKeys.SYSTEM_MESSAGE.getKey());
    }


    public Long size() throws TelewebDaoException
    {
        return Optional.ofNullable(hashOpsSystemMessage.size( TenantContext.getTenantRedisPrefix() + ":" +  RedisCacheKeys.SYSTEM_MESSAGE.getKey())).orElse(0L);
    }

}
