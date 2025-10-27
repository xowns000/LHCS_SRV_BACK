package kr.co.hkcloud.palette3.setting.agent.util;


import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import lombok.extern.slf4j.Slf4j;


/**
 * 사용자관리 Validation체크
 *
 * @author R&D
 */
@Slf4j
@Component
public class SettingAgentManageValidator implements Validator
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
        String strTransFlag = objJsonParams.getString("DATA_FLAG");
        if(objJsonParams.getHeaderString("TYPE").toString().equals("BIZ_SERVICE")) {

            if(strTransFlag.equals(TwbCmmnConst.TRANS_INS)) {
                insertValidate(target, errors);
            }
            else if(strTransFlag.equals(TwbCmmnConst.TRANS_UPD)) {
                updateValidate(target, errors);
            }
            else if(objJsonParams.getHeaderString("METHOD").toString().equals("inqire")) {
                selectUserValidate(target, errors);
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


    // 신규 등록 유효성 검사
    public void insertValidate(Object target, Errors errors)
    {
        TelewebJSON objJsonParams = (TelewebJSON) target;
        if(StringUtils.isBlank(objJsonParams.getString("ASP_NEWCUST_KEY"))) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("CUSTCO_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("PROC_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "PROC_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("USER_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("USE_YN"))) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("REGR_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "REGR_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("USER_NM"))) {
            Object[] arg = new Object[1];
            arg[0] = "USER_NM";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("USER_NICK"))) {
            Object[] arg = new Object[1];
            arg[0] = "USER_NICK";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("USER_ATTR_A"))) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ATTR_A";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("USER_ATTR_B"))) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ATTR_B";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("USER_ATTR_C"))) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ATTR_C";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("USER_ATTR_D"))) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ATTR_D";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("ATRT_GROUP_CD"))) {
            Object[] arg = new Object[1];
            arg[0] = "ATRT_GROUP_CD";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("REGR_DEPT_CD"))) {
            Object[] arg = new Object[1];
            arg[0] = "REGR_DEPT_CD";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("ASP_NEWCUST_KEY")) < 0 || StringUtils.length(objJsonParams.getString("ASP_NEWCUST_KEY")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("CUSTCO_ID")) < 0 || StringUtils.length(objJsonParams.getString("CUSTCO_ID")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("PROC_ID")) < 0 || StringUtils.length(objJsonParams.getString("PROC_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "PROC_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("USER_ID")) < 0 || StringUtils.length(objJsonParams.getString("USER_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("USE_YN")) < 0 || StringUtils.length(objJsonParams.getString("USE_YN")) > 1) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("REGR_ID")) < 0 || StringUtils.length(objJsonParams.getString("REGR_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "REGR_ID";
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
        if(StringUtils.length(objJsonParams.getString("ATRT_GROUP_CD")) < 0 || StringUtils.length(objJsonParams.getString("ATRT_GROUP_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "ATRT_GROUP_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("REGR_DEPT_CD")) < 0 || StringUtils.length(objJsonParams.getString("REGR_DEPT_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "REGR_DEPT_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
    }


    // 수정 유효성 검사
    public void updateValidate(Object target, Errors errors)
    {
        TelewebJSON objJsonParams = (TelewebJSON) target;
        if(StringUtils.isBlank(objJsonParams.getString("ASP_NEWCUST_KEY"))) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("CUSTCO_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("PROC_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "PROC_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("USER_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("USE_YN"))) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("USER_NM"))) {
            Object[] arg = new Object[1];
            arg[0] = "USER_NM";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("USER_NICK"))) {
            Object[] arg = new Object[1];
            arg[0] = "USER_NICK";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("USER_ATTR_A"))) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ATTR_A";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("USER_ATTR_B"))) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ATTR_B";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("USER_ATTR_C"))) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ATTR_C";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("USER_ATTR_D"))) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ATTR_D";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("ASP_NEWCUST_KEY")) < 0 || StringUtils.length(objJsonParams.getString("ASP_NEWCUST_KEY")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("CUSTCO_ID")) < 0 || StringUtils.length(objJsonParams.getString("CUSTCO_ID")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("PROC_ID")) < 0 || StringUtils.length(objJsonParams.getString("PROC_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "PROC_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("USER_ID")) < 0 || StringUtils.length(objJsonParams.getString("USER_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("USE_YN")) < 0 || StringUtils.length(objJsonParams.getString("USE_YN")) > 1) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("REGR_ID")) < 0 || StringUtils.length(objJsonParams.getString("REGR_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "REGR_ID";
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


    public void selectUserValidate(Object target, Errors errors)
    {
        TelewebJSON objJsonParams = (TelewebJSON) target;
        if(StringUtils.isBlank(objJsonParams.getString("ASP_NEWCUST_KEY"))) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("CUSTCO_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("ASP_NEWCUST_KEY")) < 0 || StringUtils.length(objJsonParams.getString("ASP_NEWCUST_KEY")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("CUSTCO_ID")) < 0 || StringUtils.length(objJsonParams.getString("CUSTCO_ID")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
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
        if(StringUtils.length(objJsonParams.getString("USER_ID")) < 0 || StringUtils.length(objJsonParams.getString("USER_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
    }
}
