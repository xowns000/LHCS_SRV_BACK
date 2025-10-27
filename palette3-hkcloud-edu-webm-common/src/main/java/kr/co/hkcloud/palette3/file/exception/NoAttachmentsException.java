package kr.co.hkcloud.palette3.file.exception;


import kr.co.hkcloud.palette3.exception.PaletteException;


/**
 * 첨부파일이 없음
 * 
 * @author RND
 *
 */
public class NoAttachmentsException extends PaletteException
{

    /**
     * 
     */
    private static final long serialVersionUID = -6480247994441204391L;


    public NoAttachmentsException() {
        super("첨부된 파일이 없습니다.");
    }


    public NoAttachmentsException(String message) {
        super(message);
    }


    public NoAttachmentsException(String message, Throwable cause) {
        super(message, cause);
    }


    public NoAttachmentsException(Throwable cause) {
        super(cause);
    }
}
