package kr.co.hkcloud.palette3.setting.system.util;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.netty.util.internal.StringUtil;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import lombok.extern.slf4j.Slf4j;


/**
 * 대용량엑셀 Validation체크
 * 
 * @author R&D
 */
@Slf4j
@Component
public class SettingSystemLargeCapacityExcelListValidator implements Validator
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

            if(objJsonParams.getHeaderString("METHOD").toString().equals("list")) {

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


    public void selectValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtil.length(objJsonParams.getString("REQUEST_DT_FROM")) < 0 || StringUtil.length(objJsonParams.getString("REQUEST_DT_FROM")) > 8) {
            Object[] arg = new Object[1];
            arg[0] = "REQUEST_DT_FROM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("REQUEST_DT_TO")) < 0 || StringUtil.length(objJsonParams.getString("REQUEST_DT_TO")) > 8) {
            Object[] arg = new Object[1];
            arg[0] = "REQUEST_DT_TO";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("MENU_INFO")) < 0 || StringUtil.length(objJsonParams.getString("MENU_INFO")) > 600) {
            Object[] arg = new Object[1];
            arg[0] = "MENU_INFO";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("PROCESS_CODE")) < 0 || StringUtil.length(objJsonParams.getString("PROCESS_CODE")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "PROCESS_CODE";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("USER_INFO")) < 0 || StringUtil.length(objJsonParams.getString("USER_INFO")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "USER_INFO";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

    }
}
