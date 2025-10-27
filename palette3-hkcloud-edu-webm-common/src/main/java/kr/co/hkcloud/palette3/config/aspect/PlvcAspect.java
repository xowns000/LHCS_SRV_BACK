package kr.co.hkcloud.palette3.config.aspect;


import kr.co.hkcloud.palette3.config.aspect.util.AspectNameUtils;
import kr.co.hkcloud.palette3.config.aspect.util.AspectPrvcLoggerUtils;
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
 * 개인정보 관련 로깅처리하기 위함. (@PrvcAspectAround 감시)
 */
@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
@Order(200)
public class PlvcAspect
{
    private final AspectPrvcLoggerUtils aspectPlvcLoggerUtils;
    private final AspectNameUtils      aspectNameUtils;

    @Around("@annotation(kr.co.hkcloud.palette3.config.aspect.PrvcAspectAnotation)")
    public Object processPlvcLogAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        Object result = new Object();
        try {

            result = proceedingJoinPoint.proceed();

            // anotation value ( = TASK_SE_CD ) 조회
            String taskSeCD = "";
            MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
            PrvcAspectAnotation prvcAnnotation = methodSignature.getMethod().getAnnotation(PrvcAspectAnotation.class);
            if(prvcAnnotation != null) {
                taskSeCD = prvcAnnotation.value();
                aspectPlvcLoggerUtils.processingPrvcInqLog(result, taskSeCD); // 개인정보 관련 로그처리 AOP
            }
        }
        catch(MethodArgumentTypeMismatchException ex) {
            log.error("MethodArgumentTypeMismatchException", ex);
        }
        catch(Exception ex) {
            log.error("handleException", ex);
            Throwable cause = ex;
            while(cause.getCause() != null) {
                cause = cause.getCause();
            }
            String mstrExceptMsg = cause.getMessage();

            String message = new StringBuffer("{\"HEADER\":{\"ERROR_FLAG\":true,\"ERROR_MSG\":\"").append(mstrExceptMsg).append("\"},\"DATA\":[{}]}").toString();
            log.debug("message ->\n{}", message.toString());
            result = (Object) new ResponseEntity<String>(message, HttpStatus.OK);
        }
        return result;
    }

}
