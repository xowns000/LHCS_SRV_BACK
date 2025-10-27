package kr.co.hkcloud.palette3.signup.util;

import java.nio.charset.StandardCharsets;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import lombok.extern.slf4j.Slf4j;

/**
 * 회원가입 Validation체크
 * @author R&D
 */
@Slf4j
@Component
public class TalkSignUpValidator implements Validator
{
    @Override
    public boolean supports(Class<?> clazz)
    {
        return false;
    }
    
    public void validate(Object target, Errors errors) {
      TelewebJSON objJsonParams = (TelewebJSON) target;

      if (objJsonParams.getHeaderString("TYPE").toString().equals("BIZ_SERVICE")) {

         if (objJsonParams.getHeaderString("SERVICE").toString().equals("SignUp")) {
            processValidate(target, errors);
         }

      }

   }

  // 회원가입 처리 유효성 검사
    public void processValidate(Object target, Errors errors)
    {
        TelewebJSON objJsonParams = (TelewebJSON) target;
        /*if(StringUtils.isBlank(objJsonParams.getString("REGR_DEPT_CD"))) {
            Object[] arg = new Object[1];
            arg[0] = "REGR_DEPT_CD";
            errors.reject(TelewebMessage.DATA_NOT_INPUT.getCode(), arg, TelewebMessage.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("REGR_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "REGR_ID";
            errors.reject(TelewebMessage.DATA_NOT_INPUT.getCode(), arg, TelewebMessage.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("PWD_STATUS"))) {
            Object[] arg = new Object[1];
            arg[0] = "PWD_STATUS";
            errors.reject(TelewebMessage.DATA_NOT_INPUT.getCode(), arg, TelewebMessage.DATA_NOT_INPUT.getMessage());
        }*/
        if(StringUtils.isBlank(objJsonParams.getString("USER_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("USER_NM"))) {
            Object[] arg = new Object[1];
            arg[0] = "USER_NM";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
      /*  if(StringUtils.isBlank(objJsonParams.getString("PWD"))) {
            Object[] arg = new Object[1];
            arg[0] = "PWD";
            errors.reject(TelewebMessage.DATA_NOT_INPUT.getCode(), arg, TelewebMessage.DATA_NOT_INPUT.getMessage());
        }*/ // 공백이여도 암호화된 값이 넘어옴 
      
        if(StringUtils.isBlank(objJsonParams.getString("EML"))) {
            Object[] arg = new Object[1];
            arg[0] = "EML";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("CO_NM"))) {
            Object[] arg = new Object[1];
            arg[0] = "CO_NM";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("REGR_DEPT_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("REGR_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "REGR_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("PWD_STATUS")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "PWD_STATUS";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("USER_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("USER_NM")) >300 ) {
            Object[] arg = new Object[1];
            arg[0] = "USER_NM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("PWD")) > 300) {
            Object[] arg = new Object[1];
            arg[0] = "PWD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("EML")) > 300) {
            Object[] arg = new Object[1];
            arg[0] = "EML";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("CO_NM")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "CO_NM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

        
    }
    
    
}