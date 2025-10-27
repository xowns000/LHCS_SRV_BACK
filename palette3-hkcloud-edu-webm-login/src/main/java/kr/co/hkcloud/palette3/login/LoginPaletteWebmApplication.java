package kr.co.hkcloud.palette3.login;

import kr.co.hkcloud.palette3.core.profile.enumer.PaletteProfiles;
import kr.co.hkcloud.palette3.core.profile.util.PaletteProfileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.validation.constraints.NotBlank;

@Slf4j
@RequiredArgsConstructor
@EnableCaching
@EnableAspectJAutoProxy
@SpringBootApplication
public class LoginPaletteWebmApplication implements CommandLineRunner {
    private final PaletteProfileUtils paletteProfileUtils;

    @NotBlank
    @Value("${jasypt.key}")
    private String jasyptKey;


    public static void main(String[] args) {
        new SpringApplicationBuilder(LoginPaletteWebmApplication.class).run(args);
    }


    /**
     * 상용에서는 제거하고 빌드 필요함
     */
    public void run(String... args) {
        PaletteProfiles profile = paletteProfileUtils.getActiveProfile();
        switch (profile) {
            case local:
                break;
            default:
                break;
        }
    }
}
