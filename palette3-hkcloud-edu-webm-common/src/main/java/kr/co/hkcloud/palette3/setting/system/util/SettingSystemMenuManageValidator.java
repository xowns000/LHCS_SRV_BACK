package kr.co.hkcloud.palette3.setting.system.util;


import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.netty.util.internal.StringUtil;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import lombok.extern.slf4j.Slf4j;


/**
 * 메뉴관리 Validation체크
 * 
 * @author R&D
 */
@Slf4j
@Component
public class SettingSystemMenuManageValidator implements Validator
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
                switch(objJsonParams.getHeaderString("SERVICE"))
                {
                    case "setting.system.menu-manage.tr":
                    {
                        selectTreeValidate(target, errors);
                        break;
                    }
                    case "setting.system.menu-manage":
                    {
                        selectValidate(target, errors);
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
            else if(objJsonParams.getHeaderString("METHOD").toString().equals("regist")) {
                processValidate(target, errors);
            }
            else if(objJsonParams.getHeaderString("METHOD").toString().equals("delete")) {
                switch(objJsonParams.getHeaderString("SERVICE"))
                {
                    case "setting.system.menu-manage":
                    {
                        deleteValidate(target, errors);
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


    //조회 유효성
    public void selectTreeValidate(Object target, Errors errors)
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
        if(StringUtil.length(objJsonParams.getString("USE_YN")) < 0 || StringUtil.length(objJsonParams.getString("USE_YN")) > 1) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
    }


    //상세조회 유효성
    public void selectValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtil.length(objJsonParams.getString("MENU_ID")) < 0 || StringUtil.length(objJsonParams.getString("MENU_ID")) > 90) {
            Object[] arg = new Object[1];
            arg[0] = "MENU_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("SPST_MENU_ID")) < 0 || StringUtil.length(objJsonParams.getString("SPST_MENU_ID")) > 90) {
            Object[] arg = new Object[1];
            arg[0] = "SPST_MENU_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("LOWRNK_MENU_ID")) < 0 || StringUtil.length(objJsonParams.getString("LOWRNK_MENU_ID")) > 90) {
            Object[] arg = new Object[1];
            arg[0] = "LOWRNK_MENU_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("MENU_DIV_CD")) < 0 || StringUtil.length(objJsonParams.getString("MENU_DIV_CD")) > 2) {
            Object[] arg = new Object[1];
            arg[0] = "MENU_DIV_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("MENU_NM")) < 0 || StringUtil.length(objJsonParams.getString("MENU_NM")) > 300) {
            Object[] arg = new Object[1];
            arg[0] = "MENU_NM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("MENU_XPLN")) < 0 || StringUtil.length(objJsonParams.getString("MENU_XPLN")) > 1500) {
            Object[] arg = new Object[1];
            arg[0] = "MENU_XPLN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("PUP_FILE_NM")) < 0 || StringUtil.length(objJsonParams.getString("PUP_FILE_NM")) > 600) {
            Object[] arg = new Object[1];
            arg[0] = "PUP_FILE_NM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("PGM_PARM")) < 0 || StringUtil.length(objJsonParams.getString("PGM_PARM")) > 600) {
            Object[] arg = new Object[1];
            arg[0] = "PGM_PARM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("VIEW_TRGT")) < 0 || StringUtil.length(objJsonParams.getString("VIEW_TRGT")) > 300) {
            Object[] arg = new Object[1];
            arg[0] = "VIEW_TRGT";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("PATH_NM")) < 0 || StringUtil.length(objJsonParams.getString("PATH_NM")) > 300) {
            Object[] arg = new Object[1];
            arg[0] = "PATH_NM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("PUP_WIDTH_SIZE")) < 0 || StringUtil.length(objJsonParams.getString("PUP_WIDTH_SIZE")) > 11) {
            Object[] arg = new Object[1];
            arg[0] = "PUP_WIDTH_SIZE";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("PUP_HGHT_SIZE")) < 0 || StringUtil.length(objJsonParams.getString("PUP_HGHT_SIZE")) > 11) {
            Object[] arg = new Object[1];
            arg[0] = "PUP_HGHT_SIZE";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("ICON_CLASS_NM")) < 0 || StringUtil.length(objJsonParams.getString("ICON_CLASS_NM")) > 120) {
            Object[] arg = new Object[1];
            arg[0] = "ICON_CLASS_NM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("SELECT_ATRT")) < 0 || StringUtil.length(objJsonParams.getString("SELECT_ATRT")) > 4) {
            Object[] arg = new Object[1];
            arg[0] = "SELECT_ATRT";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("PROC_ATRT")) < 0 || StringUtil.length(objJsonParams.getString("PROC_ATRT")) > 4) {
            Object[] arg = new Object[1];
            arg[0] = "PROC_ATRT";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("DEL_ATRT")) < 0 || StringUtil.length(objJsonParams.getString("DEL_ATRT")) > 4) {
            Object[] arg = new Object[1];
            arg[0] = "DEL_ATRT";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("OUTPUT_XPLN")) < 0 || StringUtil.length(objJsonParams.getString("OUTPUT_XPLN")) > 4) {
            Object[] arg = new Object[1];
            arg[0] = "OUTPUT_XPLN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("EXCEL_ATRT")) < 0 || StringUtil.length(objJsonParams.getString("EXCEL_ATRT")) > 4) {
            Object[] arg = new Object[1];
            arg[0] = "EXCEL_ATRT";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("ADT_ATRT")) < 0 || StringUtil.length(objJsonParams.getString("ADT_ATRT")) > 4) {
            Object[] arg = new Object[1];
            arg[0] = "ADT_ATRT";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("USE_YN")) < 0 || StringUtil.length(objJsonParams.getString("USE_YN")) > 1) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("SORT_ORD")) < 0 || StringUtil.length(objJsonParams.getString("SORT_ORD")) > 11) {
            Object[] arg = new Object[1];
            arg[0] = "SORT_ORD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("REGR_DEPT_CD")) < 0 || StringUtil.length(objJsonParams.getString("REGR_DEPT_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "REGR_DEPT_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("REGR_ID")) < 0 || StringUtil.length(objJsonParams.getString("REGR_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "REGR_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("AMDR_DEPT_CD")) < 0 || StringUtil.length(objJsonParams.getString("AMDR_DEPT_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "AMDR_DEPT_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("AMDR_ID")) < 0 || StringUtil.length(objJsonParams.getString("AMDR_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "AMDR_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("UPD_DTTM")) < 0 || StringUtil.length(objJsonParams.getString("UPD_DTTM")) > 26) {
            Object[] arg = new Object[1];
            arg[0] = "UPD_DTTM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("PROC_ID")) < 0 || StringUtil.length(objJsonParams.getString("PROC_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "PROC_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

    }


    //삽입 유효성
    public void processValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.isBlank(objJsonParams.getString("MENU_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "MENU_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("LOWRNK_MENU_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "LOWRNK_MENU_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("MENU_NM"))) {
            Object[] arg = new Object[1];
            arg[0] = "MENU_NM";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("VIEW_TRGT"))) {
            Object[] arg = new Object[1];
            arg[0] = "VIEW_TRGT";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("USE_YN"))) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(objJsonParams.getString("SORT_ORD"))) {
            Object[] arg = new Object[1];
            arg[0] = "SORT_ORD";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("MENU_ID")) < 0 || StringUtil.length(objJsonParams.getString("MENU_ID")) > 90) {
            Object[] arg = new Object[1];
            arg[0] = "MENU_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("SPST_MENU_ID")) < 0 || StringUtil.length(objJsonParams.getString("SPST_MENU_ID")) > 90) {
            Object[] arg = new Object[1];
            arg[0] = "SPST_MENU_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("LOWRNK_MENU_ID")) < 0 || StringUtil.length(objJsonParams.getString("LOWRNK_MENU_ID")) > 90) {
            Object[] arg = new Object[1];
            arg[0] = "LOWRNK_MENU_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("MENU_DIV_CD")) < 0 || StringUtil.length(objJsonParams.getString("MENU_DIV_CD")) > 2) {
            Object[] arg = new Object[1];
            arg[0] = "MENU_DIV_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("MENU_NM")) < 0 || StringUtil.length(objJsonParams.getString("MENU_NM")) > 300) {
            Object[] arg = new Object[1];
            arg[0] = "MENU_NM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("MENU_XPLN")) < 0 || StringUtil.length(objJsonParams.getString("MENU_XPLN")) > 1500) {
            Object[] arg = new Object[1];
            arg[0] = "MENU_XPLN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("PUP_FILE_NM")) < 0 || StringUtil.length(objJsonParams.getString("PUP_FILE_NM")) > 600) {
            Object[] arg = new Object[1];
            arg[0] = "PUP_FILE_NM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("PGM_PARM")) < 0 || StringUtil.length(objJsonParams.getString("PGM_PARM")) > 600) {
            Object[] arg = new Object[1];
            arg[0] = "PGM_PARM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("VIEW_TRGT")) < 0 || StringUtil.length(objJsonParams.getString("VIEW_TRGT")) > 300) {
            Object[] arg = new Object[1];
            arg[0] = "VIEW_TRGT";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("PATH_NM")) < 0 || StringUtil.length(objJsonParams.getString("PATH_NM")) > 300) {
            Object[] arg = new Object[1];
            arg[0] = "PATH_NM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("PUP_WIDTH_SIZE")) < 0 || StringUtil.length(objJsonParams.getString("PUP_WIDTH_SIZE")) > 11) {
            Object[] arg = new Object[1];
            arg[0] = "PUP_WIDTH_SIZE";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("PUP_HGHT_SIZE")) < 0 || StringUtil.length(objJsonParams.getString("PUP_HGHT_SIZE")) > 11) {
            Object[] arg = new Object[1];
            arg[0] = "PUP_HGHT_SIZE";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("ICON_CLASS_NM")) < 0 || StringUtil.length(objJsonParams.getString("ICON_CLASS_NM")) > 120) {
            Object[] arg = new Object[1];
            arg[0] = "ICON_CLASS_NM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("SELECT_ATRT")) < 0 || StringUtil.length(objJsonParams.getString("SELECT_ATRT")) > 4) {
            Object[] arg = new Object[1];
            arg[0] = "SELECT_ATRT";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("PROC_ATRT")) < 0 || StringUtil.length(objJsonParams.getString("PROC_ATRT")) > 4) {
            Object[] arg = new Object[1];
            arg[0] = "PROC_ATRT";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("DEL_ATRT")) < 0 || StringUtil.length(objJsonParams.getString("DEL_ATRT")) > 4) {
            Object[] arg = new Object[1];
            arg[0] = "DEL_ATRT";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("OUTPUT_XPLN")) < 0 || StringUtil.length(objJsonParams.getString("OUTPUT_XPLN")) > 4) {
            Object[] arg = new Object[1];
            arg[0] = "OUTPUT_XPLN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("EXCEL_ATRT")) < 0 || StringUtil.length(objJsonParams.getString("EXCEL_ATRT")) > 4) {
            Object[] arg = new Object[1];
            arg[0] = "EXCEL_ATRT";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("ADT_ATRT")) < 0 || StringUtil.length(objJsonParams.getString("ADT_ATRT")) > 4) {
            Object[] arg = new Object[1];
            arg[0] = "ADT_ATRT";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("USE_YN")) < 0 || StringUtil.length(objJsonParams.getString("USE_YN")) > 1) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("SORT_ORD")) < 0 || StringUtil.length(objJsonParams.getString("SORT_ORD")) > 11) {
            Object[] arg = new Object[1];
            arg[0] = "SORT_ORD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("REGR_DEPT_CD")) < 0 || StringUtil.length(objJsonParams.getString("REGR_DEPT_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "REGR_DEPT_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("REGR_ID")) < 0 || StringUtil.length(objJsonParams.getString("REGR_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "REGR_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("AMDR_DEPT_CD")) < 0 || StringUtil.length(objJsonParams.getString("AMDR_DEPT_CD")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "AMDR_DEPT_CD";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("AMDR_ID")) < 0 || StringUtil.length(objJsonParams.getString("AMDR_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "AMDR_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("UPD_DTTM")) < 0 || StringUtil.length(objJsonParams.getString("UPD_DTTM")) > 26) {
            Object[] arg = new Object[1];
            arg[0] = "UPD_DTTM";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtil.length(objJsonParams.getString("PROC_ID")) < 0 || StringUtil.length(objJsonParams.getString("PROC_ID")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "PROC_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

    }


    //삭제 유효성
    public void deleteValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.isBlank(objJsonParams.getString("LOWRNK_MENU_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "LOWRNK_MENU_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }

        if(StringUtil.length(objJsonParams.getString("LOWRNK_MENU_ID")) < 0 || StringUtil.length(objJsonParams.getString("LOWRNK_MENU_ID")) > 90) {
            Object[] arg = new Object[1];
            arg[0] = "LOWRNK_MENU_ID";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
    }


    //삭제 유효성
    public void deleteBtnValidate(Object target, Errors errors)
    {

        TelewebJSON objJsonParams = (TelewebJSON) target;

        if(StringUtils.isBlank(objJsonParams.getString("BTN_ID"))) {
            Object[] arg = new Object[1];
            arg[0] = "BTN_ID";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
    }

}
