package kr.co.hkcloud.palette3.core.security.crypto;


/**
 * 대신 Crypto.sha256 사용할 것!
 * 
 * @author jangh
 *
 */
public class ScrtSHA
{

//    public String encrypt(String input) throws NoSuchAlgorithmException
//    {
//        String output = "";
//
//        StringBuffer sb = new StringBuffer();
//        MessageDigest md = MessageDigest.getInstance("SHA-256");
//        md.update(input.getBytes());
//
//        byte[] msgb = md.digest();
//
//        for (int i = 0; i < msgb.length; i++)
//        {
//            byte temp = msgb[i];
//            String str = Integer.toHexString(temp & 0xFF);
//
//            while (str.length() < 2)
//            {
//                str = "0" + str;
//            }
//            str = str.substring(str.length() - 2);
//            sb.append(str);
//        }
//        output = sb.toString();
//        return output;
//    }
}
