package kr.co.hkcloud.palette3.config.security.properties;


import java.net.URI;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


/**
 * 채팅 보안 속성
 * 
 * @author Orange
 *
 */
@Getter
@RequiredArgsConstructor
@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "chat.security")
public class ChatSecurityProperties
{
    @NotNull
    private final List<URI> viewerWhiteListDomain;

    private final ChatMain chatMain;


    @Getter
    @RequiredArgsConstructor
    public static final class ChatMain
    {
        @NotNull
        private final boolean enabled;

        //PaletteSecurityProperties로 이동함 - 20210614
//		@NotBlank
//		private final String devEncryptKey;
//		
//		@NotBlank
//		private final String realEncryptKey;

        @NotBlank
        private final String paramKey;
    }
}
