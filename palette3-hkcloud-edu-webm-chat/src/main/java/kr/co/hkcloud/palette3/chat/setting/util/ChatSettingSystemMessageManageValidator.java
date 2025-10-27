package kr.co.hkcloud.palette3.chat.setting.util;


import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.netty.util.internal.StringUtil;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.chat.busy.util.TalkBusyUtils;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;


@Slf4j
@Component
@RequiredArgsConstructor
public class ChatSettingSystemMessageManageValidator implements Validator
{
    private final TalkBusyUtils talkBusyUtils;


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

            if(objJsonParams.getHeaderString("METHOD").toString().equals("list")) {

                selectValidate(objJsonParams, errors);

            }
            else if(objJsonParams.getHeaderString("METHOD").toString().equals("regist")) {
                insertValidate(objJsonParams, errors);
            }
            else if(objJsonParams.getHeaderString("METHOD").toString().equals("delete")) {
                deleteValidate(objJsonParams, errors);
            }
            else if(objJsonParams.getHeaderString("METHOD").toString().equals("modify")) {
                if(objJsonParams.getHeaderString("SERVICE").toString().equals("chat.setting.system-mssage-manage.text")) {
                    updateValidate(objJsonParams, errors);
                }
                else {
                    updateLinkValidate(objJsonParams, errors);

                }
            }
            else {
                Object[] arg = new Object[1];
                arg[0] = "METHOD";
                errors.reject(PaletteValidationCode.UNDEFINED_METHOD.getCode(), arg, PaletteValidationCode.UNDEFINED_METHOD.getMessage());
            }
            /*
             * switch(objJsonParams.getHeaderString("METHOD")) { case "list": { selectValidate(objJsonParams, errors); break; } default: //SQL_NM 없을 때 { Object[] arg = new Object[1]; arg[0] = "METHOD";
             * errors.reject(PaletteValidationMessage.UNDEFINED_METHOD.getCode(), arg, PaletteValidationMessage.UNDEFINED_METHOD.getMessage()); break; } }
             */
        }
        else {
            Object[] arg = new Object[1];
            arg[0] = "BIZ_SERVICE";
            errors.reject(PaletteValidationCode.UNDEFINED_TYPE.getCode(), arg, PaletteValidationCode.UNDEFINED_TYPE.getMessage());
        }
        /*
         * 
         * else if(objJsonParams.getHeaderString("TYPE").toString().equals("BIZ_SERVICE")) { switch(objJsonParams.getHeaderString("METHOD")) { case "regist" : { insertValidate(objJsonParams, errors); break; } case
         * "modifySystemMsgByText" : { updateValidate(objJsonParams, errors); break; } case "modifySystemMsgByLink" : { updateLinkValidate(objJsonParams, errors); break; } case "delete" : { deleteValidate(objJsonParams,
         * errors); break; } default: //METHOD 없을 때 { Object[] arg = new Object[1]; arg[0] = "METHOD"; errors.reject(PaletteValidationMessage.UNDEFINED_METHOD.getCode(), arg,
         * PaletteValidationMessage.UNDEFINED_METHOD.getMessage()); break; } } }else { Object[] arg = new Object[1]; arg[0] = "BIZ_SERVICE"; errors.reject(PaletteValidationMessage.UNDEFINED_TYPE.getCode(), arg,
         * PaletteValidationMessage.UNDEFINED_TYPE.getMessage()); }
         */

    }


    //조회 유효성
    public void selectValidate(TelewebJSON telewebJSON, Errors errors)
    {

        if(StringUtils.isBlank(telewebJSON.getString("ASP_NEWCUST_KEY"))) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }

        if(StringUtils.length(telewebJSON.getString("ASP_NEWCUST_KEY")) < 0 || StringUtils.length(telewebJSON.getString("ASP_NEWCUST_KEY")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(telewebJSON.getString("USE_YN")) < 0 || StringUtils.length(telewebJSON.getString("USE_YN")) > 3) {
            Object[] arg = new Object[1];
            arg[0] = "USE_YN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(telewebJSON.getString("SYSMSG_TYPE")) < 0 || StringUtils.length(telewebJSON.getString("SYSMSG_TYPE")) > 60) {
            Object[] arg = new Object[1];
            arg[0] = "SYSMSG_TYPE";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }

    }


    public void updateLinkValidate(TelewebJSON telewebJSON, Errors errors)
    {

        if(StringUtils.isBlank(telewebJSON.getString("LINK_MSG_DESC"))) {
            Object[] arg = new Object[1];
            arg[0] = "LINK_MSG_DESC";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(telewebJSON.getString("LINK_MSG_CN"))) {
            Object[] arg = new Object[1];
            arg[0] = "LINK_MSG_CN";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(telewebJSON.getString("LINK_USE_YN"))) {
            Object[] arg = new Object[1];
            arg[0] = "LINK_USE_YN";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.length(telewebJSON.getString("LINK_MSG_DESC")) < 0 || StringUtils.length(telewebJSON.getString("LINK_MSG_DESC")) > 600) {
            Object[] arg = new Object[1];
            arg[0] = "LINK_MSG_DESC";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(telewebJSON.getString("LINK_MSG_CN")) < 0 || StringUtils.length(telewebJSON.getString("LINK_MSG_CN")) > 3000) {
            Object[] arg = new Object[1];
            arg[0] = "LINK_MSG_CN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(!telewebJSON.getString("LINK_MSG_TIME").equals(telewebJSON.getString("LINK_MSG_TIME_ORG"))) {

            // 업무시간중에는 메시지시간 수정 불가능하도록 처리함.
            try {
                Object[] arg = new Object[1];
                arg[0] = "LINK_MSG_TIME";

                if(talkBusyUtils.isAvailableWorkTime(telewebJSON.getString("ASP_NEWCUST_KEY"))) {
                    errors.reject(PaletteValidationCode.NO_WORKING_TIME.getCode(), arg, "메시지 시간은 " + PaletteValidationCode.NO_WORKING_TIME.getMessage());
                }
            }
            catch(Exception e) {}

        }
        if(telewebJSON.getString("LINK_TYPE").toString().equals("WL")) { //웹링크일경우
            for(int i = 0; i < Integer.parseInt(telewebJSON.getString("SYSTEM_MSG_COUNT")); i++) {
                if(StringUtils.isBlank(telewebJSON.getString("SORT_ORD" + i))) {
                    Object[] arg = new Object[1];
                    arg[0] = "SORT_ORD" + i;
                    errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());

                }
                if(StringUtils.isBlank(telewebJSON.getString("BTN_NM" + i))) {
                    Object[] arg = new Object[1];
                    arg[0] = "BTN_NM" + i;
                    errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());

                }
                if(StringUtils.isBlank(telewebJSON.getString("USE_YN" + i))) {
                    Object[] arg = new Object[1];
                    arg[0] = "USE_YN" + i;
                    errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());

                }
                if(StringUtils.isBlank(telewebJSON.getString("URL_MOBILE" + i))) {
                    Object[] arg = new Object[1];
                    arg[0] = "URL_MOBILE" + i;
                    errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
                }
                if(StringUtils.length(telewebJSON.getString("BTN_NM" + i)) < 0 || StringUtils.length(telewebJSON.getString("BTN_NM" + i)) > 84) {
                    Object[] arg = new Object[1];
                    arg[0] = "BTN_NM" + i;
                    errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
                }
                if(StringUtils.length(telewebJSON.getString("SORT_ORD" + i)) < 0 || StringUtils.length(telewebJSON.getString("SORT_ORD" + i)) > 11) {
                    Object[] arg = new Object[1];
                    arg[0] = "SORT_ORD" + i;
                    errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
                }
                if(StringUtils.length(telewebJSON.getString("URL_MOBILE" + i)) < 0 || StringUtils.length(telewebJSON.getString("URL_MOBILE" + i)) > 3000) {
                    Object[] arg = new Object[1];
                    arg[0] = "URL_MOBILE";
                    errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
                }
                if(StringUtils.length(telewebJSON.getString("URL_PC" + i)) < 0 || StringUtils.length(telewebJSON.getString("URL_PC" + i)) > 3000) {
                    Object[] arg = new Object[1];
                    arg[0] = "URL_PC";
                    errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
                }
            }
        }
        else if(telewebJSON.getString("LINK_TYPE").toString().equals("BK")) { //키워드일경우
            for(int i = 0; i < Integer.parseInt(telewebJSON.getString("SYSTEM_MSG_COUNT")); i++) {
                if(StringUtils.isBlank(telewebJSON.getString("SORT_ORD" + i))) {
                    Object[] arg = new Object[1];
                    arg[0] = "SORT_ORD" + i;
                    errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());

                }
                if(StringUtils.isBlank(telewebJSON.getString("BTN_NM" + i))) {
                    Object[] arg = new Object[1];
                    arg[0] = "BTN_NM" + i;
                    errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());

                }
                if(StringUtils.isBlank(telewebJSON.getString("USE_YN" + i))) {
                    Object[] arg = new Object[1];
                    arg[0] = "USE_YN" + i;
                    errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());

                }
                if(StringUtils.length(telewebJSON.getString("BTN_NM" + i)) < 0 || StringUtils.length(telewebJSON.getString("BTN_NM" + i)) > 84) {
                    Object[] arg = new Object[1];
                    arg[0] = "BTN_NM" + i;
                    errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
                }
                if(StringUtils.length(telewebJSON.getString("SORT_ORD" + i)) < 0 || StringUtils.length(telewebJSON.getString("SORT_ORD" + i)) > 11) {
                    Object[] arg = new Object[1];
                    arg[0] = "SORT_ORD" + i;
                    errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
                }
            }
        }

    }


    //삽입 유효성
    public void insertValidate(TelewebJSON telewebJSON, Errors errors)
    {

        if(StringUtils.length(telewebJSON.getString("NEW_MSG_CL_NAME")) < 0 || StringUtils.length(telewebJSON.getString("NEW_MSG_CL_NAME")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "NEW_MSG_CL_NAME";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(telewebJSON.getString("ASP_NEWCUST_KEY")) < 0 || StringUtils.length(telewebJSON.getString("ASP_NEWCUST_KEY")) > 150) {
            Object[] arg = new Object[1];
            arg[0] = "ASP_NEWCUST_KEY";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(telewebJSON.getString("NEW_MSG_DESC")) < 0 || StringUtils.length(telewebJSON.getString("NEW_MSG_DESC")) > 600) {
            Object[] arg = new Object[1];
            arg[0] = "NEW_MSG_DESC";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(telewebJSON.getString("NEW_MSG_CN")) < 0 || StringUtils.length(telewebJSON.getString("NEW_MSG_CN")) > 3000) {
            Object[] arg = new Object[1];
            arg[0] = "NEW_MSG_CN";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.length(telewebJSON.getString("NEW_MSG_TIME")) < 0 || StringUtils.length(telewebJSON.getString("NEW_MSG_TIME")) > 11) {
            Object[] arg = new Object[1];
            arg[0] = "NEW_MSG_TIME";
            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
        }
        if(StringUtils.isBlank(telewebJSON.getString("NEW_MSG_CL_NAME"))) {
            Object[] arg = new Object[1];
            arg[0] = "NEW_MSG_CL_NAME";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(telewebJSON.getString("NEW_MSG_DESC"))) {
            Object[] arg = new Object[1];
            arg[0] = "NEW_MSG_DESC";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(telewebJSON.getString("NEW_MSG_CN"))) {
            Object[] arg = new Object[1];
            arg[0] = "NEW_MSG_CN";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(telewebJSON.getString("NEW_USE_YN"))) {
            Object[] arg = new Object[1];
            arg[0] = "NEW_USE_YN";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        if(StringUtils.isBlank(telewebJSON.getString("NEW_MSG_TYPE"))) {
            Object[] arg = new Object[1];
            arg[0] = "NEW_MSG_TYPE";
            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
        }
        else {
            if(telewebJSON.getString("NEW_MSG_TYPE").toString().equals("LI")) {
                if(StringUtils.isBlank(telewebJSON.getString("NEW_LINK_TYPE"))) {
                    Object[] arg = new Object[1];
                    arg[0] = "NEW_LINK_TYPE";
                    errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
                }
                else {
                    if(telewebJSON.getString("NEW_LINK_TYPE").toString().equals("WL")) { //링크메시지 -> 웹링크
                        if(StringUtils.isBlank(telewebJSON.getString("LINKDATA", "BTN_NM", 0))) {
                            Object[] arg = new Object[1];
                            arg[0] = "BTN_NM";
                            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
                        }
                        if(StringUtils.isBlank(telewebJSON.getString("LINKDATA", "URL_MOBILE", 0))) {
                            Object[] arg = new Object[1];
                            arg[0] = "URL_MOBILE";
                            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
                        }
                        if(StringUtils.isBlank(telewebJSON.getString("LINKDATA", "USE_YN", 0))) {
                            Object[] arg = new Object[1];
                            arg[0] = "USE_YN";
                            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
                        }
                        if(StringUtils.length(telewebJSON.getString("LINKDATA", "URL_MOBILE", 0)) < 0 || StringUtils.length(telewebJSON.getString("LINKDATA", "URL_MOBILE", 0)) > 3000) {
                            Object[] arg = new Object[1];
                            arg[0] = "URL_MOBILE";
                            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
                        }
                        if(StringUtils.length(telewebJSON.getString("LINKDATA", "URL_PC", 0)) < 0 || StringUtils.length(telewebJSON.getString("LINKDATA", "URL_PC", 0)) > 3000) {
                            Object[] arg = new Object[1];
                            arg[0] = "URL_PC";
                            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
                        }
                    }
                    else if(telewebJSON.getString("NEW_LINK_TYPE").toString().equals("BK")) {//링크메시지 -> 키워드
                        if(StringUtils.isBlank(telewebJSON.getString("LINKDATA", "BTN_NM", 0))) {
                            Object[] arg = new Object[1];
                            arg[0] = "BTN_NM";
                            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
                        }
                        if(StringUtils.isBlank(telewebJSON.getString("LINKDATA", "USE_YN", 0))) {
                            Object[] arg = new Object[1];
                            arg[0] = "USE_YN";
                            errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
                        }
                        if(StringUtils.length(telewebJSON.getString("LINKDATA", "BTN_NM", 0)) < 0 || StringUtils.length(telewebJSON.getString("LINKDATA", "BTN_NM", 0)) > 84) {
                            Object[] arg = new Object[1];
                            arg[0] = "BTN_NM";
                            errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
                        }
                    }
                }
            }
        }

    }


    //메세지설정 수정유효성
    public void updateValidate(TelewebJSON telewebJSON, Errors errors)
    {

        if(telewebJSON.getString("strDivID").toString().equals("divHelloMsg")) {	//메시지설정->일반텍스트메시지수정

            if(StringUtils.isBlank(telewebJSON.getString("MSG_CN"))) {
                Object[] arg = new Object[1];
                arg[0] = "MSG_CN";
                errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
            }
            if(StringUtils.isBlank(telewebJSON.getString("ASP_NEWCUST_KEY"))) {
                Object[] arg = new Object[1];
                arg[0] = "ASP_NEWCUST_KEY";
                errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
            }
            if(StringUtils.isBlank(telewebJSON.getString("SYS_MSG_ID"))) {
                Object[] arg = new Object[1];
                arg[0] = "SYS_MSG_ID";
                errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
            }
            if(StringUtils.isBlank(telewebJSON.getString("TEXT_USE_YN"))) {
                Object[] arg = new Object[1];
                arg[0] = "TEXT_USE_YN";
                errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
            }

            if(StringUtil.length(telewebJSON.getString("MSG_CN")) < 0 || StringUtil.length(telewebJSON.getString("MSG_CN")) > 3000) {
                Object[] arg = new Object[1];
                arg[0] = "MSG_CN";
                errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
            }
            if(StringUtil.length(telewebJSON.getString("ASP_NEWCUST_KEY")) < 0 || StringUtil.length(telewebJSON.getString("ASP_NEWCUST_KEY")) > 150) {
                Object[] arg = new Object[1];
                arg[0] = "ASP_NEWCUST_KEY";
                errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
            }
            if(StringUtil.length(telewebJSON.getString("SYS_MSG_ID")) < 0 || StringUtil.length(telewebJSON.getString("SYS_MSG_ID")) > 75) {
                Object[] arg = new Object[1];
                arg[0] = "SYS_MSG_ID";
                errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
            }
            if(StringUtil.length(telewebJSON.getString("TEXT_USE_YN")) < 0 || StringUtil.length(telewebJSON.getString("TEXT_USE_YN")) > 1) {
                Object[] arg = new Object[1];
                arg[0] = "TEXT_USE_YN";
                errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
            }
        }
        else if(telewebJSON.getString("strDivID").toString().equals("divCnslMsg")) {//메시지설정->일반메시지+시간(분) 수정

            if(StringUtils.isBlank(telewebJSON.getString("MSG_TIME"))) {
                Object[] arg = new Object[1];
                arg[0] = "MSG_TIME";
                errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
            }
            if(StringUtils.isBlank(telewebJSON.getString("MSG_CN"))) {
                Object[] arg = new Object[1];
                arg[0] = "MSG_CN";
                errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
            }
            if(StringUtils.isBlank(telewebJSON.getString("ASP_NEWCUST_KEY"))) {
                Object[] arg = new Object[1];
                arg[0] = "ASP_NEWCUST_KEY";
                errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
            }
            if(StringUtils.isBlank(telewebJSON.getString("SYS_MSG_ID"))) {
                Object[] arg = new Object[1];
                arg[0] = "SYS_MSG_ID";
                errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
            }
            if(StringUtils.isBlank(telewebJSON.getString("TEXT_USE_YN"))) {
                Object[] arg = new Object[1];
                arg[0] = "TEXT_USE_YN";
                errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
            }
            if(StringUtil.length(telewebJSON.getString("MSG_CN")) < 0 || StringUtil.length(telewebJSON.getString("MSG_CN")) > 3000) {
                Object[] arg = new Object[1];
                arg[0] = "MSG_CN";
                errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
            }
            if(StringUtil.length(telewebJSON.getString("MSG_TIME")) < 0 || StringUtil.length(telewebJSON.getString("MSG_TIME")) > 11) {
                Object[] arg = new Object[1];
                arg[0] = "MSG_TIME";
                errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
            }
            if(StringUtil.length(telewebJSON.getString("ASP_NEWCUST_KEY")) < 0 || StringUtil.length(telewebJSON.getString("ASP_NEWCUST_KEY")) > 150) {
                Object[] arg = new Object[1];
                arg[0] = "ASP_NEWCUST_KEY";
                errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
            }
            if(StringUtil.length(telewebJSON.getString("SYS_MSG_ID")) < 0 || StringUtil.length(telewebJSON.getString("SYS_MSG_ID")) > 75) {
                Object[] arg = new Object[1];
                arg[0] = "SYS_MSG_ID";
                errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
            }
            if(StringUtil.length(telewebJSON.getString("TEXT_USE_YN")) < 0 || StringUtil.length(telewebJSON.getString("TEXT_USE_YN")) > 1) {
                Object[] arg = new Object[1];
                arg[0] = "TEXT_USE_YN";
                errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
            }

        }
        else if(telewebJSON.getString("strDivID").toString().equals("systemMsg")) {//시스템메시지관리 -> 일반메시지 수정 
            if(StringUtils.isBlank(telewebJSON.getString("TEXT_MSG_DESC"))) {
                Object[] arg = new Object[1];
                arg[0] = "TEXT_MSG_DESC";
                errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
            }
            if(StringUtils.isBlank(telewebJSON.getString("TEXT_MSG_CN"))) {
                Object[] arg = new Object[1];
                arg[0] = "TEXT_MSG_CN";
                errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
            }
            if(StringUtils.isBlank(telewebJSON.getString("TEXT_USE_YN"))) {
                Object[] arg = new Object[1];
                arg[0] = "TEXT_USE_YN";
                errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
            }
            if(StringUtil.length(telewebJSON.getString("TEXT_MSG_CN")) < 0 || StringUtil.length(telewebJSON.getString("TEXT_MSG_CN")) > 3000) {
                Object[] arg = new Object[1];
                arg[0] = "TEXT_MSG_CN";
                errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
            }
            if(!telewebJSON.getString("TEXT_MSG_TIME").equals(telewebJSON.getString("TEXT_MSG_TIME_ORG"))) {

                // 업무시간중에는 메시지시간 수정 불가능하도록 처리함.
                try {
                    Object[] arg = new Object[1];
                    arg[0] = "TEXT_MSG_TIME";

                    if(talkBusyUtils.isAvailableWorkTime(telewebJSON.getString("ASP_NEWCUST_KEY"))) {
                        errors.reject(PaletteValidationCode.NO_WORKING_TIME.getCode(), arg, "메시지 시간은 " + PaletteValidationCode.NO_WORKING_TIME.getMessage());
                    }
                }
                catch(Exception e) {}

            }
            if(StringUtil.length(telewebJSON.getString("TEXT_MSG_TIME")) < 0 || StringUtil.length(telewebJSON.getString("TEXT_MSG_TIME")) > 11) {

                Object[] arg = new Object[1];
                arg[0] = "TEXT_MSG_TIME";
                errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
            }
            if(StringUtil.length(telewebJSON.getString("TEXT_MSG_DESC")) < 0 || StringUtil.length(telewebJSON.getString("TEXT_MSG_DESC")) > 600) {
                Object[] arg = new Object[1];
                arg[0] = "TEXT_MSG_DESC";
                errors.reject(PaletteValidationCode.EXCEED_INPUT.getCode(), arg, PaletteValidationCode.EXCEED_INPUT.getMessage());
            }

        }
    }


    //삭제 유효성
    public void deleteValidate(TelewebJSON telewebJSON, Errors errors)
    {

        JSONArray objArry = telewebJSON.getDataObject();
        int objArrSize = objArry.size();

        // 업무시간중에는 삭제 불가능하도록 처리함.
        try {
            if(talkBusyUtils.isAvailableWorkTime(telewebJSON.getString("ASP_NEWCUST_KEY"))) {
                errors.reject(PaletteValidationCode.NO_WORKING_TIME.getCode(), new Object[]{}, PaletteValidationCode.NO_WORKING_TIME.getMessage());
            }
        }
        catch(Exception e) {}

        for(int i = 0; i < objArrSize; i++) {
            if(StringUtils.isBlank(objArry.getJSONObject(i).getString("ASP_NEWCUST_KEY"))) {
                Object[] arg = new Object[1];
                arg[0] = "ASP_NEWCUST_KEY";
                errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
            }
            if(StringUtils.isBlank(objArry.getJSONObject(i).getString("SYS_MSG_ID"))) {
                Object[] arg = new Object[1];
                arg[0] = "SYS_MSG_ID";
                errors.reject(PaletteValidationCode.DATA_NOT_EXIST.getCode(), arg, PaletteValidationCode.DATA_NOT_EXIST.getMessage());
            }
        }

    }

}