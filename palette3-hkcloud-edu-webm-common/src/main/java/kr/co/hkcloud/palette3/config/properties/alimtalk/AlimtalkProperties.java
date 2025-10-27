package kr.co.hkcloud.palette3.config.properties.alimtalk;


import java.net.URI;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


/**
 * 팔레트 알림톡 속성
 * 
 * @author leeiy
 *
 */
@Getter
@RequiredArgsConstructor
@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "alimtalk")
public class AlimtalkProperties
{
    @NotNull
    private final boolean enabled;

    @NotNull
    private final boolean testMode;  //카카오 알림톡 개발서버 미지원으로 운영서버 테스트 true일 경우 실발송 X, 과금 X

    @NotBlank
    private final String senderkey;  //전송키

    @NotBlank
    private final String authCode;  //인증코드

    @NotNull
    private final boolean tranType;  //알림톡발송 실패시 sms전송 전환여부

    //기본 URL
    private final Urls urls;


    @Getter
    @RequiredArgsConstructor
    public static final class Urls
    {
        @NotNull
        private final URI targetUrl;
    }
}
