package kr.co.hkcloud.palette3.exception.teleweb;

import kr.co.hkcloud.palette3.exception.model.ErrorCode;

/**
 * 
 * @author jangh
 *
 */
public class InvalidValueException extends BusinessException
{
    /**
     * 
     */
    private static final long serialVersionUID = 8200546628456633457L;


    public InvalidValueException(String value) {
        super(value, ErrorCode.INVALID_INPUT_VALUE);
    }


    public InvalidValueException(String value, ErrorCode errorCode) {
        super(value, errorCode);
    }
}
