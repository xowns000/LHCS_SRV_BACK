package kr.co.hkcloud.palette3.core.util;


import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import kr.co.hkcloud.palette3.config.security.properties.PaletteSecurityProperties;
import kr.co.hkcloud.palette3.core.security.crypto.AES128Cipher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Component
public class PaletteUtils
{
    private final PaletteSecurityProperties paletteSecurityProperties;

    @Value("${spring.app.palette.repository-root}")
    private String REPOSITORY_ROOT;


    /**
     * String Null 체크
     * 
     * @param  agStr
     * @return
     */
    public String nvl(String agStr)
    {
        if(agStr == null) {
            return "";
        }
        else {
            return agStr;
        }
    }


    /**
     * Object Null 체크
     * 
     * @param  str
     * @return
     */
    public String nvl(Object str)
    {
        if(str == null) {
            return "";
        }
        else {
            return str.toString();
        }
    }


    /**
     * 입력된 String이 숫자인지 확인
     * 
     * @param  strMatch
     * @return
     */
    public boolean isNumeric(String strMatch)
    {
        return Pattern.matches("[0-9]+", strMatch);
    }


    /**
     * 완성형문자(한글)여부 체크(한문자라도 2바이트 이상의 문자가 있을 경우 완성형이라 판단)
     * 
     * @param  strValue
     * @return
     */
    public boolean isWansungChar(String strValue)
    {
        String strChar = strValue.substring(0, 1);
        for(int i = 0; i < strChar.length(); i++) {
            strChar = strValue.substring(i, i + 1);
            if(strChar.getBytes().length > 1) { return true; }
        }
        return false;
    }


    /**
     * 입력받은 String에서 숫자만 발췌한다.
     * 
     * @param  data
     * @return
     */
    public String selectNum(String data)
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


