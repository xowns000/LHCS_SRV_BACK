package kr.co.hkcloud.palette3.exception.teleweb;


/**
 * 상담메인(vue) 파라미터 위변조 감지
 * 
 * @author 서주희
 *
 */
public class TelewebMainParamSignatureException extends RuntimeException
{
    /**
     * 
     */
    private static final long serialVersionUID = -7265402590459645863L;


    /**
     * 
     */
    public TelewebMainParamSignatureException() {
        super("Main Params Signature Excepiton :::");
    }

}
