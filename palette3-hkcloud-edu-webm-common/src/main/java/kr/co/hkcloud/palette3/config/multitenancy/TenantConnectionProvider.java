package kr.co.hkcloud.palette3.config.multitenancy;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * TenantConnectionProvider 설정
 *
 * entityManagerFactoryBean 에 설정되고,
 * entityManagerFactoryBean은 transactionManager 의 JpaTransactionManager에 연결되어 datasource 에서 커넥션 호출 시  getConnection이 실행된다.
 * TenantIdentifierResolver에서 tenantIdentifier 값을 받아 실행할 db 스키마 값을 변경시킨다.(set schema 실행)
 *
 * @author 황종혁
 */
@RequiredArgsConstructor
@Component
public class TenantConnectionProvider implements MultiTenantConnectionProvider {

    /**
     *
     */
    private static final long serialVersionUID = -4459019017656081379L;
    private final DataSource paletteRoutingLazyDataSource;
    
    @Value("${datasources.datasource.palette.db-vendor}")
    private String dbVendor;
    

    //    public TenantConnectionProvider(DataSource dataSource) {
    //        this.dataSource = dataSource;
    //    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return paletteRoutingLazyDataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        //동작 확인 후 System.out.println 삭제
        final Connection connection = getAnyConnection();
        //SET SCHEMA가 안될 시, service의 method에 @Transaction 어노테이션이 붙어 있는지 확인.(붙어 있어야 함)
        if("POSTGRESQL".equals(dbVendor.toUpperCase())) {
            connection.createStatement().execute(String.format("SET SCHEMA '%s';", tenantIdentifier));
        } else if("ORACLE".equals(dbVendor.toUpperCase())) {
            connection.createStatement().execute(String.format("ALTER SESSION SET CURRENT_SCHEMA = %s", tenantIdentifier.equalsIgnoreCase("custco") ? tenantIdentifier : "C##" + tenantIdentifier));
        }
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {

        //오류 발생 체크를 위해 custco로 변경 후 release
        if("POSTGRESQL".equals(dbVendor.toUpperCase())) {
            connection.createStatement().execute(String.format("SET SCHEMA '%s';", "custco"));
        } else if("ORACLE".equals(dbVendor.toUpperCase())) {
            connection.createStatement().execute(String.format("ALTER SESSION SET CURRENT_SCHEMA = %s", "custco"));
        }
        releaseAnyConnection(connection);
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return null;
    }
}