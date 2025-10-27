package kr.co.hkcloud.palette3.chat.setting.util;


import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class ChatSettingImageManageValidator implements Validator
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
        else  //BIZ_SERVICE값이 없을 때 
        {
            Object[] arg = new Object[1];
            arg[0] = "BIZ_SERVICE";
            errors.reject(PaletteValidationCode.UNDEFINED_TYPE.getCode(), arg, PaletteValidationCode.UNDEFINED_TYPE.getMessage());
        }
    }


    //상담이미지관리 조회
    public void selectValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.length(objJsonParams.getString("CUSTCO_ID")) < 0 || StringUtils.length(objJsonParams.getString("CUSTCO_ID")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("BRD_ID")) < 0 || StringUtils.length(objJsonParams.getString("BRD_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "BRD_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("START_DT")) < 0 || StringUtils.length(objJsonParams.getString("START_DT")) > 8) {
            Object[] arg = new Object[1];
            arg[0] = "START_DT";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("END_DT")) < 0 || StringUtils.length(objJsonParams.getString("END_DT")) > 8) {
            Object[] arg = new Object[1];
            arg[0] = "END_DT";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("BRD_TIT")) < 0 || StringUtils.length(objJsonParams.getString("BRD_TIT")) > 300) {
            Object[] arg = new Object[1];
            arg[0] = "BRD_TIT";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("CUSTCO_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("BRD_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "BRD_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }

    }

}
