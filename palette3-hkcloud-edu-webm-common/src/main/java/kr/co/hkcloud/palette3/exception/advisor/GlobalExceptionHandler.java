package kr.co.hkcloud.palette3.exception.advisor;


import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
//import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.model.ErrorCode;
import kr.co.hkcloud.palette3.exception.model.ErrorResponse;
import kr.co.hkcloud.palette3.exception.teleweb.BusinessException;
import lombok.extern.slf4j.Slf4j;


/**
 * 
 * @author Orange
 *
 */
@Slf4j
@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalExceptionHandler
{
    /**
     * 400 enum type 일치하지 않아 binding 못할 경우 발생 주로 @RequestParam enum으로 binding 못했을 경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e)
    {
        log.error("handleMethodArgumentTypeMismatchException", e);
        final ErrorResponse response = ErrorResponse.of(e);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    /**
     * 405 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e)
    {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }


    /**
     * 
     * @param  e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e)
    {
        log.error("handleBusinessException", e);
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse response = ErrorResponse.of(errorCode);
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }


    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<TelewebJSON> handleAccessDeniedException(AccessDeniedException e)
    {
        log.error("handleAccessDeniedException", e);
        TelewebJSON responseJson = new TelewebJSON();
        return new ResponseEntity<>(responseJson, HttpStatus.valueOf(ErrorCode.ACCESS_DENIED.getStatus()));
    }


    /**
     * 
     * @param  e
     * @return
    
    @ExceptionHandler(InvalidCsrfTokenException.class)
    protected ResponseEntity<TelewebJSON> handleInvalidCsrfTokenException(final InvalidCsrfTokenException e)
    {
        log.error("handleInvalidCsrfTokenException", e);
        TelewebJSON responseJson = new TelewebJSON();
        return new ResponseEntity<>(responseJson, HttpStatus.UNAUTHORIZED);
    } */


    /**
     * 
     * @param  e
     * @return
     */
    @ExceptionHandler(InsufficientAuthenticationException.class)
    protected ResponseEntity<TelewebJSON> handleInsufficientAuthenticationException(final InsufficientAuthenticationException e)
    {
        log.error("handleInsufficientAuthenticationException", e);
        TelewebJSON responseJson = new TelewebJSON();
        return new ResponseEntity<>(responseJson, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 500
     * 
     * @param  e
     * @return
     */
//    @ExceptionHandler(Exception.class)
//    protected ResponseEntity<ErrorResponse> handleException(Exception e)
//    {
//        log.error("handleException", e);
//        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
//        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//    }


    /**
     * 컨트롤러 호출 시 500 에라 발생 하면 오류 화면 전송
     * 
     * @param  e
     * @return
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<String> handleTalkSignUpException(final Exception e)
    {
        log.error("handleSecuritySessionExpiredException", e);
        String responseString = "<script type=\"text/javascript\" src=\"/common/js/sweetalert.js\"></script><script>window.onload = function () { swal(\"%s\",\"서버 오류가 발생하였습니다.\",\"error\");}</script>";
        responseString = String.format(responseString, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(responseString, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
