package kr.co.hkcloud.palette3.svy.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

public interface SvyExclusionTargetService {
    public TelewebJSON selectTargetList(TelewebJSON jsonParam) throws TelewebAppException;
    public TelewebJSON checkCustPhnNo(TelewebJSON jsonParam) throws TelewebAppException;
    public TelewebJSON mergeTarget(TelewebJSON jsonParam) throws TelewebAppException;
    public TelewebJSON deleteTarget(TelewebJSON jsonParam) throws TelewebAppException;

}
