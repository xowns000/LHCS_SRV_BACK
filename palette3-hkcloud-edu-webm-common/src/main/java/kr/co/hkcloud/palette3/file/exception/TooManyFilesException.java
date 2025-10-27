package kr.co.hkcloud.palette3.file.exception;


import kr.co.hkcloud.palette3.exception.PaletteException;


/**
 * 허용된 첨부파일 갯수 초과
 * 
 * @author leeiy
 *
 */
public class TooManyFilesException extends PaletteException
{

    /**
     * 
     */
    private static final long serialVersionUID = -3861301915379056739L;


    public TooManyFilesException() {
        super("허용된 첨부파일 갯수를 초과했습니다.");
    }


    public TooManyFilesException(String message) {
        super(message);
    }


    public TooManyFilesException(String message, Throwable cause) {
        super(message, cause);
    }


    public TooManyFilesException(Throwable cause) {
        super(cause);
    }
}
