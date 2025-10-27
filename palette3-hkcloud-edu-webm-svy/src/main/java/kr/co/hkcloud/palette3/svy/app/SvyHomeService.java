package kr.co.hkcloud.palette3.svy.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

public interface SvyHomeService {
    public TelewebJSON selectStatusStat(TelewebJSON jsonParam) throws TelewebAppException;
    public TelewebJSON selectBySeStat(TelewebJSON jsonParam) throws TelewebAppException;
    public TelewebJSON selectRspnsRateStat(TelewebJSON jsonParam) throws TelewebAppException;
    public TelewebJSON selectGoalRateStat(TelewebJSON jsonParam) throws TelewebAppException;
    public TelewebJSON selectBySndngSeStat(TelewebJSON jsonParam) throws TelewebAppException;
    public TelewebJSON selectApprStat(TelewebJSON jsonParam) throws TelewebAppException;

}
