package kr.co.hkcloud.palette3.core.security.crypto;


/**
 * Session Bean implementation class AbstractCipherTypeHandler 채팅내용 컬럼 암복호화 수행 하는 type handler
 */
public class ContentCipherTypeHandler extends AbstractCipherTypeHandler
{
    /**
     * 채팅내용 (단문) 설정파일의 암호화 옵션에 따라 암복호화 여부값 셋팅
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

}
