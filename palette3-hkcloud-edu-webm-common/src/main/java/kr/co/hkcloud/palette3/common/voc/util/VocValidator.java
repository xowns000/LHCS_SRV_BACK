package kr.co.hkcloud.palette3.common.voc.util;


import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.netty.util.internal.StringUtil;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import lombok.extern.slf4j.Slf4j;


/**
 * 
 * Description : IP 내선번호 설정 Validator
 * package  : kr.co.hkcloud.palette3.setting.voc.util
 * filename : VocValidator.java
 * Date : 2023. 6. 9.
 * History :
 * - 작성자 : ryucease, 날짜 : 2023. 6. 9., 설명 : 최초작성<br>
 *
 * @author ryucease
 * @version 1.0
 */
@Slf4j
@Component
public class VocValidator implements Validator
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

        if(objJsonParams.getHeaderString("METHOD").toString().equals("regist")) {

            processValidate(target, errors);

        }
        else if(objJsonParams.getHeaderString("METHOD").toString().equals("delete")) {

            deleteValidate(target, errors);

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


    //게시판생성관리 조회 유효성
    public void selectValidate(Object target, Errors errors)
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
        if(StringUtil.length(objJsonParams.getString("BRD_NAME")) < 0 || StringUtil.length(objJsonParams.getString("BRD_NAME")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "BRD_NAME";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("USE_YN")) < 0 || StringUtil.length(objJsonParams.getString("USE_YN")) > 1) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("BRD_ID")) < 0 || StringUtil.length(objJsonParams.getString("BRD_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "BRD_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
    }


    //게시판생성관리 처리 유효성
    public void processValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.isBlank(objJsonParams.getString("ASP_NEWCUST_KEY"))) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("BRD_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "BRD_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("BRD_NAME"))) {
            Object[] arg = new Object[1];
            arg[0] = "BRD_NAME";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("USE_YN"))) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("PROC_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("ASP_NEWCUST_KEY")) < 0 || StringUtil.length(objJsonParams.getString("ASP_NEWCUST_KEY")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("BRD_NAME")) < 0 || StringUtil.length(objJsonParams.getString("BRD_NAME")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "BRD_NAME";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("BRD_ID")) < 0 || StringUtil.length(objJsonParams.getString("BRD_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "BRD_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

        if(StringUtil.length(objJsonParams.getString("BRD_RMK")) < 0 || StringUtil.length(objJsonParams.getString("BRD_RMK")) > 300) {
            Object[] arg = new Object[1];
            arg[0] = "BRD_RMK";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("FILE_YN")) < 0 || StringUtil.length(objJsonParams.getString("FILE_YN")) > 1) {
            Object[] arg = new Object[1];
            arg[0] = "FILE_YN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("RT_NOTI_YN")) < 0 || StringUtil.length(objJsonParams.getString("RT_NOTI_YN")) > 1) {
            Object[] arg = new Object[1];
            arg[0] = "RT_NOTI_YN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("NEW_DAY")) < 0 || StringUtil.length(objJsonParams.getString("NEW_DAY")) > 6) {
            Object[] arg = new Object[1];
            arg[0] = "NEW_DAY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("ETC01")) < 0 || StringUtil.length(objJsonParams.getString("ETC01")) > 20) {
            Object[] arg = new Object[1];
            arg[0] = "ETC01";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("ETC02")) < 0 || StringUtil.length(objJsonParams.getString("ETC02")) > 20) {
            Object[] arg = new Object[1];
            arg[0] = "ETC02";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("ETC03")) < 0 || StringUtil.length(objJsonParams.getString("ETC03")) > 20) {
            Object[] arg = new Object[1];
            arg[0] = "ETC03";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("ETC04")) < 0 || StringUtil.length(objJsonParams.getString("ETC04")) > 20) {
            Object[] arg = new Object[1];
            arg[0] = "ETC04";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("ETC05")) < 0 || StringUtil.length(objJsonParams.getString("ETC05")) > 20) {
            Object[] arg = new Object[1];
            arg[0] = "ETC05";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("PROC_ID")) < 0 || StringUtil.length(objJsonParams.getString("PROC_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "PROC_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

    }


    //게시판생성관리삭제 유효성
    public void deleteValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.isBlank(objJsonParams.getString("ASP_NEWCUST_KEY"))) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("BRD_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "BRD_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("ASP_NEWCUST_KEY")) < 0 || StringUtil.length(objJsonParams.getString("ASP_NEWCUST_KEY")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("BRD_ID")) < 0 || StringUtil.length(objJsonParams.getString("BRD_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "BRD_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

    }

}
