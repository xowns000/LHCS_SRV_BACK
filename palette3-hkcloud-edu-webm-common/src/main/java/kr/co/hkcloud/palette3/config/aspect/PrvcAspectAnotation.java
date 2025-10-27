package kr.co.hkcloud.palette3.config.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 개인정보관련 컨트롤러 API에 명시한다.
 * 개인정보 등록,수정,삭제,조회 로깅관련 처리하기 위함.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PrvcAspectAnotation
{
    /* TASK_SE_CD 값 활용 ( none => /api/palette/common/prvcInqLog 호출 하며 DATA[0][TASK_SE_CD] Param으로 전달해야함. ) */
    String value() default "none";
}
