package kr.co.hkcloud.palette3.chat.status.util;


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class ChatStatusAgentValidator implements Validator
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
            if(objJsonParams.getHeaderString("TYPE").toString().equals("BIZ_SERVICE")) {
                switch(objJsonParams.getHeaderString("SERVICE"))
                {
                    case "chat.status.agent.dashboard":
                    {
                        selectRtnAgentMonitoringValidate(objJsonParams, errors);
                        break;
                    }
                    case "chat.status.agent.sttus":
                    {
                        selectRtnAgentMonitoringStatusValidate(objJsonParams, errors);
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
        else {
            Object[] arg = new Object[1];
            arg[0] = "BIZ_SERVICE";
            errors.reject(PaletteValidationCode.UNDEFINED_TYPE.getCode(), arg, PaletteValidationCode.UNDEFINED_TYPE.getMessage());
        }
    }


    /**
     * 상담사대시보드 조회 유효성
     * 
     * @param target
     * @param errors
     */
    public void selectRtnAgentMonitoringValidate(TelewebJSON telewebJSON, Errors errors)
    {
        return;
    }


    /**
     * 상담사모니터링현황 조회 유효성
     * 
     * @param target
     * @param errors
     */
    public void selectRtnAgentMonitoringStatusValidate(TelewebJSON telewebJSON, Errors errors)
    {
        if(StringUtils.isBlank(telewebJSON.getString("DASHBOARD_TYPE"))) {
            Object[] arg = new Object[1];
            arg[0] = "DASHBOARD_TYPE";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.length(telewebJSON.getString("DASHBOARD_TYPE")) < 0 && StringUtils.length(telewebJSON.getString("DASHBOARD_TYPE")) > 15) {
            Object[] arg = new Object[1];
            arg[0] = "DASHBOARD_TYPE";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        DashboardType dashboardType = DashboardType.valueOf(telewebJSON.getString("DASHBOARD_TYPE"));
        switch(dashboardType)
        {
            case ALL:
            case LOGGING_IN:
            case READY:
            case IN_CONSULTATION:
            case AWAY:
            case LOGOUT:
            {
                break;
            }
            default:
            {
                Object[] arg = new Object[1];
                arg[0] = "DASHBOARD_TYPE";
                errors.reject(PaletteValidationCode.DATA_IS_NOT_ALLOWED.getCode(), arg, PaletteValidationCode.DATA_IS_NOT_ALLOWED.getMessage());
            }
        }
    }


    private enum DashboardType
    {
        ALL, LOGGING_IN, READY, IN_CONSULTATION, AWAY, LOGOUT
    }
}
