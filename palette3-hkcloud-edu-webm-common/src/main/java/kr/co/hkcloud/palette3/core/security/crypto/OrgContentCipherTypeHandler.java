package kr.co.hkcloud.palette3.core.security.crypto;


import java.io.IOException;
import java.io.StringReader;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.util.FileCopyUtils;


/**
 * Session Bean implementation class AbstractCipherTypeHandler 채팅내용 (장문) 컬럼 암복호화 수행 하는 type handler
 */
public class OrgContentCipherTypeHandler extends AbstractCipherTypeHandler
{
    /**
     * 채팅내용 (장문) 설정파일의 암호화 옵션에 따라 암복호화 여부값 셋팅
     * 
     * @return 암호화 여부
     */
    @Override
    protected final boolean isCipher()
    {
//        String isCipher = env.getString("isCipher");
        String isCipher = System.getProperty("palette.chat.cipher");

        return isCipher != null && isCipher.equals("Y") ? true : false;
    }


    /**
     * 채팅내용 (장문) clob 데이타 삽입 처리
     * 
     * @return 암호화 여부
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

        StringReader reader = new StringReader(parameter);
        Clob clob = ps.getConnection().createClob();

        try {
            FileCopyUtils.copy(reader, clob.setCharacterStream(1));
        }
        catch(IOException ex) {
            throw new DataAccessResourceFailureException("Could not copy into LOB stream", ex);
        }

        ps.setClob(i, clob);

    }

}
