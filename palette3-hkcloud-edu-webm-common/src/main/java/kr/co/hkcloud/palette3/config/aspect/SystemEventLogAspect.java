package kr.co.hkcloud.palette3.config.aspect;

import kr.co.hkcloud.palette3.config.aspect.util.AspectSystemEventLoggerUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * packageName    : kr.co.hkcloud.palette3.config.aspect
 * fileName       : SystemEventAspect
 * author         : KJD
 * date           : 2023-12-26
 * description    : 시스템설정 관련 로깅처리하기 위함. (@SystemEventLogAspectAround 감시)
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-12-26        KJD       최초 생성
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
@Order(200)
public class SystemEventLogAspect {

    private final AspectSystemEventLoggerUtils systemEventLoggerUtils;

    @Around("@annotation(kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation)")
    public Object processAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        // 처리결과 정보
        Object result = new Object();
        try {
            result = proceedingJoinPoint.proceed();
            MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
            SystemEventLogAspectAnotation eventAnnotation = methodSignature.getMethod().getAnnotation(SystemEventLogAspectAnotation.class);
            if (eventAnnotation != null) {
                systemEventLoggerUtils.processingLog(result, eventAnnotation); // 로깅처리 , eventAnnotation.value() ==> CUTT_TYPE_PROC .... 등등
            }
        } catch (MethodArgumentTypeMismatchException ex) {
            log.error("MethodArgumentTypeMismatchException", ex);
        } catch (Exception ex) {
            log.error("handleException", ex);
            Throwable cause = ex;
            while (cause.getCause() != null) {
                cause = cause.getCause();
            }
            String mstrExceptMsg = cause.getMessage();

            String message = new StringBuffer("{\"HEADER\":{\"ERROR_FLAG\":true,\"ERROR_MSG\":\"").append(mstrExceptMsg)
                .append("\"},\"DATA\":[{}]}").toString();
            log.debug("message ->\n{}", message.toString());
            result = (Object) new ResponseEntity<String>(message, HttpStatus.OK);
        }
        return result;
    }

}
