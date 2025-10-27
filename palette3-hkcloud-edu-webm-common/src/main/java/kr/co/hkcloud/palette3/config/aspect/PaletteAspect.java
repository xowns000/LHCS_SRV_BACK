package kr.co.hkcloud.palette3.config.aspect;


import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.config.aspect.util.AspectLoggerUtils;
import kr.co.hkcloud.palette3.config.aspect.util.AspectNameUtils;
import kr.co.hkcloud.palette3.config.properties.palette.PaletteProperties;
import kr.co.hkcloud.palette3.exception.model.ErrorCode;
import kr.co.hkcloud.palette3.exception.model.ErrorResponse;
import kr.co.hkcloud.palette3.exception.teleweb.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
@Order(200)
public class PaletteAspect {

    private final AspectLoggerUtils aspectLoggerUtils;
    private final AspectNameUtils aspectNameUtils;
    private final PaletteProperties paletteProperties;


    /**
     * 웹컨트롤러 Advice
     */
    @Around("execution (* kr.co.hkcloud.palette3..web.*Controller.*(..)) && !@annotation(kr.co.hkcloud.palette3.config.aspect.NoAspectAround)")
    public Object webControllerAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String methodpath = aspectNameUtils.getMehodPath(proceedingJoinPoint);
        log.trace("\n===============================================================================\n<!--W E B C O N T R O L L E R  START\n{}\n\n",
            methodpath);

        StopWatch stopWatch = new StopWatch(methodpath);
        /////////////////////////////////////////////////////////////////////////////////
        //BEFORE
        stopWatch.start("before");
        aspectLoggerUtils.webControllerBeforeLog(proceedingJoinPoint);

