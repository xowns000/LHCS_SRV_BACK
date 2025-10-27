package kr.co.hkcloud.palette3.admin.lkag.custco.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

/**
 * packageName    : kr.co.hkcloud.palette3.admin.lkag.custco.app
 * fileName       : LkagCustcoService
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
public interface LkagCustcoService {
    TelewebJSON selectList(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectTblList(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectHeadersList(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON treeHeadersList(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON treeParamsList(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insert(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON update(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON delete(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertParamsTbl(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertHeaders(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateHeaders(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteHeaders(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON dpcnChk(TelewebJSON jsonParams) throws TelewebAppException;
}
