package kr.co.hkcloud.palette3.config.cache;


import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Component
public class CacheManagerChecker implements CommandLineRunner
{
    private final CacheManager cacheManager;


    @Override
    public void run(String... args)
    {
        log.info("=================================");
        log.info("Using spring cache manager ::: {}", this.cacheManager.getClass().getName());
        log.info("=================================");
    }
}
