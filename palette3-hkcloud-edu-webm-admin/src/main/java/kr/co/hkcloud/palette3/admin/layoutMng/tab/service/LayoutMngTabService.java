package kr.co.hkcloud.palette3.admin.layoutMng.tab.service;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

/**
 * packageName    : kr.co.hkcloud.palette3.admin.layoutMng.tab.service
 * fileName       : LayoutMngTabService
 * author         : njy
 * date           : 2024-03-25
 * description    : <<여기 설명>>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-03-25           njy            최초 생성
 * </pre>
 */
public interface LayoutMngTabService {
    public TelewebJSON selectTabList(TelewebJSON jsonParam) throws TelewebAppException;
    public TelewebJSON insertUpdateTab(TelewebJSON jsonParam) throws TelewebAppException;
    public TelewebJSON deleteTab(TelewebJSON jsonParam) throws TelewebAppException;
    public TelewebJSON dpcnChkTab(TelewebJSON jsonParam) throws TelewebAppException;
}
