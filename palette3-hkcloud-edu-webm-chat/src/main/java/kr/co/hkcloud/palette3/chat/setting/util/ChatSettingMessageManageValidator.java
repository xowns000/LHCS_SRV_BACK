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
 * 메세지설정 Validation체크
 * 
 * @author R&D
 */
@Slf4j
@Component
public class ChatSettingMessageManageValidator implements Validator
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
            if(objJsonParams.getHeaderString("METHOD").equals("inqire")) {
                switch(objJsonParams.getHeaderString("SERVICE"))
                {
                    case "chat.setting.message-manage.cstmr-nthg-rspons":
                    {
                        selectNoRespValidate(objJsonParams, errors);
                        break;
                    }
                    case "chat.setting.message-manage.rcept-delay":
                    case "chat.setting.message-manage.rspons-delay":
                    {
                        selectValidate(objJsonParams, errors);
                        break;
                    }
                    default: // SERVICE값이 없을 때
                    {
                        Object[] arg = new Object[1];
                        arg[0] = "SERVICE";
                        errors.reject(PaletteValidationCode.UNDEFINED_SERVICE.getCode(), arg, PaletteValidationCode.UNDEFINED_SERVICE.getMessage());
                        break;
                    }
                }
            }
            else if(objJsonParams.getHeaderString("METHOD").equals("regist")) {
                insertValidate(objJsonParams, errors);
            }
            else if(objJsonParams.getHeaderString("METHOD").equals("delete")) {
                deleteValidate(objJsonParams, errors);
            }
            else if(objJsonParams.getHeaderString("METHOD").equals("list")) {
                selectValidate(objJsonParams, errors);
            }
            else {
                Object[] arg = new Object[1];
                arg[0] = "METHOD";
                errors.reject(PaletteValidationCode.UNDEFINED_METHOD.getCode(), arg, PaletteValidationCode.UNDEFINED_METHOD.getMessage());
            }

        }
        else // BIZ_SERVICE값이 없을 때
        {
            Object[] arg = new Object[1];
            arg[0] = "BIZ_SERVICE";
            errors.reject(PaletteValidationCode.UNDEFINED_TYPE.getCode(), arg, PaletteValidationCode.UNDEFINED_TYPE.getMessage());
        }

    }


    //고객무응답조회유효성
    public void selectNoRespValidate(TelewebJSON telewebJSON, Errors errors)
    {

        if(StringUtil.length(telewebJSON.getString("ASP_NEWCUST_KEY")) < 0 || StringUtil.length(telewebJSON.getString("ASP_NEWCUST_KEY")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(telewebJSON.getString("MSG_CL")) < 0 || StringUtil.length(telewebJSON.getString("MSG_CL")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "MSG_CL";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.isBlank(telewebJSON.getString("ASP_NEWCUST_KEY"))) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(telewebJSON.getString("MSG_CL"))) {
            Object[] arg = new Object[1];
            arg[0] = "MSG_CL";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
    }


    //조회 유효성
    public void selectValidate(TelewebJSON telewebJSON, Errors errors)
    {

        if(StringUtil.length(telewebJSON.getString("ASP_NEWCUST_KEY")) < 0 || StringUtil.length(telewebJSON.getString("ASP_NEWCUST_KEY")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(telewebJSON.getString("SYS_MSG_ID")) < 0 || StringUtil.length(telewebJSON.getString("SYS_MSG_ID")) > 75) {
            Object[] arg = new Object[1];
            arg[0] = "SYS_MSG_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(telewebJSON.getString("MSG_CL")) < 0 || StringUtil.length(telewebJSON.getString("MSG_CL")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "MSG_CL";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.isBlank(telewebJSON.getString("ASP_NEWCUST_KEY"))) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(telewebJSON.getString("SYS_MSG_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "SYS_MSG_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }

    }


    //메세지 삽입유효성
    public void insertValidate(TelewebJSON telewebJSON, Errors errors)
    {

        if(StringUtils.isBlank(telewebJSON.getString("ASP_NEWCUST_KEY"))) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(telewebJSON.getString("MSG_CL"))) {
            Object[] arg = new Object[1];
            arg[0] = "MSG_CL";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(telewebJSON.getString("SNDRCV_CD"))) {
            Object[] arg = new Object[1];
            arg[0] = "SNDRCV_CD";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(telewebJSON.getString("MSG_TYPE"))) {
            Object[] arg = new Object[1];
            arg[0] = "MSG_TYPE";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(telewebJSON.getString("USER_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(telewebJSON.getString("USE_YN"))) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(telewebJSON.getString("MSG_TIME"))) {
            Object[] arg = new Object[1];
            arg[0] = "MSG_TIME";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(telewebJSON.getString("MSG_CN"))) {
            Object[] arg = new Object[1];
            arg[0] = "MSG_CN";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.length(telewebJSON.getString("ASP_NEWCUST_KEY")) < 0 || StringUtils.length(telewebJSON.getString("ASP_NEWCUST_KEY")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(telewebJSON.getString("MSG_CL")) < 0 || StringUtils.length(telewebJSON.getString("MSG_CL")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "MSG_CL";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(telewebJSON.getString("SNDRCV_CD")) < 0 || StringUtils.length(telewebJSON.getString("SNDRCV_CD")) > 30) {
            Object[] arg = new Object[1];
            arg[0] = "SNDRCV_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(telewebJSON.getString("MSG_TYPE")) < 0 || StringUtils.length(telewebJSON.getString("MSG_TYPE")) > 6) {
            Object[] arg = new Object[1];
            arg[0] = "MSG_TYPE";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(telewebJSON.getString("USER_ID")) < 0 || StringUtils.length(telewebJSON.getString("USER_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(telewebJSON.getString("USE_YN")) < 0 || StringUtils.length(telewebJSON.getString("USE_YN")) > 1) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(telewebJSON.getString("MSG_TIME")) < 0 || StringUtils.length(telewebJSON.getString("MSG_TIME")) > 11) {
            Object[] arg = new Object[1];
            arg[0] = "MSG_TIME";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(telewebJSON.getString("MSG_CN")) < 0 || StringUtils.length(telewebJSON.getString("MSG_CN")) > 3000) {
            Object[] arg = new Object[1];
            arg[0] = "MSG_CN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(telewebJSON.getString("DEPT_CODE")) < 0 || StringUtils.length(telewebJSON.getString("DEPT_CODE")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "DEPT_CODE";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(telewebJSON.getString("MSG_DESC")) < 0 || StringUtils.length(telewebJSON.getString("MSG_DESC")) > 600) {
            Object[] arg = new Object[1];
            arg[0] = "MSG_DESC";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(telewebJSON.getString("LINK_TYPE")) < 0 || StringUtils.length(telewebJSON.getString("LINK_TYPE")) > 6) {
            Object[] arg = new Object[1];
            arg[0] = "LINK_TYPE";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

    }


    //메세지삭제 유효성
    public void deleteValidate(TelewebJSON telewebJSON, Errors errors)
    {

        if(StringUtils.length(telewebJSON.getString("ASP_NEWCUST_KEY")) < 0 || StringUtils.length(telewebJSON.getString("ASP_NEWCUST_KEY")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(telewebJSON.getString("SYS_MSG_ID")) < 0 || StringUtils.length(telewebJSON.getString("SYS_MSG_ID")) > 70) {
            Object[] arg = new Object[1];
            arg[0] = "SYS_MSG_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

        if(StringUtils.isBlank(telewebJSON.getString("ASP_NEWCUST_KEY"))) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(telewebJSON.getString("SYS_MSG_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "SYS_MSG_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
    }

}
