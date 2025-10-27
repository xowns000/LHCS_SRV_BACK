package kr.co.hkcloud.palette3.chat.setting.util;


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;


@Slf4j
@Component
public class ChatSettingManageValidator implements Validator
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
        JSONArray objArry = objJsonParams.getDataObject();

        String strEnvCd = ""; // 환경설정코드
        int objArrSize = objArry.size();

        if(objJsonParams.getHeaderString("TYPE").toString().equals("BIZ_SERVICE")) {
            if(objJsonParams.getHeaderString("SERVICE").equals("chat.setting.manage.cnslt-perm-cnt") && objJsonParams.getHeaderString("METHOD").equals("modify")) {
                updateValidate(objJsonParams, errors);
            }
            else if(objJsonParams.getHeaderString("SERVICE").equals("chat.setting.manage")) {
                for(int i = 0; i < objArrSize; i++) {

                    strEnvCd = objArry.getJSONObject(i).getString("STNG_CD");
                    switch(strEnvCd)
                    {
                        case "ROUTE_WAITING_CNT":
                        {
                            waitingCntValidate(objJsonParams, errors);
                            break;
                        }
                        case "CONT_CHATAGENT_CNT":
                        {
                            ChatAgentValidate(objJsonParams, errors);
                            break;
                        }
                        case "INQRY_USE_LVL":
                        {
                            InqryUseLevelValidate(objJsonParams, errors);
                            break;
                        }
                        case "WORK_START_TIME":
                        case "WORK_END_TIME":
                        {
                            updateCnslTimeValidate(objJsonParams, errors);
                            break;
                        }
                    }
                }
            }
            else {
                Object[] arg = new Object[1];
                arg[0] = "BIZ_SERVICE";
                errors.reject(PaletteValidationCode.UNDEFINED_TYPE.getCode(), arg, PaletteValidationCode.UNDEFINED_TYPE.getMessage());
            }
        }
        else {
            Object[] arg = new Object[1];
            arg[0] = "BIZ_SERVICE";
            errors.reject(PaletteValidationCode.UNDEFINED_TYPE.getCode(), arg, PaletteValidationCode.UNDEFINED_TYPE.getMessage());
        }
    }


    public void waitingCntValidate(TelewebJSON telewebJSON, Errors errors)
    {
        JSONArray objArry = telewebJSON.getDataObject();
        String strEnvCd = ""; // 환경설정코드
        int objArrSize = objArry.size();

        for(int i = 0; i < objArrSize; i++) {

            strEnvCd = objArry.getJSONObject(i).getString("STNG_CD");
            if(strEnvCd.equals("ROUTE_WAITING_CNT")) {
                if(StringUtils.isBlank(objArry.getJSONObject(i).getString("STNG_VL"))) {
                    Object[] arg = new Object[1];
                    arg[0] = "STNG_VL";
                    errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
                }
                if(StringUtils.length(objArry.getJSONObject(i).getString("STNG_VL")) < 0 || StringUtils.length(objArry.getJSONObject(i).getString("STNG_VL")) > 600) {
                    Object[] arg = new Object[1];
                    arg[0] = "STNG_VL";
                    errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
                }
            }
        }

    }


    public void ChatAgentValidate(TelewebJSON telewebJSON, Errors errors)
    {
        JSONArray objArry = telewebJSON.getDataObject();
        String strEnvCd = ""; // 환경설정코드
        int objArrSize = objArry.size();

        for(int i = 0; i < objArrSize; i++) {
            strEnvCd = objArry.getJSONObject(i).getString("STNG_CD");
            if(strEnvCd.equals("CONT_CHATAGENT_CNT")) {

                if(StringUtils.isBlank(objArry.getJSONObject(i).getString("STNG_VL"))) {
                    Object[] arg = new Object[1];
                    arg[0] = "STNG_VL";
                    errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
                }

                if(StringUtils.length(objArry.getJSONObject(i).getString("STNG_VL")) < 0 || StringUtils.length(objArry.getJSONObject(i).getString("STNG_VL")) > 600) {
                    Object[] arg = new Object[1];
                    arg[0] = "STNG_VL";
                    errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
                }

            }
        }

    }


    public void InqryUseLevelValidate(TelewebJSON telewebJSON, Errors errors)
    {
        JSONArray objArry = telewebJSON.getDataObject();
        String strEnvCd = ""; // 환경설정코드
        int objArrSize = objArry.size();

        for(int i = 0; i < objArrSize; i++) {

            strEnvCd = objArry.getJSONObject(i).getString("STNG_CD");
            if(strEnvCd.equals("INQRY_USE_LVL")) {
                if(StringUtils.isBlank(objArry.getJSONObject(i).getString("STNG_VL"))) {
                    Object[] arg = new Object[1];
                    arg[0] = "STNG_VL";
                    errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
                }
                if(StringUtils.length(objArry.getJSONObject(i).getString("STNG_VL")) < 0 || StringUtils.length(objArry.getJSONObject(i).getString("STNG_VL")) > 600) {
                    Object[] arg = new Object[1];
                    arg[0] = "STNG_VL";
                    errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
                }
            }
        }

    }


    // 상담시간 설정 저장 유효성 검사
    public void updateCnslTimeValidate(TelewebJSON telewebJSON, Errors errors)
    {
        JSONArray objArry = telewebJSON.getDataObject();

        String strEnvCd = ""; // 환경설정코드
        int objArrSize = objArry.size();

        if(StringUtils.isBlank(telewebJSON.getString("PROC_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "PROC_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.length(telewebJSON.getString("PROC_ID")) < 0 || StringUtils.length(telewebJSON.getString("PROC_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "PROC_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

        for(int i = 0; i < objArrSize; i++) {

            strEnvCd = objArry.getJSONObject(i).getString("STNG_CD");

            if(strEnvCd.equals("WORK_START_TIME")) {

                if(StringUtils.isBlank(objArry.getJSONObject(i).getString("STNG_VL"))) {
                    Object[] arg = new Object[1];
                    arg[0] = "STNG_VL";
                    errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
                }
                if(StringUtils.length(objArry.getJSONObject(i).getString("STNG_VL")) < 0 || StringUtils.length(objArry.getJSONObject(i).getString("STNG_VL")) > 8) {
                    Object[] arg = new Object[1];
                    arg[0] = "STNG_VL";
                    errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
                }
            }
            if(strEnvCd.equals("WORK_END_TIME")) {
                if(StringUtils.isBlank(objArry.getJSONObject(i).getString("STNG_VL"))) {
                    Object[] arg = new Object[1];
                    arg[0] = "STNG_VL";
                    errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
                }
                if(StringUtils.length(objArry.getJSONObject(i).getString("STNG_VL")) < 0 || StringUtils.length(objArry.getJSONObject(i).getString("STNG_VL")) > 8) {
                    Object[] arg = new Object[1];
                    arg[0] = "STNG_VL";
                    errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
                }
            }
            if(StringUtils.isBlank(objArry.getJSONObject(i).getString("ASP_NEWCUST_KEY"))) {
                Object[] arg = new Object[1];
                arg[0] = "ASP_NEWCUST_KEY";
                errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
            }

            if(StringUtils.length(objArry.getJSONObject(i).getString("ASP_NEWCUST_KEY")) < 0 || StringUtils.length(objArry.getJSONObject(i).getString("ASP_NEWCUST_KEY")) > 150) {
                Object[] arg = new Object[1];
                arg[0] = "ASP_NEWCUST_KEY";
                errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
            }
            if(StringUtils.length(objArry.getJSONObject(i).getString("STNG_CD_NM")) < 0 || StringUtils.length(objArry.getJSONObject(i).getString("STNG_CD_NM")) > 300) {
                Object[] arg = new Object[1];
                arg[0] = "STNG_CD_NM";
                errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
            }
            if(StringUtils.length(strEnvCd) < 0 || StringUtils.length(strEnvCd) > 150) {
                Object[] arg = new Object[1];
                arg[0] = "STNG_CD";
                errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
            }
        }
    }


    // 파레트메인 > 상담설정 개인상담허용수 저장(수정) 유효성 체크 
    public void updateValidate(Object target, Errors errors)
    {
        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.isBlank(objJsonParams.getString("USER_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("ASP_NEWCUST_KEY"))) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("MAX_CHAT_AGENT"))) {
            Object[] arg = new Object[1];
            arg[0] = "MAX_CHAT_AGENT";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("USER_ID")) < 0 || StringUtils.length(objJsonParams.getString("USER_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("ASP_NEWCUST_KEY")) < 0 || StringUtils.length(objJsonParams.getString("ASP_NEWCUST_KEY")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("MAX_CHAT_AGENT")) < 0) {
            Object[] arg = new Object[1];
            arg[0] = "MAX_CHAT_AGENT";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
    }
}
