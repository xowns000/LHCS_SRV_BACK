package kr.co.hkcloud.palette3.admin.calculate.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

public interface MtsCalculateManageService {

    public TelewebJSON sndngStat(TelewebJSON mjsonParams) throws TelewebAppException;
}
