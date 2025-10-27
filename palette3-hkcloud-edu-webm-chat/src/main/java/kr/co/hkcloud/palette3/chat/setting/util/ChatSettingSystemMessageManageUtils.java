package kr.co.hkcloud.palette3.chat.setting.util;


import org.springframework.stereotype.Component;

import kr.co.hkcloud.palette3.exception.teleweb.TelewebUtilException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Component
public class ChatSettingSystemMessageManageUtils
{
    /**
     * 시스템메시지 validation check
     */
    public int systemMsgValidation(String type, JSONArray jsonArray) throws TelewebUtilException
    {
        boolean ErrorFlag = false;
        int ErrorCode = 0;
        String key = "";
        for(int i = 0; i < jsonArray.size(); i++) {
            JSONObject obj = (JSONObject) jsonArray.get(i);

            java.util.Iterator<?> iterator = obj.keys();

            while(iterator.hasNext()) {
                key = iterator.next().toString();

                if(!key.contains("LINK_TYPE")) {
                    if("".equals(obj.get(key)) || null == obj.get(key)) {
                        ErrorFlag = true;
                        break;
                    }

                }
            }
            if(ErrorFlag)
                break;
        }

        if(ErrorFlag) {
            ErrorCode = getErrorCodeBySystemMSg(key);
        }
        return ErrorCode;
    }


    /**
     * 링크메시지 정보 validation check
     */
    public int systemMsgValidationByLinkMsg(JSONArray jsonArray) throws TelewebUtilException
    {
        boolean ErrorFlag = false;
        int ErrorCode = 0;
        String key = "";
        if(jsonArray != null && jsonArray.size() > 0) {
            for(int i = 0; i < jsonArray.size(); i++) {
                JSONObject obj = (JSONObject) jsonArray.get(i);
                java.util.Iterator<?> iterator = obj.keys();
                while(iterator.hasNext()) {
                    key = iterator.next().toString();
                    if("".equals(obj.get(key)) || null == obj.get(key)) {
                        ErrorFlag = true;
                        break;
                    }
                }
                if(ErrorFlag)
                    break;
            }
        }
        if(ErrorFlag) {
            ErrorCode = getErrorCodeByLink(key);
        }

        return ErrorCode;
    }


    /**
     * 시스템메시지 삭제 validation Check
     */
    public int deleteSystemMsgValidationCheck(JSONArray jsonArray) throws TelewebUtilException
    {
        int ErrorCode = 0;
        for(int i = 0; i < jsonArray.size(); i++) {
            JSONObject obj = (JSONObject) jsonArray.get(i);
            if("00".equals(obj.get("MSG_CL"))) {
                ErrorCode = 12;
                break;
            }
        }
        return ErrorCode;
    }


    /**
     * Validation 에러코드 (링크정보)
     */
    public int getErrorCodeByLink(String key) throws TelewebUtilException
    {
        int ErrorCode = 0;
        if(key.contains("LINKS_ID")) {
            ErrorCode = 7;
        }
        else if(key.contains("BTN_NM")) {
            ErrorCode = 8;
        }
        else if(key.contains("USE_YN")) {
            ErrorCode = 9;
        }
        else if(key.contains("URL_MOBILE")) {
            ErrorCode = 10;
        }

        return ErrorCode;
    }


    /**
     * Validation 에러코드
     */
    public int getErrorCodeBySystemMSg(String key) throws TelewebUtilException
    {
        int ErrorCode = 0;
        if(key.contains("MSG_ID")) {
            ErrorCode = 1;
        }
        else if(key.contains("MSG_TYPE")) {
            ErrorCode = 2;
        }
        else if(key.contains("MSG_DESC")) {
            ErrorCode = 3;
        }
        else if(key.contains("MSG_CN")) {
            ErrorCode = 4;
        }
        else if(key.contains("USE_YN")) {
            ErrorCode = 5;
        }
        else if(key.contains("MSG_TYPE")) {
            ErrorCode = 6;
        }
        else if(key.contains("MSG_CL")) {
            ErrorCode = 7;
        }

        return ErrorCode;
    }


    /**
     * 에러코드에 해당하는 에러메시지 정보
     */
    public String getErrorMsg(int errorCode) throws TelewebUtilException
    {
        String errorMsg = "";
        switch(errorCode)
        {
            case 1:
                errorMsg = "시스템메시지ID가 생성되지 않았습니다";
                break;
            case 2:
                errorMsg = "시스템메시지 타입을 지정하지 않았습니다.";
                break;
            case 3:
                errorMsg = "시스템메시지명이 입력되지 않았습니다.";
                break;
            case 4:
                errorMsg = "시스템메시지 내용이 입력되지 않았습니다.";
                break;
            case 5:
                errorMsg = "시스템메시지 사용여부를 지정하지 않았습니다.";
                break;
            case 6:
                errorMsg = "시스템메시지의 링크타입을 지정하지 않았습니다.";
                break;
            case 7:
                errorMsg = "시스템메시지의 메시지 분류를 지정하지 않았습니다.";
                break;
            case 8:
                errorMsg = "링크메시지ID가 생성되지 않았습니다.";
                break;
            case 9:
                errorMsg = "링크메시지의 버튼명을 입력하지 않았습니다.";
                break;
            case 10:
                errorMsg = "링크메시지의 사용여부를 지정하지 않았습니다.";
                break;
            case 11:
                errorMsg = "링크메시지의 모바일URL을 입력하지 않았습니다.";
                break;
            case 12:
                errorMsg = "메시지 분류가 시스템 메시지인 경우는 삭제할 수 없습니다.";
                break;
            case 20:
                errorMsg = "링크 정보를 입력하지 않았습니다.";
                break;
        }
        return errorMsg;
    }
}
