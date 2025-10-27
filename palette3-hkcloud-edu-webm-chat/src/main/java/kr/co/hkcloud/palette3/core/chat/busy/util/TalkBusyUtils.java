package kr.co.hkcloud.palette3.core.chat.busy.util;

import java.sql.SQLException;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbEnvironment;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebUtilException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class TalkBusyUtils {

    private final TwbComDAO mobjDao;

    /**
     * 업무시간 체크 하는 로직 , SJH 2019/03/27
     * 
     * @param custcoId
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = {Exception.class,
        SQLException.class}, readOnly = false)
    public boolean isAvailableWorkTime(String custcoId) throws TelewebUtilException {
        String workStartTime = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "WORK_START_TIME");
        String workEndTime = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "WORK_END_TIME");

        TelewebJSON inJson = new TelewebJSON();
        inJson.setString("WORK_START_TIME", workStartTime);
        inJson.setString("WORK_END_TIME", workEndTime);

        // 업무 시간인지 체크 하는 로직 
        TelewebJSON resultJson = mobjDao.select("kr.co.hkcloud.palette3.core.chat.busy.dao.TalkBusyMapper", "checkAvailableWorkTime",
            inJson);

        return (resultJson.getInt("AVAILABLE_WORK_TIME") == 1) ? true : false;
    }
}
