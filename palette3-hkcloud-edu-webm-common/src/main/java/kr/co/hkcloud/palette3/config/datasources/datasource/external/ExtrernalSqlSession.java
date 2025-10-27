package kr.co.hkcloud.palette3.config.datasources.datasource.external;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Reader;
import java.util.Properties;


@Slf4j
@Configuration
@EnableConfigurationProperties(kr.co.hkcloud.palette3.config.datasources.datasource.external.ExternalDataSourceProperty.class)
@RequiredArgsConstructor

public class ExtrernalSqlSession
{

    private final ExternalDataSourceProperty externalDataSourceProperty;


    public SqlSession getExternalSqlSession(String externalDb)
    {
        SqlSession sqlSession = null;
        try {

            ExternalDataSourceProperty.ExternalDataSource eternalDataSource = externalDataSourceProperty.getDbs().get(externalDb);

            String configPath = eternalDataSource.getConfigPath();

            String driver = eternalDataSource.getDriver();
            String url = eternalDataSource.getUrl();
            String username = eternalDataSource.getUsername();
            String password = eternalDataSource.getPassword();

            Properties properties = new Properties();

            properties.setProperty("driver", driver);
            properties.setProperty("url", url);
            properties.setProperty("username", username);
            properties.setProperty("password", password);

//            properties.setProperty("driver", "oracle.jdbc.driver.OracleDriver");
//            properties.setProperty("url", "jdbc:oracle:thin:@172.16.0.12:1521/ORCL");
//            properties.setProperty("username", "C##PALETTE");
//            properties.setProperty("password", "Palette12#$");

            Reader reader = Resources.getResourceAsReader(configPath);
            sqlSession = new SqlSessionFactoryBuilder().build(reader, properties).openSession(ExecutorType.SIMPLE, false);

        }
        catch(Exception e) {
            log.error(e.getMessage());
        }
        return sqlSession;
    }
}
