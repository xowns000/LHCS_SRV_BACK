package kr.co.hkcloud.palette3.file.constraint;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileGroupKeyValidator.class)
public @interface FileGroupKeyConstraint {
    String message() default "파일(그룹)키가 유효하지 않습니다.";


    Class<?>[] groups() default {};


    Class<? extends Payload>[] payload() default {};
}
