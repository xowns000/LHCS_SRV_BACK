package kr.co.hkcloud.palette3.chat.dashboard.util;


import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class ChatDashboardCounselValidator implements Validator
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
                    case "chat.dashboard.counsel.cnslt-requst-sttus-by-plus-frnd":
                    case "selectRtnMonitoring": //사용안함
                    case "chat.dashboard.counsel.dashboard-data":
                    case "chat.dashboard.counsel.dashboard-table-data":
                    case "chat.dashboard.counsel.rcept-sttus-by-time":
                    case "chat.dashboard.counsel.cnslt-requst-sttus-by-svc":
                    case "chat.dashboard.counsel.compt-sttus-by-time":
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


    // 조회 유효성
    public void selectValidate(TelewebJSON telewebJSON, Errors errors)
    {

        if(StringUtils.isBlank(telewebJSON.getString("CUSTCO_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.length(telewebJSON.getString("CUSTCO_ID")) < 0 || StringUtils.length(telewebJSON.getString("CUSTCO_ID")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

    }
}
