package kr.co.hkcloud.palette3.config.security;


import org.springframework.security.core.GrantedAuthority;


/**
 * 역할 정의
 * 
 * @author 61000216
 *
 */
public enum TeletalkAuthority implements GrantedAuthority
{

    SYSTEM(ROLES.SYSTEM, "시스템관리자"), ADMIN(ROLES.ADMIN, "관리자"), MANAGER(ROLES.MANAGER, "매니저"), MEMBER(ROLES.MEMBER, "상담원");


    /**
     * 코드 정의 상수
     *
     */
    public static class CODES
    {
        public static final String SYSTEM  = "1";
        public static final String ADMIN   = "2";
        public static final String MANAGER = "3";
        public static final String MEMBER  = "4";
    }


    /**
     * 역할 정의 상수
     *
     */
    public static class ROLES
    {
        public static final String SYSTEM  = "ROLE_1";
        public static final String ADMIN   = "ROLE_2";
        public static final String MANAGER = "ROLE_3";
        public static final String MEMBER  = "ROLE_4";
    }


    private String authority;
    private String description;


    private TeletalkAuthority(String authority, String description) {
        this.authority = authority;
        this.description = description;
    }


    @Override
    public String getAuthority()
    {
        return this.authority;
    }


    public String getDescription()
    {
        return this.description;
    }
}
