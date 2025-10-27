package kr.co.hkcloud.palette3.exception.teleweb;


import java.util.List;

import org.springframework.validation.ObjectError;

import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;


/**
 * Validation Exception
 * 
 * @author R&D
 *
 */
public class TelewebValidationException extends RuntimeException
{
    private static final long serialVersionUID = -3048852574571734484L;

    private PaletteValidationCode twbErrorMsg;
    private List<ObjectError>     objError;


    public TelewebValidationException(String message, PaletteValidationCode twbErrorMsg) {
        super(message);
        this.twbErrorMsg = twbErrorMsg;
    }


    public TelewebValidationException(PaletteValidationCode twbErrorMsg) {
        super(twbErrorMsg.getMessage());
        this.twbErrorMsg = twbErrorMsg;
    }


    public TelewebValidationException(List<ObjectError> objError) {
        super();
        this.objError = objError;
    }


    public List<ObjectError> getListObjError()
    {
        return this.objError;
    }


    public PaletteValidationCode getErrorCode()
    {
        return twbErrorMsg;
    }
}
