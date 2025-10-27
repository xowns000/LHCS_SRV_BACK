package kr.co.hkcloud.palette3.core.palette.asp.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import kr.co.hkcloud.palette3.core.palette.asp.dao.TalkAspBizChannelJpaRepository;
import kr.co.hkcloud.palette3.core.palette.asp.domain.TwbAspBizChannel;
import kr.co.hkcloud.palette3.core.palette.asp.exception.AspDoesNotExistException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 비즈채널관리 서비스 구현체
 * 
 * @author Orange
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("talkAspBizChannelService")
public class TalkAspBizChannelServiceImpl implements TalkAspBizChannelService
{
    private final TalkAspBizChannelJpaRepository aspBizChannelJpaRepository;


    @Override
    @Transactional(readOnly = true)
    public TwbAspBizChannel findByChnClsfCd(String chnClsfCd) throws TelewebAppException
    {
        TwbAspBizChannel aspBizChannel = aspBizChannelJpaRepository.findByChnClsfCd(chnClsfCd);
        //존재하지 않음
        if(ObjectUtils.isEmpty(aspBizChannel)) { throw new AspDoesNotExistException(); }
        return aspBizChannel;
    }

}
