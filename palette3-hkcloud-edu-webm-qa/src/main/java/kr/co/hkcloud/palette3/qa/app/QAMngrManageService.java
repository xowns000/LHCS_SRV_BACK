package kr.co.hkcloud.palette3.qa.app;

import java.util.HashMap;
import java.util.List;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;

public interface QAMngrManageService {
    TelewebJSON selectUserList(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertQaMngr(TelewebJSON jsonParams) throws TelewebDaoException;
    TelewebJSON deleteQaMngr(TelewebJSON jsonParams) throws TelewebDaoException;
    TelewebJSON selectMngrList(TelewebJSON jsonParams) throws TelewebDaoException;
}
