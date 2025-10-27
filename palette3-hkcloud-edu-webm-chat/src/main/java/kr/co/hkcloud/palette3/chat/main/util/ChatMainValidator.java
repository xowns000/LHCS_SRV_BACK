package kr.co.hkcloud.palette3.chat.main.util;


import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.netty.util.internal.StringUtil;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import lombok.extern.slf4j.Slf4j;


/**
 * 메인 Validation체크
 * 
 * @author R&D
 */
@Slf4j
@Component
public class ChatMainValidator implements Validator
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

        if(objJsonParams.getHeaderString("METHOD").toString().equals("UNTACTS")) {

            selectUntactValidate(target, errors);

        }
        else if(objJsonParams.getHeaderString("METHOD").toString().equals("SCRIPTS")) {

            selectValidate(target, errors);

        }
        else if(objJsonParams.getHeaderString("METHOD").toString().equals("SCRIPTS_INSERT")) {

            insertValidate(target, errors);

        }
        else if(objJsonParams.getHeaderString("METHOD").toString().equals("SCRIPTS_UPDATE")) {

            updateValidate(target, errors);

        }
        else if(objJsonParams.getHeaderString("METHOD").toString().equals("SCRIPTS_DELETE")) {

            deleteValidate(target, errors);

        }
        else if(objJsonParams.getHeaderString("METHOD").toString().equals("MEMO_INSERT")) {

            insertMemoValidate(target, errors);
        }
        else {
            Object[] arg = new Object[1];
            arg[0] = "METHOD";
            errors.reject(PaletteValidationCode.UNDEFINED_METHOD.getCode(), arg, PaletteValidationCode.UNDEFINED_METHOD.getMessage());
        }
    }


    public void insertMemoValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.isBlank(objJsonParams.getString("TALK_USER_KEY"))) {
            Object[] arg = new Object[1];
            arg[0] = "TALK_USER_KEY";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("SNDR_KEY"))) {
            Object[] arg = new Object[1];
            arg[0] = "SNDR_KEY";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("TALK_USER_KEY"))) {
            Object[] arg = new Object[1];
            arg[0] = "TALK_USER_KEY";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("TALK_USER_KEY")) < 0 || StringUtil.length(objJsonParams.getString("TALK_USER_KEY")) > 80) {
            Object[] arg = new Object[1];
            arg[0] = "TALK_USER_KEY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("SNDR_KEY")) < 0 || StringUtil.length(objJsonParams.getString("SNDR_KEY")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "SNDR_KEY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("TALK_USER_KEY")) < 0 || StringUtil.length(objJsonParams.getString("TALK_USER_KEY")) > 80) {
            Object[] arg = new Object[1];
            arg[0] = "TALK_USER_KEY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("MEMO")) < 0 || StringUtils.length(objJsonParams.getString("MEMO")) > TwbCmmnConst.LIMITED_CHARACTERS_1300) {
            Object[] arg = new Object[1];
            arg[0] = "MEMO";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("USER_ID")) < 0 || StringUtils.length(objJsonParams.getString("USER_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("EML")) < 0 || StringUtils.length(objJsonParams.getString("EML")) > 300) {
            Object[] arg = new Object[1];
            arg[0] = "EML";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("ATENT_CUSTOMER")) < 0 || StringUtils.length(objJsonParams.getString("ATENT_CUSTOMER")) > 3) {
            Object[] arg = new Object[1];
            arg[0] = "ATENT_CUSTOMER";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("CUSTOMER_TYPE")) < 0 || StringUtils.length(objJsonParams.getString("CUSTOMER_TYPE")) > 6) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTOMER_TYPE";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("CUSTOMER_TYPE")) < 0 || StringUtils.length(objJsonParams.getString("CUSTOMER_TYPE")) > 6) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTOMER_TYPE";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("CUSTOMER_GENDER")) < 0 || StringUtils.length(objJsonParams.getString("CUSTOMER_GENDER")) > 6) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTOMER_GENDER";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("CUSTOMER_CAREER_STATUS")) < 0 || StringUtils.length(objJsonParams.getString("CUSTOMER_CAREER_STATUS")) > 6) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTOMER_CAREER_STATUS";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("CUSTOMER_EDU_CHK_YN")) < 0 || StringUtils.length(objJsonParams.getString("CUSTOMER_EDU_CHK_YN")) > 3) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTOMER_EDU_CHK_YN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("CUSTOMER_EDU_LEV")) < 0 || StringUtils.length(objJsonParams.getString("CUSTOMER_EDU_LEV")) > 6) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTOMER_EDU_LEV";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("CUSTOMER_EDU_MAJOR")) < 0 || StringUtils.length(objJsonParams.getString("CUSTOMER_EDU_MAJOR")) > 6) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTOMER_EDU_MAJOR";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("CUSTOMER_JOIN_POLICY_YN")) < 0 || StringUtils.length(objJsonParams.getString("CUSTOMER_JOIN_POLICY_YN")) > 3) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTOMER_JOIN_POLICY_YN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("CUSTOMER_JOIN_POLICY")) < 0 || StringUtils.length(objJsonParams.getString("CUSTOMER_JOIN_POLICY")) > 300) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTOMER_JOIN_POLICY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("CUSTOMER_REGION")) < 0 || StringUtils.length(objJsonParams.getString("CUSTOMER_REGION")) > 6) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTOMER_REGION";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("CUSTOMER_BIRTH")) < 0 || StringUtils.length(objJsonParams.getString("CUSTOMER_BIRTH")) > 24) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTOMER_BIRTH";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

    }


    //스크립트정보조회
    public void selectValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.isBlank(objJsonParams.getString("CUSTCO_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }

        if(StringUtil.length(objJsonParams.getString("CUSTCO_ID")) < 0 || StringUtil.length(objJsonParams.getString("CUSTCO_ID")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

        if(StringUtil.length(objJsonParams.getString("USER_ID")) < 0 || StringUtil.length(objJsonParams.getString("USER_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

        if(StringUtil.length(objJsonParams.getString("SCRIPT_TIT")) < 0 || StringUtil.length(objJsonParams.getString("SCRIPT_TIT")) > 600) {
            Object[] arg = new Object[1];
            arg[0] = "SCRIPT_TIT";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

    }


    //비대면링크정보조회
    public void selectUntactValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.isBlank(objJsonParams.getString("CUSTCO_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }

        if(StringUtil.length(objJsonParams.getString("CUSTCO_ID")) < 0 || StringUtil.length(objJsonParams.getString("CUSTCO_ID")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

//    	if(StringUtil.length(objJsonParams.getString("SCRIPT_TIT"))<0|| StringUtil.length(objJsonParams.getString("SCRIPT_TIT"))> 600) {
//    		Object[] arg = new Object[1];
//    		arg[0] = "SCRIPT_TIT";
//    		errors.reject(TelewebMessage.EXCEED_INPUT.getCode(), arg, TelewebMessage.EXCEED_INPUT.getMessage());
//    	}

    }


    public void insertValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.isBlank(objJsonParams.getString("CUSTCO_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("SCRIPT_TIT"))) {
            Object[] arg = new Object[1];
            arg[0] = "SCRIPT_TIT";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("SCRIPT_RMK"))) {
            Object[] arg = new Object[1];
            arg[0] = "SCRIPT_RMK";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("USE_YN"))) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("CUSTCO_ID")) < 0 || StringUtil.length(objJsonParams.getString("CUSTCO_ID")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("USER_ID")) < 0 || StringUtil.length(objJsonParams.getString("USER_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("SCRIPT_TIT")) < 0 || StringUtil.length(objJsonParams.getString("SCRIPT_TIT")) > 600) {
            Object[] arg = new Object[1];
            arg[0] = "SCRIPT_TIT";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("SCRIPT_RMK")) < 0 || StringUtils.length(objJsonParams.getString("SCRIPT_RMK")) > TwbCmmnConst.LIMITED_CHARACTERS_1300) {
            Object[] arg = new Object[1];
            arg[0] = "SCRIPT_RMK";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("USE_YN")) < 0 || StringUtils.length(objJsonParams.getString("USE_YN")) > 1) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("ORD_SEQ")) < 0 || StringUtils.length(objJsonParams.getString("ORD_SEQ")) > 11) {
            Object[] arg = new Object[1];
            arg[0] = "ORD_SEQ";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("LAST_USER_ID")) < 0 || StringUtils.length(objJsonParams.getString("LAST_USER_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "LAST_USER_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("SCRIPT_AUTH_TYPE")) < 0 || StringUtils.length(objJsonParams.getString("SCRIPT_AUTH_TYPE")) > 30) {
            Object[] arg = new Object[1];
            arg[0] = "SCRIPT_AUTH_TYPE";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

    }


    //스크립트정보조회
    public void updateValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

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
        if(StringUtil.length(objJsonParams.getString("CUSTCO_ID")) < 0 || StringUtil.length(objJsonParams.getString("CUSTCO_ID")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("SCRIPT_ID")) < 0 || StringUtil.length(objJsonParams.getString("SCRIPT_ID")) > 90) {
            Object[] arg = new Object[1];
            arg[0] = "SCRIPT_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("SCRIPT_TIT")) < 0 || StringUtil.length(objJsonParams.getString("SCRIPT_TIT")) > 600) {
            Object[] arg = new Object[1];
            arg[0] = "SCRIPT_TIT";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("SCRIPT_RMK")) < 0 || StringUtils.length(objJsonParams.getString("SCRIPT_RMK")) > TwbCmmnConst.LIMITED_CHARACTERS_1300) {
            Object[] arg = new Object[1];
            arg[0] = "SCRIPT_RMK";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("ORD_SEQ")) < 0 || StringUtils.length(objJsonParams.getString("ORD_SEQ")) > 11) {
            Object[] arg = new Object[1];
            arg[0] = "ORD_SEQ";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("USE_YN")) < 0 || StringUtils.length(objJsonParams.getString("USE_YN")) > 1) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
    }


    //스크립트정보조회
    public void deleteValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

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
        if(StringUtil.length(objJsonParams.getString("CUSTCO_ID")) < 0 || StringUtil.length(objJsonParams.getString("CUSTCO_ID")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("SCRIPT_ID")) < 0 || StringUtil.length(objJsonParams.getString("SCRIPT_ID")) > 90) {
            Object[] arg = new Object[1];
            arg[0] = "SCRIPT_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
    }
}
