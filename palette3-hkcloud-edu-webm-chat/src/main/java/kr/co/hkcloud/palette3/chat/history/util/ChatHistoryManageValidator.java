package kr.co.hkcloud.palette3.chat.history.util;


import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.netty.util.internal.StringUtil;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class ChatHistoryManageValidator implements Validator
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

                selectConsHistValidate(target, errors);

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


    //상담이력관리 조회유효성
    public void selectConsHistValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.isBlank(objJsonParams.getString("SEARCH_FROM"))) {
            Object[] arg = new Object[1];
            arg[0] = "SEARCH_FROM";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("SEARCH_TO"))) {
            Object[] arg = new Object[1];
            arg[0] = "SEARCH_TO";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("CUSTCO_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("SEARCH_FROM")) < 0 || StringUtil.length(objJsonParams.getString("SEARCH_FROM")) > 8) {
            Object[] arg = new Object[1];
            arg[0] = "SEARCH_FROM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("SEARCH_TO")) < 0 || StringUtil.length(objJsonParams.getString("SEARCH_TO")) > 8) {
            Object[] arg = new Object[1];
            arg[0] = "SEARCH_TO";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("CUSTCO_ID")) < 0 || StringUtil.length(objJsonParams.getString("CUSTCO_ID")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("SRH_CALL_TYP_CD")) < 0 || StringUtil.length(objJsonParams.getString("SRH_CALL_TYP_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "SRH_CALL_TYP_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("SRH_SNDR_KEY")) < 0 || StringUtil.length(objJsonParams.getString("SRH_SNDR_KEY")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "SRH_SNDR_KEY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("USER")) < 0 || StringUtil.length(objJsonParams.getString("USER")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "USER";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("SRH_INQRY_TYP_CD")) < 0 || StringUtil.length(objJsonParams.getString("SRH_INQRY_TYP_CD")) > 120) {
            Object[] arg = new Object[1];
            arg[0] = "SRH_INQRY_TYP_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("SRH_INQRY_TYP_CD2")) < 0 || StringUtil.length(objJsonParams.getString("SRH_INQRY_TYP_CD2")) > 120) {
            Object[] arg = new Object[1];
            arg[0] = "SRH_INQRY_TYP_CD2";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("TALK_STAT_CD")) < 0 || StringUtil.length(objJsonParams.getString("TALK_STAT_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "TALK_STAT_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("TALK_CONTACT_ID")) < 0 || StringUtil.length(objJsonParams.getString("TALK_CONTACT_ID")) > 90) {
            Object[] arg = new Object[1];
            arg[0] = "TALK_CONTACT_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("SRH_CNSL_TYP_CD")) < 0 || StringUtil.length(objJsonParams.getString("SRH_CNSL_TYP_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "SRH_CNSL_TYP_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("SRH_CNSL_TYP_CD_2")) < 0 || StringUtil.length(objJsonParams.getString("SRH_CNSL_TYP_CD_2")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "SRH_CNSL_TYP_CD_2";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("SRH_CNSL_TYP_CD_3")) < 0 || StringUtil.length(objJsonParams.getString("SRH_CNSL_TYP_CD_3")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "SRH_CNSL_TYP_CD_3";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("SRH_CNSL_TYP_CD_4")) < 0 || StringUtil.length(objJsonParams.getString("SRH_CNSL_TYP_CD_4")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "SRH_CNSL_TYP_CD_4";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("TALK_QST")) < 0 || StringUtil.length(objJsonParams.getString("TALK_QST")) > TwbCmmnConst.LIMITED_CHARACTERS_1300) {
            Object[] arg = new Object[1];
            arg[0] = "TALK_QST";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

        if(StringUtil.length(objJsonParams.getString("CUSTOMER_ID")) < 0 || StringUtil.length(objJsonParams.getString("CUSTOMER_ID")) > 10) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTOMER_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(!StringUtils.isBlank(objJsonParams.getString("CUSTOMER_ID"))) {
            if(objJsonParams.getString("SRH_CUSTOMER_ID").toString().equals("01")) {
                if(StringUtil.length(objJsonParams.getString("CUSTOMER_ID")) < 0 || StringUtil.length(objJsonParams.getString("CUSTOMER_ID")) > 90) {
                    Object[] arg = new Object[1];
                    arg[0] = "CUSTOMER_ID";
                    errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
                }
            }
            if(objJsonParams.getString("SRH_CUSTOMER_ID").toString().equals("02")) {
                if(StringUtil.length(objJsonParams.getString("CUSTOMER_ID")) < 0 || StringUtil.length(objJsonParams.getString("CUSTOMER_ID")) > 100) {
                    Object[] arg = new Object[1];
                    arg[0] = "CUSTOMER_ID";
                    errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
                }
            }
        }
    }
}
