package kr.co.hkcloud.palette3.chat.script.util;


import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;


@Slf4j
@Component
public class ChatScriptManageValidator implements Validator
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

            if(objJsonParams.getHeaderString("METHOD").toString().equals("regist") || objJsonParams.getHeaderString("METHOD").toString().equals("modify")) {

                processValidate(target, errors);

            }
            else if(objJsonParams.getHeaderString("METHOD").toString().equals("list")) {

                selectValidate(target, errors);
            }
            else if(objJsonParams.getHeaderString("METHOD").toString().equals("delete")) {
                deleteValidate(target, errors);
            }

            else {
                Object[] arg = new Object[1];
                arg[0] = "METHOD";
                errors.reject(PaletteValidationCode.UNDEFINED_METHOD.getCode(), arg, PaletteValidationCode.UNDEFINED_METHOD.getMessage());
            }
        }
        else {
            Object[] arg = new Object[1];
            arg[0] = "METHOD";
            errors.reject(PaletteValidationCode.UNDEFINED_METHOD.getCode(), arg, PaletteValidationCode.UNDEFINED_METHOD.getMessage());
        }
    }


    //스크립트관리 조회유효성
    public void selectValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.length(objJsonParams.getString("CUSTCO_ID")) < 0 || StringUtils.length(objJsonParams.getString("CUSTCO_ID")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("SCRIPT_TIT")) < 0 || StringUtils.length(objJsonParams.getString("SCRIPT_TIT")) > 600) {
            Object[] arg = new Object[1];
            arg[0] = "SCRIPT_TIT";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("USE_YN")) < 0 || StringUtils.length(objJsonParams.getString("USE_YN")) > 1) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("SEARCH_INQRY_TYP_CD")) < 0 || StringUtils.length(objJsonParams.getString("SEARCH_INQRY_TYP_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "SEARCH_INQRY_TYP_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("SEARCH_INQRY_TYP_CD_2")) < 0 || StringUtils.length(objJsonParams.getString("SEARCH_INQRY_TYP_CD_2")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "SEARCH_INQRY_TYP_CD_2";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("CUSTCO_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
    }


    //스크립트관리 처리 유효성
    public void processValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.length(objJsonParams.getString("CUSTCO_ID")) < 0 || StringUtils.length(objJsonParams.getString("CUSTCO_ID")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("SCRIPT_ID")) < 0 || StringUtils.length(objJsonParams.getString("SCRIPT_ID")) > 90) {
            Object[] arg = new Object[1];
            arg[0] = "SCRIPT_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("SCRIPT_TIT")) < 0 || StringUtils.length(objJsonParams.getString("SCRIPT_TIT")) > 600) {
            Object[] arg = new Object[1];
            arg[0] = "SCRIPT_TIT";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("USER_ID")) < 0 || StringUtils.length(objJsonParams.getString("USER_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("ORD_SEQ")) < 0 || StringUtils.length(objJsonParams.getString("ORD_SEQ")) > 11) {
            Object[] arg = new Object[1];
            arg[0] = "ORD_SEQ";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("SCRIPT_RMK")) < 0 || StringUtils.length(objJsonParams.getString("SCRIPT_RMK")) > TwbCmmnConst.LIMITED_CHARACTERS_1000) {
            Object[] arg = new Object[1];
            arg[0] = "SCRIPT_RMK";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("USE_YN")) < 0 || StringUtils.length(objJsonParams.getString("USE_YN")) > 1) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("INQRY_TYP_CD")) < 0 || StringUtils.length(objJsonParams.getString("INQRY_TYP_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "INQRY_TYP_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("INQRY_TYP_CD_2")) < 0 || StringUtils.length(objJsonParams.getString("INQRY_TYP_CD_2")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "INQRY_TYP_CD_2";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("SCRIPT_AUTH_TYPE")) < 0 || StringUtils.length(objJsonParams.getString("SCRIPT_AUTH_TYPE")) > 30) {
            Object[] arg = new Object[1];
            arg[0] = "SCRIPT_AUTH_TYPE";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("LAST_USER_ID")) < 0 || StringUtils.length(objJsonParams.getString("LAST_USER_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "LAST_USER_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("CUSTCO_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("SCRIPT_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "SCRIPT_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("USER_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("SCRIPT_TIT"))) {
            Object[] arg = new Object[1];
            arg[0] = "SCRIPT_TIT";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("ORD_SEQ"))) {
            Object[] arg = new Object[1];
            arg[0] = "ORD_SEQ";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("USE_YN"))) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("SCRIPT_RMK"))) {
            Object[] arg = new Object[1];
            arg[0] = "SCRIPT_RMK";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
    }


    //스크립트관리 삭제 유효성
    public void deleteValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        JSONArray objArry = objJsonParams.getDataObject();
        int objArrSize = objArry.size();

        for(int i = 0; i < objArrSize; i++) {
            if(StringUtils.length(objArry.getJSONObject(i).getString("SCRIPT_ID")) < 0 || StringUtils.length(objArry.getJSONObject(i).getString("SCRIPT_ID")) > 90) {
                Object[] arg = new Object[1];
                arg[0] = "SCRIPT_ID";
                errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
            }
            if(StringUtils.length(objArry.getJSONObject(i).getString("CUSTCO_ID")) < 0 || StringUtils.length(objArry.getJSONObject(i).getString("CUSTCO_ID")) > 150) {
                Object[] arg = new Object[1];
                arg[0] = "CUSTCO_ID";
                errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
            }
            if(StringUtils.isBlank(objArry.getJSONObject(i).getString("SCRIPT_ID"))) {
                Object[] arg = new Object[1];
                arg[0] = "SCRIPT_ID";
                errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
            }
            if(StringUtils.isBlank(objArry.getJSONObject(i).getString("CUSTCO_ID"))) {
                Object[] arg = new Object[1];
                arg[0] = "CUSTCO_ID";
                errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());

            }
        }
    }
}