        Object result = new Object();
        try {
            stopWatch.stop();
            /////////////////////////////////////////////////////////////////////////////////
            //PROCEED
            stopWatch.start("proceed");
            result = proceedingJoinPoint.proceed();

            stopWatch.stop();
            /////////////////////////////////////////////////////////////////////////////////
            //AFTER
            stopWatch.start("after");
            aspectLoggerUtils.webControllerAfterLog(stopWatch, result);

            log.trace("\n\n{}\nW E B C O N T R O L L E R  END-->\n===============================================================================", methodpath);
        } catch(MethodArgumentTypeMismatchException ex) {
            log.error("TeletalkAspect::MethodArgumentTypeMismatchException", ex);
            if(stopWatch.isRunning()) {
                stopWatch.stop();
            }
        } catch(Exception ex) {
            log.error("TeletalkAspect::handleException", ex);
            if(stopWatch.isRunning()) {
                stopWatch.stop();
            }

            //ApiExceptionHandler::handleException을 사용하고자 할 때
            throw ex;

            //서버 오류 페이지를 보여 주고자 할 때 사용
            //result = "error/error";
        }
        return result;
    }

    /**
     * REST컨트롤러 Advice
     */
    @Around("execution (* kr.co.hkcloud.palette3..api.*RestController.*(..)) && !@annotation(kr.co.hkcloud.palette3.config.aspect.NoAspectAround)")
    public Object restControllerAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String methodpath = aspectNameUtils.getMehodPath(proceedingJoinPoint);
        log.trace("\n===============================================================================\n<!--R E S T C O N T R O L L E R  START\n{}\n\n",
            methodpath);

        StopWatch stopWatch = new StopWatch(methodpath);
        /////////////////////////////////////////////////////////////////////////////////
        //BEFORE
        stopWatch.start("before");
        aspectLoggerUtils.restControllerBeforeLog(proceedingJoinPoint);

        stopWatch.stop();

        Object result = new Object();
        try {
            /////////////////////////////////////////////////////////////////////////////////
            //PROCEED
            stopWatch.start("proceed");
            result = proceedingJoinPoint.proceed();

            stopWatch.stop();
            /////////////////////////////////////////////////////////////////////////////////
            //AFTER
            stopWatch.start("after");
            aspectLoggerUtils.restControllerAfterLog(stopWatch, result);

            // 개인정보 관련 로그처리 AOP
            //aspectLoggerUtils.processingPrvcInqLog(result);

            log.trace("\n\n{}\nR E S T C O N T R O L L E R  END-->\n===============================================================================",
                methodpath);
        } catch(MethodArgumentTypeMismatchException ex) {
            log.error("MethodArgumentTypeMismatchException", ex);
            if(stopWatch.isRunning()) {
                stopWatch.stop();
            }
        } catch(Exception ex) {
            log.error("handleException", ex);
            if(stopWatch.isRunning()) {
                stopWatch.stop();
            }
            String mstrExceptMsg;

            Throwable cause = ex;
            while(cause.getCause() != null) {
                cause = cause.getCause();
            }

            switch(paletteProperties.getServiceMode()) {
                case REAL:
                    mstrExceptMsg = "시스템 ---------내부 장애가 발생하였습니다.\\n잠시 후에 접속하시기 바랍니다.";
                    break;
                default:
                    mstrExceptMsg = cause.getMessage();
            }

            String message = new StringBuffer("{\"HEADER\":{\"ERROR_FLAG\":true,\"ERROR_MSG\":\"").append(mstrExceptMsg).append("\"},\"DATA\":[{}]}")
                .toString();
            log.debug("message ->\n{}", message.toString());
            result = (Object) new ResponseEntity<String>(message, HttpStatus.OK);
        }
        return result;
    }


    /**
     *
     */
    @Before("execution (* kr.co.hkcloud.palette3..api.*RestController.*(..)) && !@annotation(kr.co.hkcloud.palette3.config.aspect.NoBizLog) && !@annotation(kr.co.hkcloud.palette3.config.aspect.NoAspectAround)")
    public void restControllerBeforeAdvice(JoinPoint joinPoint) throws Throwable {
        // 로그여부는 옵션 처리 필요
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Optional.ofNullable(requestAttributes).ifPresent(ra -> {

            HttpServletRequest objRequest = ra.getRequest();

            String apiOperationVal = null;

            // api operation value ( 기능명 )
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            ApiOperation apiAnnotation = methodSignature.getMethod().getAnnotation(ApiOperation.class);
            if(apiAnnotation != null) {
                apiOperationVal = apiAnnotation.value();
                log.debug("restControllerBeforeAdvice ApiOperation value ={}", apiOperationVal);
            }
        });
    }

    /**
     * 서비스 Advice
     */
    @Around("execution (* kr.co.hkcloud.palette3..app.*Impl.*(..)) && !@annotation(kr.co.hkcloud.palette3.config.aspect.NoAspectAround)")
    public Object serviceAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String methodpath = aspectNameUtils.getMehodPath(proceedingJoinPoint);
        log.trace("\n===============================================================================\n<!--S E R V I C E  START\n{}\n\n", methodpath);

        StopWatch stopWatch = new StopWatch(methodpath);
        /////////////////////////////////////////////////////////////////////////////////
        //BEFORE
        stopWatch.start("before");
        aspectLoggerUtils.serviceBeforeLog(proceedingJoinPoint);

        stopWatch.stop();
        /////////////////////////////////////////////////////////////////////////////////
        //PROCEED
        stopWatch.start("proceed");
        Object result = proceedingJoinPoint.proceed();

        stopWatch.stop();
        /////////////////////////////////////////////////////////////////////////////////
        //AFTER
        stopWatch.start("after");
        aspectLoggerUtils.serviceAfterLog(stopWatch, result);

        log.trace("\n\n{}\nS E R V I C E  END-->\n===============================================================================", methodpath);
        return result;
    }
    
    
    /**
     * V3 REST API 컨트롤러 Advice - v3-api에서 restful api 규칙을 따를때 error exception 별도 처리.(Carrier API를 restful api로 개발)
     */
    @Around("execution (* kr.co.hkcloud.palette3.v3..api.*RestApiController.*(..)) && !@annotation(kr.co.hkcloud.palette3.config.aspect.NoAspectAround)")
    public Object v3RestApiControllerAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String methodpath = aspectNameUtils.getMehodPath(proceedingJoinPoint);
        log.trace("\n===============================================================================\n<!--R E S T C O N T R O L L E R  START\n{}\n\n",
            methodpath);

        StopWatch stopWatch = new StopWatch(methodpath);
        /////////////////////////////////////////////////////////////////////////////////
        //BEFORE
        stopWatch.start("before");
        aspectLoggerUtils.restControllerBeforeLog(proceedingJoinPoint);

        stopWatch.stop();

        Object result = new Object();
        try {
            /////////////////////////////////////////////////////////////////////////////////
            //PROCEED
            stopWatch.start("proceed");
            result = proceedingJoinPoint.proceed();

            stopWatch.stop();
            /////////////////////////////////////////////////////////////////////////////////
            //AFTER
            stopWatch.start("after");
            aspectLoggerUtils.restControllerAfterLog(stopWatch, result);

            // 개인정보 관련 로그처리 AOP
            //aspectLoggerUtils.processingPrvcInqLog(result);

            log.trace("\n\n{}\nR E S T A P I C O N T R O L L E R  END-->\n===============================================================================",
                methodpath);
        } catch(MethodArgumentTypeMismatchException ex) {
            log.error("MethodArgumentTypeMismatchException", ex);
            if(stopWatch.isRunning()) {
                stopWatch.stop();
            }
        } catch(BusinessException ex) {
            log.error("BusinessException", ex);
            final ErrorResponse response = ErrorResponse.of(ex.getErrorCode(), ex.getMessage());
            result = (Object) new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
        } catch(Exception ex) {
            log.error("handleException", ex);
            if(stopWatch.isRunning()) {
                stopWatch.stop();
            }
            String mstrExceptMsg;

            Throwable cause = ex;
            while(cause.getCause() != null) {
                cause = cause.getCause();
            }

//            switch(paletteProperties.getServiceMode()) {
//                case REAL:
//                    mstrExceptMsg = "시스템 ---------내부 장애가 발생하였습니다.\\n잠시 후에 접속하시기 바랍니다.";
//                    break;
//                default:
                    mstrExceptMsg = cause.getMessage();
//            }
            final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, mstrExceptMsg);
            log.error("v3RestApiControllerAroundAdvice handleException message ->\n{}", response.toString());
            
            result = (Object) new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            
        }
        return result;
    }

}
