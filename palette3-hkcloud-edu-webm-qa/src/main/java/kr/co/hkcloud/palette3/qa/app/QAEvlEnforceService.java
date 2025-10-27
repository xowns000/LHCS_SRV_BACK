package kr.co.hkcloud.palette3.qa.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;

public interface QAEvlEnforceService {
    TelewebJSON selectQaEnforceList(TelewebJSON jsonParams) throws TelewebDaoException;
    TelewebJSON selectQaEvlSheet(TelewebJSON jsonParams) throws TelewebDaoException;
    TelewebJSON setEvlRslt(TelewebJSON jsonParams) throws TelewebDaoException;
    TelewebJSON setCyclEvlComplete(TelewebJSON jsonParams) throws TelewebDaoException;
 }
