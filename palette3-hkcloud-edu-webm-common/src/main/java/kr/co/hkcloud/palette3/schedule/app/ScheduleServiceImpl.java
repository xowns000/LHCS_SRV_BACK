package kr.co.hkcloud.palette3.schedule.app;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service("ScheduleService")
public class ScheduleServiceImpl implements ScheduleService{

    private final TwbComDAO mobjDao;

    private final InnbCreatCmmnService innbCreatCmmnService;

    @Override
    @Transactional(readOnly = true)
    public TelewebJSON MySchdlStat(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.schedule.dao.ScheduleMapper", "selectMySchdlStat", jsonParams);
    }

    @Override
    public TelewebJSON selectSchedule(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.schedule.dao.ScheduleMapper", "selectSchedule", jsonParams);
    }

    @Override
    public TelewebJSON selectScheduleRtn(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.schedule.dao.ScheduleMapper", "selectScheduleRtn", jsonParams);
    }

    @Override
    public TelewebJSON insertScheduleRtn(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams); // 반환 파라메터 생성

        //등록
        if(StringUtils.isBlank(jsonParams.getString("SCHDL_ID"))) {
            int SCHDL_ID = innbCreatCmmnService.createSeqNo("SCHDL_ID");
            jsonParams.setInt("SCHDL_ID", SCHDL_ID);
        }
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.schedule.dao.ScheduleMapper", "insertScheduleRtn", jsonParams);

        return objRetParams;
    }

    @Override
    public TelewebJSON updateScheduleRtn(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.schedule.dao.ScheduleMapper", "updateScheduleRtn", jsonParams);
    }

    @Override
    public TelewebJSON deleteScheduleRtn(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.delete("kr.co.hkcloud.palette3.schedule.dao.ScheduleMapper", "deleteScheduleRtn", jsonParams);
    }

}
