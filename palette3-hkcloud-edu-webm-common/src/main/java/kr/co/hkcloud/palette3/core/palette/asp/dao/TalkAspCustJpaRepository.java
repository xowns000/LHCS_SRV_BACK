package kr.co.hkcloud.palette3.core.palette.asp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.hkcloud.palette3.core.palette.asp.domain.TwbAspCust;

/**
 * 고객사관리 JPA
 * 
 * @author Orange
 *
 */
public interface TalkAspCustJpaRepository extends JpaRepository<TwbAspCust, Integer> {

    /**
     * 
     * @param custcoId
     * @return TwbAspCust
     */
    TwbAspCust findByCustcoId(int custcoId);

    /**
     * 서비스 사용여부에 따른 고객사리스트를 가지고 온다.
     * 
     * @param srvcMaintYn 서비스사용여부
     * @return List<TwbAspCust> 고객사 리스트
     */
    <T> List<TwbAspCust> findAllBySrvcMaintYn(String srvcMaintYn);

}
