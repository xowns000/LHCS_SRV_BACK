package kr.co.hkcloud.palette3.core.security.crypto;


public interface KakaoPrt
{
    String encrypt(String value) throws Exception;

    String decrypt(String value) throws Exception;
}
