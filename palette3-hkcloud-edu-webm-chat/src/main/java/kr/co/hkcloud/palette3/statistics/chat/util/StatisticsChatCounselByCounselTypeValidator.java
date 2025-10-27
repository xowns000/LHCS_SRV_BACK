package kr.co.hkcloud.palette3.statistics.chat.util;


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import lombok.extern.slf4j.Slf4j;


/**
 * 통계채팅상담(상담유형별) Validation체크
 * 
 * @author 이동욱
 */
@Slf4j
@Component
public class StatisticsChatCounselByCounselTypeValidator implements Validator
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

            if(objJsonParams.getHeaderString("METHOD").toString().equals("inqire")) {

                selectCounselingTypeValidate(target, errors);
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


    //상담유형별 조회유효성
    public void selectCounselingTypeValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.isBlank(objJsonParams.getString("CUSTCO_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("CUSTCO_ID")) < 0 || StringUtils.length(objJsonParams.getString("CUSTCO_ID")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "CUSTCO_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("CNSL_TYP_CD")) < 0 || StringUtils.length(objJsonParams.getString("CNSL_TYP_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "CNSL_TYP_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("CNSL_TYP_CD_2")) < 0 || StringUtils.length(objJsonParams.getString("CNSL_TYP_CD_2")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "CNSL_TYP_CD_2";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("CNSL_TYP_CD_3")) < 0 || StringUtils.length(objJsonParams.getString("CNSL_TYP_CD_3")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "CNSL_TYP_CD_3";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("CNSL_TYP_CD_4")) < 0 || StringUtils.length(objJsonParams.getString("CNSL_TYP_CD_4")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "CNSL_TYP_CD_4";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("SNDR_KEY")) < 0 || StringUtils.length(objJsonParams.getString("SNDR_KEY")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "SNDR_KEY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
    }
}
