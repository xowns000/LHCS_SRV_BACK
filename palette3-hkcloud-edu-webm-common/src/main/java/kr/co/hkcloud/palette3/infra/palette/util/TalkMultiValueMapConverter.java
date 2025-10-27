package kr.co.hkcloud.palette3.infra.palette.util;


import java.util.Map;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 
 * @author Orange
 *
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class TalkMultiValueMapConverter
{
    public static MultiValueMap<String, String> convert(ObjectMapper objectMapper, Object dto)
    {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> map = objectMapper.convertValue(dto, new TypeReference<Map<String, String>>() {});
        params.setAll(map);

        return params;
    }
}
