package kr.co.hkcloud.palette3.config.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * packageName    : kr.co.hkcloud.palette3.config.aspect
 * fileName       : SystemEventAspectAnotation
 * author         : KJD
 * date           : 2023-12-26
 * description    : 주요 시스템설정 변경( 등록,수정,삭제) 로깅관련 처리하기 위함.
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-12-26        KJD       최초 생성
 * </pre>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemEventLogAspectAnotation {

    String value() default "none";  // 시스템설정 로그 코드

    String note() default "";  // 시스템설정 로그 코드

    boolean isSaveParameter() default true; // parameter이 긴 경우 저장하지 않기 위한 여부 용도.
}
