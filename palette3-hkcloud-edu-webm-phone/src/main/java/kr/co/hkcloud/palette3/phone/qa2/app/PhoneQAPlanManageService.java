package kr.co.hkcloud.palette3.phone.qa2.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneQAPlanManageService
{
    TelewebJSON selectRtnQa(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON insertRtnQa(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnEvlPaper(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON deleteRtnQa(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON updateRtnQa(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnPhoneCnslHist(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnChatCnslHist(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnQaDivCnt(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnQaDiv(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON processRtnQaDiv(TelewebJSON jsonParams) throws TelewebAppException;

}
