package kr.co.hkcloud.palette3.common.log.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 접속로그공통 서비스 인터페이스
 *
 * @author Orange
 *
 */
public interface AccesLogCmmnService {

    TelewebJSON insertAccesLog(TelewebJSON jsonParams) throws TelewebAppException;
}
