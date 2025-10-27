package kr.co.hkcloud.palette3.admin.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Flyway @Configuration 설정
 *
 * @author 황종혁
 */
@Configuration
class FlywayConfiguration {

    private final String flywayUrl;
    private final String flywayUser;
    private final String flywayPassword;
    private final String flywayDriver;
    private final String dbVendor;
    private DataSource dataSource;

    public FlywayConfiguration(@Value("${datasources.datasource.palette.master.jdbc-url}") String flywayUrl,
        @Value("${datasources.datasource.palette.master.username}") String flywayUser,
        @Value("${datasources.datasource.palette.master.password}") String flywayPassword,
        @Value("${datasources.datasource.palette.master.driver-class-name}") String flywayDriver,
        @Value("${datasources.datasource.palette.db-vendor}") String dbVendor) {

        this.flywayUrl = flywayUrl;
        this.flywayUser = flywayUser;
        this.flywayPassword = flywayPassword;
        this.flywayDriver = flywayDriver;
        this.dataSource = dataSource();
        this.dbVendor = dbVendor;
    }

    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(flywayDriver);
        dataSource.setUrl(flywayUrl);
        dataSource.setUsername(flywayUser);
        dataSource.setPassword(flywayPassword);
        return dataSource;
    }

    @Bean
    FlywayBuilder flywayBuilder() {
        return new FlywayBuilder(dataSource, flywayDriver, flywayUrl, dbVendor);
    }

    //	@Bean
    //	Flyway flyway() {
    //		Flyway flyway = flywayBuilder().createFlyway(TenantContext.DEFAULT_TENANT_CODE);
    //		flyway.migrate();
    //		return flyway;
    //	}

    /**
     * 서버 실행 시 자동으로 실행
     * @return
     */
    //	@Bean
    //    CommandLineRunner commandLineRunner(AdminCertCustcoManageService service) {
    //        return args -> {
    //        	service.migrationFlyway(new TelewebJSON());
    //        };
    //    }

}