    /**
     * 현재시간을 입력된 패턴으로 리턴한다.
     * 
     * @param  pattern
     * @return
     */
    public String getFormatString(String pattern)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, java.util.Locale.KOREA);
        String dateString = formatter.format(new Date());
        return dateString;
    }


    /**
     * 현재일시 리턴 yyyy-MM-dd-HH:mm:ss:SSS
     * 
     * @return
     */
    public String getTimeStampString()
    {
        return getFormatString("yyyy-MM-dd-HH:mm:ss:SSS");
    }


    /**
     * 현재일시 리턴 yyyyMMddHHmmssSSS
     * 
     * @return
     */
    public String getTimeStampMilis()
    {
        return getFormatString("yyyyMMddHHmmssSSS");
    }


    /**
     * 현재 일시 리턴 yyyy/MM/dd
     * 
     * @return
     */
    public String getDateTypeString()
    {
        return getFormatString("yyyy/MM/dd");
    }


    /**
     * 현재일시 리턴 HH:mm:ss
     * 
     * @return
     */
    public String getTimeString()
    {
        return getFormatString("HH:mm:ss");
    }


    /**
     * 현재 일시리턴 yyyyMMddHHmmss
     * 
     * @return
     */
    public String getShortString()
    {
        return getFormatString("yyyyMMddHHmmss");
    }


    /**
     * 현재일 리턴 yyyyMMdd
     * 
     * @return
     */
    public String getShortDateString()
    {
        return getFormatString("yyyyMMdd");
    }


    /**
     * 현재일 리턴 yyyyMM
     * 
     * @return
     */
    public String getYYYYMMDateString()
    {
        return getFormatString("yyyyMM");
    }


    /**
     * 현재 시간 리턴 HHmmss
     * 
     * @return
     */
    public String getShortTimeString()
    {
        return getFormatString("HHmmss");
    }


    /**
     * 현재년도 리턴 yyyy
     * 
     * @return
     */
    public String getYearString()
    {
        return getFormatString("yyyy");
    }


    /**
     * 현재 년도 리턴 MM
     * 
     * @return
     */
    public String getMonthString()
    {
        return getFormatString("MM");
    }


    /**
     * 현재 일 리턴 dd
     * 
     * @return
     */
    public String getDayString()
    {
        return getFormatString("dd");
    }


    /**
     * 특정 자릿수만큼 오른쪽 혹은 왼쪽으로 특정 문자열을 채워서 리턴해준다.
     * 
     * @return
     */
    public String characterPad(String strVal, int intLen, String strMakeVal, boolean blnIsLeft)
    {
        String retVal = "";

        if(intLen <= strVal.length() || strMakeVal.equals("") || strMakeVal.length() > 1) {
            retVal = strVal;
        }
        else {
            for(int i = 0; i < intLen - strVal.length(); i++) {
                retVal += strMakeVal;
            }
            if(blnIsLeft) {
                retVal += strVal;
            }
            else {
                retVal = strVal + retVal;
            }
        }

        return retVal;
    }


    /**
     * 숫자만 있는 문자로 변환(빈문자일 경우 0으로 반환: 계산식할 때 사용)
     * 
     * @return
     */
    public long getNumber(String strValue)
    {
        return getLongNumber(strValue);
    }


    public long getLongNumber(String strValue)
    {
        if(strValue == null || strValue.equals("")) { return 0; }
        return Long.valueOf(strValue.replaceAll(",", ""));
    }


    /**
     * 현재일자의 밀리세컨드 구하기
     * 
     * @return
     */
    public long getMilliseconds()
    {
        return getMilliseconds("");
    }


    /**
     * 해당 일자의 밀리세컨드 구하기
     * 
     * @param  dateTime
     * @return
     */
    public long getMilliseconds(String dateTime)
    {
        Calendar now = Calendar.getInstance(Locale.KOREA);

        if(dateTime == null || dateTime.trim().equals("")) { return now.getTimeInMillis(); }

        try {
            Integer.parseInt(dateTime.trim(), 10);
        }
        catch(NumberFormatException nfE) {
            return now.getTimeInMillis();
        }

        dateTime = dateTime.trim() + "00000000000000";
        dateTime = dateTime.substring(0, 14);

        now.set(Calendar.YEAR, Integer.parseInt(dateTime.substring(0, 4), 10));
        now.set(Calendar.MONTH, Integer.parseInt(dateTime.substring(4, 6), 10) - 1);
        now.set(Calendar.DATE, Integer.parseInt(dateTime.substring(6, 8), 10));
        now.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dateTime.substring(8, 10), 10));
        now.set(Calendar.MINUTE, Integer.parseInt(dateTime.substring(10, 12), 10));
        now.set(Calendar.SECOND, Integer.parseInt(dateTime.substring(12, 14), 10));

        return now.getTimeInMillis();
    }


    /**
     * 숫자만 있는 문자로 변환(빈문자일 경우 0으로 반환: 계산식할 때 사용)
     * 
     * @return
     */
    public float getFloatNumber(String strValue)
    {
        if(strValue == null || strValue.equals("")) { return 0; }
        return Float.valueOf(strValue.replaceAll(",", ""));
    }


    /**
     * property파일에서 값을 리턴한다.
     * 
     * @param  key
     * @return
     */
    public String getMessageFormet(String strMsg, String strParams01, String strParams02)
    {
        String[] objParams = new String[2];
        objParams[0] = strParams01;
        objParams[1] = strParams02;
        MessageFormat objFormat = new MessageFormat(strMsg);
        return objFormat.format(objParams);
    }


    /**
     * XSS을 방지하기 위해서 문자열에 있는 위험 특수문자를 치환한다. JSP에서 직접 파라메터를 찍을 때 해당 메서드를 이용하여 필터처리한다.
     * 
     * @param  key
     * @return
     */
    public String chkXSSConstraints(String strData)
    {
        strData = strData.replaceAll("&", "&amp;");
        strData = strData.replaceAll("<", "&lt;");
        strData = strData.replaceAll(">", "&gt;");
        strData = strData.replaceAll("%00", null);
        strData = strData.replaceAll("\"", "&#34;");
        strData = strData.replaceAll("\'", "&#39;");
        strData = strData.replaceAll("%", "&#37;");
        strData = strData.replaceAll("../", "");
        strData = strData.replaceAll("..\\\\", "");
        strData = strData.replaceAll("./", "");
        strData = strData.replaceAll("%2F", "");
        return strData;
    }


    public String chkXSSConstraints2(String strData)
    {
        strData = strData.replaceAll("<", "&lt;");
        strData = strData.replaceAll(">", "&gt;");
        strData = strData.replaceAll("&#40;", "(");
        strData = strData.replaceAll("&#41;", ")");
        return strData;
    }


    /**
     * json obj 문제로 인한 [ { 문자만 치환함
     * 
     * @param  strData
     * @return         : GS인증 요청사항으로인한 추가 => %, _ 치환
     */
    public String chkJsonObjChar(String strData)
    {
        strData = strData.replaceAll("\\[", "&#91;");
        strData = strData.replaceAll("\\]", "&#93;");

        return strData;
    }


    /**
     * 20.06.17 knj
     * 
     * @param  strData
     * @return         : GS인증 요청사항으로인한 추가 => %, _ 치환
     */
    public String replacePercentUnderBar(String strData)
    {
        if(null != strData && !"".equals(strData)) {
            strData = strData.replaceAll("%", "\\\\%");
            strData = strData.replaceAll("_", "\\\\_");
        }
        else {
            strData = "";
        }

        return strData;
    }


    public String chkXSSConstraintsDec(String strData)
    {
        strData = strData.replaceAll("&lt;", "<");
        strData = strData.replaceAll("&gt;", ">");
        strData = strData.replaceAll("&amp;", "&");
        strData = strData.replaceAll("&quot;", "\"");
        strData = strData.replaceAll("&apos;", "'");
        strData = strData.replaceAll("&#40;", "(");
        strData = strData.replaceAll("&#41;", ")");
        strData = strData.replaceAll("&#91;", "[");
        strData = strData.replaceAll("&#93;", "]");
        return strData;
    }


    public String chkXSSConstraintsDecHTMLTagFilter(String strData)
    {
        strData = strData.replaceAll("&lt;", "<");
        strData = strData.replaceAll("&gt;", ">");
        strData = strData.replaceAll("&amp;", "&");
        strData = strData.replaceAll("&quot;", "\"");
        strData = strData.replaceAll("&apos;", "\'");
        return strData;
    }


    public String[] getCryptColumns(String cryptColumnKey)
    {
        String[] columnsLst;

        //String strVal = telewebProperty.getString("CRYPT_COLUMNS");
        if(cryptColumnKey != null && !"".equals(cryptColumnKey)) {
            columnsLst = cryptColumnKey.split(",");
        }
        else {
            columnsLst = new String[0];
        }
        return columnsLst;
    }


    public String getEncryptString(String str, String cryptKey)
    {
        String retVal = "";
        try {
            if(str == null || "".equals(str)) {
                retVal = "";
            }
            else {
                retVal = AES128Cipher.encryptString(str, cryptKey);
            }

            //objParams.setString("CNSLT_RSLT_CN", AES128Cipher.encryptString(addStr.toString(), mobjProperty.getString("CRYPT_KEY")));
        }
        catch(InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            retVal = str;
        }
        return retVal;
    }


    public String getDecryptExcelString(String str, String cryptKey)
    {
        String retVal = "";
        try {
            if(str == null || "".equals(str)) {
                retVal = "";
            }
            else {
                retVal = AES128Cipher.decryptString(str, cryptKey);
            }
            //objParams.setString("CNSLT_RSLT_CN", AES128Cipher.encryptString(addStr.toString(), mobjProperty.getString("CRYPT_KEY")));
        }
        catch(InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            retVal = str;
        }
        return retVal;
    }


    public static String getDecryptString(String str, String cryptKey)
    {
        String retVal = "";
        try {
            if(str == null || "".equals(str)) {
                retVal = "";
            }
            else {
                retVal = AES128Cipher.decryptString(str, cryptKey);
            }
            //objParams.setString("CNSLT_RSLT_CN", AES128Cipher.encryptString(addStr.toString(), mobjProperty.getString("CRYPT_KEY")));
        }
        catch(InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            retVal = str;
        }
        return retVal;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList stringToArray(String id, String divChar ){
        StringTokenizer st = new StringTokenizer(id, divChar);
        String tempID = null;
        ArrayList al = new ArrayList();
        for(int i =0; st.hasMoreTokens(); i++){
        	tempID = st.nextToken();
        	al.add(i,tempID);
        }
        return al;
    }
    
    public static String customSpecialChar( String value){

		try {
			value = value.replaceAll("&Dagger;","\\‡");	//특수문자 처리를 위해
			value = value.replaceAll("&dagger;","\\†");	//특수문자 처리를 위해
		    value = value.replaceAll("&harr;","\\↔");	//특수문자 처리를 위해
		    value = value.replaceAll("_SC%24","\\$");	//특수문자 처리를 위해
		    value = value.replaceAll("_SC%25","\\&"); //특수문자 처리를 위해
		}catch(Exception e) {}
	    
		return value;
	}
    
    public static String convertHtmlCharacter(String data ) {
		try {
			String r = customSpecialChar(data);
			//r = r.replaceAll("amp;", "");
			r = r.replaceAll("&nbsp;", " ");
			r = r.replaceAll("&lt;", "<");
			r = r.replaceAll("&gt;", ">");
			r = r.replaceAll("&apos;", "'");
			r = r.replaceAll("&quot;", "\"");
			r = r.replaceAll("&amp;", "&");
			r = r.replaceAll("&#39;", "'");
			r = r.replaceAll("&hellip;", "…");
			r = r.replaceAll("&nbsp;", " ");
			r = r.replaceAll("&lsquo;", "‘");
			r = r.replaceAll("&rsquo;", "’");
			r = r.replaceAll("&ldquo;", "“");
			r = r.replaceAll("&rdquo;", "”");
			r = r.replaceAll("&ndash;", "–");
			r = r.replaceAll("&times;", "×");
			r = r.replaceAll("&divide;", "÷");
			r = r.replaceAll("&minus;", "-");
			r = r.replaceAll("&sim;", "∼");
			r = r.replaceAll("  ", " ");
			r = r.replaceAll("\t\t", " ");
			r = r.replaceAll("&radic;", "√");
			r = r.replaceAll("&comspace;", ", ");
			
			return r;
		}catch(Exception e){
			return data;
		}
	}
    
    public static String removeTag(String data ) {
		try {
			
			
			String r = customSpecialChar(data);
			r = convertHtmlCharacter(r);
//			r = r.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "").replaceAll("(\r\n|\r|\n|\n\r)", "").replaceAll("&nbsp;&nbsp;", " ").replaceAll("&nbsp;", " ").replaceAll("  ", " ").replaceAll("\t\t", " ");			
			String strRegEx = "<[^>]*>";
			r = r.replaceAll(strRegEx, "");
			return r.trim();
		}catch(Exception e){
			return data;
		}
	}

    /* refresh token 유실방지를 위해 저장소에도 저장함. */
    public void setRefreshTokenRepository(String fileName, String saveToken) {
        try {
            File folder = new File(REPOSITORY_ROOT + "/ecommerce/token");
            if (!folder.exists()) folder.mkdirs();

            String filePath = REPOSITORY_ROOT + "/ecommerce/token/" + fileName + ".token";
            File file = new File(filePath); // File객체 생성
            if (file.exists()) { // 파일이 존재
                file.delete();
            }
            FileWriter fw = new FileWriter(file, false);
            fw.write(saveToken);
            fw.close();
        }catch(IOException ie ) {
            ie.printStackTrace();
        }
    }

    public String getRefreshTokenRepository( String fileName ) {
        try {
            String saveToken = null;
            String filePath = REPOSITORY_ROOT + "/ecommerce/token/" + fileName + ".token";
            File file = new File(filePath); // File객체 생성
            if(file.exists()){ // 파일이 존재하면
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = null;
                while ((line = reader.readLine()) != null){
                    saveToken = line;
                }
                reader.close();
            }
            return saveToken;

        }catch(IOException ie ) {
            ie.printStackTrace();
            return null;
        }
    }


}
