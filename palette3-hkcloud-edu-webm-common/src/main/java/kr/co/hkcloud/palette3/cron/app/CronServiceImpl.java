package kr.co.hkcloud.palette3.cron.app;

import java.sql.SQLException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service("siteCronService")
public class CronServiceImpl implements CronService {
    private final TwbComDAO mobjDao;
    private String namespace = "kr.co.hkcloud.palette3.cron.dao.CronMapper";

    /**
     * 전화 상담 통계 RowData
     */
    @Override
    @Transactional(readOnly = false)
    public void executeItgrtStatisticsRowData() throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(); // 반환 파라메터 생성

        mobjDao.update(namespace, "executeItgrtStatisticsRowData", objRetParams);
    }

    /**
     * SNS 상담 통계 RowData
     */
    @Override
    @Transactional(readOnly = false)
    public void executeItgrtChatStatisticsRowData() throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(); // 반환 파라메터 생성

        mobjDao.update(namespace, "executeItgrtChatStatisticsRowData", objRetParams);
    }

    /**
     * 설문, QA, 캠페인 상태 변경
     */
    @Override
    @Transactional(readOnly = false)
    public void executePlanStatChange() throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(); // 반환 파라메터 생성

        mobjDao.update(namespace, "executePlanStatChange", objRetParams);
    }
    
    
    /**
     * 배치실행을 위한 채팅고객사 테넌트 체크 - (서비스_상태_코드:ON, 이전_시스템_여부:N)
     * 
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   isolation = Isolation.READ_COMMITTED,
                   rollbackFor = {SQLException.class},
                   readOnly = true)
    public TelewebJSON selectBatchTenantList() throws TelewebAppException
    {
        TelewebJSON mjsonParams = new TelewebJSON();
        TelewebJSON tenantObj = new TelewebJSON(mjsonParams);

        tenantObj = mobjDao.select(namespace, "selectBatchTenantList", mjsonParams);
        
        return tenantObj;
    }
}
