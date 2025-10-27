package kr.co.hkcloud.palette3.config.properties.palette;


import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.validation.annotation.Validated;

import kr.co.hkcloud.palette3.config.properties.palette.enumer.PaletteLicense;
import kr.co.hkcloud.palette3.config.properties.palette.enumer.PaletteServiceMode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


/**
 * 팔레트(palette) 속성
 * 
 * @author Orange
 *
 */
@Getter
@RequiredArgsConstructor
@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "palette")
public class PaletteProperties
{
    @NotNull
    private final boolean            enabled;
    @NotNull
    private final URI                serviceUrl;  //팔레트 URL
    @NotNull
    private final URI                publicUrl;   //DMZ구간 외부에서 접속할 URL(망분리된 경우 사용/serviceUrl과 같을 수 있음)
    @NotNull
    private final PaletteLicense     license;
    private final String             partnerId;
    @NotNull
    private final PaletteServiceMode mode;
    @NotNull
    private final PaletteServiceMode serviceMode;
    @NotNull
    private final boolean            mainNoticeEnabled;  //공지사항 활성화 여부

    private final Asp       asp;
    private final Pay       pay;
    private final Thymleaf  thymleaf;
    private final CtiServer ctiServer;


    //ASP 속성
    @Getter
    @RequiredArgsConstructor
    public static final class Asp
    {
        @NotNull
        private final boolean enabled = false;  //사용여부(기본값: false)
        private final URL     singnuoEmailActivationUrl;  //ASP 서버 접속 URL
        private final String  callCenterPhoneNum;  //콜센터 전화번호

        @DurationUnit(ChronoUnit.DAYS)
        private final Duration trialDuration = Duration.ofDays(6);  //트라이얼 기간(기본값: 6일)
    }


    //결제 속성
    @Getter
    @RequiredArgsConstructor
    public static final class Pay
    {
        @NotNull
        private final boolean enabled = false;  //사용여부(기본값: false)
    }


    //타임리프 속성
    @Getter
    @RequiredArgsConstructor
    public static final class Thymleaf
    {
        @NotBlank
        private final String mainPath;  //메인페이지 경로
    }


    //CTI 서버 속성 (통합이력용)
    @Getter
    @RequiredArgsConstructor
    public static final class CtiServer
    {
        @NotBlank
        private final String url;

        @NotBlank
        private final String ctiPort;

        @NotBlank
        private final String ctiSecretPw;

        @NotBlank
        private final String ctiOutboundNumber;

        @NotBlank
        private final String recordingPort;

        @NotBlank
        private final String recordingPath;

        @NotBlank
        private final String recordingBackupPath;

    }
}
