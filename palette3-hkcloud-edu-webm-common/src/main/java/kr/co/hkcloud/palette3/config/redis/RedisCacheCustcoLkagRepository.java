package kr.co.hkcloud.palette3.config.redis;


import java.util.Optional;
import javax.annotation.Resource;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;


@Slf4j
@Repository
public class RedisCacheCustcoLkagRepository {

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, TelewebJSON> hashOpsCustcoLkag;

    public boolean has(String lkagId, String certCustcoId) throws TelewebDaoException {
        return hashOpsCustcoLkag.hasKey("custco:1:palette:cache:lkag:" + lkagId, certCustcoId);
    }

    public void set(String lkagId, String certCustcoId, TelewebJSON obj) throws TelewebDaoException {
        hashOpsCustcoLkag.put("custco:1:palette:cache:lkag:" + lkagId, certCustcoId, obj);
    }


    public TelewebJSON get(String lkagId, String certCustcoId) throws TelewebDaoException {
        return hashOpsCustcoLkag.get("custco:1:palette:cache:lkag:" + lkagId, certCustcoId);
    }

    public void remove(String lkagId) throws TelewebDaoException {
        hashOpsCustcoLkag.entries("custco:1:palette:cache:lkag:" + lkagId).keySet()
            .forEach(haskKey -> {
                hashOpsCustcoLkag.delete("custco:1:palette:cache:lkag:" + lkagId, haskKey);
            });
    }


    public Long remove(String lkagId, String certCustcoId) throws TelewebDaoException {
        return hashOpsCustcoLkag.delete("custco:1:palette:cache:lkag:" + lkagId, certCustcoId);
    }


    public void removeAll() throws TelewebDaoException {

        hashOpsCustcoLkag.entries("custco:1:palette:cache:lkag").keySet()
            .forEach(haskKey -> {
                    hashOpsCustcoLkag.delete("custco:1:palette:cache:lkag", haskKey);

            });
    }


    public Long size(String lkagId, String certCustcoId) throws TelewebDaoException {
        Long size = Optional.ofNullable(
                hashOpsCustcoLkag.size("custco:1:palette:cache:lkag:" + lkagId + ":" + certCustcoId))
            .orElse(0L);
        log.info("RedisCacheCustcoLkagRepository.size ::: custco:1, " + size);
        return size;
    }

}
