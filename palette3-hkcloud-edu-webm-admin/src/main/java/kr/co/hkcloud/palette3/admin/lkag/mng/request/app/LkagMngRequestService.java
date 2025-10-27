package kr.co.hkcloud.palette3.admin.lkag.mng.request.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

/**
 * packageName    : kr.co.hkcloud.palette3.admin.lkag.mng.request.app
 * fileName       : LkagMngRequestService
 * author         : KJD
 * date           : 2024-03-25
 * description    : << 여기 설명 >>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-03-25        KJD       최초 생성
 * </pre>
 */
public interface LkagMngRequestService {
    TelewebJSON selectList(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insert(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON update(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON delete(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON dpcnChk(TelewebJSON jsonParams) throws TelewebAppException;    /* 연동인터페이스명 중복체크용도 */
    TelewebJSON orderUpdate(TelewebJSON jsonParams) throws TelewebAppException;    /* 연동인터페이스명 중복체크용도 */
}
