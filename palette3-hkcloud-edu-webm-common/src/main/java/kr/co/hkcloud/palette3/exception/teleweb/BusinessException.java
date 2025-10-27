package kr.co.hkcloud.palette3.exception.teleweb;

import kr.co.hkcloud.palette3.exception.model.ErrorCode;

/**
 * 
 * @author jangh
 *
 */
public class BusinessException extends RuntimeException
{
    /**
     * 
     */
    private static final long serialVersionUID = 8612931320766960277L;

    private ErrorCode errorCode;


    public BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }


    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }


    public ErrorCode getErrorCode()
    {
        return errorCode;
    }
}
