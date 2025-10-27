package kr.co.hkcloud.palette3;

import java.io.File;
import javax.validation.constraints.NotBlank;
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
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * packageName    : kr.co.hkcloud.palette3
 * fileName       : PaletteWebmApplication.java
 * author         : Orange
 * date           : 2023-10-13
 * description    : Palette 3.0 Web Application
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-13       Orange       최초 생성
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableAspectJAutoProxy
@ServletComponentScan
@EnableJpaRepositories
public class PaletteWebmApplication implements CommandLineRunner {

    private final PaletteProfileUtils paletteProfileUtils;

    @NotBlank
    @Value("${jasypt.key}")
    private String jasyptKey;

    @NotBlank
    @Value("${spring.servlet.multipart.location}")
    private String multipartTempDir;

    public static void main(String[] args) {
        new SpringApplicationBuilder(PaletteWebmApplication.class).run(args);
    }

    /**
     * 상용에서는 제거하고 빌드 필요함
     */
    public void run(String... args) {

        /* 임시디렉토리 없을시 생성. */
        File tempDir = new File(multipartTempDir);
        if (!tempDir.isDirectory()) {
            tempDir.mkdir();
        }

        PaletteProfiles profile = paletteProfileUtils.getActiveProfile();
        switch (profile) {
            case local:
                PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
                SimpleStringPBEConfig config = new SimpleStringPBEConfig();
                config.setPassword(jasyptKey);
                config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
                config.setKeyObtentionIterations("1000");
                config.setPoolSize("1");
                config.setProviderName("SunJCE");
                config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
                config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
                config.setStringOutputType("base64");
                encryptor.setConfig(config);
                
                break;
            case production:
                log.debug("");
                break;
            case productionlhcs:
                log.debug("");
                break;
            case productioncloud:
                log.debug("");
                break;
            default:
                break;
        }
    }
}
