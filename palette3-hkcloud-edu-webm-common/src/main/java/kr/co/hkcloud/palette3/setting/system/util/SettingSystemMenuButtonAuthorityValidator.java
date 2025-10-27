package kr.co.hkcloud.palette3.setting.system.util;


import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.netty.util.internal.StringUtil;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;


/**
 * 메뉴버튼권한 Validation
 * 
 * @author R&D
 */
@Slf4j
@Component
public class SettingSystemMenuButtonAuthorityValidator implements Validator
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

        if(objJsonParams.getHeaderString("TYPE").toString().equals("XML_SERVICE")) {
            Object[] arg = new Object[1];
            arg[0] = "SQL_NM";
            errors.reject(PaletteValidationCode.UNDEFINED_METHOD.getCode(), arg, PaletteValidationCode.UNDEFINED_METHOD.getMessage());
        }
        else if(objJsonParams.getHeaderString("TYPE").toString().equals("BIZ_SERVICE")) {
            if(objJsonParams.getHeaderString("METHOD").toString().equals("regist")) {
                proecssBtnValidate(target, errors);
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


    //메뉴버튼 처리유효성
    public void proecssBtnValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;
        JSONArray objArry = objJsonParams.getDataObject();

        if(!objArry.isEmpty()) {
            for(int i = 0; i < objArry.size(); i++) {
                if(objJsonParams.getString("DATA_FLAG", i).equals(TwbCmmnConst.TRANS_DEL)) {
                    if(StringUtils.isBlank(objJsonParams.getString("ATRT_GROUP_ID", i))) {
                        Object[] arg = new Object[1];
                        arg[0] = "ATRT_GROUP_ID";
                        errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
                    }
                    if(StringUtil.length(objJsonParams.getString("ATRT_GROUP_ID", i)) < 0 || StringUtil.length(objJsonParams.getString("ATRT_GROUP_ID", i)) > 30) {
                        Object[] arg = new Object[1];
                        arg[0] = "ATRT_GROUP_ID";
                        errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
                    }
                    if(StringUtil.length(objJsonParams.getString("MENU_ID", i)) < 0 || StringUtil.length(objJsonParams.getString("MENU_ID", i)) > 90) {
                        Object[] arg = new Object[1];
                        arg[0] = "MENU_ID";
                        errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
                    }
                }
                else if(objJsonParams.getString("DATA_FLAG", i).equals(TwbCmmnConst.TRANS_INS)) {
                    if(StringUtils.isBlank(objJsonParams.getString("ATRT_GROUP_ID", i))) {
                        Object[] arg = new Object[1];
                        arg[0] = "ATRT_GROUP_ID";
                        errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
                    }
                    if(StringUtils.isBlank(objJsonParams.getString("MENU_ID", i))) {
                        Object[] arg = new Object[1];
                        arg[0] = "MENU_ID";
                        errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
                    }
                    if(StringUtils.isBlank(objJsonParams.getString("BTN_ID", i))) {
                        Object[] arg = new Object[1];
                        arg[0] = "BTN_ID";
                        errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
                    }
                    if(StringUtils.isBlank(objJsonParams.getString("REGR_ID", i))) {
                        Object[] arg = new Object[1];
                        arg[0] = "REGR_ID";
                        errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
                    }
                    if(StringUtil.length(objJsonParams.getString("ATRT_GROUP_ID", i)) < 0 || StringUtil.length(objJsonParams.getString("ATRT_GROUP_ID", i)) > 30) {
                        Object[] arg = new Object[1];
                        arg[0] = "ATRT_GROUP_ID";
                        errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
                    }
                    if(StringUtil.length(objJsonParams.getString("BTN_ID", i)) < 0 || StringUtil.length(objJsonParams.getString("BTN_ID", i)) > 30) {
                        Object[] arg = new Object[1];
                        arg[0] = "BTN_ID";
                        errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
                    }
                    if(StringUtil.length(objJsonParams.getString("REGR_ID", i)) < 0 || StringUtil.length(objJsonParams.getString("REGR_ID", i)) > 20) {
                        Object[] arg = new Object[1];
                        arg[0] = "REGR_ID";
                        errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
                    }
                }
            }
        }
    }
}
