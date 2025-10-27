package kr.co.hkcloud.palette3.statistics.chat.util;


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import lombok.extern.slf4j.Slf4j;


/**
 * 통계채팅상담(상담사별) Validation체크
 * 
 * @author 이동욱
 */
@Slf4j
@Component
public class StatisticsChatCounselByAgentValidator implements Validator
{

    @Override
    public boolean supports(Class<?> clazz)
    {
        return false;
    }


    @Override
    public void validate(Object target, Errors errors)
    {
        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(objJsonParams.getHeaderString("TYPE").toString().equals("BIZ_SERVICE")) {

            if(objJsonParams.getHeaderString("METHOD").toString().equals("inqire")) {

                selectValidate(target, errors);
            }
            else {
                Object[] arg = new Object[1];
                arg[0] = "METHOD";
                errors.reject(PaletteValidationCode.UNDEFINED_METHOD.getCode(), arg, PaletteValidationCode.UNDEFINED_METHOD.getMessage());
            }
        }
        else {
            Object[] arg = new Object[1];
            arg[0] = "BIZ_SERVICE";
            errors.reject(PaletteValidationCode.UNDEFINED_TYPE.getCode(), arg, PaletteValidationCode.UNDEFINED_TYPE.getMessage());
        }

    }


    //상담사별상담통계 조회유효성
    public void selectValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(objJsonParams.getString("SEARCH_TYPE").toString().equals("MONTH")) {
            if(StringUtils.isBlank(objJsonParams.getString("SEARCH_MONTH_FROM"))) {
                Object[] arg = new Object[1];
                arg[0] = "SEARCH_MONTH_FROM";
                errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
            }
            if(StringUtils.isBlank(objJsonParams.getString("SEARCH_MONTH_TO"))) {
                Object[] arg = new Object[1];
                arg[0] = "SEARCH_MONTH_TO";
                errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
            }
        }
        else if(objJsonParams.getString("SEARCH_TYPE").toString().equals("DAY")) {
            if(StringUtils.isBlank(objJsonParams.getString("SEARCH_FROM"))) {
                Object[] arg = new Object[1];
                arg[0] = "SEARCH_FROM";
                errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
            }
            if(StringUtils.isBlank(objJsonParams.getString("SEARCH_TO"))) {
                Object[] arg = new Object[1];
                arg[0] = "SEARCH_TO";
                errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
            }
        }
        else if(objJsonParams.getString("SEARCH_TYPE").toString().equals("YEAR")) {
            if(StringUtils.isBlank(objJsonParams.getString("SEARCH_YEAR"))) {
                Object[] arg = new Object[1];
                arg[0] = "SEARCH_YEAR";
                errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
            }
        }
        if(StringUtils.isBlank(objJsonParams.getString("CUSTCO_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("CUSTCO_ID")) < 0 || StringUtils.length(objJsonParams.getString("CUSTCO_ID")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("USER_ID")) < 0 || StringUtils.length(objJsonParams.getString("USER_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("USER_NM")) < 0 || StringUtils.length(objJsonParams.getString("USER_NM")) > 300) {
            Object[] arg = new Object[1];
            arg[0] = "USER_NM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("USER_NICK")) < 0 || StringUtils.length(objJsonParams.getString("USER_NICK")) > 300) {
            Object[] arg = new Object[1];
            arg[0] = "USER_NICK";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("USER_ATTR_A")) < 0 || StringUtils.length(objJsonParams.getString("USER_ATTR_A")) > 20) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ATTR_A";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("USER_ATTR_B")) < 0 || StringUtils.length(objJsonParams.getString("USER_ATTR_B")) > 20) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ATTR_B";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("USER_ATTR_C")) < 0 || StringUtils.length(objJsonParams.getString("USER_ATTR_C")) > 20) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ATTR_C";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("USER_ATTR_D")) < 0 || StringUtils.length(objJsonParams.getString("USER_ATTR_D")) > 20) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ATTR_D";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("SNDR_KEY")) < 0 || StringUtils.length(objJsonParams.getString("SNDR_KEY")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "SNDR_KEY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
    }
}
