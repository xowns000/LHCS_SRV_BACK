package kr.co.hkcloud.palette3.infra.palette.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import kr.co.hkcloud.palette3.exception.teleweb.TelewebUtilException;


/**
 * Validator
 * 
 * @author liy
 */
public class TalkValidationUtils extends ValidationUtils
{

    /**
     * Object(filed)에서 해당 데이터를 정수형으로 변환한다.
     * 
     * @param  errors
     * @param  field
     * @return
     * @throws TelewebUtilException
     */
    public static int getInt(Errors errors, String field)
    {
        return Integer.parseInt(errors.getFieldValue(field).toString());
    }


    /**
     * Object(filed)에서 해당 데이터를 문자열로 변환한다.
     * 
     * @param  errors
     * @param  field
     * @return
     * @throws TelewebUtilException
     */
    public static String getString(Errors errors, String field)
    {
        return errors.getFieldValue(field).toString();
    }


    /**
     * error 코드를 global로 지정한다.
     * 
     * @param errors
     * @param msgKey
     */
    public static void reject(Errors errors, String msgKey)
    {
        errors.reject(msgKey);
    }


    /**
     * error 코드와 swap 문자열이 존재 할 경우
     * 
     * @param errors
     * @param msgKey
     * @param arguments
     */
    public static void reject(Errors errors, String msgKey, Object[] arguments)
    {
        errors.reject(msgKey, arguments, null);
    }


    /**
     * error 코드와 field 를 직접 지정
     * 
     * @param errors
     * @param field
     * @param msgKey
     */
    public static void rejectValue(Errors errors, String field, String msgKey)
    {
        errors.rejectValue(field, msgKey, null, null);
    }


    /**
     * error 코드와 field 를 직접 지정하며 swap 문자열이 존재 할 경우
     * 
     * @param errors
     * @param field
     * @param msgKey
     * @param arguments
     */
    public static void rejectValue(Errors errors, String field, String msgKey, Object[] arguments)
    {
        errors.rejectValue(field, msgKey, arguments, null);
    }


    // ====================================================================================================
    // Number
    // ====================================================================================================
    /**
     * 숫자형 데이터인지 체크한다.
     * 
     * @param errors
     * @param field
     * @param msgKey
     */
    public static void rejectIsNumber(Errors errors, String field, String msgKey)
    {
        try {
            String value = getString(errors, field);

            Pattern pattern = Pattern.compile("[^\\d]");
            Matcher matcher = pattern.matcher(value);
            if(matcher.find()) {
                rejectValue(errors, field, msgKey);
            }
        }
        catch(Exception e) {
            rejectValue(errors, field, msgKey);
        }
    }


    /**
     * eml형식인지 체크한다.
     * 
     * @param errors
     * @param field
     * @param msgKey
     */
    public static void rejectIsEmail(Errors errors, String field, String msgKey)
    {
        try {
            String value = getString(errors, field);
            //PATCH-Bug: The regular expression "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$" is vulnerable to a denial of service attack (ReDOS)
//            Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            Pattern pattern = Pattern.compile("\\\\w+@\\\\w+\\\\.\\\\w+(\\\\.\\\\w+)?");
            Matcher matcher = pattern.matcher(value);

            if(!matcher.find()) {
                rejectValue(errors, field, msgKey);
            }
        }
        catch(Exception e) {
            rejectValue(errors, field, msgKey);
        }
    }


    // ====================================================================================================
    // String
    // ====================================================================================================
    /**
     * 문자열의 길이를 체크한다. (maxlength)
     * 
     * @param errors
     * @param field
     * @param msgKey
     * @param max
     */
    public static void rejectMaxlength(Errors errors, String field, String msgKey, int max)
    {
        try {
            String value = getString(errors, field);
            if(value.length() > max) {
                rejectValue(errors, field, msgKey);
            }

        }
        catch(Exception e) {
            rejectValue(errors, field, msgKey);
        }
    }


    /**
     * 문자열의 길이를 체크(최소길이)
     * 
     * @param errors
     * @param field
     * @param msgKey
     * @param min
     */
    public static void rejectMinlength(Errors errors, String field, String msgKey, int min)
    {
        try {
            String value = getString(errors, field);
            if(value.length() <= min) {
                rejectValue(errors, field, msgKey);
            }

        }
        catch(Exception e) {
            rejectValue(errors, field, msgKey);
        }
    }


    /**
     * 문자열의 길이가 다른지 체크한다.
     * 
     * @param errors
     * @param field
     * @param msgKey
     * @param length
     */
    public static void rejectNotEqualslength(Errors errors, String field, String msgKey, int length)
    {
        try {
            String value = getString(errors, field);
            if(value.length() != length) {
                rejectValue(errors, field, msgKey);
            }
        }
        catch(Exception e) {
            rejectValue(errors, field, msgKey);
        }
    }


    /**
     * 데이터에 해당하는 데이터만 있는지 체크한다.
     * 
     * @param errors
     * @param field
     * @param msgKey
     * @param chkPattern
     */
    public static void rejectExistsValue(Errors errors, String field, String msgKey, String chkPattern)
    {
        try {
            String value = getString(errors, field);
            Pattern pattern = Pattern.compile(chkPattern);
            Matcher matcher = pattern.matcher(value);
            if(!matcher.find()) {
                rejectValue(errors, field, msgKey);
            }
        }
        catch(Exception e) {
            rejectValue(errors, field, msgKey);
        }
    }
}
