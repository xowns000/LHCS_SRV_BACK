package kr.co.hkcloud.palette3.config.datasources.datasource.external;


import org.springframework.stereotype.Repository;

import java.lang.annotation.*;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repository
public @interface ExternalConnMapper {
    String value() default "";
}
