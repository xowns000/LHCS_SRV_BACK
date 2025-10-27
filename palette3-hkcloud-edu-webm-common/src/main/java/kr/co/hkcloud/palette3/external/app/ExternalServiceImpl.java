package kr.co.hkcloud.palette3.external.app;

import kr.co.hkcloud.palette3.common.twb.dao.TwbExternalDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.datasources.datasource.external.ExtrernalSqlSession;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
@Service("externalService")
public class ExternalServiceImpl implements ExternalService {

    @Autowired
    private TwbExternalDAO twbExternalDAO ;//= new TwbExternalDAO(ExternalConnectionFactory.getSqlSession("db1"));

    @Autowired
    private ExtrernalSqlSession extrernalSqlSession;
    /**
     * 시스템공지사항 데이터 조회 템플릿
     *
     * @return           TelewebJSON 형식의 조회결과 데이터
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON selectRtnSystemNotice1(TelewebJSON jsonParams) throws TelewebDaoException
    {
        twbExternalDAO = new TwbExternalDAO(extrernalSqlSession.getExternalSqlSession("db1"));
        return twbExternalDAO.select("kr.co.hkcloud.palette3.external.dao.ExternalMapper", "selectRtnSystemNotice", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnBrdSystem1(TelewebJSON jsonParams) throws TelewebAppException
    {
        twbExternalDAO = new TwbExternalDAO(extrernalSqlSession.getExternalSqlSession("db1"));

        return twbExternalDAO.select("kr.co.hkcloud.palette3.external.dao.ExternalMapper", "selectRtnBrdSystem", jsonParams);
//        return twbExternalDAO.select("", "selectRtnBrdSystem", jsonParams);
    }

    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnBrdList1(TelewebJSON jsonParams) throws TelewebAppException
    {


        twbExternalDAO=new TwbExternalDAO(extrernalSqlSession.getExternalSqlSession("db1"));
        return twbExternalDAO.select("kr.co.hkcloud.palette3.external.dao.ExternalMapper", "selectRtnBrdList", jsonParams);
//        return twbExternalDAO.select("", "selectRtnBrdList", jsonParams);
    }

    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnBrdCheck1(TelewebJSON jsonParams) throws TelewebAppException
    {
        twbExternalDAO=new TwbExternalDAO(extrernalSqlSession.getExternalSqlSession("db1"));
        return twbExternalDAO.select("kr.co.hkcloud.palette3.external.dao.ExternalMapper", "selectRtnBrdCheck", jsonParams);
//        return twbExternalDAO.select("", "selectRtnBrdCheck", jsonParams);
    }
}
