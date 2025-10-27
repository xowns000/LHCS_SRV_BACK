package kr.co.hkcloud.palette3.phone.qa2.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneQAUsRaterManageService
{

    /* QA담당자 리스트 조회 */
    TelewebJSON selectRtnQaUsRater(TelewebJSON jsonParams) throws TelewebAppException;

    /* 관리자 리스트 조회 */
    TelewebJSON selectRtnMngrUser(TelewebJSON jsonParams) throws TelewebAppException;

    /* QA담당자 등록 */
    TelewebJSON processRtnQaUsRater(TelewebJSON jsonParams) throws TelewebAppException;
}
