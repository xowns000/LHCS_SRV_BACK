package kr.co.hkcloud.palette3.config.properties.proxy;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


/**
 * 팔레트 PROXY 속성
 * 
 * @author Orange
 *
 */
@Getter
@RequiredArgsConstructor
@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "proxy")
public class ProxyProperties
{
    @NotNull
    private final boolean enabled;

    @NotBlank
    private final String domain; // 도메인(IP)

    @NotNull
    private final int port; // PORT

    @NotBlank
    private final String schema; // http or https

}
