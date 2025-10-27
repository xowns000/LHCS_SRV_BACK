package kr.co.hkcloud.palette3.core.util;


import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class PaletteDataFormatUtils
{
    private PaletteUtils telewebUtil;


    /**
     * 데이터의 포맷팅을 위한 메소드
     * 
     * @param  gubun
     * @param  value
     * @return
     */
    public String getFormatData(String gubun, String value)
    {
        String strReturn = "";
        String workStr = "";

        if(gubun.indexOf("class_biz") >= 0) {				//사업자 번호
            workStr = exceptMask(value);
            if(workStr.length() != 10) {
                strReturn = value;
            }
            else {
                strReturn = workStr.substring(0, 3) + "-" + workStr.substring(3, 5) + "-" + workStr.substring(5);
            }
        }
        else if(gubun.indexOf("class_post") >= 0) {		//우편번호
            workStr = exceptMask(value);
            if(workStr.length() != 6) {
                strReturn = value;
            }
            else {
                strReturn = workStr.substring(0, 3) + "-" + workStr.substring(3);
            }
        }
        else if(gubun.indexOf("class_rrno") >= 0) {		//주민번호
            workStr = exceptMask(value);
            if(workStr.length() != 13) {
                strReturn = value;
            }
            else {
                strReturn = workStr.substring(0, 6) + "-" + workStr.substring(6);
            }
        }
        else if(gubun.indexOf("class_tlnoor") >= 0) {	//전화번호
            int nCnt = 0;
            workStr = exceptMask(value);
            if(workStr.length() == 4) {
                strReturn = value;
            }
            else if(workStr.length() < 7 || workStr.length() > 11) {
                strReturn = value;
            }
            else {
                String sTel3 = workStr.substring(workStr.length() - 4, workStr.length());
                String sTemp = workStr.substring(0, workStr.length() - 4);

                if(!sTemp.substring(0, 1).equals("0")) {
                    if(workStr.length() < 9) { return sTemp + "-" + sTel3; }
                }
                else {
                    if(sTemp.substring(0, 2).equals("02")) {
                        nCnt = 2;
                    }
                    else {
                        if(sTemp.substring(0, 3).equals("011") || sTemp.substring(0, 3).equals("013") || sTemp.substring(0, 3).equals("016") || sTemp.substring(0, 3).equals("017") || sTemp.substring(0, 3)
                            .equals("018") || sTemp.substring(0, 3).equals("019") || sTemp.substring(0, 3).equals("010") || sTemp.substring(0, 3).equals("070") || sTemp.substring(0, 3).equals("080") || sTemp
                                .substring(0, 3).equals("031") || sTemp.substring(0, 3).equals("032") || sTemp.substring(0, 3).equals("033") || sTemp.substring(0, 3).equals("041") || sTemp.substring(0, 3)
                                    .equals("042") || sTemp.substring(0, 3).equals("043") || sTemp.substring(0, 3).equals("051") || sTemp.substring(0, 3).equals("052") || sTemp.substring(0, 3).equals("053") || sTemp
                                        .substring(0, 3).equals("054") || sTemp.substring(0, 3).equals("055") || sTemp.substring(0, 3)
                                            .equals("061") || sTemp.substring(0, 3).equals("062") || sTemp.substring(0, 3).equals("063") || sTemp.substring(0, 3).equals("064") || sTemp.substring(0, 3).equals("050")) {
                            nCnt = 3;
                        }
                    }
                    strReturn = sTemp.substring(0, nCnt) + "-" + sTemp.substring(nCnt, sTemp.length()) + "-" + sTel3;
                }
            }
        }
        else if(gubun.indexOf("class_number") >= 0) {	//숫자
            String minus = "";
            String strInt = "";
            workStr = value;

            if(value.equals("")) {
                strReturn = "";
            }
            else {
                if(workStr.substring(0, 1).equals("-")) {
                    minus = "-";
                }

                if(workStr.lastIndexOf(".") == -1) {

                    if("0".equals(workStr.substring(0, 1))) {
                        while(workStr.length() > 0 && "0".equals(workStr.substring(0, 1))) {
                            workStr = workStr.substring(1, workStr.length());
                        }
                        if("".equals(workStr)) {
                            workStr = "0";
                        }
                    }

                    if(minus.equals("-")) {
                        strInt = workStr.substring(1);
                    }
                    else {
                        strInt = workStr;
                    }

                    int index = strInt.length();
                    String sRet = "";
                    for(int i = 0; i < index; i += 3) {
                        if(index > i + 3) {
                            sRet = "," + strInt.substring(index - i - 3, index - i) + sRet;
                        }
                        else {
                            sRet = strInt.substring(0, index - i) + sRet;
                        }
                    }
                    strReturn = minus + sRet;
                }
                else {
                    if(minus.equals("-")) {
                        strInt = workStr.substring(1, workStr.lastIndexOf("."));
                    }
                    else {
                        strInt = workStr.substring(0, workStr.lastIndexOf("."));
                    }

                    int index = strInt.length();
                    String sRet = "";
                    for(int i = 0; i < index; i += 3) {
                        if(index > i + 3) {
                            sRet = "," + strInt.substring(index - i - 3, index - i) + sRet;
                        }
                        else {
                            sRet = strInt.substring(0, index - i) + sRet;
                        }
                    }
                    strReturn = minus + sRet + "." + workStr.substring(workStr.lastIndexOf(".") + 1, workStr.length());
                }
            }
        }
        else if(gubun.indexOf("class_date") >= 0 || gubun.indexOf("class_month") >= 0) {		//날짜
            workStr = exceptMask(value);

            if(gubun.indexOf("time") >= 0) {
                workStr = exceptMask(value);
                if(workStr.length() != 12 && workStr.length() != 14) {
                    strReturn = workStr;
                }
                else {
                    if(workStr.length() == 12) {
                        strReturn = workStr.substring(0, 4) + "/" + workStr.substring(4, 6) + "/" + workStr.substring(6, 8) + " " + workStr.substring(8, 10) + ":" + workStr.substring(10);
                    }
                    else {
                        strReturn = workStr.substring(0, 4) + "/" + workStr.substring(4, 6) + "/" + workStr.substring(6, 8) + " " + workStr.substring(8, 10) + ":" + workStr.substring(10, 12) + ":" + workStr
                            .substring(12);
                    }
                }
            }
            else {
                if(workStr.equals("")) {
                    strReturn = value;
                }
                else {
                    if(workStr.length() != 6 && workStr.length() != 8) {
                        strReturn = workStr;
                    }
                    else {
                        if(workStr.length() == 6) {
                            strReturn = workStr.substring(0, 4) + "/" + workStr.substring(4);
                        }
                        else {
                            strReturn = workStr.substring(0, 4) + "/" + workStr.substring(4, 6) + "/" + workStr.substring(6);
                        }
                    }
                }
            }

        }
        else if(gubun.indexOf("class_time") >= 0) {		//시간
            workStr = exceptMask(value);
            if(workStr.length() != 4 && workStr.length() != 6) {
                strReturn = workStr;
            }
            else {
                if(workStr.length() == 4) {
                    strReturn = workStr.substring(0, 2) + ":" + workStr.substring(2);
                }
                else {
                    strReturn = workStr.substring(0, 2) + ":" + workStr.substring(2, 4) + ":" + workStr.substring(4);
                }
            }
        }
        else if(gubun.indexOf("class_birth") >= 0) {		//생년월일
            workStr = exceptMask(value);

            if(workStr.equals("")) {
                strReturn = value;
            }
            else {
                if(workStr.length() != 8) {
                    strReturn = value;
                }
                else {
                    strReturn = workStr.substring(0, 4) + "/**/" + workStr.substring(6, 8);
                }
            }
        }
        else if(gubun.indexOf("class_rrn") >= 0) {		//생년월일
            workStr = exceptMask(value);

            if(workStr.equals("")) {
                strReturn = value;
            }
            else {
                if(workStr.length() != 6 && workStr.length() != 8) {
                    strReturn = workStr;
                }
                else {
                    if(workStr.length() == 6) {
                        strReturn = workStr.substring(0, 2) + "/**/" + workStr.substring(4);
                    }
                    else {
                        strReturn = workStr.substring(0, 4) + "/**/" + workStr.substring(6);
                    }
                }
            }
        }
        else if(gubun.indexOf("class_card") >= 0) {		//카드
            workStr = exceptMask(value);
            if(workStr.length() != 16) {
                strReturn = workStr;
            }
            else {
                strReturn = workStr.substring(0, 4) + "-" + workStr.substring(4, 8) + "-" + workStr.substring(8, 12) + "-" + workStr.substring(12);
            }
        }
        else if(gubun.indexOf("class_cardEnc") >= 0) {	//카드마스킹
            workStr = exceptMask(value);
            if(!"".equals(workStr)) {
                if(workStr.length() != 16) {
                    strReturn = workStr.substring(0, 4);
                    for(int i = 0; i < workStr.length() - 4; i++) {
                        strReturn += "*";
                    }
                }
                else {
                    strReturn = workStr.substring(0, 4) + "-****-****-" + workStr.substring(12);
                }
            }
        }
        else if(gubun.indexOf("class_acct") >= 0) {		//계좌번호
            workStr = exceptMask(value);
            if(workStr.length() > 8) {
                strReturn = workStr.substring(0, workStr.length() - 4) + "****";
            }
            else {
                strReturn = workStr;
            }
        }
        else if(gubun.indexOf("class_eml") >= 0) {		//이메일
            workStr = value;
            int eIdx = workStr.indexOf("@");
            int intLen = 0;
            String strMask = "";
            if(eIdx != -1) {
                intLen = workStr.substring(0, eIdx).length();
                if(intLen > 4) {
                    for(int i = 3; i < eIdx; i++) {
                        strMask += "*";
                    }
                    strReturn = workStr.substring(0, 3) + strMask + workStr.substring(eIdx);
                }
                else if(intLen == 4) {
                    strReturn = workStr.substring(0, 2) + "**" + workStr.substring(eIdx);
                }
                else if(intLen == 3) {
                    strReturn = workStr.substring(0, 1) + "**" + workStr.substring(eIdx);
                }
                else {
                    strReturn = "**" + workStr.substring(eIdx);
                }
            }
            else {
                strReturn = workStr;
            }
        }
        else if(gubun.indexOf("class_nameEnc") >= 0) {	//이름
            workStr = value;
            strReturn = workStr;
            if(workStr.length() > 2) {
                String tvar1 = workStr.substring(0, 1);
                String tvar2 = workStr.substring(workStr.length() - 1);
                String tmp = "";
                for(int i = 1; i < workStr.length() - 1; i++) {
                    tmp += "*";
                }
                strReturn = tvar1 + tmp + tvar2;
            }
            else if(workStr.length() == 2) {
                strReturn = workStr.substring(0, 1) + "*";
            }
        }
        else if(gubun.indexOf("class_addr") >= 0) {		//주소
            workStr = value;
            strReturn = workStr;
            if(workStr.length() > 0) {
                int sIdx = -1;
                int sIdx2 = -1;
                int eIdx = -1;
                int eIdx2 = -1;
                String strMask = "";
                String strMask2 = "";
                if(workStr.indexOf("읍 ") >= 0) {
                    eIdx = workStr.indexOf("읍 ");
                }
                else if(workStr.indexOf("면 ") >= 0) {
                    eIdx = workStr.indexOf("면 ");
                }
                else if(workStr.indexOf("동 ") >= 0) {
                    eIdx = workStr.indexOf("동 ");
                }
                else if(workStr.indexOf("로 ") >= 0) {
                    eIdx = workStr.indexOf("로 ");
                    if(workStr.indexOf("동) ") >= 0) {
                        eIdx2 = workStr.indexOf("동) ");
                    }
                    else if(workStr.indexOf("동&#41; ") >= 0) {
                        eIdx2 = workStr.indexOf("동&#41; ");
                    }
                    else if(workStr.indexOf("읍) ") >= 0) {
                        eIdx2 = workStr.indexOf("읍) ");
                    }
                    else if(workStr.indexOf("읍&#41; ") >= 0) {
                        eIdx2 = workStr.indexOf("읍&#41; ");
                    }
                    else if(workStr.indexOf("면) ") >= 0) {
                        eIdx2 = workStr.indexOf("면) ");
                    }
                    else if(workStr.indexOf("면&#41; ") >= 0) {
                        eIdx2 = workStr.indexOf("면&#41; ");
                    }
                }
                else if(workStr.indexOf("길 ") >= 0) {
                    eIdx = workStr.indexOf("길 ");
                    if(workStr.indexOf("동) ") >= 0) {
                        eIdx2 = workStr.indexOf("동) ");
                    }
                    else if(workStr.indexOf("동&#41; ") >= 0) {
                        eIdx2 = workStr.indexOf("동&#41; ");
                    }
                    else if(workStr.indexOf("읍) ") >= 0) {
                        eIdx2 = workStr.indexOf("읍) ");
                    }
                    else if(workStr.indexOf("읍&#41; ") >= 0) {
                        eIdx2 = workStr.indexOf("읍&#41; ");
                    }
                    else if(workStr.indexOf("면) ") >= 0) {
                        eIdx2 = workStr.indexOf("면) ");
                    }
                    else if(workStr.indexOf("면&#41; ") >= 0) {
                        eIdx2 = workStr.indexOf("면&#41; ");
                    }

                }
                for(int i = eIdx; i >= 0; i--) {
                    if(" ".equals(workStr.substring(i - 1, i))) {
                        sIdx = i;
                        break;
                    }
                    else if(i - 1 < 0) {
                        sIdx = i;
                        break;
                    }
                    strMask += "*";
                }
                for(int j = eIdx2; j >= 0; j--) {
                    if("(".equals(workStr.substring(j - 1, j))) {
                        sIdx2 = j;
                        break;
                    }
                    else if("&#40;".equals(workStr.substring(j - 5, j))) {
                        sIdx2 = j;
                        break;
                    }
                    else if(j - 1 < 0) {
                        sIdx2 = j;
                        break;
                    }
                    strMask2 += "*";
                }
                if(sIdx >= 0 && eIdx >= 0) {
                    if(sIdx2 >= 0 && eIdx2 >= 0) {
                        strReturn = workStr.substring(0, sIdx) + strMask + workStr.substring(eIdx, sIdx2) + strMask2 + workStr.substring(eIdx2);
                    }
                    else {
                        strReturn = workStr.substring(0, sIdx) + strMask + workStr.substring(eIdx);
                    }
                }
            }
        }
        else if(gubun.indexOf("class_tlno") >= 0) {		//전화번호
            int nCnt = 0;
            workStr = exceptMask(value);
            if(workStr.length() == 4) {
                strReturn = value;
            }
            else if(workStr.length() < 7 || workStr.length() > 11) {
                strReturn = value;
            }
            else {
                String sTel3 = workStr.substring(workStr.length() - 4, workStr.length());
                String sTemp = workStr.substring(0, workStr.length() - 4);

                if(!sTemp.substring(0, 1).equals("0")) {
                    if(workStr.length() < 9) { return sTemp + "-" + sTel3; }
                }
                else {
                    if(sTemp.substring(0, 2).equals("02")) {
                        nCnt = 2;
                    }
                    else {
                        if(sTemp.substring(0, 3).equals("011") || sTemp.substring(0, 3).equals("013") || sTemp.substring(0, 3).equals("016") || sTemp.substring(0, 3).equals("017") || sTemp.substring(0, 3)
                            .equals("018") || sTemp.substring(0, 3).equals("019") || sTemp.substring(0, 3).equals("010") || sTemp.substring(0, 3).equals("070") || sTemp.substring(0, 3).equals("080") || sTemp
                                .substring(0, 3).equals("031") || sTemp.substring(0, 3).equals("032") || sTemp.substring(0, 3).equals("033") || sTemp.substring(0, 3).equals("041") || sTemp.substring(0, 3)
                                    .equals("042") || sTemp.substring(0, 3).equals("043") || sTemp.substring(0, 3).equals("051") || sTemp.substring(0, 3).equals("052") || sTemp.substring(0, 3).equals("053") || sTemp
                                        .substring(0, 3).equals("054") || sTemp.substring(0, 3).equals("055") || sTemp.substring(0, 3)
                                            .equals("061") || sTemp.substring(0, 3).equals("062") || sTemp.substring(0, 3).equals("063") || sTemp.substring(0, 3).equals("064") || sTemp.substring(0, 3).equals("050")) {
                            nCnt = 3;
                        }
                    }
                    strReturn = sTemp.substring(0, nCnt) + "-****-" + sTel3;
                }
            }
        }
        else if(gubun.indexOf("class_tmtime") >= 0) {
            strReturn = value;
            workStr = exceptMask(value);
            if(!"".equals(workStr)) {
                int intHour;
                int intMinute;
                int intSecond;
                int workVal = Integer.parseInt(workStr);
                intHour = workVal / 3600;
                intMinute = (workVal % 3600) / 60;
                intSecond = workVal % 60;
                strReturn = telewebUtil.characterPad(String.valueOf(intHour), 2, "0", true) + ":" + telewebUtil.characterPad(String.valueOf(intMinute), 2, "0", true) + ":" + telewebUtil
                    .characterPad(String.valueOf(intSecond), 2, "0", true);
            }
        }

        return strReturn;
    }


    /**
     * 숫자만 가지고온다.
     * 
     * @param  data
     * @return
     */
    public String exceptMask(String data)
    {
        String lsReturn = "", lsTemp = data.trim();
        char lcChar;

        for(int i = 0; i < lsTemp.length(); i++) {
            lcChar = lsTemp.charAt(i);
            if(Character.isDigit(lcChar)) {
                lsReturn += lcChar;
            }
        }
        return lsReturn;
    }
}
