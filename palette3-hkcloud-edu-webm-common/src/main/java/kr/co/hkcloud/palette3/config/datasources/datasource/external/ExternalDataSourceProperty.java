package kr.co.hkcloud.palette3.config.datasources.datasource.external;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "external-data")
@Validated
@Data
public class ExternalDataSourceProperty
{
//    private final String configPath;
    private final Map<String, ExternalDataSource> dbs;


    @RequiredArgsConstructor
    @Data
    public static class ExternalDataSource
    {
        private String driver;
        private String url;
        private String username;
        private String password;
        private String configPath;

    }
}
