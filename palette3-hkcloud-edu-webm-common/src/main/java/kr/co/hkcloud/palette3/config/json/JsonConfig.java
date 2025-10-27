package kr.co.hkcloud.palette3.config.json;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Configuration
public class JsonConfig
{
    @Bean
    public ObjectMapper jsonObjectMapper()
    {
        log.info("Loading Bean jsonObjectMapper:::");

        return Jackson2ObjectMapperBuilder.json().propertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE).indentOutput(true).build();
    }
}
