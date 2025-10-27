package kr.co.hkcloud.palette3.common.twb.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 
 * 
 * @author Orange
 *
 */
public interface TwbUserBizService
{
    TelewebJSON selectRtn(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnForID(TelewebJSON jsonParams) throws TelewebAppException;
    boolean isUserReg(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertRtn(TelewebJSON jsonParams) throws TelewebAppException;
    
    /**
  	 * 
  	 * 사용자관리 - 등록 - 등록 사용자 CUSTCO 스키마에 INSERT
  	 * @Method Name  	: insertCertUser
  	 * @date   			: 2023. 7. 26.
  	 * @author   		: NJY
  	 * @version     	: 1.0
  	 * ----------------------------------------
  	 * @param  mjsonParams	등록할 사용자 정보
  	 * @return objRetParams
  	 * @throws TelewebAppException
  	 */
    TelewebJSON insertCertUser(TelewebJSON jsonParams) throws TelewebAppException;
    
    TelewebJSON updateRtn(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateRtnPwd(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateRtnLoginInfo(TelewebJSON jsonParams, String strUserID, String strAccessIP) throws TelewebAppException;
    TelewebJSON updateRtnLogoutInfo(TelewebJSON jsonParams, String strUserID) throws TelewebAppException;
    TelewebJSON updateRtnFailPwd(TelewebJSON jsonParams, int strUserID, int intFailCnt) throws TelewebAppException;
    byte[] procEncDecBytes(TelewebJSON mjsonParams) throws TelewebAppException;
    String createEncryptKey() throws TelewebAppException;
    String keyEncString(String orgPwd) throws TelewebAppException;
    String keyDecString(String encStr) throws TelewebAppException;
    String pwdEncString(String orgPwd, String encryptKey) throws TelewebAppException;
    String pwdDecString(String encStr, String encryptKey) throws TelewebAppException;
    
    TelewebJSON initPswd(TelewebJSON mjsonParams);
    TelewebJSON insertChatUser(TelewebJSON mjsonParams);
}
