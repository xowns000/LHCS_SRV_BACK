package kr.co.hkcloud.palette3.phone.lm.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneLmUsRaterManageService
{
    /* LMS담당자 리스트 조회 */
    TelewebJSON selectRtnLmUsRater(TelewebJSON jsonParams) throws TelewebAppException;

    /* 관리자 리스트 조회 */
    TelewebJSON selectRtnMngrUser(TelewebJSON jsonParams) throws TelewebAppException;

    /* LMS담당자 등록 */
    TelewebJSON processRtnLmUsRater(TelewebJSON jsonParams) throws TelewebAppException;
}
