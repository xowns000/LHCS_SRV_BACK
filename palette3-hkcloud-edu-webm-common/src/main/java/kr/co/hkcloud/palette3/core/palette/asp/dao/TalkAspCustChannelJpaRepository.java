package kr.co.hkcloud.palette3.core.palette.asp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.hkcloud.palette3.core.palette.asp.domain.TwbAspCustChannel;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;

/**
 * 고객사채널관리 JPA
 * 
 * @author Orange
 *
 */
public interface TalkAspCustChannelJpaRepository extends JpaRepository<TwbAspCustChannel, Integer> {

    /**
     * 
     * @param dsptchPrfKey
     * @param chnClsfCd
     * @return TwbAspCustChannel
     */
    TwbAspCustChannel findByDsptchPrfKeyAndChnClsfCd(String dsptchPrfKey, String chnClsfCd) throws TelewebDaoException;

    /**
     * 
     * @param sndrKey
     * @return TwbAspCustChannel
     */
    TwbAspCustChannel findBySndrKey(int sndrKey) throws TelewebDaoException;

    /**
     * 
     * @param sndrKey
     * @return TwbAspCustChannel
     */
    TwbAspCustChannel findByUuid(String uuid) throws TelewebDaoException;
}
