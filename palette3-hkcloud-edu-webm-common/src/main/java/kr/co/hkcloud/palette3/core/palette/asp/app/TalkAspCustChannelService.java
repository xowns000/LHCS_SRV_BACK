package kr.co.hkcloud.palette3.core.palette.asp.app;


import kr.co.hkcloud.palette3.core.palette.asp.domain.TwbAspCustChannel;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 고객사채널관리 서비스
 * 
 * @author Orange
 */
public interface TalkAspCustChannelService
{
    TwbAspCustChannel findByDsptchPrfKeyAndChnClsfCd(String dsptchPrfKey, String chnClsfCd) throws TelewebAppException;
    TwbAspCustChannel findBySndrKey(String sndrKey) throws TelewebAppException;
    TwbAspCustChannel findByUuid(String uuid) throws TelewebAppException;
}
