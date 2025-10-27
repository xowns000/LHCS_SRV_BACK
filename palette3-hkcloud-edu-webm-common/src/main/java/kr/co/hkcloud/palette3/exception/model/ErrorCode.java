package kr.co.hkcloud.palette3.exception.model;


import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


/**
 * 
 * @author Orange
 *
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode
{
    //OK
    OK(HttpStatus.OK.value(), "C000", "OK" ),

    //COMMON
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST.value(), "C001", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED.value(), "C002", "Invalid Input Value"),
    ENTITY_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "C003", "Entity Not Found"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "C004", "Server Error"),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST.value(), "C005", "Invalid Type Value"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "C006","접근 거부되었습니다.(Access is Denied)"),

    X_ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "X403","접근(v3-api) 거부되었습니다.(Invalid Token)"),
    X_ACCESS_EXPIRED(HttpStatus.UNAUTHORIZED.value(), "X401",  "접근(v3-api) 거부되었습니다.(Expired Token)" ),

    //JWT
    ACCESS_TOKEN_INVALID(HttpStatus.UNAUTHORIZED.value(), "C901",  "잘못된 인증정보 입니다.(Invalid JWT Token)" ),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED.value(), "C902",  "인증정보가 만료 되었습니다.(Expired JWT Token)" ),
    ACCESS_TOKEN_UNKNOWN(HttpStatus.UNAUTHORIZED.value(), "C903",  "알수 없는 인증정보 입니다.(Unknown JWT Token)" ),
    ACCESS_TOKEN_UNSUPPORTED(HttpStatus.UNAUTHORIZED.value(), "C904",  "지원하지 않는 인증정보 입니다.(Unsupported JWT Token)" ),

    ;


    private final int    status;
    private final String code;
    private final String message;
}
