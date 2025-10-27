package kr.co.hkcloud.palette3.chat.setting.util;


import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.netty.util.internal.StringUtil;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import lombok.extern.slf4j.Slf4j;


/**
 * 채널 연동목록 Validation체크
 * 
 * @author R&D
 */
@Slf4j
@Component
public class ChatSettingMessengerConnectManageValidator implements Validator
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

            if(objJsonParams.getHeaderString("METHOD").toString().equals("regist")) {
                insertValidate(target, errors);
            }
            if(objJsonParams.getHeaderString("METHOD").toString().equals("update")) {
                updateValidate(target, errors);
            }
            if(objJsonParams.getHeaderString("METHOD").toString().equals("delete")) {
                deleteValidate(target, errors);
            }
        }
        else {
            Object[] arg = new Object[1];
            arg[0] = "BIZ_SERVICE";
            errors.reject(PaletteValidationCode.UNDEFINED_TYPE.getCode(), arg, PaletteValidationCode.UNDEFINED_TYPE.getMessage());
        }
    }


    //신규 등록 유효성 검사
    public void insertValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.isBlank(objJsonParams.getString("CUSTCO_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("ASP_CUST_INFO"))) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_CUST_INFO";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("CHN_CLSF_CD"))) {
            Object[] arg = new Object[1];
            arg[0] = "CHN_CLSF_CD";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("UUID"))) {
            Object[] arg = new Object[1];
            arg[0] = "UUID";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("DSPTCH_PRF_KEY"))) {
            Object[] arg = new Object[1];
            arg[0] = "DSPTCH_PRF_KEY";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("DSPTCH_PRF_NM"))) {
            Object[] arg = new Object[1];
            arg[0] = "DSPTCH_PRF_NM";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("PROC_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "PROC_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("AMDR_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "AMDR_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("CUSTCO_ID")) < 0 || StringUtil.length(objJsonParams.getString("CUSTCO_ID")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("ASP_CUST_INFO")) < 0 || StringUtil.length(objJsonParams.getString("ASP_CUST_INFO")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_CUST_INFO";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("CHN_CLSF_CD")) < 0 || StringUtil.length(objJsonParams.getString("CHN_CLSF_CD")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "CHN_CLSF_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("UUID")) < 0 || StringUtil.length(objJsonParams.getString("UUID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "UUID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("DSPTCH_PRF_KEY")) < 0 || StringUtil.length(objJsonParams.getString("DSPTCH_PRF_KEY")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "DSPTCH_PRF_KEY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("DSPTCH_PRF_NM")) < 0 || StringUtil.length(objJsonParams.getString("DSPTCH_PRF_NM")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "DSPTCH_PRF_NM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("AMDR_ID")) < 0 || StringUtil.length(objJsonParams.getString("AMDR_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "AMDR_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("PROC_ID")) < 0 || StringUtil.length(objJsonParams.getString("PROC_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "PROC_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

    }


    //수정 등록 유효성 검사
    public void updateValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.isBlank(objJsonParams.getString("CUSTCO_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("ASP_CUST_INFO"))) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_CUST_INFO";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("CHN_CLSF_CD"))) {
            Object[] arg = new Object[1];
            arg[0] = "CHN_CLSF_CD";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("UUID"))) {
            Object[] arg = new Object[1];
            arg[0] = "UUID";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("DSPTCH_PRF_KEY"))) {
            Object[] arg = new Object[1];
            arg[0] = "DSPTCH_PRF_KEY";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("DSPTCH_PRF_NM"))) {
            Object[] arg = new Object[1];
            arg[0] = "DSPTCH_PRF_NM";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("PROC_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "PROC_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("AMDR_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "AMDR_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("CUSTCO_ID")) < 0 || StringUtil.length(objJsonParams.getString("CUSTCO_ID")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("ASP_CUST_INFO")) < 0 || StringUtil.length(objJsonParams.getString("ASP_CUST_INFO")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_CUST_INFO";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("CHN_CLSF_CD")) < 0 || StringUtil.length(objJsonParams.getString("CHN_CLSF_CD")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "CHN_CLSF_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("UUID")) < 0 || StringUtil.length(objJsonParams.getString("UUID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "UUID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("DSPTCH_PRF_KEY")) < 0 || StringUtil.length(objJsonParams.getString("DSPTCH_PRF_KEY")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "DSPTCH_PRF_KEY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("DSPTCH_PRF_NM")) < 0 || StringUtil.length(objJsonParams.getString("DSPTCH_PRF_NM")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "DSPTCH_PRF_NM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("AMDR_ID")) < 0 || StringUtil.length(objJsonParams.getString("AMDR_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "AMDR_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("PROC_ID")) < 0 || StringUtil.length(objJsonParams.getString("PROC_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "PROC_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

    }


    //삭제 등록 유효성 검사
    public void deleteValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;
        if(StringUtils.isBlank(objJsonParams.getString("ASP_NEWCUST_KEY"))) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("ASP_NEWCUST_KEY")) < 0 || StringUtil.length(objJsonParams.getString("ASP_NEWCUST_KEY")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

    }
}
