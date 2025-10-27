package kr.co.hkcloud.palette3.config.security.properties;


import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;


/**
 * 팔레트 보안 속성
 *
 * @author Orange
 *
 */
@Getter
@RequiredArgsConstructor
@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "palette.security")
public class PaletteSecurityProperties {

    @NotNull
    private final boolean duplicateLogin; //중복로그인허용여부
    @NotNull
    private final String duplicateLoginAllowip; //중복로그인예외 IP 또는 IP대역(127.0.0.1/32,121.67.187.0/24 .....)

    private final Bizlog bizlog;
    private final PwdUserTerm pwdUserTerm;
    private final VdiIp vdiIp;

    @Getter
    @RequiredArgsConstructor
    public static final class Bizlog {

        @NotNull
        private final boolean enabled;
    }


    @Getter
    @RequiredArgsConstructor
    public static final class PwdUserTerm {

        @NotNull
        private final boolean enabled;

        @NotNull
        private final int failCnt;

        @NotNull
        private final boolean firstLogin;

        //@NotNull
        private final boolean reset;

        //@NotBlank
        private final String resetKeyword;

        //@NotBlank
        private final String encKeyword;
    }


    //@NotBlank
    private final String devEncryptKey;

    //    /@NotBlank
    private final String realEncryptKey;


    @Getter
    @RequiredArgsConstructor
    public static final class VdiIp {

        @NotNull
        private final boolean enabled;

        @NotNull
        private final List<String> list;
    }
}
