package kr.co.hkcloud.palette3.setting.system.util;


import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.netty.util.internal.StringUtil;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;


/**
 * 메뉴권한관리 Validation체크
 * 
 * @author R&D
 */
@Slf4j
@Component
public class SettingSystemMenuAuthorityManageValidator implements Validator
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

        if(objJsonParams.getHeaderString("TYPE").toString().equals("XML_SERVICE")) {
            Object[] arg = new Object[1];
            arg[0] = "SQL_NM";
            errors.reject(PaletteValidationCode.UNDEFINED_METHOD.getCode(), arg, PaletteValidationCode.UNDEFINED_METHOD.getMessage());
        }
        else if(objJsonParams.getHeaderString("TYPE").toString().equals("BIZ_SERVICE")) {
            if(objJsonParams.getHeaderString("METHOD").toString().equals("inqire")) {
                //사용자별 할당 정보 조회
                if(objJsonParams.getHeaderString("SERVICE").toString().equals("setting.system.menu-author-manage.asgn-info-by-agent")) {
                    selectRtnAuthGroupMngValidate(target, errors);
                }
                //할당, 미할당 모두 같은 벨리데이션 체크
                else {
                    selectValidate(target, errors);
                }
            }
            else if(objJsonParams.getHeaderString("METHOD").toString().equals("regist")) {
                proecssValidate(target, errors);
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


    //조회 유효성
    public void selectValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.isBlank(objJsonParams.getString("MENU_GROUP"))) {
            Object[] arg = new Object[1];
            arg[0] = "MENU_GROUP";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("MENU_GROUP")) < 0 || StringUtil.length(objJsonParams.getString("MENU_GROUP")) > 90) {
            Object[] arg = new Object[1];
            arg[0] = "MENU_GROUP";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("ATRT_GROUP_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "ATRT_GROUP_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("ATRT_GROUP_ID")) < 0 || StringUtil.length(objJsonParams.getString("ATRT_GROUP_ID")) > 90) {
            Object[] arg = new Object[1];
            arg[0] = "ATRT_GROUP_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
    }


    public void proecssValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;
        JSONArray objArry = objJsonParams.getDataObject();

        if(!objArry.isEmpty()) {
            for(int i = 0; i < objArry.size(); i++) {
                if(objJsonParams.getString("DATA_FLAG", i).equals(TwbCmmnConst.TRANS_DEL)) {
                    if(StringUtils.isBlank(objJsonParams.getString("ATRT_GROUP_ID", i))) {
                        Object[] arg = new Object[1];
                        arg[0] = "ATRT_GROUP_ID";
                        errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
                    }
                    if(StringUtil.length(objJsonParams.getString("ATRT_GROUP_ID", i)) < 0 || StringUtil.length(objJsonParams.getString("ATRT_GROUP_ID", i)) > 30) {
                        Object[] arg = new Object[1];
                        arg[0] = "ATRT_GROUP_ID";
                        errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
                    }
                    if(StringUtil.length(objJsonParams.getString("MENU_ID", i)) < 0 || StringUtil.length(objJsonParams.getString("MENU_ID", i)) > 90) {
                        Object[] arg = new Object[1];
                        arg[0] = "MENU_ID";
                        errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
                    }
                }
                else if(objJsonParams.getString("DATA_FLAG", i).equals(TwbCmmnConst.TRANS_INS)) {
                    if(StringUtils.isBlank(objJsonParams.getString("ATRT_GROUP_ID", i))) {
                        Object[] arg = new Object[1];
                        arg[0] = "ATRT_GROUP_ID";
                        errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
                    }
                    if(StringUtils.isBlank(objJsonParams.getString("MENU_ID", i))) {
                        Object[] arg = new Object[1];
                        arg[0] = "MENU_ID";
                        errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
                    }
                    if(StringUtils.isBlank(objJsonParams.getString("REGR_DEPT_CD", i))) {
                        Object[] arg = new Object[1];
                        arg[0] = "REGR_DEPT_CD";
                        errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
                    }
                    if(StringUtils.isBlank(objJsonParams.getString("REGR_ID", i))) {
                        Object[] arg = new Object[1];
                        arg[0] = "REGR_ID";
                        errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
                    }
                    if(StringUtils.isBlank(objJsonParams.getString("PROC_ID", i))) {
                        Object[] arg = new Object[1];
                        arg[0] = "PROC_ID";
                        errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
                    }
                    if(StringUtil.length(objJsonParams.getString("ATRT_GROUP_ID", i)) < 0 || StringUtil.length(objJsonParams.getString("ATRT_GROUP_ID", i)) > 90) {
                        Object[] arg = new Object[1];
                        arg[0] = "ATRT_GROUP_ID";
                        errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
                    }
                    if(StringUtil.length(objJsonParams.getString("MENU_ID", i)) < 0 || StringUtil.length(objJsonParams.getString("MENU_ID", i)) > 90) {
                        Object[] arg = new Object[1];
                        arg[0] = "MENU_ID";
                        errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
                    }
                    if(StringUtil.length(objJsonParams.getString("REGR_DEPT_CD", i)) < 0 || StringUtil.length(objJsonParams.getString("REGR_DEPT_CD", i)) > 90) {
                        Object[] arg = new Object[1];
                        arg[0] = "REGR_DEPT_CD";
                        errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
                    }
                    if(StringUtil.length(objJsonParams.getString("REGR_ID", i)) < 0 || StringUtil.length(objJsonParams.getString("REGR_ID", i)) > 90) {
                        Object[] arg = new Object[1];
                        arg[0] = "REGR_ID";
                        errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
                    }
                    if(StringUtil.length(objJsonParams.getString("REGR_ID")) < 0 || StringUtil.length(objJsonParams.getString("REGR_ID")) > 90) {
                        Object[] arg = new Object[1];
                        arg[0] = "REGR_ID";
                        errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
                    }
                    if(StringUtil.length(objJsonParams.getString("PROC_ID", i)) < 0 || StringUtil.length(objJsonParams.getString("PROC_ID", i)) > 90) {
                        Object[] arg = new Object[1];
                        arg[0] = "PROC_ID";
                        errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
                    }
                }

            }
        }
    }


    public void selectRtnAuthGroupMngValidate(Object target, Errors errors)
    {
        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.isBlank(objJsonParams.getString("USER_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_INPUT.getCode(), arg, PaletteValidationCode.DATA_NOT_INPUT.getMessage());
        }
        if(StringUtils.length(objJsonParams.getString("USER_ID")) < 0 || StringUtils.length(objJsonParams.getString("USER_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "USER_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

    }
}
