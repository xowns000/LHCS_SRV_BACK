package kr.co.hkcloud.palette3.chat.setting.util;


import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import lombok.extern.slf4j.Slf4j;


/**
 * 문의하기 Validation체크
 * 
 * @author R&D
 */
@Slf4j
@Component
public class ChatSettingInquiryTypeManageValidator implements Validator
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

            if(objJsonParams.getHeaderString("METHOD").equals("regist")) {
                processValidate(target, errors);
            }
            else if(objJsonParams.getHeaderString("METHOD").equals("delete")) {
                deleteValidate(target, errors);
            }
            else {
                Object[] arg = new Object[1];
                arg[0] = "METHOD";
                errors.reject(PaletteValidationCode.UNDEFINED_METHOD.getCode(), arg, PaletteValidationCode.UNDEFINED_METHOD.getMessage());
            }
        }
        else if(objJsonParams.getHeaderString("TYPE").toString().equals("XML_SERVICE")) {

            if(objJsonParams.getHeaderString("SQL_NM").toString().equals("selectRtnTwbTalkInqryTypTreeView") || objJsonParams.getHeaderString("SQL_NM").toString().equals("selectRtnNodeDetail")) {

                selectTreeValidate(target, errors);

            }
            else {
                Object[] arg = new Object[1];
                arg[0] = "SQL_NM";
                errors.reject(PaletteValidationCode.UNDEFINED_METHOD.getCode(), arg, PaletteValidationCode.UNDEFINED_METHOD.getMessage());
            }
        }
        else {
            Object[] arg = new Object[1];
            arg[0] = "BIZ_SERVICE";
            errors.reject(PaletteValidationCode.UNDEFINED_TYPE.getCode(), arg, PaletteValidationCode.UNDEFINED_TYPE.getMessage());
        }

    }


    //문의유형관리 tree조회
    public void selectTreeValidate(Object target, Errors errors)
    {
        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.isBlank(objJsonParams.getString("ASP_NEWCUST_KEY"))) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("ASP_NEWCUST_KEY")) < 0 || StringUtils.length(objJsonParams.getString("ASP_NEWCUST_KEY")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("INQRY_TYP_CD")) < 0 || StringUtils.length(objJsonParams.getString("INQRY_TYP_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "INQRY_TYP_CD ";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("USE_YN")) < 0 || StringUtils.length(objJsonParams.getString("USE_YN")) > 1) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("SPST_INQRY_TYP_CD")) < 0 || StringUtils.length(objJsonParams.getString("SPST_INQRY_TYP_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "SPST_INQRY_TYP_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("INQRY_TYP_DIV_CD")) < 0 || StringUtils.length(objJsonParams.getString("INQRY_TYP_DIV_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "INQRY_TYP_DIV_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
    }


    //문의유형 처리유효성
    public void processValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.isBlank(objJsonParams.getString("ASP_NEWCUST_KEY"))) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("INQRY_TYP_CD"))) {
            Object[] arg = new Object[1];
            arg[0] = "INQRY_TYP_CD";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("INQRY_TYP_NM"))) {
            Object[] arg = new Object[1];
            arg[0] = "INQRY_TYP_NM";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("INQRY_USE_CHANNEL"))) {
            Object[] arg = new Object[1];
            arg[0] = "INQRY_USE_CHANNEL";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("SPST_INQRY_TYP_CD"))) {
            Object[] arg = new Object[1];
            arg[0] = "SPST_INQRY_TYP_CD";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("USE_YN"))) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("SORT_ORD"))) {
            Object[] arg = new Object[1];
            arg[0] = "SORT_ORD";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
//	    if(StringUtils.isBlank(objJsonParams.getString("REGR_DEPT_CD"))) {
//	        Object[] arg = new Object[1];
//	        arg[0] = "REGR_DEPT_CD";
//	        errors.reject(TelewebMessage.DATA_NOT_EXIST.getCode(), arg, TelewebMessage.DATA_NOT_EXIST.getMessage());
//	    }

        if(StringUtils.length(objJsonParams.getString("ASP_NEWCUST_KEY")) < 0 || StringUtils.length(objJsonParams.getString("ASP_NEWCUST_KEY")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("INQRY_TYP_CD")) < 0 || StringUtils.length(objJsonParams.getString("INQRY_TYP_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "INQRY_TYP_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("INQRY_TYP_NM")) < 0 || StringUtils.length(objJsonParams.getString("INQRY_TYP_NM")) > 450) {
            Object[] arg = new Object[1];
            arg[0] = "INQRY_TYP_NM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("INQRY_DESC")) < 0 || StringUtils.length(objJsonParams.getString("INQRY_DESC")) > TwbCmmnConst.LIMITED_CHARACTERS_1300) {
            Object[] arg = new Object[1];
            arg[0] = "INQRY_DESC";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("INQRY_TYPE")) < 0 || StringUtils.length(objJsonParams.getString("INQRY_TYPE")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "INQRY_TYPE";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("INQRY_USE_CHANNEL")) < 0 || StringUtils.length(objJsonParams.getString("INQRY_USE_CHANNEL")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "INQRY_USE_CHANNEL";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("SPST_INQRY_TYP_CD")) < 0 || StringUtils.length(objJsonParams.getString("SPST_INQRY_TYP_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "SPST_INQRY_TYP_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("INQRY_TYP_DIV_CD")) < 0 || StringUtils.length(objJsonParams.getString("INQRY_TYP_DIV_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "INQRY_TYP_DIV_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("USE_YN")) < 0 || StringUtils.length(objJsonParams.getString("USE_YN")) > 1) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("SORT_ORD")) < 0 || StringUtils.length(objJsonParams.getString("SORT_ORD")) > 11) {
            Object[] arg = new Object[1];
            arg[0] = "SORT_ORD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
//    	if(StringUtils.length(objJsonParams.getString("REGR_DEPT_CD")) < 0 || StringUtils.length(objJsonParams.getString("REGR_DEPT_CD")) >60) {
//    		Object[] arg = new Object[1];
//    		arg[0] = "REGR_DEPT_CD";
//    		errors.reject(TelewebMessage.EXCEED_INPUT.getCode(), arg,TelewebMessage.EXCEED_INPUT.getMessage());
//    	}
        if(StringUtils.length(objJsonParams.getString("REGR_ID")) < 0 || StringUtils.length(objJsonParams.getString("REGR_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "REGR_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

    }


    //문의유형 삭제유효성
    public void deleteValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.length(objJsonParams.getString("INQRY_TYP_CD")) < 0 || StringUtils.length(objJsonParams.getString("INQRY_TYP_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "INQRY_TYP_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("INQRY_TYP_DIV_CD")) < 0 || StringUtils.length(objJsonParams.getString("INQRY_TYP_DIV_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "INQRY_TYP_DIV_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("INQRY_TYP_CD"))) {
            Object[] arg = new Object[1];
            arg[0] = "INQRY_TYP_CD";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("INQRY_TYP_DIV_CD"))) {
            Object[] arg = new Object[1];
            arg[0] = "INQRY_TYP_DIV_CD";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
    }

}
