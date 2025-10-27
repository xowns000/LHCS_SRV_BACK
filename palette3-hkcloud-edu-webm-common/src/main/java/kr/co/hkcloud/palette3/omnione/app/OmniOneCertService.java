package kr.co.hkcloud.palette3.omnione.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

public interface OmniOneCertService {

    TelewebJSON omniGetInfo(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON omniOneRequestReg(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON omniOneResultReg(TelewebJSON jsonParams) throws TelewebAppException;
}
