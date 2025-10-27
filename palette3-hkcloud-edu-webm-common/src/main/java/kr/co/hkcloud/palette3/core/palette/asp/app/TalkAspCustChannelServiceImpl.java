package kr.co.hkcloud.palette3.core.palette.asp.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import kr.co.hkcloud.palette3.core.palette.asp.dao.TalkAspCustChannelJpaRepository;
import kr.co.hkcloud.palette3.core.palette.asp.domain.TwbAspCustChannel;
import kr.co.hkcloud.palette3.core.palette.asp.exception.AspCustChannelServiceIsDisabledException;
import kr.co.hkcloud.palette3.core.palette.asp.exception.AspDoesNotExistException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 고객사채널관리 서비스 구현체
 * 
 * @author Orange
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("talkAspCustChannelService")
public class TalkAspCustChannelServiceImpl implements TalkAspCustChannelService
{
    private final TalkAspCustChannelJpaRepository aspCustChannelJpaRepository;


    @Override
    @Transactional(readOnly = true)
    public TwbAspCustChannel findByDsptchPrfKeyAndChnClsfCd(String dsptchPrfKey, String chnClsfCd) throws TelewebAppException
    {
        TwbAspCustChannel aspCustChannel = aspCustChannelJpaRepository.findByDsptchPrfKeyAndChnClsfCd(dsptchPrfKey, chnClsfCd);
        //존재하지 않음
        if(ObjectUtils.isEmpty(aspCustChannel)) { throw new AspDoesNotExistException(); }
        //비활성
        if("N".equals(aspCustChannel.getSrvcMaintYn())) { throw new AspCustChannelServiceIsDisabledException(); }

        return aspCustChannel;
    }


    @Override
    @Transactional(readOnly = true)
    public TwbAspCustChannel findBySndrKey(String sndrKey) throws TelewebAppException
    {
        TwbAspCustChannel aspCustChannel = aspCustChannelJpaRepository.findBySndrKey(Integer.parseInt(sndrKey));
        //존재하지 않음
        if(ObjectUtils.isEmpty(aspCustChannel)) { throw new AspDoesNotExistException(); }
        //비활성
        if("N".equals(aspCustChannel.getSrvcMaintYn())) { throw new AspCustChannelServiceIsDisabledException(); }
        return aspCustChannel;
    }


    @Override
    @Transactional(readOnly = true)
    public TwbAspCustChannel findByUuid(String uuid) throws TelewebAppException
    {
        TwbAspCustChannel aspCustChannel = aspCustChannelJpaRepository.findByUuid(uuid);
        //존재하지 않음
        if(ObjectUtils.isEmpty(aspCustChannel)) { throw new AspDoesNotExistException(); }
        //비활성
        if("N".equals(aspCustChannel.getSrvcMaintYn())) { throw new AspCustChannelServiceIsDisabledException(); }
        return aspCustChannel;
    }

}
