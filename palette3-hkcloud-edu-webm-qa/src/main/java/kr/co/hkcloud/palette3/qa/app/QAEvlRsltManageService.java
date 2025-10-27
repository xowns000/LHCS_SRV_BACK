package kr.co.hkcloud.palette3.qa.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;

public interface QAEvlRsltManageService {
    TelewebJSON selectQaEvlRsltMngrList(TelewebJSON jsonParams) throws TelewebDaoException;
    TelewebJSON selectQaEvlRsltTrgtList(TelewebJSON jsonParams) throws TelewebDaoException;
 }
