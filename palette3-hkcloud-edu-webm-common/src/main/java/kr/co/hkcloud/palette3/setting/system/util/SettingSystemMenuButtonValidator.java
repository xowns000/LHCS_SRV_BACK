package kr.co.hkcloud.palette3.setting.system.util;


import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.netty.util.internal.StringUtil;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import lombok.extern.slf4j.Slf4j;


/**
 * 메뉴버튼 Validation체크
 * 
 * @author R&D
 */
@Slf4j
@Component
public class SettingSystemMenuButtonValidator implements Validator
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
            if(objJsonParams.getHeaderString("METHOD").toString().equals("modify")) {
                updateValidate(target, errors);
            }
            else if(objJsonParams.getHeaderString("METHOD").toString().equals("regist")) {
                insertValidate(target, errors);
            }
            else if(objJsonParams.getHeaderString("METHOD").toString().equals("delete")) {
                deleteBtnValidate(target, errors);
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


    //삽입 유효성
    public void insertValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.isBlank(objJsonParams.getString("BTN_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "BTN_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("MENU_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "MENU_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("SCR_BTN_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "SCR_BTN_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("REGR_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "REGR_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("BTN_TYPE"))) {
            Object[] arg = new Object[1];
            arg[0] = "BTN_TYPE";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("SCR_BTN_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "SCR_BTN_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("SCR_BTN_NM"))) {
            Object[] arg = new Object[1];
            arg[0] = "SCR_BTN_NM";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("BTN_ID")) < 0 || StringUtil.length(objJsonParams.getString("BTN_ID")) > 30) {
            Object[] arg = new Object[1];
            arg[0] = "BTN_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("MENU_ID")) < 0 || StringUtil.length(objJsonParams.getString("MENU_ID")) > 30) {
            Object[] arg = new Object[1];
            arg[0] = "MENU_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("SCR_BTN_ID")) < 0 || StringUtil.length(objJsonParams.getString("SCR_BTN_ID")) > 30) {
            Object[] arg = new Object[1];
            arg[0] = "SCR_BTN_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("SCR_BTN_NM")) < 0 || StringUtil.length(objJsonParams.getString("SCR_BTN_NM")) > 100) {
            Object[] arg = new Object[1];
            arg[0] = "SCR_BTN_NM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("BTN_TYPE")) < 0 || StringUtil.length(objJsonParams.getString("BTN_TYPE")) > 30) {
            Object[] arg = new Object[1];
            arg[0] = "BTN_TYPE";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("REGR_ID")) < 0 || StringUtil.length(objJsonParams.getString("REGR_ID")) > 20) {
            Object[] arg = new Object[1];
            arg[0] = "REGR_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
    }


    //수정 유효성
    public void updateValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.isBlank(objJsonParams.getString("BTN_TYPE"))) {
            Object[] arg = new Object[1];
            arg[0] = "BTN_TYPE";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("SCR_BTN_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "SCR_BTN_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("SCR_BTN_NM"))) {
            Object[] arg = new Object[1];
            arg[0] = "SCR_BTN_NM";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("AMDR_ID")) < 0 || StringUtil.length(objJsonParams.getString("AMDR_ID")) > 20) {
            Object[] arg = new Object[1];
            arg[0] = "AMDR_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("SCR_BTN_ID")) < 0 || StringUtil.length(objJsonParams.getString("SCR_BTN_ID")) > 30) {
            Object[] arg = new Object[1];
            arg[0] = "SCR_BTN_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("SCR_BTN_NM")) < 0 || StringUtil.length(objJsonParams.getString("SCR_BTN_NM")) > 100) {
            Object[] arg = new Object[1];
            arg[0] = "SCR_BTN_NM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("BTN_TYPE")) < 0 || StringUtil.length(objJsonParams.getString("BTN_TYPE")) > 30) {
            Object[] arg = new Object[1];
            arg[0] = "BTN_TYPE";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("BTN_ID")) < 0 || StringUtil.length(objJsonParams.getString("BTN_ID")) > 30) {
            Object[] arg = new Object[1];
            arg[0] = "BTN_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

    }


    //삭제 유효성
    public void deleteBtnValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.isBlank(objJsonParams.getString("BTN_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "BTN_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
    }
}
