package kr.co.hkcloud.palette3.core.security.crypto;


import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;


/**
 * Session Bean implementation class AbstractCipherTypeHandler 컬럼 암복호화 수행 하는 type handler 추상클래스
 */
public abstract class AbstractCipherTypeHandler implements TypeHandler<String>
{
//    protected static final PropertiesConfiguration env = PaletteOldProperties.getInstance().getPaletteConfig();

    /**
     * @see org.apache.ibatis.type.TypeHandler#setParameter(java.sql.PreparedStatement, int, java.lang.Object, org.apache.ibatis.type.JdbcType)
     */
    @Override
    public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException
    {
        // 암호화 여부 확인
        if(isCipher()) {
            try {
                parameter = encode(parameter);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        ps.setString(i, parameter);
    }


    /**
     * @see org.apache.ibatis.type.TypeHandler#getResult(java.sql.ResultSet, java.lang.String)
     */
    @Override
    public String getResult(ResultSet rs, String columnName) throws SQLException
    {
        String value = rs.getString(columnName);
        // 암호화 여부 확인
        if(isCipher() && value != null && Base64.isBase64(value)) {
            try {
                value = decode(value);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }


    /**
     * @see org.apache.ibatis.type.TypeHandler#getResult(java.sql.ResultSet, int)
     */
    @Override
    public String getResult(ResultSet rs, int columnIndex) throws SQLException
    {
        String value = rs.getString(columnIndex);
        // 암호화 여부 확인
        if(isCipher() && value != null && Base64.isBase64(value)) {
            try {
                value = decode(value);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }


    /**
     * @throws SQLException
     * @see                 org.apache.ibatis.type.TypeHandler#getResult(java.sql.CallableStatement, int)
     */
    @Override
    public String getResult(CallableStatement cs, int columnIndex) throws SQLException
    {
        String value = cs.getString(columnIndex);

        // 암호화 여부 확인
        if(isCipher() && value != null && Base64.isBase64(value)) {
            try {
                value = decode(value);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }


    /**
     * 암호화 여부
     * 
     * @return 암호화 여부
     */
    protected abstract boolean isCipher();


    /**
     * 암호화 수행
     * 
     * @param  encryptString                      변환 문자
     * @return                                    암호화된 문자
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     */
    protected String encode(String encryptString) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
    {
//        String encryptKey = env.getString("realDevMode").equals("REAL")?env.getString("RealEncryptKey"):env.getString("DevEncryptKey");
        String encryptKey = System.getProperty("palette.chat.key");

        if(encryptString != null && !"".equals(encryptString)) {
            encryptString = AES256Cipher.encryptString(encryptString, encryptKey);
        }

        return encryptString;
    }


    /**
     * 복호화 수행
     * 
     * @param  encryptString                      변환 문자
     * @return                                    decryptString 복호화된 문자
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     */
    protected String decode(String encryptString) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
    {
//        String decryptKey = env.getString("realDevMode").equals("REAL")?env.getString("RealEncryptKey"):env.getString("DevEncryptKey");
        String decryptKey = System.getProperty("palette.chat.key");
        String decryptString = "";

        if(encryptString != null && !"".equals(encryptString)) {
            decryptString = AES256Cipher.decryptString(encryptString, decryptKey);
        }

        return decryptString;

    }

}
