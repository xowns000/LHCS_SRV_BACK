package kr.co.hkcloud.palette3.core.palette.asp.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.hkcloud.palette3.core.palette.asp.domain.TwbAspBizChannel;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;


/**
 * 비즈채널관리 JPA
 * 
 * @author Orange
 *
 */
public interface TalkAspBizChannelJpaRepository extends JpaRepository<TwbAspBizChannel, String>
{
    /**
     * 
     * @param  chnClsfCd
     * @return               TwbAspBizChannel
     */
    TwbAspBizChannel findByChnClsfCd(String chnClsfCd) throws TelewebDaoException;
}
