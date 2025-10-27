package kr.co.hkcloud.palette3.svy.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

public interface SvyStatService {
    public TelewebJSON selectSendList(TelewebJSON mjsonParams) throws TelewebAppException;
    public TelewebJSON selectSendTypeStat(TelewebJSON mjsonParams) throws TelewebAppException;
    public TelewebJSON selectRspnsStat(TelewebJSON mjsonParams) throws TelewebAppException;
}
