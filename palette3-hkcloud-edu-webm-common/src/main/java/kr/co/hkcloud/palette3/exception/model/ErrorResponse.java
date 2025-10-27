package kr.co.hkcloud.palette3.exception.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * 
 * @author Orange
 *
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse
{
    private String           code;
    private String           message;
    private int              status;
    private List<FieldError> errors;


    private ErrorResponse(final ErrorCode code, final List<FieldError> errors) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.errors = errors;
        this.code = code.getCode();
    }


    private ErrorResponse(final ErrorCode code) {
        this.status = code.getStatus();
        this.message = code.getMessage();
        this.code = code.getCode();
        this.errors = new ArrayList<>();
    }
    
    private ErrorResponse(final ErrorCode code, final String message) {
        this.status = code.getStatus();
        this.message = message;
        this.code = code.getCode();
        this.errors = new ArrayList<>();
    }


    public static ErrorResponse of(final ErrorCode code)
    {
        return new ErrorResponse(code);
    }


    public static ErrorResponse of(final ErrorCode code, final BindingResult bindingResult)
    {
        return new ErrorResponse(code, FieldError.of(bindingResult));
    }


    public static ErrorResponse of(final ErrorCode code, final ConstraintViolationException constraintViolationException)
    {
        return new ErrorResponse(code, FieldError.of(constraintViolationException));
    }


    public static ErrorResponse of(final ErrorCode code, final List<FieldError> errors)
    {
        return new ErrorResponse(code, errors);
    }


    public static ErrorResponse of(MethodArgumentTypeMismatchException e)
    {
        final String value = e.getValue() == null ? "" : e.getValue().toString();
        final List<ErrorResponse.FieldError> errors = ErrorResponse.FieldError.of(e.getName(), value, e.getErrorCode());
        return new ErrorResponse(ErrorCode.INVALID_TYPE_VALUE, errors);
    }
    
    public static ErrorResponse of(final ErrorCode code, final String message)
    {
        return new ErrorResponse(code, message);
    }



    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError
    {
        private String field;
        private String value;
        private String reason;


        private FieldError(final String field, final String value, final String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }


        public static List<FieldError> of(final String field, final String value, final String reason)
        {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, value, reason));
            return fieldErrors;
        }


        private static List<FieldError> of(final BindingResult bindingResult)
        {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream().map(error ->

            new FieldError(error.getField(), error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(), error.getDefaultMessage())).collect(Collectors.toList());
        }


        private static List<FieldError> of(final ConstraintViolationException constraintViolationException)
        {
            final Set<ConstraintViolation<?>> violationIterator = constraintViolationException.getConstraintViolations();
            return violationIterator.stream().map(constraintViolation ->

            new FieldError(getPopertyName(constraintViolation.getPropertyPath().toString())  // 유효성 검사가 실패한 속성
                , constraintViolation.getInvalidValue() == null ? "" : constraintViolation.getInvalidValue().toString()  // 유효하지 않은 값
                , constraintViolation.getMessage())  // 유효성 검사 실패 시 메시지

            ).collect(Collectors.toList());
        }


        private static String getPopertyName(String propertyPath)
        {
            return propertyPath.substring(propertyPath.lastIndexOf('.') + 1); // 전체 속성 경로에서 속성 이름만 가져온다.
        }
    }
}
