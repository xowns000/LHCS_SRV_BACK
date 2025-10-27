package kr.co.hkcloud.palette3.schedule.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;

public interface ScheduleService {

    TelewebJSON MySchdlStat(TelewebJSON jsonParams) throws TelewebAppException;

    // 스케줄 조회 등록 수정 삭제 서비스
    TelewebJSON selectSchedule(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectScheduleRtn(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertScheduleRtn(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateScheduleRtn(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteScheduleRtn(TelewebJSON jsonParams) throws TelewebAppException;


}
