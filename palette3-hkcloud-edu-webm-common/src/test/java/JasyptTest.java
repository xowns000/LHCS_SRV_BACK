import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import kr.co.hkcloud.palette3.config.security.datasource.JasyptConfig;
import kr.co.hkcloud.palette3.core.security.crypto.AES128Cipher;
import kr.co.hkcloud.palette3.core.security.crypto.AES256Cipher;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.test.context.ActiveProfiles;

/**
 * packageName    : PACKAGE_NAME
 * fileName       : JasyptTest
 * author         : USER
 * date           : 2023-10-10
 * description    : Jasypt Encoding Test
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-10        USER       최초 생성
 */
@DisplayName("Palette 기본 기능 테스트")
@ActiveProfiles(profiles = "local")
@AutoConfigureMockMvc
@Slf4j
public class JasyptTest {

    @Test
    @DisplayName("Jasypt를 활용하여 서버 설정 암복호화 테스트")
    public void jasypt암복호화테스트() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(JasyptConfig.KEY);
        config.setAlgorithm(JasyptConfig.ALGORITHM);
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);

        log.info("Welcome-Palette3.0 => " + encryptor.encrypt("Welcome-Palette3.0"));

        //테넌트아이디_##_CUSTCO_ID_##_인증키 로그인아이디_##_인증키 UserID_##_키 만료일자_##_더미데이타
        log.info("v3 key => " + encryptor.encrypt("public_##_1_##_plt30_##_1_##_20241231235959_##_" + UUID.randomUUID()));
        log.info("v3 starlaw key => " + encryptor.encrypt("starlaw_##_1_##_starlaw_##_3_##_20241231235959_##_" + UUID.randomUUID()));
        log.info("v3 starlaw key => " + encryptor.decrypt(
            "8BgLmgAPSXpEkNxPikhyZuwIC9Z3gPEqACYNjNP6T/SBF3Cpd3U60Ioj1diBs1quQhX1kkuYfVQDte3WNyJjOgnU3xeGF8HnZPBriyZ05FRtnsqUXkThmwxwl1vJwJ5NF0Yd1D0pgBXVIwV0nTW1uk/jxnm0hJsI4fN6lo6dtkI="));

        log.info("v3 starlaw key(allpass) => " + encryptor.encrypt(
                "starlaw_##_1_##_starlaw_##_3_##_29991231235959_##_" + UUID.randomUUID() + "_##_allpass"));
        //==> CrZd8HL3OquGFc+oXBKGwleU3SCbLnByYCfTKh+ZIkSmTP/fsLDTBRP2buDIiT11iLG+81k9ARkM18P/VQRoY5DgG42qwYVOPipkUBXdGzSXtupJ+KvQAIBY31Fn7QKokGj84b07gLJuloeUKsSiwMsjzg2D6ukaAEBCNXQ8iI9QSUMW2xv93l93S7UXvA88

        log.info(
            "v3 goodwearmall key(DEV) => " + encryptor.encrypt("goodwearmall_##_1_##_goodwearmall_##_3_##_29991231235959_##_" + UUID.randomUUID() + "_dev"));
        log.info("v3 goodwearmall key(DEV) => " + encryptor.decrypt(
            "iGyUpyUtuQQe6N6Y16OddL9CsxL0mdXTg2JZ0j3KNKbYjCvKHvbwqw/McN92QfXYdE0b8qSJIKJ9xuvn7TP3IFrp0NPCsRKaBy/u+nkTo6U4+YvTzm11oaOmmo3uY11D"));

        log.info("v3 goodwearmall key(local) => " + encryptor.encrypt(
            "goodwearmall_##_1_##_goodwearmall_##_3_##_29991231235959_##_" + UUID.randomUUID() + "_##_local"));
        log.info("v3 goodwearmall key(productioncloud) => " + encryptor.encrypt(
            "goodwearmall_##_1_##_goodwearmall_##_3_##_29991231235959_##_" + UUID.randomUUID() + "_##_productioncloud"));
        //2024.0418 에 운영환경 발급함.
        log.info("v3 goodwearmall key(productioncloud) => " + encryptor.decrypt(
            "ceotZqYJT7s8kCYyWS5ygTpsTFrOtk8+y6r4Db33BS1Z8gg4GAhbs2p38+vEcQ7AXwrBbQCzSh40F4v3xlrhe5Q5QkvWTiluPPjhxL/spaPWgcYM4ZO+U8hc/sa8A6KGAexeWFw1Td8DQjA6LW6iS72DUPyzYeh/lEAT01e6GSPv3+4mmHF1Rbt+7RSiMHR41D5nkXIDGOrvfp0AK6Dz2g=="));

        log.info("v3 goodwearmall key(allpass) => " + encryptor.encrypt(
            "goodwearmall_##_1_##_goodwearmall_##_3_##_29991231235959_##_" + UUID.randomUUID() + "_##_allpass"));
        log.info("v3 goodwearmall key(allpass) => " + encryptor.decrypt(
            "UXzCwOm6/OpcDRPZZ40aVe83e1bHXI/HoyWh+4lK6JYGe3nrah6nDYJNcje2Jn7a7GjZiXigYTTOAdmJg2WV3RrekvOm1cKYHss+A07BO+nQW5Py6XlaMj3SnbQT4GM7Wxj8CekT4T0EZI2rxIc+P2BmFFLdXMdjf0KzBpieksgzjgKqC4lH6rE5HcpkBrEB"));

        int i = 1;
        log.info("================================================================================================");
        String s = "jdbc:log4jdbc:postgresql://121.67.187.119:5432/palette3??prepareThreshold=0";
        log.info("\n" + (i++) + ". " + s + "\n  => " + encryptor.encrypt(s) + "\n");

        s = "jdbc:log4jdbc:postgresql://172.16.0.100:5432/palette3";
        log.info("\n" + (i++) + ". " + s + "\n  => " + encryptor.encrypt(s) + "\n");

        s = "palette3";
        log.info("\n" + (i++) + ". " + s + "\n  => " + encryptor.encrypt(s) + "\n");

        s = "Palette12#$";
        log.info("\n" + (i++) + ". " + s + "\n  => " + encryptor.encrypt(s) + "\n");

        log.info("================================================================================================");
        log.info("===============> MICC cloud db");
        s = "jdbc:log4jdbc:postgresql://pg-ij9kv.vpc-cdb-kr.ntruss.com:5432/hkcloud?prepareThreshold=0";
        log.info("\n" + (i++) + ". " + s + "\n  => " + encryptor.encrypt(s) + "\n");
        s = "hkc_admin";
        log.info("\n" + (i++) + ". " + s + "\n  => " + encryptor.encrypt(s) + "\n");
        s = "hkc_admin1!";
        log.info("\n" + (i++) + ". " + s + "\n  => " + encryptor.encrypt(s) + "\n");

        log.info("================================================================================================");
        log.info("사용안함XXXXXXXXXXXXX ===============>GWM cloud db");
        s = "jdbc:log4jdbc:postgresql://pg-l9dtb.vpc-cdb-kr.ntruss.com:5432/hkcloud?prepareThreshold=0";
        log.info("\n" + (i++) + ". " + s + "\n  => " + encryptor.encrypt(s) + "\n");
        s = "gwm_admin";
        log.info("\n" + (i++) + ". " + s + "\n  => " + encryptor.encrypt(s) + "\n");
        s = "gwm_admin1!";
        log.info("\n" + (i++) + ". " + s + "\n  => " + encryptor.encrypt(s) + "\n");

        log.info("================================================================================================");
        log.info("===============>GWM cloud db - gwmcloud");
        s = "jdbc:log4jdbc:postgresql://pg-l9dtb.vpc-cdb-kr.ntruss.com:5432/gwmcloud?prepareThreshold=0";
        log.info("\n" + (i++) + ". " + s + "\n  => " + encryptor.encrypt(s) + "\n");
        s = "gwm_admin";
        log.info("\n" + (i++) + ". " + s + "\n  => " + encryptor.encrypt(s) + "\n");
        s = "gwm_admin1!";
        log.info("\n" + (i++) + ". " + s + "\n  => " + encryptor.encrypt(s) + "\n");

        log.info("===============>GWM연동용으로 제공받은 AES-256 암복호화용 키");
        log.info("\n 54f920a2dd1b4969b920a2dd1bf9691d :: " + encryptor.encrypt("54f920a2dd1b4969b920a2dd1bf9691d") + "\n");

        log.info("===============>#개인정보 암호화 설정용");
        log.info("\n" + (i++) + ". key\n  => " + encryptor.decrypt("9uy5OF31uepxMzVYab/vFdxoXJUR0l3Pwu4+atHKfyipb86j5CHGMyhPiCQ7jZKl") + "\n");
        log.info("\n" + (i++) + ". alg\n  => " + encryptor.decrypt("B6gA81d2icO3bIW1AXkrvvtKuIrRkbkYMNGo1H2cUo1F/mQZmfRrAys5Gw+M6UE7") + "\n");
        log.info("\n" + (i++) + ". dev-encrypt-key \n  => " + encryptor.decrypt("VGdqWIinNyLWXKLlSc9PL0gxfsFzec3SiZcyhHA8IhBLL2doeeO4HM/7mXadhch71Ejqme/Fj/NW3crLCwwX+A==") + "\n");
        log.info("\n" + (i++) + ". dev-encrypt-key \n  => " + encryptor.decrypt("NzPdf+oKz3+6XFaeTnlcR5V+MSeivJGysceLB8eQCx29fJK4kqLTKcRtuEGSCKLO/etDbLs1uU4BfW8P7iZElA==") + "\n");


        log.info("");
    }

    @Test
    @DisplayName("EXTERNAL-API-토큰발행")
    public void EXTERNAL_API_토큰발행() {

        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(JasyptConfig.KEY);
        config.setAlgorithm(JasyptConfig.ALGORITHM);
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);

        log.info("SW HEALTHCARE  => " + encryptor.encrypt(
            "palette3-swhealthcare_##_1_##_sw_login_id_##_3_##_29991231235959_##_" + UUID.randomUUID() + "_##_allpass"));
        // ==> FHeyMNl6jNknvgTGfT3RS5a3w1EQd1fqLVriEH3F/Jql7rNDbu5mqRyVwPbLH0NEGdsGt2eS+wAz0Dx+3iM2H5YEEpDzjEushrjU4f/B5Mp0onp0O4s2l3AA96sucDzCaQQw1qrPcSIKBrHkbSbEfUNNDwwZGobPkHiZyqQCfBAqLNRqMlBvDOEFnewI1iQJJ7NL7QPZP375Ys2QcFqUQQ==


    }

    @Test
    public void testIpAddressMatcher() throws Exception {
        List<IpAddressMatcher> ipAddressMatchers = new ArrayList<>();
        String allowIp = "127.0.0.1/32,121.67.187.114/32";
        String[] splitStr = allowIp.split(",");
        for (String s : splitStr) {
            ipAddressMatchers.add(new IpAddressMatcher(s)); // Spring Security에서 제공하는 IP 매칭 여부를 판단하기 위한 객체
        }
        log.info("======================================");
        log.info("==> :: " + isAccessible(ipAddressMatchers, "121.67.187.114"));
    }

    public boolean isAccessible(List<IpAddressMatcher> ipAddressMatchers, String ipAddress) {
        //List<IpAddressMatcher> ipAddressMatchers = this.getIpAddressMatchers();
        return ipAddressMatchers.stream().anyMatch(matcher -> matcher.matches(ipAddress));
    }


    @Test
    public void pgcrypto암복호화테스트() throws InvalidAlgorithmParameterException, UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, DecoderException {
        String enstr = AES128Cipher.encryptString("김종덕", "MnR6bzI4bjdocGEw");
        String enstrHex = Hex.encodeHexString(enstr.getBytes("UTF-8"));
        System.out.println( "==>" + enstr +"<==");
        System.out.println( "==>" + enstrHex +"<==");

        String destrHex = new String(Hex.decodeHex(enstrHex));
        String destr = AES128Cipher.decryptString( destrHex, "MnR6bzI4bjdocGEw" ) ;
        System.out.println( "==>" + destrHex +"<==");
        System.out.println( "==>" + destr +"<==");


        String enstr2 = AES256Cipher.encryptString("김종덕", "MnR6bzI4bjdocGEw");
        System.out.println( "==>" + enstr2 +"<==");
        System.out.println( "==>" + Hex.encodeHexString(enstr2.getBytes("UTF-8")) +"<==");


        System.out.println("===========================================");
        String iv = encryptionKey;
        System.out.println("iv = "+iv);

        String encryptedData= encryptWithIVandKey(iv ,encryptionKey,"김종덕");
        String encryptedDataHex= encryptWithIVandKeyHex(iv ,encryptionKey,"김종덕");
        System.out.println("encryptedData : " + encryptedData);
        System.out.println("encryptedDataHex : " + encryptedDataHex);
        System.out.println(Hex.encodeHexString(encryptedData.getBytes("UTF-8")));
        String decryptedData = decrypt (iv,encryptionKey,encryptedData);
        System.out.println(decryptedData);

        System.out.println("===========================================");
        String encryptedData2= AES256Cipher.encryptString("김종덕", "MnR6bzI4bjdocGEw");
        String encryptedData2Hex= AES256Cipher.encryptHexString("김종덕", "MnR6bzI4bjdocGEw");
        System.out.println("encryptedData2 : " + encryptedData2);
        System.out.println("encryptedData2Hex : " + encryptedData2Hex);

    }

    static final String encryptionKey = "MnR6bzI4bjdocGEw";


        static byte[] doFinal(int encryptMode, SecretKey key, String iv, byte[] bytes) {

            try {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(encryptMode, key, new IvParameterSpec(iv.getBytes()));
                byte[] data = cipher.doFinal(bytes);

                return data;

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }
            return null;

        }



        static SecretKey generateKey(String passphrase) {

            SecretKey key = null;

            try {

                key = new SecretKeySpec(passphrase.getBytes("UTF-8"), "AES");


            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }

            return key;
        }




        static String getEncryptionIV() {
            SecureRandom random = new SecureRandom();
            byte[] ivBytes = new byte[16];
            random.nextBytes(ivBytes);
            return DatatypeConverter.printHexBinary(ivBytes);
        }

        static String encryptWithIVandKey( String iv, String passphrase, final String strToEncrypt) {
            String encryptedStr = "";

            try {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                SecretKey key = generateKey(passphrase);
                cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv.getBytes()));

                encryptedStr = DatatypeConverter.printBase64Binary(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }


            return encryptedStr;
        }

        static String encryptWithIVandKeyHex( String iv, String passphrase, final String strToEncrypt) {
                String encryptedStr = "";

                try {
                    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                    SecretKey key = generateKey(passphrase);
                    cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv.getBytes()));

                    encryptedStr = DatatypeConverter.printHexBinary(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e);
                }


                return encryptedStr;
            }

        static String decrypt(String iv, String passphrase, String ciphertext) {
            try {
                SecretKey key = generateKey(passphrase);
                byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, key, iv, DatatypeConverter.parseBase64Binary(ciphertext));
                return new String(decrypted, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }
            return "";
        }
}
