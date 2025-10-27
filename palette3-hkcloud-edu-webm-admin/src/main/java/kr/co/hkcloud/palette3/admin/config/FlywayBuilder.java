package kr.co.hkcloud.palette3.admin.config;

import javax.sql.DataSource;
import lombok.AllArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Flyway Builder
 *
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-27        황종혁       최초 생성
 * </pre>
 */
@AllArgsConstructor
public class FlywayBuilder {

    private static final String TENANT_SCHEMA_LOCATION = "db/migration";

    private final DataSource dataSource;
    private final String flywayDriver;
    private final String flywayUrl;
    private final String dbVendor;
    

    /**
     * 스키마명 받아서 flyway load
     */
    public Flyway createFlyway(String schemaName) {
        Flyway flyway = null;
        
        if("POSTGRESQL".equals(dbVendor.toUpperCase())) {
            flyway = Flyway.configure().dataSource(dataSource).locations(TENANT_SCHEMA_LOCATION).schemas(schemaName).load();
        } else if("ORACLE".equals(dbVendor.toUpperCase())) {
            DataSource oracleDataSource = getOracleDataSource("C##"+schemaName.toUpperCase(), "Palette12#$");
            flyway = Flyway.configure()
                .dataSource(oracleDataSource)
                .locations(TENANT_SCHEMA_LOCATION + "_oracle")
                .schemas("C##"+schemaName.toUpperCase())
                .load();
        }
        return flyway;
    }
    
    private DataSource getOracleDataSource(String userName, String password) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(flywayDriver);
        dataSource.setUrl(flywayUrl);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        return dataSource;
    }

    //	public Flyway createFlyway(TenantResponseDto tenant) {
    //		return Flyway.configure()
    //				.dataSource(dataSource)
    //				.locations(TENANT_SCHEMA_LOCATION)
    //				.schemas(getSchemaName(tenant))
    //				.load();
    //	}
    //
    //	private String getSchemaName(TenantResponseDto tenant) {
    //		return Optional.ofNullable(tenant)
    //				.map(TenantResponseDto::getSchema)
    //				.orElseThrow(() -> new RuntimeException("tenant model without schema name -> " + tenant.getTenantName()));
    //	}

}
