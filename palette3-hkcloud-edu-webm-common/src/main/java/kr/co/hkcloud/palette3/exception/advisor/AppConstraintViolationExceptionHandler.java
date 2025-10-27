package kr.co.hkcloud.palette3.exception.advisor;


import javax.validation.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kr.co.hkcloud.palette3.core.model.PaletteApiResponse;
import kr.co.hkcloud.palette3.exception.model.ErrorCode;
import kr.co.hkcloud.palette3.exception.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;


/**
 * 데이터 유효성 검사 시 실패가 발생하면 ConstraintViolationException을 발생시킨다.
 * 
 * @author leeiy
 *
 */
@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AppConstraintViolationExceptionHandler
{
    /**
     * 유효성 검사 실패 시 발생하는 예외를 처리
     * 
     * @param  exception
     * @return
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ResponseEntity<PaletteApiResponse<?>> handleConstraintViolationException(final ConstraintViolationException exception)
    {
        log.error("AppExceptionHandler::handleConstraintViolationException", exception);

        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, exception); // 오류 응답을 생성
        return new ResponseEntity<PaletteApiResponse<?>>(PaletteApiResponse.res(errorResponse), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
