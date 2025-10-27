package kr.co.hkcloud.palette3.setting.system.util;


import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import lombok.extern.slf4j.Slf4j;


/**
 * 사용자별권한관리 Validation체크
 * 
 * @author R&D
 */
@Slf4j
@Component
public class SettingSystemAuthorityByAgentManageValidator implements Validator
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
                switch(objJsonParams.getHeaderString("SERVICE"))
                {
                    case "setting.system.author-by-agent-manage.asgn-info-by-agent":
                    {
                        selectValidate(target, errors);
                        break;
                    }
                    default: // SERVICE값이 없을 때
                    {
                        Object[] arg = new Object[1];
                        arg[0] = "SERVICE";
                        errors.reject(PaletteValidationCode.UNDEFINED_SERVICE.getCode(), arg, PaletteValidationCode.UNDEFINED_SERVICE.getMessage());
                        break;
                    }
                }
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


    // 조회 유효성 검사
    public void selectValidate(Object target, Errors errors)
    {
        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.isBlank(objJsonParams.getString("ATRT_GROUP_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "ATRT_GROUP_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
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

    }

}
