package kr.co.hkcloud.palette3.common.log.app;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * packageName    : kr.co.hkcloud.palette3.common.log.app
 * fileName       : SystemEventLogServiceImpl
 * author         : KJD
 * date           : 2023-12-29
 * description    : 주요 시스템설정정보 변경 로깅처리.
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-12-29        KJD       최초 생성
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@Service("systemEventLogService")
public class SystemEventLogServiceImpl implements SystemEventLogService {

    private final TwbComDAO twbComDAO;
    private String namespace = "kr.co.hkcloud.palette3.common.log.dao.SystemEventLogMapper";
    
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertSystemEventLog(TelewebJSON jsonParams) throws TelewebAppException {
        return twbComDAO.insert(namespace, "INSERT_PLT_SYS_LOG", jsonParams);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectSysLogList(TelewebJSON jsonParams) throws TelewebAppException {
        return twbComDAO.select(namespace, "selectSysLogList", jsonParams);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectSeList(TelewebJSON jsonParams) throws TelewebAppException {
        return twbComDAO.select(namespace, "selectSeList", jsonParams);
    }
}
