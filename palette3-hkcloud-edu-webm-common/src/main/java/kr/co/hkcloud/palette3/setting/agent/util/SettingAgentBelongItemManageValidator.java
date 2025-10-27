package kr.co.hkcloud.palette3.setting.agent.util;


import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import lombok.extern.slf4j.Slf4j;


/**
 * 사용자소속항목관리 Validation체크
 *
 * @author R&D
 */
@Slf4j
@Component
public class SettingAgentBelongItemManageValidator implements Validator
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
            if(objJsonParams.getHeaderString("METHOD").toString().equals("inqire")) {
                selectAttrViewValidate(target, errors);
            }
            else if(objJsonParams.getHeaderString("METHOD").toString().equals("regist")) {
                insertValidate(target, errors);
            }
            else if(objJsonParams.getHeaderString("METHOD").toString().equals("modify")) {
                updateValidate(target, errors);
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


    // 조회 유효성 검사
    public void selectAttrViewValidate(Object target, Errors errors)
    {
        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.isBlank(objJsonParams.getString("ASP_NEWCUST_KEY"))) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());

        }
        if(StringUtils.isBlank(objJsonParams.getString("ATTR_CD"))) {
            Object[] arg = new Object[1];
            arg[0] = "ATTR_CD";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());

        }
        if(StringUtils.length(objJsonParams.getString("ASP_NEWCUST_KEY")) < 0 || StringUtils.length(objJsonParams.getString("ASP_NEWCUST_KEY")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

        if(StringUtils.length(objJsonParams.getString("ATTR_CD")) < 0 || StringUtils.length(objJsonParams.getString("ATTR_CD")) > 20) {
            Object[] arg = new Object[1];
            arg[0] = "ATTR_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

    }


    // 신규 유효성 검사
    public void insertValidate(Object target, Errors errors)
    {
        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.isBlank(objJsonParams.getString("ASP_NEWCUST_KEY"))) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("ATTR_CD"))) {
            Object[] arg = new Object[1];
            arg[0] = "ATTR_CD";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("ATTR_DIV_CD"))) {
            Object[] arg = new Object[1];
            arg[0] = "ATTR_DIV_CD";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("ATTR_DIV_NM"))) {
            Object[] arg = new Object[1];
            arg[0] = "ATTR_DIV_NM";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("USE_YN"))) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("SORT_ORD"))) {
            Object[] arg = new Object[1];
            arg[0] = "SORT_ORD";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("REGR_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "REGR_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("AMDR_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "AMDR_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("ASP_NEWCUST_KEY")) < 0 || StringUtils.length(objJsonParams.getString("ASP_NEWCUST_KEY")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

        if(StringUtils.length(objJsonParams.getString("ATTR_CD")) < 0 || StringUtils.length(objJsonParams.getString("ATTR_CD")) > 20) {
            Object[] arg = new Object[1];
            arg[0] = "ATTR_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("ATTR_DIV_CD")) < 0 || StringUtils.length(objJsonParams.getString("ATTR_DIV_CD")) > 20) {
            Object[] arg = new Object[1];
            arg[0] = "ATTR_DIV_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("ATTR_DIV_NM")) < 0 || StringUtils.length(objJsonParams.getString("ATTR_DIV_NM")) > 100) {
            Object[] arg = new Object[1];
            arg[0] = "ATTR_DIV_NM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("USE_YN")) < 0 || StringUtils.length(objJsonParams.getString("USE_YN")) > 1) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("SORT_ORD")) < 0 || StringUtils.length(objJsonParams.getString("SORT_ORD")) > 5) {
            Object[] arg = new Object[1];
            arg[0] = "SORT_ORD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("REGR_ID")) < 0 || StringUtils.length(objJsonParams.getString("REGR_ID")) > 20) {
            Object[] arg = new Object[1];
            arg[0] = "REGR_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("AMDR_ID")) < 0 || StringUtils.length(objJsonParams.getString("AMDR_ID")) > 20) {
            Object[] arg = new Object[1];
            arg[0] = "AMDR_ID";
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
        if(StringUtils.isBlank(objJsonParams.getString("ATTR_CD"))) {
            Object[] arg = new Object[1];
            arg[0] = "ATTR_CD";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("ATTR_DIV_CD"))) {
            Object[] arg = new Object[1];
            arg[0] = "ATTR_DIV_CD";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("ATTR_DIV_NM"))) {
            Object[] arg = new Object[1];
            arg[0] = "ATTR_DIV_NM";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("USE_YN"))) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("SORT_ORD"))) {
            Object[] arg = new Object[1];
            arg[0] = "SORT_ORD";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }

        if(StringUtils.length(objJsonParams.getString("ASP_NEWCUST_KEY")) < 0 || StringUtils.length(objJsonParams.getString("ASP_NEWCUST_KEY")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

        if(StringUtils.length(objJsonParams.getString("ATTR_CD")) < 0 || StringUtils.length(objJsonParams.getString("ATTR_CD")) > 20) {
            Object[] arg = new Object[1];
            arg[0] = "ATTR_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("ATTR_DIV_CD")) < 0 || StringUtils.length(objJsonParams.getString("ATTR_DIV_CD")) > 20) {
            Object[] arg = new Object[1];
            arg[0] = "ATTR_DIV_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("ATTR_DIV_NM")) < 0 || StringUtils.length(objJsonParams.getString("ATTR_DIV_NM")) > 100) {
            Object[] arg = new Object[1];
            arg[0] = "ATTR_DIV_NM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("USE_YN")) < 0 || StringUtils.length(objJsonParams.getString("USE_YN")) > 1) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("SORT_ORD")) < 0 || StringUtils.length(objJsonParams.getString("SORT_ORD")) > 5) {
            Object[] arg = new Object[1];
            arg[0] = "SORT_ORD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("REGR_ID")) < 0 || StringUtils.length(objJsonParams.getString("REGR_ID")) > 20) {
            Object[] arg = new Object[1];
            arg[0] = "REGR_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("AMDR_ID")) < 0 || StringUtils.length(objJsonParams.getString("AMDR_ID")) > 20) {
            Object[] arg = new Object[1];
            arg[0] = "AMDR_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

    }


    // 삭제 유효성 검사
    public void deleteValidate(Object target, Errors errors)
    {
        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.isBlank(objJsonParams.getString("ASP_NEWCUST_KEY"))) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("ATTR_CD"))) {
            Object[] arg = new Object[1];
            arg[0] = "ATTR_CD";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("ATTR_DIV_CD"))) {
            Object[] arg = new Object[1];
            arg[0] = "ATTR_DIV_CD";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("ASP_NEWCUST_KEY")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("ATTR_CD")) > 20) {
            Object[] arg = new Object[1];
            arg[0] = "ATTR_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("ATTR_DIV_CD")) > 20) {
            Object[] arg = new Object[1];
            arg[0] = "ATTR_DIV_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
    }

}
