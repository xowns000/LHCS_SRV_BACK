package kr.co.hkcloud.palette3.excel.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 대용량 엑셀 공통 서비스
 * 
 * @author RND
 *
 */
public interface LrgeCpctyExcelCmmnServcie
{
    TelewebJSON requestLargeExcelDown(String strSqlNameSpace, String strSqlName, TelewebJSON jsonParams) throws TelewebAppException;
}
