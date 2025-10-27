package kr.co.hkcloud.palette3.infra.email.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface InfraEmailSndngService
{
    TelewebJSON trnsmisEmail(TelewebJSON mjsonParams) throws TelewebAppException;
}
