package kr.co.hkcloud.palette3.core.security.crypto;


import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Hash
{
    public static String encryptMD5(String strData, Charset strCharSet) throws NoSuchAlgorithmException
    {
        return encrypt(strData, "MD5", strCharSet);
    }


    public static String encryptSHA1(String strData, Charset strCharSet) throws NoSuchAlgorithmException
    {
        return encrypt(strData, "SHA-1", strCharSet);
    }


    public static String encryptSHA256(String strData, Charset strCharSet) throws NoSuchAlgorithmException
    {
        return encrypt(strData, "SHA-256", strCharSet);
    }

    public static String encryptSHA256(String strData) throws NoSuchAlgorithmException
    {
        return encrypt(strData, "SHA-256", StandardCharsets.UTF_8);
    }


    public static String encryptSHA256(byte[] strData) throws NoSuchAlgorithmException
    {
        return encrypt(strData, "SHA-256");
    }


    public static String encryptMD5(byte[] strData) throws NoSuchAlgorithmException
    {
        return encrypt(strData, "MD5");
    }


    /**
     * 해쉬알고리즘으로 암호화 한다.
     * 
     * @param  strData                  원문
     * @param  strAlgCase               알고리즘구분(MD5, SHA-1, SHA-256)
     * @param  strCharSet               미정의시 빈문자
     * @throws NoSuchAlgorithmException
     */
    private static String encrypt(String strData, String strAlgCase, Charset strCharSet) throws NoSuchAlgorithmException
    {

        //java.security.MessageDigest 객체를 이용한해쉬알고리즘을 정의한다.
        MessageDigest objMD = MessageDigest.getInstance("MD5");
        if(strAlgCase.equals("SHA-1")) {
            objMD = MessageDigest.getInstance("SHA-1");
        }
        else if(strAlgCase.equals("SHA-256")) {
            objMD = MessageDigest.getInstance("SHA-256");
        }

        //원문을 바이트 Array로 변환시켜 메시지 다이제스트 객체에 로드한다.
        objMD.update(strData.getBytes(strCharSet));

        //해쉬알고리즘을 통한 메시지다이제스트 값을 추출한다.
        byte byteData[] = objMD.digest();

        //문자열로 변환한다.
        StringBuffer objStringBuffer = new StringBuffer();
        for(int i = 0; i < byteData.length; i++) {
            objStringBuffer.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return objStringBuffer.toString();

    }


    /**
     * 해쉬알고리즘으로 암호화 한다.
     * 
     * @param  strData                      원문
     * @param  strAlgCase                   알고리즘구분(MD5, SHA-1, SHA-256)
     * @param  strCharSet                   미정의시 빈문자
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    private static String encrypt(byte[] strData, String strAlgCase) throws NoSuchAlgorithmException
    {

        //java.security.MessageDigest 객체를 이용한해쉬알고리즘을 정의한다.
        MessageDigest objMD = MessageDigest.getInstance("MD5");
        if(strAlgCase.equals("SHA-1")) {
            objMD = MessageDigest.getInstance("SHA-1");
        }
        else if(strAlgCase.equals("SHA-256")) {
            objMD = MessageDigest.getInstance("SHA-256");
        }

        //원문을 바이트 Array로 변환시켜 메시지 다이제스트 객체에 로드한다.
        objMD.update(strData);
        //해쉬알고리즘을 통한 메시지다이제스트 값을 추출한다.
        byte byteData[] = objMD.digest();

        //문자열로 변환한다.
        StringBuffer objStringBuffer = new StringBuffer();
        for(int i = 0; i < byteData.length; i++) {
            objStringBuffer.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return objStringBuffer.toString();

    }

}
