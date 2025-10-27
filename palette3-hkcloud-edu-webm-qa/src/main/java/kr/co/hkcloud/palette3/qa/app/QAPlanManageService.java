package kr.co.hkcloud.palette3.qa.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

public interface QAPlanManageService {
    TelewebJSON selectQaPlanList(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertQaPlan(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateQaPlanStts(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteQaPlan(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateQaPlanSttsBatch(TelewebJSON jsonParams) throws TelewebAppException;
    //TelewebJSON deleteQaCycl(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON qaPlanStrtNow(TelewebJSON jsonParams) throws TelewebAppException;
}
