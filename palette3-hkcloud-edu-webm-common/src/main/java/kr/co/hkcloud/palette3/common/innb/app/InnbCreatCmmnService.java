package kr.co.hkcloud.palette3.common.innb.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 고유번호생성 공통 서비스
 * 
 * @author RND
 *
 */
public interface InnbCreatCmmnService
{

    String getSeqNo(TelewebJSON jsonParams, String strBizCase) throws TelewebAppException;
    String getSeqNo(String tableName) throws TelewebAppException;
    String getSeqNo(String tableName, String strBizCase) throws TelewebAppException;
    String getLoginNextStringId() throws TelewebAppException;
    int  createSeqNo(String columnId) throws TelewebAppException;
}
