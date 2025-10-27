package kr.co.hkcloud.palette3.core.palette.asp.app;


import kr.co.hkcloud.palette3.core.palette.asp.domain.TwbAspCust;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 고객사관리 서비스
 * 
 * @author Orange
 */
public interface TalkAspCustService
{
    TwbAspCust findByCustcoId(String custcoId) throws TelewebAppException;
}
