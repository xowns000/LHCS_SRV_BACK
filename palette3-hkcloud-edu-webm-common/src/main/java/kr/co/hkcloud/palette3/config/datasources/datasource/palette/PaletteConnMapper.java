package kr.co.hkcloud.palette3.config.datasources.datasource.palette;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 멀티테넌시(multitenancy) 처리 : @Transactional 어노테이션 추가- @Transactional 적용되어야 Mapper 클래스에서 TransactionManager 타게 되어 TenantConnectionProvider(set schema ) 타게 됨
 * @author hjh
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repository
@Transactional
public @interface PaletteConnMapper {
    String value() default "";
}
