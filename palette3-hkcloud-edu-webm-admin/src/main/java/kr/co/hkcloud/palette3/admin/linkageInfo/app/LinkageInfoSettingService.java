package kr.co.hkcloud.palette3.admin.linkageInfo.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import net.sf.json.JSONObject;

/**
 * packageName    : kr.co.hkcloud.palette3.admin.linkageInfo.app
 * fileName       :
 * author         : njy
 * date           : 2024-03-19
 * description    : <<여기 설명>>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-03-19           njy            최초 생성
 * </pre>
 */
public interface LinkageInfoSettingService {
    public TelewebJSON responseParamDetailInfo(JSONObject obj) throws TelewebAppException;
}
