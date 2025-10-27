package kr.co.hkcloud.palette3.layout.service;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

/**
 * packageName    : kr.co.hkcloud.palette3.layout.service
 * fileName       :
 * author         : njy
 * date           : 2024-04-17
 * description    : <<여기 설명>>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-04-17           njy            최초 생성
 * </pre>
 */
public interface LayoutService {
    public TelewebJSON selectLayout(TelewebJSON jsonParams) throws TelewebAppException;

}
