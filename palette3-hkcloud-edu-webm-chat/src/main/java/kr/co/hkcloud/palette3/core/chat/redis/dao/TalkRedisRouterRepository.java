package kr.co.hkcloud.palette3.core.chat.redis.dao;


import java.util.Optional;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.hkcloud.palette3.core.chat.redis.domain.RedisCacheKeys;
import kr.co.hkcloud.palette3.core.chat.redis.domain.RedundancyStatus;
import kr.co.hkcloud.palette3.core.chat.redis.domain.RedundancyStatusVO;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service
public class TalkRedisRouterRepository
{
    private final ObjectMapper objectMapper;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsRedundancyStatusInfo;


    public boolean hasKey(@NotEmpty RedundancyStatus redundancyStatus) throws TelewebDaoException
    {
        return hashOpsRedundancyStatusInfo.hasKey(TenantContext.getTenantRedisPrefix() + ":" + RedisCacheKeys.REDUNDANCY_STATUS.getKey(), redundancyStatus.name());
    }


    public void setRedundancyStatusVO(@NotNull RedundancyStatusVO redundancyStatusVO) throws TelewebDaoException
    {
        String redundancyStatusVOToString;
        try {
            redundancyStatusVOToString = objectMapper.writeValueAsString(redundancyStatusVO);
        }
        catch(JsonProcessingException e) {
            log.error(e.getLocalizedMessage());
            throw new TelewebDaoException(e.getLocalizedMessage());
        }
        log.debug("redundancyStatusVOToString={}", redundancyStatusVOToString);

        hashOpsRedundancyStatusInfo.put(TenantContext.getTenantRedisPrefix() + ":" + RedisCacheKeys.REDUNDANCY_STATUS.getKey(), RedundancyStatus.ACTIVE
            .toString(), redundancyStatusVOToString);
    }


    public RedundancyStatusVO getRedundancyStatusVO(@NotEmpty RedundancyStatus redundancyStatus) throws TelewebDaoException
    {
        log.debug("redundancyStatus={}", redundancyStatus);

        String redundancyStatusJson = hashOpsRedundancyStatusInfo
            .get(TenantContext.getTenantRedisPrefix() + ":" + RedisCacheKeys.REDUNDANCY_STATUS.getKey(), redundancyStatus.name());
        log.debug("redundancyStatusJson={}", redundancyStatusJson);

        //RedundancyStatusVO 객채로 맵핑
        RedundancyStatusVO redundancyStatusVO;
        try {
            redundancyStatusVO = objectMapper.readValue(redundancyStatusJson, RedundancyStatusVO.class);
        }
        catch(JsonProcessingException e) {
            log.error(e.getLocalizedMessage());
            throw new TelewebDaoException(e.getLocalizedMessage());
        }

        return redundancyStatusVO;
    }


    public void removeRedundancyStatus(@NotEmpty RedundancyStatus redundancyStatus) throws TelewebDaoException
    {
        hashOpsRedundancyStatusInfo.delete(TenantContext.getTenantRedisPrefix() + ":" +RedisCacheKeys.REDUNDANCY_STATUS.getKey(), redundancyStatus.name());
    }


    public Long getSize() throws TelewebDaoException
    {
        return Optional.ofNullable(hashOpsRedundancyStatusInfo.size(TenantContext.getTenantRedisPrefix() + ":" + RedisCacheKeys.REDUNDANCY_STATUS.getKey()))
            .orElse(0L);
    }
}
