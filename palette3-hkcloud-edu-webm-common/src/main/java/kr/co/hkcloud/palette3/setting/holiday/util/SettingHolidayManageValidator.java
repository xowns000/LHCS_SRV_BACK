package kr.co.hkcloud.palette3.setting.holiday.util;


import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;


/**
 * 휴일관리 Validation체크
 * 
 * @author R&D
 */
@Slf4j
@Component
public class SettingHolidayManageValidator implements Validator
{
    @Override
    public boolean supports(Class<?> clazz)
    {
        return TelewebJSON.class.equals(clazz);
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
            if(objJsonParams.getHeaderString("METHOD").toString().equals("delete")) {
                deleteValidate(target, errors);
            }
            else if(objJsonParams.getHeaderString("METHOD").toString().equals("regist")) {
                insertValidate(target, errors);
            }
            else if(objJsonParams.getHeaderString("METHOD").toString().equals("modify")) {
                updateValidate(target, errors);
            }
            else if(objJsonParams.getHeaderString("METHOD").toString().equals("list")) {
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


    //조회유효성
    public void selectValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

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
        if(StringUtils.length(objJsonParams.getString("SEARCH_YYYY")) < 0 || StringUtils.length(objJsonParams.getString("SEARCH_YYYY")) > 4) {
            Object[] arg = new Object[1];
            arg[0] = "SEARCH_YYYY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("SEARCH_HOLIDAY_GB_CD")) < 0 || StringUtils.length(objJsonParams.getString("SEARCH_HOLIDAY_GB_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "SEARCH_HOLIDAY_GB_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
    }


    //삽입 유효성
    public void insertValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.length(objJsonParams.getString("CUSTCO_ID")) < 0 || StringUtils.length(objJsonParams.getString("CUSTCO_ID")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("TALK_HOLIDAY_ID")) < 0 || StringUtils.length(objJsonParams.getString("TALK_HOLIDAY_ID")) > 90) {
            Object[] arg = new Object[1];
            arg[0] = "TALK_HOLIDAY_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("HOLIDAY_DT")) < 0 || StringUtils.length(objJsonParams.getString("HOLIDAY_DT")) > 24) {
            Object[] arg = new Object[1];
            arg[0] = "HOLIDAY_DT";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("HOLIDAY_NM")) < 0 || StringUtils.length(objJsonParams.getString("HOLIDAY_NM")) > 300) {
            Object[] arg = new Object[1];
            arg[0] = "HOLIDAY_NM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("HOLIDAY_GB_CD")) < 0 || StringUtils.length(objJsonParams.getString("HOLIDAY_GB_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "HOLIDAY_GB_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("HOLIDAY_DESC")) < 0 || StringUtils.length(objJsonParams.getString("HOLIDAY_DESC")) > TwbCmmnConst.LIMITED_CHARACTERS_1300) {
            Object[] arg = new Object[1];
            arg[0] = "HOLIDAY_DESC";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("REGR_DEPT_CD")) < 0 || StringUtils.length(objJsonParams.getString("REGR_DEPT_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "REGR_DEPT_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("REGR_ID")) < 0 || StringUtils.length(objJsonParams.getString("REGR_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "REGR_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("AMDR_DEPT_CD")) < 0 || StringUtils.length(objJsonParams.getString("AMDR_DEPT_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "AMDR_DEPT_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("AMDR_ID")) < 0 || StringUtils.length(objJsonParams.getString("AMDR_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "AMDR_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("PROC_ID")) < 0 || StringUtils.length(objJsonParams.getString("PROC_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "PROC_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("CUSTCO_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("TALK_HOLIDAY_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "TALK_HOLIDAY_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("HOLIDAY_DT"))) {
            Object[] arg = new Object[1];
            arg[0] = "HOLIDAY_DT";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("HOLIDAY_NM"))) {
            Object[] arg = new Object[1];
            arg[0] = "HOLIDAY_NM";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("HOLIDAY_GB_CD"))) {
            Object[] arg = new Object[1];
            arg[0] = "HOLIDAY_GB_CD";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
    }


    //수정 유효성
    public void updateValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.length(objJsonParams.getString("CUSTCO_ID")) < 0 || StringUtils.length(objJsonParams.getString("CUSTCO_ID")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("TALK_HOLIDAY_ID")) < 0 || StringUtils.length(objJsonParams.getString("TALK_HOLIDAY_ID")) > 90) {
            Object[] arg = new Object[1];
            arg[0] = "TALK_HOLIDAY_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("HOLIDAY_DT")) < 0 || StringUtils.length(objJsonParams.getString("HOLIDAY_DT")) > 24) {
            Object[] arg = new Object[1];
            arg[0] = "HOLIDAY_DT";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("HOLIDAY_NM")) < 0 || StringUtils.length(objJsonParams.getString("HOLIDAY_NM")) > 300) {
            Object[] arg = new Object[1];
            arg[0] = "HOLIDAY_NM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("HOLIDAY_GB_CD")) < 0 || StringUtils.length(objJsonParams.getString("HOLIDAY_GB_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "HOLIDAY_GB_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("HOLIDAY_DESC")) < 0 || StringUtils.length(objJsonParams.getString("HOLIDAY_DESC")) > TwbCmmnConst.LIMITED_CHARACTERS_1300) {
            Object[] arg = new Object[1];
            arg[0] = "HOLIDAY_DESC";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("AMDR_DEPT_CD")) < 0 || StringUtils.length(objJsonParams.getString("AMDR_DEPT_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "AMDR_DEPT_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("AMDR_ID")) < 0 || StringUtils.length(objJsonParams.getString("AMDR_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "AMDR_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("PROC_ID")) < 0 || StringUtils.length(objJsonParams.getString("PROC_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "PROC_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("CUSTCO_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("TALK_HOLIDAY_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "TALK_HOLIDAY_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("HOLIDAY_DT"))) {
            Object[] arg = new Object[1];
            arg[0] = "HOLIDAY_DT";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("HOLIDAY_NM"))) {
            Object[] arg = new Object[1];
            arg[0] = "HOLIDAY_NM";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("HOLIDAY_GB_CD"))) {
            Object[] arg = new Object[1];
            arg[0] = "HOLIDAY_GB_CD";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
    }


    //삭제 유효성
    public void deleteValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        JSONArray objArry = objJsonParams.getDataObject();
        int objArrSize = objArry.size();

        for(int i = 0; i < objArrSize; i++) {
            if(StringUtils.isBlank(objArry.getJSONObject(i).getString("CUSTCO_ID"))) {
                Object[] arg = new Object[1];
                arg[0] = "CUSTCO_ID";
                errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
            }
            if(StringUtils.isBlank(objArry.getJSONObject(i).getString("TALK_HOLIDAY_ID"))) {
                Object[] arg = new Object[1];
                arg[0] = "TALK_HOLIDAY_ID";
                errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
            }
            if(StringUtils.length(objArry.getJSONObject(i).getString("CUSTCO_ID")) < 0 || StringUtils.length(objArry.getJSONObject(i).getString("CUSTCO_ID")) > 150) {
                Object[] arg = new Object[1];
                arg[0] = "CUSTCO_ID";
                errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
            }
            if(StringUtils.length(objArry.getJSONObject(i).getString("TALK_HOLIDAY_ID")) < 0 || StringUtils.length(objArry.getJSONObject(i).getString("TALK_HOLIDAY_ID")) > 90) {
                Object[] arg = new Object[1];
                arg[0] = "TALK_HOLIDAY_ID";
                errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
            }
        }
    }
}
