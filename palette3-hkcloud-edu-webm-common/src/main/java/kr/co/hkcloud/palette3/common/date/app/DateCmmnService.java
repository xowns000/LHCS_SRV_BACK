package kr.co.hkcloud.palette3.common.date.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 날짜공통 서비스 인터페이스
 * 
 * @author Orange
 *
 */
public interface DateCmmnService
{
    TelewebJSON selectServerDate(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectAddDate(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectDiffDate(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectLastDay(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectDatePattern(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectAfterDatePattern(TelewebJSON jsonParams) throws TelewebAppException;
}
