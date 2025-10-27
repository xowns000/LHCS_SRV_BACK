package kr.co.hkcloud.palette3.login.app;


import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.login.dao.PaletteUserJpaRepository;
import kr.co.hkcloud.palette3.login.dao.domain.PltUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("userService")
public class UserServiceImpl implements UserService
{
    private final PaletteUserJpaRepository talkUserJpaRepository;
    private final TwbComDAO                mobjDao;


    /**
     * 
     */
    @Override
    @Transactional(readOnly = true)
    public PltUser findByUserId(String userId)
    {
        return talkUserJpaRepository.findByUsername(userId);
    }


    /**
     * 비밀번호 안만료여부 체크
     */
    @Override
    @Transactional(readOnly = false)
    public boolean checkNonExpiredPwd(TelewebJSON jsonParams, String pwdTerm) throws TelewebAppException
    {
        TelewebJSON jsonParam01 = new TelewebJSON();
        String telIdentifier = DateFormatUtils.format((new Date()), "HH:mm:ss") + "-" + UUID.randomUUID().toString()
            .substring(0, 8);
        jsonParam01.setHeader("TELEWEB_IDENTIFIER", telIdentifier);
        jsonParam01.setString("USER_ID", jsonParams.getString("USER_ID"));
        jsonParam01.setString("pwdTerm", pwdTerm);
        TelewebJSON jsonUserData = mobjDao
            .select("com.hcteletalk.teletalk.main.hkcdv.dao.TwbMainMapper", "checkExpiredPwd", jsonParam01);

        if(jsonUserData.getString("EXPIREYN").equals("N")) {
            //비밀번호가 만료되어 비밀번호 상태값 변경
            jsonParam01.setString("PWD_STATUS", "EXPIRED");
            mobjDao.update("com.hcteletalk.teletalk.mng.bas.bas01.dao.Bas01Mapper", "UPDATE_PWD_STATUS", jsonParam01);
            return false;
        }
        return true;
    }
}
