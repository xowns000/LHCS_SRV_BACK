package kr.co.hkcloud.palette3.exception.teleweb;

import kr.co.hkcloud.palette3.exception.model.ErrorCode;

/**
 * 
 * @author jangh
 *
 */
public class EntityNotFoundException extends BusinessException
{
    /**
     * 
     */
    private static final long serialVersionUID = 2797920609004403926L;


    public EntityNotFoundException(String message) {
        super(message, ErrorCode.ENTITY_NOT_FOUND);
    }
}
