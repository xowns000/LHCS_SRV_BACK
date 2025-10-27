package kr.co.hkcloud.palette3.config.security.datasource;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * packageName    : kr.co.hkcloud.palette3.config.security.datasource
 * fileName       : JasyptConfig.java
 * author         : Orange
 * date           : 2023-10-13
 * description    : yaml내에 포함된 ENC 암복호화 처리용도.
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-13       Orange       최초 생성
 */
@Validated
@Configuration
public class JasyptConfig {

    public final static String KEY = "paletteHello";
    public final static String ALGORITHM = "PBEWITHHMACSHA512ANDAES_256";

    /**
     * -Djasypt.key=paletteHello ==> 고정으로 변경.
     */
    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(KEY);
        config.setAlgorithm(ALGORITHM);
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }
}
