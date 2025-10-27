package kr.co.hkcloud.palette3.config.datasources.properties;


import java.util.Properties;
import org.springframework.stereotype.Component;


@Component
public class VendorDatabaseIdProperies {

    /**
     * DatabaseMetaData#getDatabaseProductName() 에 의해 리턴된 문자열로 databaseId를 설정한다. http://mybatis.org/spring/factorybean.html
     *
     * @return Properties
     */
    public Properties getDbVendorProperties() {
        Properties dbVendorProperties = new Properties();
        // @formatter:off
        dbVendorProperties.setProperty("MySQL"               , "Mysql");
        dbVendorProperties.setProperty("Oracle"              , "Oracle");
        dbVendorProperties.setProperty("Microsoft SQL Server", "Sqlserver");
        dbVendorProperties.setProperty("DB2"                 , "Db2");
        dbVendorProperties.setProperty("Tibero"              , "Tibero");
        dbVendorProperties.setProperty("PostgreSQL"          , "PostgreSQL");
        // @formatter:on
        return dbVendorProperties;
    }
}
