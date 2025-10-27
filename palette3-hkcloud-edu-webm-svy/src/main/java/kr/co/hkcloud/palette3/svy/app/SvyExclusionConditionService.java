package kr.co.hkcloud.palette3.svy.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

public interface SvyExclusionConditionService {
    public TelewebJSON selectConditionList(TelewebJSON jsonParam) throws TelewebAppException;
    public TelewebJSON mergeCondition(TelewebJSON jsonParam) throws TelewebAppException;
    public TelewebJSON deleteCondition(TelewebJSON jsonParam) throws TelewebAppException;

}
