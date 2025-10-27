package kr.co.hkcloud.palette3.admin.layoutMng.tab.list.service;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

/**
 * packageName    : kr.co.hkcloud.palette3.admin.layoutMng.tab.list.service
 * fileName       :
 * author         : njy
 * date           : 2024-03-28
 * description    : <<여기 설명>>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-03-28           njy            최초 생성
 * </pre>
 */
public interface LayoutMngTabListThumnailService {
    public TelewebJSON selectThumnail(TelewebJSON jsonParam) throws TelewebAppException;
    public TelewebJSON insertUpdateThumnail(TelewebJSON jsonParam) throws TelewebAppException;
    public TelewebJSON deleteThumnail(TelewebJSON jsonParam) throws TelewebAppException;

}
