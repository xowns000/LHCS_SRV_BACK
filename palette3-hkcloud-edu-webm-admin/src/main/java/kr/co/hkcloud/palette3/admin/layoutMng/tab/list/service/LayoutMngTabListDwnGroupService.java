package kr.co.hkcloud.palette3.admin.layoutMng.tab.list.service;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

/**
 * packageName    : kr.co.hkcloud.palette3.admin.layoutMng.tab.list.service
 * fileName       :
 * author         : njy
 * date           : 2024-04-01
 * description    : <<여기 설명>>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-04-01           njy            최초 생성
 * </pre>
 */
public interface LayoutMngTabListDwnGroupService {
    public TelewebJSON insertUpdateDwnGroup(TelewebJSON jsonParam) throws TelewebAppException;
    public TelewebJSON deleteDwnGroup(TelewebJSON jsonParam) throws TelewebAppException;
    public TelewebJSON deleteDwnGroupDtl(TelewebJSON jsonParam) throws TelewebAppException;

}
