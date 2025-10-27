package kr.co.hkcloud.palette3.login.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.login.dao.domain.PltUser;


/**
 * @author jangh
 *
 */
public interface UserService
{
    PltUser findByUserId(String userId);
    boolean checkNonExpiredPwd(TelewebJSON jsonParams, String pwdTerm) throws TelewebAppException;
}
