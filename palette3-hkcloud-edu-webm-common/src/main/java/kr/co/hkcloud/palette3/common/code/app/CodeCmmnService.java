package kr.co.hkcloud.palette3.common.code.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 코드공통 서비스 인터페이스
 * 
 * @author Orange
 *
 */
public interface CodeCmmnService
{
    TelewebJSON selectRtnCodeBook(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnCachingCodeBook(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnCachingAspBook(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnCachingSenderAspBook(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnCachingAllAspBook(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectTranCommCode(TelewebJSON jsonParams) throws TelewebAppException;
}
