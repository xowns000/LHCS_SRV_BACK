package kr.co.hkcloud.palette3.core.palette.asp.app;


import kr.co.hkcloud.palette3.core.palette.asp.domain.TwbAspBizChannel;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 비즈채널관리 서비스
 * 
 * @author Orange
 */
public interface TalkAspBizChannelService
{
    TwbAspBizChannel findByChnClsfCd(String chnClsfCd) throws TelewebAppException;
}
