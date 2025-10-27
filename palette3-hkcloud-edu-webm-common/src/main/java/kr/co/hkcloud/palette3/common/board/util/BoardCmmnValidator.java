package kr.co.hkcloud.palette3.common.board.util;


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
 * 게시물 Validation체크
 * 
 * @author R&D
 */
@Slf4j
@Component
public class BoardCmmnValidator implements Validator
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

        if(objJsonParams.getHeaderString("METHOD").toString().equals("upload")) {

            processValidate(target, errors);
        }
        else {
            Object[] arg = new Object[1];
            arg[0] = "METHOD";
            errors.reject(PaletteValidationCode.UNDEFINED_METHOD.getCode(), arg, PaletteValidationCode.UNDEFINED_METHOD.getMessage());
        }

    }


    //게시물 등록/수정 유효성
    public void processValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.isBlank(objJsonParams.getString("BRD_TIT"))) {
            Object[] arg = new Object[1];
            arg[0] = "BRD_TIT";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
//    	if(StringUtils.isBlank(objJsonParams.getString("CUSTCO_ID"))) {
//            Object[] arg = new Object[1];
//            arg[0] = "CUSTCO_ID";
//            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
//        }
        if(StringUtils.isBlank(objJsonParams.getString("BRD_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "BRD_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("BRD_NO"))) {
            Object[] arg = new Object[1];
            arg[0] = "BRD_NO";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
//        if(StringUtils.isBlank(objJsonParams.getString("PROC_ID"))) {
//            Object[] arg = new Object[1];
//            arg[0] = "PROC_ID";
//            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
//        }

//        if(StringUtil.length(objJsonParams.getString("CUSTCO_ID")) < 0 || StringUtil.length(objJsonParams.getString("CUSTCO_ID")) > 10) {
//            Object[] arg = new Object[1];
//            arg[0] = "CUSTCO_ID";
//            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
//        }
        if(StringUtil.length(objJsonParams.getString("BRD_ID")) < 0 || StringUtil.length(objJsonParams.getString("BRD_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "BRD_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("BRD_NO")) < 0 || StringUtil.length(objJsonParams.getString("BRD_NO")) > 90) {
            Object[] arg = new Object[1];
            arg[0] = "BRD_NO";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("UPPER_BRD_NO")) < 0 || StringUtil.length(objJsonParams.getString("UPPER_BRD_NO")) > 90) {
            Object[] arg = new Object[1];
            arg[0] = "UPPER_BRD_NO";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("LVL_NO")) < 0 || StringUtil.length(objJsonParams.getString("LVL_NO")) > 6) {
            Object[] arg = new Object[1];
            arg[0] = "LVL_NO";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("BRD_TIT")) < 0 || StringUtil.length(objJsonParams.getString("BRD_TIT")) > 300) {
            Object[] arg = new Object[1];
            arg[0] = "BRD_TIT";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("BRD_RMK_NO")) < 0 || StringUtil.length(objJsonParams.getString("BRD_RMK_NO")) > 74) {
            Object[] arg = new Object[1];
            arg[0] = "BRD_RMK_NO";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

        if(StringUtils.length(objJsonParams.getString("BRD_RMK")) < 0 || StringUtils.length(objJsonParams.getString("BRD_RMK")) > TwbCmmnConst.LIMITED_CHARACTERS_1300) {
            Object[] arg = new Object[1];
            arg[0] = "BRD_RMK";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("FILE_GROUP_KEY")) < 0 || StringUtils.length(objJsonParams.getString("FILE_GROUP_KEY")) > 90) {
            Object[] arg = new Object[1];
            arg[0] = "FILE_GROUP_KEY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
//        if(StringUtils.length(objJsonParams.getString("FST_USER_ID")) < 0 || StringUtils.length(objJsonParams.getString("FST_USER_ID")) > 60) {
//            Object[] arg = new Object[1];
//            arg[0] = "FST_USER_ID";
//            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
//        }
//        if(StringUtils.length(objJsonParams.getString("LAST_USER_ID")) < 0 || StringUtils.length(objJsonParams.getString("LAST_USER_ID")) > 60) {
//            Object[] arg = new Object[1];
//            arg[0] = "LAST_USER_ID";
//            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
//        }
        if(StringUtils.length(objJsonParams.getString("ACCESS_IP")) < 0 || StringUtils.length(objJsonParams.getString("ACCESS_IP")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "ACCESS_IP";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("SELECT_NO")) < 0 || StringUtils.length(objJsonParams.getString("SELECT_NO")) > 20) {
            Object[] arg = new Object[1];
            arg[0] = "SELECT_NO";
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

        if(StringUtils.length(objJsonParams.getString("PROC_ID")) < 0 || StringUtils.length(objJsonParams.getString("PROC_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "PROC_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("KAGE_FILE_URL")) < 0 || StringUtils.length(objJsonParams.getString("KAGE_FILE_URL")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "KAGE_FILE_URL";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("KAGE_ACS_KEY")) < 0 || StringUtils.length(objJsonParams.getString("KAGE_ACS_KEY")) > 1000) {
            Object[] arg = new Object[1];
            arg[0] = "KAGE_ACS_KEY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("PUP_YN")) < 0 || StringUtils.length(objJsonParams.getString("PUP_YN")) > 1) {
            Object[] arg = new Object[1];
            arg[0] = "PUP_YN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
    }

}
