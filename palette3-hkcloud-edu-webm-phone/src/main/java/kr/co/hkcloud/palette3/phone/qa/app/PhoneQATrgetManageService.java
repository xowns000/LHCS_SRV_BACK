package kr.co.hkcloud.palette3.phone.qa.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * SMS 서비스 인터페이스
 *
 */
public interface PhoneQATrgetManageService
{
    TelewebJSON selectRtn(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnDetails(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertRtn(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateRtn(TelewebJSON jsonParams) throws TelewebAppException;
    Boolean selectHaveExtractClose(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnCheckYN(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateRtnInsert(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnQA_ID(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteRtn(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON processRtnExtractClose(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnCnslCode(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnCnslCodeDetail(TelewebJSON jsonParams) throws TelewebAppException;
}