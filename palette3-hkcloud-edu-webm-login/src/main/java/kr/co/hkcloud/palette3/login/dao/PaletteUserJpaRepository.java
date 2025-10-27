package kr.co.hkcloud.palette3.login.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.hkcloud.palette3.login.dao.domain.PltUser;

/**
 * 
 * @author jangh
 *
 */

public interface PaletteUserJpaRepository extends JpaRepository<PltUser, Integer> {

    /**
     * 
     * @param userId
     * @return TwbBas01
     */
    PltUser findByUsername(String userId);
}
