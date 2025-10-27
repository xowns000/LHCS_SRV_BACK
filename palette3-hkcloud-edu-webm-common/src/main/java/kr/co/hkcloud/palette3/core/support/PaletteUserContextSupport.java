package kr.co.hkcloud.palette3.core.support;


import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;

import kr.co.hkcloud.palette3.core.security.authentication.domain.PaletteUserDetailsVO;


/**
 * SecurityContext 안에 채워진 Authentication 객체는 인증 시 수집한 모든 사용자 정보를 나타낸다.
 * 
 * 즉, SecurityContextHolder를 사용하여 현재 로그인한 사용자에 대한 정보를 얻을 수 있다.
 */
public class PaletteUserContextSupport
{
    /**
     * 사용자정보 반환 (Principal)
     * 
     * @return
     */
    public static PaletteUserDetailsVO getCurrentUser()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(ObjectUtils.isEmpty(authentication)) { return null; }
        return (PaletteUserDetailsVO) authentication.getPrincipal();
    }


    /**
     * 유저ID 반환 (Principal)
     * 
     * @return
     */
    public static String getUserId()
    {
        return Optional.ofNullable(getCurrentUser().getName()).orElse(null);
    }


    /**
     * 사용자명 반환 (Principal)
     * 
     * @return
     */
    public static String getUserName()
    {
        return Optional.ofNullable(getCurrentUser().getUsrname()).orElse(null);
    }
}
