package kr.co.hkcloud.palette3.common.log.app;


import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 접속로그공통 서비스 인터페이스 구현체
 *
 * @author Orange
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("accesLogCmmnService")
public class AccesLogCmmnServiceImpl implements AccesLogCmmnService {

    private final TwbComDAO twbComDAO;


    /**
     * 접속로그 테이블 삽입
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertAccesLog(TelewebJSON jsonParams) throws TelewebAppException {
        return twbComDAO.insert("kr.co.hkcloud.palette3.common.log.dao.AccesLogCmmnMapper", "INSERT_PLT_PWD_LOG", jsonParams);
    }

}
