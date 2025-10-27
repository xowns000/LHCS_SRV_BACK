package kr.co.hkcloud.palette3.core.security.crypto;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import lombok.extern.slf4j.Slf4j;


/**
 * 암호화 클래스
 * 
 * @author Orange
 *
 */
@Slf4j
public class Crypto
{
    /**
     * SHA-256으로 암호화한 결과를 반환한다
     * 
     * @param  string
     * @return
     */
    public static String sha256(final String string)
    {
        log.debug("Crypto.sha256 ::::");
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(string.getBytes(StandardCharsets.UTF_8));
            StringBuffer hexString = new StringBuffer();

            for(int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
