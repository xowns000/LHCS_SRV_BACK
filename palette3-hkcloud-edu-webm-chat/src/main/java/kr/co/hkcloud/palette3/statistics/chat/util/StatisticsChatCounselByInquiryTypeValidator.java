package kr.co.hkcloud.palette3.statistics.chat.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import lombok.extern.slf4j.Slf4j;

/**
 * 통계채팅상담(문의유형별) Validation체크
 * 
 * @author 이동욱
 */
@Slf4j
@Component
public class StatisticsChatCounselByInquiryTypeValidator implements Validator {
	@Override
	public boolean supports(Class<?> clazz) {
		return false;
	}

	@Override
	public void validate(Object target, Errors errors) {
		TelewebJSON objJsonParams = (TelewebJSON) target;
		
		if(objJsonParams.getHeaderString("TYPE").toString().equals("BIZ_SERVICE")) {
        	
        	if (objJsonParams.getHeaderString("METHOD").toString().equals("inqire")){
            	
        		selectStatisticsByInqryTypeValidate(target, errors);
            }else {
            	 Object[] arg = new Object[1];
                 arg[0] = "METHOD";
                 errors.reject(PaletteValidationCode.UNDEFINED_METHOD.getCode(), arg, PaletteValidationCode.UNDEFINED_METHOD.getMessage());
            }
        }else {
        	 Object[] arg = new Object[1];
             arg[0] = "BIZ_SERVICE";
             errors.reject(PaletteValidationCode.UNDEFINED_TYPE.getCode(), arg, PaletteValidationCode.UNDEFINED_TYPE.getMessage());
        }
	}
	
	//상담문의유형별통계 조회 유효성
    public void selectStatisticsByInqryTypeValidate(Object target, Errors errors) {
    	
    	TelewebJSON objJsonParams = (TelewebJSON) target;
    	
    	if(objJsonParams.getString("SEARCH_TYPE").toString().equals("MONTH")) {
            if(StringUtils.isBlank(objJsonParams.getString("SEARCH_MONTH_FROM"))) {
                Object[] arg = new Object[1];
                arg[0] = "SEARCH_MONTH_FROM";
                errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
            }
            if(StringUtils.isBlank(objJsonParams.getString("SEARCH_MONTH_TO"))) {
                Object[] arg = new Object[1];
                arg[0] = "SEARCH_MONTH_TO";
                errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
            }  
        }else if(objJsonParams.getString("SEARCH_TYPE").toString().equals("DAY")) {
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
        }else if(objJsonParams.getString("SEARCH_TYPE").toString().equals("YEAR")) {
            if(StringUtils.isBlank(objJsonParams.getString("SEARCH_YEAR"))) {
                Object[] arg = new Object[1];
                arg[0] = "SEARCH_YEAR";
                errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
            }
        }
    	if(StringUtils.length(objJsonParams.getString("INQRY_TYP_CD_1LEVEL")) <0 || StringUtils.length(objJsonParams.getString("INQRY_TYP_CD_1LEVEL")) > 60) {
	    	Object[] arg = new Object[1];
	    	arg[0] = "INQRY_TYP_CD_1LEVEL";
	    	errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg,PaletteValidationCode.EXCEED_INPUT.getMessage());
    	}
    	if(StringUtils.length(objJsonParams.getString("INQRY_TYP_CD_2LEVEL")) <0 || StringUtils.length(objJsonParams.getString("INQRY_TYP_CD_2LEVEL")) > 60) {
	    	Object[] arg = new Object[1];
	    	arg[0] = "INQRY_TYP_CD_2LEVEL";
	    	errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg,PaletteValidationCode.EXCEED_INPUT.getMessage());
    	}
    	if(StringUtils.length(objJsonParams.getString("SNDR_KEY")) <0 || StringUtils.length(objJsonParams.getString("SNDR_KEY")) > 60) {
	    	Object[] arg = new Object[1];
	    	arg[0] = "SNDR_KEY";
	    	errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg,PaletteValidationCode.EXCEED_INPUT.getMessage());
    	}
    }
}
