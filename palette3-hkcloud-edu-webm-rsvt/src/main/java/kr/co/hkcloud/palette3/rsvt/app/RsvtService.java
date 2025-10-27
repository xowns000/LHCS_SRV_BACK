package kr.co.hkcloud.palette3.rsvt.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;

public interface RsvtService {
    TelewebJSON getRsvtList(TelewebJSON jsonParams) throws TelewebDaoException;
    TelewebJSON getRsvtCnList(TelewebJSON jsonParams) throws TelewebDaoException;
    TelewebJSON regRsvtCn(TelewebJSON jsonParams) throws TelewebDaoException;
    TelewebJSON setCuslAltmnt(TelewebJSON jsonParams) throws TelewebDaoException;
    TelewebJSON selectCustcoCuslId(TelewebJSON jsonParams) throws TelewebDaoException;
    TelewebJSON selectBookingId(TelewebJSON jsonParams) throws TelewebDaoException;
    TelewebJSON selectBookStat(TelewebJSON jsonParams) throws TelewebDaoException;
    TelewebJSON getRsvtAltmntChgLog(TelewebJSON jsonParams) throws TelewebDaoException;
    TelewebJSON getRsvtCuslList(TelewebJSON jsonParams) throws TelewebDaoException;
    TelewebJSON monthSchedule(TelewebJSON jsonParams) throws TelewebDaoException;
 }
