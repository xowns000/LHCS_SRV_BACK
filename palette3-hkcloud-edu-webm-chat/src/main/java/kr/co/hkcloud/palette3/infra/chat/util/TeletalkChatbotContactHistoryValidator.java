package kr.co.hkcloud.palette3.infra.chat.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import lombok.extern.slf4j.Slf4j;

/**
 * 챗봇 대화내용 저장 Validation체크
 * @author R&D
 */
@Slf4j
@Component
public class TeletalkChatbotContactHistoryValidator implements Validator
{
    @Override
    public boolean supports(Class<?> clazz)
    {
        return false;
    }

    @Override
	public void validate(Object target, Errors errors) {

		TelewebJSON objJsonParams = (TelewebJSON) target;

		if(StringUtils.length(objJsonParams.getString("CUSTCO_ID")) < 0 || StringUtils.length(objJsonParams.getString("CUSTCO_ID")) > 10) {
    		Object[] arg = new Object[1];
    		arg[0] = "CUSTCO_ID";
    		errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
    	}
    	
    	if(StringUtils.isBlank(objJsonParams.getString("CUSTCO_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
    	
    	if(StringUtils.length(objJsonParams.getString("SNDR_KEY")) < 0 || StringUtils.length(objJsonParams.getString("SNDR_KEY")) > 60) {
    		Object[] arg = new Object[1];
    		arg[0] = "SNDR_KEY";
    		errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
    	}
    	
    	if(StringUtils.isBlank(objJsonParams.getString("SNDR_KEY"))) {
            Object[] arg = new Object[1];
            arg[0] = "SNDR_KEY";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
    	
    	if(StringUtils.length(objJsonParams.getString("user_key")) < 0 || StringUtils.length(objJsonParams.getString("user_key")) > 80) {
    		Object[] arg = new Object[1];
    		arg[0] = "user_key";
    		errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
    	}
    	
    	if(StringUtils.isBlank(objJsonParams.getString("user_key"))) {
            Object[] arg = new Object[1];
            arg[0] = "user_key";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
    	
    	if(StringUtils.length(objJsonParams.getString("SNDRCV_CD")) < 0 || StringUtils.length(objJsonParams.getString("SNDRCV_CD")) > 60) {
    		Object[] arg = new Object[1];
    		arg[0] = "SNDRCV_CD";
    		errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
    	}
    	
    	if(StringUtils.isBlank(objJsonParams.getString("SNDRCV_CD"))) {
            Object[] arg = new Object[1];
            arg[0] = "SNDRCV_CD";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
    	
    	if(objJsonParams.containsKey("TALK_CONTACT_ID") && StringUtils.length(objJsonParams.getString("TALK_CONTACT_ID")) > 90) {
    		Object[] arg = new Object[1];
    		arg[0] = "TALK_CONTACT_ID";
    		errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
    	}
    	
    	if(objJsonParams.containsKey("TYPE") && StringUtils.length(objJsonParams.getString("TYPE")) > 20) {
    		Object[] arg = new Object[1];
    		arg[0] = "TYPE";
    		errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
    	}
    	
    	if(objJsonParams.containsKey("CONTENT") && StringUtils.length(objJsonParams.getString("CONTENT")) > 1000) {
    		Object[] arg = new Object[1];
    		arg[0] = "CONTENT";
    		errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
    	}
    	
    	if(objJsonParams.containsKey("TALK_TITLE") && StringUtils.length(objJsonParams.getString("TALK_TITLE")) > 100) {
    		Object[] arg = new Object[1];
    		arg[0] = "TALK_TITLE";
    		errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
    	}
    	
    	if(objJsonParams.containsKey("CUSTOMER_SEQ") && StringUtils.length(objJsonParams.getString("CUSTOMER_SEQ")) > 12) {
    		Object[] arg = new Object[1];
    		arg[0] = "CUSTOMER_SEQ";
    		errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
    	}
    	
	}
}
