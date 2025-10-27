package kr.co.hkcloud.palette3.common.log.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

/**
 * packageName    : kr.co.hkcloud.palette3.common.log.app
 * fileName       : SystemEventLogService
 * author         : KJD
 * date           : 2023-12-29
 * description    : << 여기 설명 >>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-12-29        KJD       최초 생성
 * </pre>
 */
public interface SystemEventLogService {
    TelewebJSON insertSystemEventLog(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectSysLogList(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectSeList(TelewebJSON jsonParams) throws TelewebAppException;
}
