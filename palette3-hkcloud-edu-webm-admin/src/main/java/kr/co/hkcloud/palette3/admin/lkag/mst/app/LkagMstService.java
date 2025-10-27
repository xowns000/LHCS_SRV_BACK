package kr.co.hkcloud.palette3.admin.lkag.mst.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

/**
 * packageName    : kr.co.hkcloud.palette3.admin.lkag.mst.app
 * fileName       : LkagMstService
 * author         : KJD
 * date           : 2024-03-20
 * description    : << 여기 설명 >>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-03-20        KJD       최초 생성
 * </pre>
 */
public interface LkagMstService {
    TelewebJSON selectList(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectBox(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insert(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON update(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON delete(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON dpcnChkMstNm(TelewebJSON jsonParams) throws TelewebAppException;
}
