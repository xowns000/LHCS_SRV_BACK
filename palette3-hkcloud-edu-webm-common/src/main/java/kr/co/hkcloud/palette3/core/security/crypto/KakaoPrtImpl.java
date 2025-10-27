package kr.co.hkcloud.palette3.core.security.crypto;


import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;


@Service("KakaoPrt")
public class KakaoPrtImpl implements KakaoPrt
{
    private Cipher          rijndael;
    private SecretKeySpec   key;
    private IvParameterSpec initalVector;

    private static String kakaokey = "teletalk-sonykorea-201901";
    private static String kakaoIv  = "teletalk";


    /**
     * Creates a StringEncrypter instance.
     * 
     * @param  key           A kOey string which is converted into UTF-8 and hashed by MD5. Null or an empty string is not allowed.
     * @param  initialVector An initial vector string which is converted into UTF-8 and hashed by MD5. Null or an empty string is not allowed.
     * @return
     * @throws Exception
     */

    public KakaoPrtImpl() throws Exception {
        // Create a AES algorithm.
        this.rijndael = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // Initialize an encryption key and an initial vector.
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        this.key = new SecretKeySpec(md5.digest(kakaokey.getBytes("UTF8")), "AES");
        this.initalVector = new IvParameterSpec(md5.digest(kakaoIv.getBytes("UTF8")));
    }


    /**
     * Encrypts a string.
     * 
     * @param  value     A string to encrypt. It is converted into UTF-8 before being encrypted. Null is regarded as an empty string.
     * @return           An encrypted string.
     * @throws Exception
     */
    public String encrypt(String value) throws Exception
    {
        if(value == null)
            value = "";

        // Initialize the cryptography algorithm.
        this.rijndael.init(Cipher.ENCRYPT_MODE, this.key, this.initalVector);

        // Get a UTF-8 byte array from a unicode string.
        byte[] utf8Value = value.getBytes("UTF8");

        // Encrypt the UTF-8 byte array.
        byte[] encryptedValue = this.rijndael.doFinal(utf8Value);

        // Return a base64 encoded string of the encrypted byte array.
        return KakaoBase64Encoder.encode(encryptedValue);
    }


    /**
     * Decrypts a string which is encrypted with the same key and initial vector.
     * 
     * @param  value     A string to decrypt. It must be a string encrypted with the same key and initial vector. Null or an empty string is not allowed.
     * @return           A decrypted string
     * @throws Exception
     */
    public String decrypt(String value) throws Exception
    {
        if(value == null || value.equals(""))
            throw new Exception("The cipher string can not be null or an empty string.");

        // Initialize the cryptography algorithm.
        this.rijndael.init(Cipher.DECRYPT_MODE, this.key, this.initalVector);

        // Get an encrypted byte array from a base64 encoded string.
        byte[] encryptedValue = KakaoBase64Encoder.decode(value);

        // Decrypt the byte array.
        byte[] decryptedValue = this.rijndael.doFinal(encryptedValue);

        // Return a string converted from the UTF-8 byte array.
        return new String(decryptedValue, "UTF8");
    }
}
