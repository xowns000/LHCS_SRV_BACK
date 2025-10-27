package kr.co.hkcloud.palette3.setting.system.util;


import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.netty.util.internal.StringUtil;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;


/**
 * 공통코드관리 Validation체크
 * 
 * @author R&D
 */
@Slf4j
@Component
public class SettingSystemCommonCodeManageValidator implements Validator
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

            if(objJsonParams.getHeaderString("METHOD").toString().equals("regist") || objJsonParams.getHeaderString("METHOD").toString().equals("modify")) {
                processTypeValidate(target, errors);
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
            arg[0] = "BIZ_SERVICE";
            errors.reject(PaletteValidationCode.UNDEFINED_TYPE.getCode(), arg, PaletteValidationCode.UNDEFINED_TYPE.getMessage());
        }
    }


    public void selectValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtil.length(objJsonParams.getString("GROUP_CD")) < 0 || StringUtil.length(objJsonParams.getString("GROUP_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "GROUP_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("USE_YN")) < 0 || StringUtil.length(objJsonParams.getString("USE_YN")) > 1) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("CD_NM")) < 0 || StringUtil.length(objJsonParams.getString("CD_NM")) > 300) {
            Object[] arg = new Object[1];
            arg[0] = "CD_NM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
    }


    //공통 유효성
    public void processTypeValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if("TRNSF_TRGT_CD".equals(objJsonParams.getString("GROUP_CD_ID")) && !objJsonParams.getString("CD_ID").matches("^[1-9][0-9]*$")) {
            Object[] arg = new Object[1];
            arg[0] = "CD_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.IS_NOT_NUMBER.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("GROUP_CD_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "GROUP_CD_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("CD_NM"))) {
            Object[] arg = new Object[1];
            arg[0] = "CD_NM";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("USE_YN"))) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("SYS_CD_YN"))) {
            Object[] arg = new Object[1];
            arg[0] = "SYS_CD_YN";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("USER_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(!objJsonParams.getString("USER_ID").matches("^[1-9][0-9]*$")) {
            Object[] arg = new Object[1];
            arg[0] = "CD_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.IS_NOT_NUMBER.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("CD_ID")) < 0 || StringUtil.length(objJsonParams.getString("CD_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "CD_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("CD_NM")) < 0 || StringUtil.length(objJsonParams.getString("CD_NM")) > 300) {
            Object[] arg = new Object[1];
            arg[0] = "CD_NM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("CD_VL")) < 0 || StringUtil.length(objJsonParams.getString("CD_VL")) > 100) {
            Object[] arg = new Object[1];
            arg[0] = "CD_VL";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("CD_EXPLN")) < 0 || StringUtil.length(objJsonParams.getString("CD_EXPLN")) > 1000) {
            Object[] arg = new Object[1];
            arg[0] = "CD_EXPLN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("USER_ID")) < 0 || StringUtils.length(objJsonParams.getString("USER_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
    }


    //상세  유효성
    public void processDetailValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.isBlank(objJsonParams.getString("GROUP_CD"))) {
            Object[] arg = new Object[1];
            arg[0] = "GROUP_CD";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("CD"))) {
            Object[] arg = new Object[1];
            arg[0] = "CD";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("CD_NM"))) {
            Object[] arg = new Object[1];
            arg[0] = "CD_NM";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("D_CD_USE_FR_DT"))) {
            Object[] arg = new Object[1];
            arg[0] = "D_CD_USE_FR_DT";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("D_CD_USE_TO_DT"))) {
            Object[] arg = new Object[1];
            arg[0] = "D_CD_USE_TO_DT";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("CD_TYPE"))) {
            Object[] arg = new Object[1];
            arg[0] = "CD_TYPE";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("SORT_ORD"))) {
            Object[] arg = new Object[1];
            arg[0] = "SORT_ORD";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("USE_YN"))) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("GROUP_CD")) < 0 || StringUtil.length(objJsonParams.getString("GROUP_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "GROUP_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("CD")) < 0 || StringUtil.length(objJsonParams.getString("CD")) > 120) {
            Object[] arg = new Object[1];
            arg[0] = "CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("CD_TYPE")) < 0 || StringUtil.length(objJsonParams.getString("CD_TYPE")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "CD_TYPE";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("CD_NM")) < 0 || StringUtil.length(objJsonParams.getString("CD_NM")) > 300) {
            Object[] arg = new Object[1];
            arg[0] = "CD_NM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("CD_EXPLN")) < 0 || StringUtil.length(objJsonParams.getString("CD_EXPLN")) > 600) {
            Object[] arg = new Object[1];
            arg[0] = "CD_EXPLN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("CD_USE_FR_DT")) < 0 || StringUtil.length(objJsonParams.getString("CD_USE_FR_DT")) > 24) {
            Object[] arg = new Object[1];
            arg[0] = "CD_USE_FR_DT";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("CD_USE_TO_DT")) < 0 || StringUtil.length(objJsonParams.getString("CD_USE_TO_DT")) > 24) {
            Object[] arg = new Object[1];
            arg[0] = "CD_USE_TO_DT";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("CD_PRE_TYPE")) < 0 || StringUtil.length(objJsonParams.getString("CD_PRE_TYPE")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "CD_PRE_TYPE";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("ETC_INFO01")) < 0 || StringUtil.length(objJsonParams.getString("ETC_INFO01")) > 600) {
            Object[] arg = new Object[1];
            arg[0] = "ETC_INFO01";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("ETC_INFO02")) < 0 || StringUtil.length(objJsonParams.getString("ETC_INFO02")) > 600) {
            Object[] arg = new Object[1];
            arg[0] = "ETC_INFO02";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("ETC_INFO03")) < 0 || StringUtil.length(objJsonParams.getString("ETC_INFO03")) > 600) {
            Object[] arg = new Object[1];
            arg[0] = "ETC_INFO02";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("USE_YN")) < 0 || StringUtil.length(objJsonParams.getString("USE_YN")) > 1) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("SORT_ORD")) < 0 || StringUtil.length(objJsonParams.getString("SORT_ORD")) > 11) {
            Object[] arg = new Object[1];
            arg[0] = "SORT_ORD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("REGR_DEPT_CD")) < 0 || StringUtil.length(objJsonParams.getString("REGR_DEPT_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "REGR_DEPT_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("REGR_ID")) < 0 || StringUtil.length(objJsonParams.getString("REGR_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "REGR_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("AMDR_DEPT_CD")) < 0 || StringUtil.length(objJsonParams.getString("AMDR_DEPT_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "AMDR_DEPT_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("AMDR_ID")) < 0 || StringUtil.length(objJsonParams.getString("AMDR_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "AMDR_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("UPD_DTTM")) < 0 || StringUtil.length(objJsonParams.getString("UPD_DTTM")) > 26) {
            Object[] arg = new Object[1];
            arg[0] = "UPD_DTTM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("PROC_ID")) < 0 || StringUtil.length(objJsonParams.getString("PROC_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "PROC_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
    }


    //삭제 유효성
    public void deleteValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        JSONArray objArry = objJsonParams.getDataObject();
        int objArrSize = objArry.size();

        for(int i = 0; i < objArrSize; i++) {
            if(StringUtils.isBlank(objArry.getJSONObject(i).getString("GROUP_CD"))) {
                Object[] arg = new Object[1];
                arg[0] = "GROUP_CD";
                errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
            }

            if(StringUtil.length(objArry.getJSONObject(i).getString("GROUP_CD")) < 0 || StringUtil.length(objArry.getJSONObject(i).getString("GROUP_CD")) > 60) {
                Object[] arg = new Object[1];
                arg[0] = "PROC_ID";
                errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
            }
        }
    }

}
