package kr.co.hkcloud.palette3.core.cache.app;


import java.util.Set;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;

import kr.co.hkcloud.palette3.config.aspect.NoAspectAround;
import kr.co.hkcloud.palette3.core.cache.dao.TalkRedisCacheDbEnvRepository;
import kr.co.hkcloud.palette3.core.cache.dao.TalkRedisCacheSystemMessageRepository;
import kr.co.hkcloud.palette3.core.chat.router.domain.CustcoId;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Service("talkRedisCacheService")
public class TalkRedisCacheServiceImpl implements TalkRedisCacheService
{
    private final TalkRedisCacheDbEnvRepository         dbEnvRepository;
    private final TalkRedisCacheSystemMessageRepository systemMessageRepository;


    @Override
    @NoAspectAround
    public Set<CustcoId> keysDbEnv() throws TelewebAppException
    {
        return dbEnvRepository.keys();
    }


    @Override
    @NoAspectAround
    public Long getSizeDbEnv() throws TelewebAppException
    {
        return dbEnvRepository.size();
    }


    @Override
    @NoAspectAround
    public void setRedisDbEnvironment(CustcoId envCustcoId, String envValue) throws TelewebAppException
    {
        dbEnvRepository.set(envCustcoId, envValue);
    }


    @Override
    @NoAspectAround
    public String getRedisStringByDbEnv(CustcoId envCustcoId) throws TelewebAppException
    {
        return dbEnvRepository.get(envCustcoId);
    }


    @Override
    @NoAspectAround
    public Set<CustcoId> keysSystemMessage() throws TelewebAppException
    {
        return systemMessageRepository.keys();
    }


    @Override
    @NoAspectAround
    public Long getSizeSystemMessage() throws TelewebAppException
    {
        return systemMessageRepository.size();
    }


    @Override
    @NoAspectAround
    public void setRedisSystemMessage(CustcoId custcoId, JSONObject sysMsgJSON) throws TelewebAppException
    {
        systemMessageRepository.set(custcoId, sysMsgJSON);;
    }


    @Override
    @NoAspectAround
    public JSONObject getRedisJsonBySystemMsgId(CustcoId custcoId) throws TelewebAppException
    {
        return systemMessageRepository.get(custcoId);
    }


    @Override
    public void removeAllSystemMessage() throws TelewebAppException
    {
        systemMessageRepository.removeAll();
    }


    @Override
    public void removeSystemMessage(@NotNull CustcoId custcoId) throws TelewebAppException
    {
        systemMessageRepository.remove(custcoId);
    }
}
