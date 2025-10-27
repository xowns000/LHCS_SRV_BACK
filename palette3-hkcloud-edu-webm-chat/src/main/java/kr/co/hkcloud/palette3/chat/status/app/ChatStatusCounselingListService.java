package kr.co.hkcloud.palette3.chat.status.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface ChatStatusCounselingListService
{
    TelewebJSON selectRtnPageCnslMgmtByKaom_new(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectCorrectedDate(TelewebJSON jsonParams) throws TelewebAppException;
}
