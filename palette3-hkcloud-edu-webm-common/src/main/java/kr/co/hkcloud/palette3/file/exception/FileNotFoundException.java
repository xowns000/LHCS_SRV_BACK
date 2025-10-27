package kr.co.hkcloud.palette3.file.exception;


import kr.co.hkcloud.palette3.exception.PaletteException;


/**
 * 파일을 찾을 수 없음
 * 
 * @author RND
 *
 */
public class FileNotFoundException extends PaletteException
{
    /**
     * 
     */
    private static final long serialVersionUID = -4848433023929649649L;


    public FileNotFoundException() {
        super("파일을 찾을 수 없습니다.");
    }


    public FileNotFoundException(String message) {
        super(message);
    }


    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }


    public FileNotFoundException(Throwable cause) {
        super(cause);
    }
}
