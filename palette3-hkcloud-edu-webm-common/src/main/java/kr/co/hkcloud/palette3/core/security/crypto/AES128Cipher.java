package kr.co.hkcloud.palette3.core.security.crypto;


import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AES128Cipher
{

    public static byte[]    ivBytes = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    public static SecretKey key;


    /**
     * AES암호화 알고리즘(128비트)을 이용하여 바이트어레이 형식의 값을 암호화한다(대칭키방식).
     * 
     * @param  bytOrg                             암호화 할 바이트어레이
     * @param  strKey                             암호화 키 문자열 값
     * @return                                    암호화 된 바이트어레이
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     */
    public static byte[] encrypt(byte[] bytOrg, String strKey) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException
    {
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(strKey.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
        return cipher.doFinal(bytOrg);
    }


    /**
     * AES암호화 알고리즘(128비트)을 이용하여 문자열 형식의 값을 암호화한다(대칭키방식).
     * 
     * @param  strOrg                               암호화 할 문자열값
     * @param  strKey                               암호화 키 문자열 값
     * @return                                      Base64 인코딩 된 암호화 된 값
     * @throws java.io.UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String encryptString(String strOrg, String strKey) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
    {
        byte[] textBytes = strOrg.getBytes(StandardCharsets.UTF_8);
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(strKey.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
        return Base64.encode(cipher.doFinal(textBytes));
    }


    /**
     * AES암호화 알고리즘(128비트)를 이용하여 암호화 된 바이트어레이 값을 암호화키(대칭키)를 이용하여 복화하한다.
     * 
     * @param  encrypted                          암호화 된 바이트어레이
     * @param  strKey                             암호화(대칭키) 키 문자열 값
     * @return                                    복호화 된 바이트어레이
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     */
    public static byte[] decrypt(byte[] encrypted, String strKey) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException
    {
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(strKey.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
        return cipher.doFinal(encrypted);
    }


    /**
     * AES암호화 알고리즘(128비트)를 이용하여 암호화 된 문자열 값을 암호화키(대칭키)를 이용하여 복화하한다.
     * 
     * @param  strEnc                               암호화 된 문자열 값
     * @param  strKey                               암호화(대칭키) 키 문자열 값
     * @return                                      복호화 된 문자열 값
     * @throws java.io.UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String decryptString(String strEnc, String strKey) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
    {
        byte[] textBytes = Base64.decode(strEnc);
        // byte[] textBytes = str.getBytes(StandardCharsets.UTF_8);
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(strKey.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);

        return new String(cipher.doFinal(textBytes), StandardCharsets.UTF_8);
    }


    /**
     * AES알고리즘의 바이트 어레이 형식으로 암호화 키를 암호화한다.
     * 
     * @param  strKey 문자열 암호화 키 값
     * @return        암호화 된 암호화 키 값
     */
    public static byte[] generateKey(String strKey)
    {
        SecretKeySpec newKey = new SecretKeySpec(strKey.getBytes(StandardCharsets.UTF_8), "AES");
        return newKey.getEncoded();
    }
}