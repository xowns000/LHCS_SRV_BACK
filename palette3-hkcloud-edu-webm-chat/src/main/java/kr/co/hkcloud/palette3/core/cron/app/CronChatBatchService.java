package kr.co.hkcloud.palette3.core.cron.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

public interface CronChatBatchService {
    /**
     * sw상담예약데이터 수집
     * @return
     */
    public TelewebJSON getSwRsvt(TelewebJSON jsonParams) throws TelewebAppException;
}
