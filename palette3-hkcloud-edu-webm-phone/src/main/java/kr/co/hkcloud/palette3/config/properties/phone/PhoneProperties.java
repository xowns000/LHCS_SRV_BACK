package kr.co.hkcloud.palette3.config.properties.phone;


import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


/**
 * 팔레트 전화(phone) 속성
 * 
 * @author Orange
 *
 */
@Getter
@RequiredArgsConstructor
@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "phone")
public class PhoneProperties
{
    @NotNull
    private final boolean enabled;

    @NotNull
    private final boolean mainResveCallEnabled;  //전화메인 예약콜 활성화 여부

    @NotNull
    private final boolean mainCheckBoardEnabled;  //전화메인 전광판 활성화 여부

    private final CtiServer ctiServer;


    //CTI 서버 속성
    @Getter
    @RequiredArgsConstructor
    public static final class CtiServer
    {
        private final String url;
        private final String ctiPort;
        private final String ctiSecretPw;
        private final String ctiOutboundNumber;
        private final String recordingPort;
        private final String recordingPath;
        private final String recordingBackupPath;

    }
}
