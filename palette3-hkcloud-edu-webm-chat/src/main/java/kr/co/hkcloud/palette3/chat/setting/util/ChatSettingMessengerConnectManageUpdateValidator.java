package kr.co.hkcloud.palette3.chat.setting.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import lombok.extern.slf4j.Slf4j;

/**
 * 채널 연동목록 신규등록  Validation체크
 * @author R&D
 */
@Slf4j
@Component
public class ChatSettingMessengerConnectManageUpdateValidator implements Validator {@Override
	public boolean supports(Class<?> clazz) {
		return false;
	}

	@Override
	public void validate(Object target, Errors errors) {
		TelewebJSON objJsonParams = (TelewebJSON) target;
        
        if(StringUtils.isBlank(objJsonParams.getString("ASP_CUST_INFO"))) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_CUST_INFO";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("CHN_CLSF_CD"))) {
            Object[] arg = new Object[1];
            arg[0] = "CHN_CLSF_CD";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("UUID"))) {
            Object[] arg = new Object[1];
            arg[0] = "UUID";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("DSPTCH_PRF_KEY"))) {
            Object[] arg = new Object[1];
            arg[0] = "DSPTCH_PRF_KEY";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("DSPTCH_PRF_NM"))) {
            Object[] arg = new Object[1];
            arg[0] = "DSPTCH_PRF_NM";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
	}

}
