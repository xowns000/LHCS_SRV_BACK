package kr.co.hkcloud.palette3.qa.app;

import java.util.HashMap;
import java.util.List;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;

public interface QAEvlManageService {
    TelewebJSON insertQaPlanQlty(TelewebJSON jsonParams) throws TelewebDaoException;
    TelewebJSON selectQaTrgtSlctnList(TelewebJSON jsonParams) throws TelewebDaoException;
    TelewebJSON selectQaTrgtAlmntList(TelewebJSON jsonParams) throws TelewebDaoException;

    TelewebJSON insertQaTrgtSlctn(TelewebJSON jsonParams) throws TelewebDaoException;
    //TelewebJSON updateQaTrgtSlctn(TelewebJSON jsonParams) throws TelewebDaoException;
    TelewebJSON deleteQaTrgtSlctn(TelewebJSON jsonParams) throws TelewebDaoException;
    
    TelewebJSON execQaTrgtAlmnt(TelewebJSON jsonParams) throws TelewebDaoException;
}
