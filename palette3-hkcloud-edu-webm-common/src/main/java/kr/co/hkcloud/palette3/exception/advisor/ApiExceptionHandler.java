package kr.co.hkcloud.palette3.exception.advisor;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import kr.co.hkcloud.palette3.config.properties.palette.PaletteProperties;
import kr.co.hkcloud.palette3.core.model.PaletteApiResponse;
import kr.co.hkcloud.palette3.core.security.exception.SecurityInvalidSessionException;
import kr.co.hkcloud.palette3.core.security.exception.SecuritySessionExpiredException;
import kr.co.hkcloud.palette3.exception.PaletteValidationException;
import kr.co.hkcloud.palette3.exception.model.ErrorCode;
import kr.co.hkcloud.palette3.exception.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;


/**
 *
 * @author Orange
 *
 */
@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE + 1000)
public final class ApiExceptionHandler extends ResponseEntityExceptionHandler
{
    @Autowired
    private PaletteProperties paletteProperties;


    /**
     * 400 javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다. HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생 주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("handleMethodArgumentNotValidException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, ex.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("handleHttpMessageNotReadable", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * 400 @ModelAttribut 으로 binding error 발생시 BindException 발생한다. ref https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args
     */
    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        String uri = ((ServletWebRequest)request).getRequest().getRequestURI().toString();
        log.error("handleBindException", ex);
        log.error("handleBindException uri ::: " + uri);
        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, ex); // 오류 응답을 생성
        
        //restful api 방식은 ErrorResponse로 만들어서 json으로 출력함.
        if(uri != null && uri.startsWith("/v3-api/carrier/")) {
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        
        //palette3 내부에서 동작 시 PaletteApiResponse로 만들어서 출력함.
        return new ResponseEntity<>(PaletteApiResponse.res(errorResponse), HttpStatus.BAD_REQUEST);
    }


    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e)
    {
        log.error("handleAccessDeniedException", e);
        String responseString = "{\"HEADER\":{\"ERROR_FLAG\":true,\"AUTH_CHECK_FAIL\":true,\"ERROR_MSG\":\"페이지 접근 권한이 없습니다.\"},\"DATA\":[{}]}";
        return new ResponseEntity<>(responseString, HttpStatus.valueOf(ErrorCode.ACCESS_DENIED.getStatus()));
    }


    /**
     *
     * @param  e
     * @return

    @ExceptionHandler(InvalidCsrfTokenException.class)
    protected ResponseEntity<String> handleInvalidCsrfTokenException(final InvalidCsrfTokenException e, final HttpServletRequest objRequest)
    {
        log.error("handleInvalidCsrfTokenException", e);
        //String responseString = "{\"HEADER\":{\"ERROR_FLAG\":true,\"EXPIRE_SESSION\":true,\"ERROR_MSG\":\"세션정보가 유효하지 않습니다.(" + objRequest.getRequestURI() + ")\"},\"DATA\":[{}]}";
        String responseString = "{\"HEADER\":{\"ERROR_FLAG\":true,\"EXPIRE_SESSION\":true,\"ERROR_MSG\":\"Csrf 정보가 유효하지 않습니다.\"},\"DATA\":[{}]}";
        return new ResponseEntity<>(responseString, HttpStatus.UNAUTHORIZED);
    }*/


    /**
     *
     * @param  e
     * @return
     */
    @ExceptionHandler(InsufficientAuthenticationException.class)
    protected ResponseEntity<String> handleInsufficientAuthenticationException(final InsufficientAuthenticationException e, final HttpServletRequest objRequest)
    {
        log.error("handleInsufficientAuthenticationException", e);
        String responseString = "{\"HEADER\":{\"ERROR_FLAG\":true,\"EXPIRE_SESSION\":true,\"ERROR_MSG\":\"인증 정보가 유효하지 않습니다.(" + objRequest.getRequestURI() + ")\"},\"DATA\":[{}]}";
        return new ResponseEntity<>(responseString, HttpStatus.UNAUTHORIZED);
    }


    /**
     *
     * @param  e
     * @return
     */
    @ExceptionHandler(SecurityInvalidSessionException.class)
    protected ResponseEntity<String> handleInvalidSessionException(final SecurityInvalidSessionException e, final HttpServletRequest objRequest)
    {
        log.error("handleSecurityInvalidSessionException", e);
        String responseString = "{\"HEADER\":{\"ERROR_FLAG\":true,\"EXPIRE_SESSION\":true,\"ERROR_MSG\":\"세션이 유효하지 않습니다.\"},\"DATA\":[{}]}";
        return new ResponseEntity<>(responseString, HttpStatus.UNAUTHORIZED);
    }


    /**
     *
     * @param  e
     * @return
     */
    @ExceptionHandler(SecuritySessionExpiredException.class)
    protected ResponseEntity<String> handleSecuritySessionExpiredException(final SecuritySessionExpiredException e, final HttpServletRequest objRequest)
    {
        log.error("handleSecuritySessionExpiredException", e);
        String responseString = "{\"HEADER\":{\"ERROR_FLAG\":true,\"EXPIRE_SESSION\":true,\"ERROR_MSG\":\"세션이 만료되었습니다.\"},\"DATA\":[{}]}";
        return new ResponseEntity<>(responseString, HttpStatus.UNAUTHORIZED);
    }


    /**
     * 400
     *
     * @param  e
     * @return
     */
    @ExceptionHandler({PaletteValidationException.class})
    protected ResponseEntity<String> handlePaletteValidationException(final PaletteValidationException e, final HttpServletRequest objRequest)
    {
        log.error("ApiExceptionHandler::PaletteValidationException {}", e.getListObjError());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * 500
     *
     * @param  e
     * @return
     */
    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handleException(Exception ex, final HttpServletRequest objRequest)
    {
        String uri = objRequest.getRequestURI();
        log.error("ApiExceptionHandler::handleException", ex);
        log.error("ApiExceptionHandler::handleException uri ::: ", uri);
        String mstrExceptMsg;
        Throwable cause = ex;
        while(cause.getCause() != null) {
            cause = cause.getCause();
        }

        switch(paletteProperties.getServiceMode())
        {
            case REAL:
                mstrExceptMsg = "시스템 내부 오류가 발생하였습니다.\\n잠시 후에 접속하시기 바랍니다.";
                break;
            default:
                mstrExceptMsg = cause.getMessage();
        }
        
        //restful api 방식은 ErrorResponse로 만들어서 json으로 출력함.
        if(uri != null && uri.startsWith("/v3-api/carrier/")) {
            final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, mstrExceptMsg);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        //palette3 내부에서 동작 시 출력 방식.
        String responseString = "<script type=\"text/javascript\" src=\"/common/js/sweetalert.js\"></script><script>window.onload = function () { swal(\"%s\",\"서버 오류\",\"error\");}</script>";
        responseString = String.format(responseString, mstrExceptMsg);
        return new ResponseEntity<>(responseString, HttpStatus.OK);
    }
}
