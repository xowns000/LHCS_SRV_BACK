package kr.co.hkcloud.palette3.core.security.authentication.domain;

import java.security.Principal;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Orange
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(of = "userId", callSuper = false)
public class PaletteUserDetailsVO extends User implements Principal {

    /**
     *
     */
    private static final long serialVersionUID = -1077388733925087421L;

    private int userId;       //사용자ID(for teletalk)
    private String lgnId;       //사용자ID(for teletalk)
    private String usrname;      //사용자명
    private String nickname;     //닉네임
    private String useYn;        //사용여부
    private String certiYn;      //사용자인증(이메일 또는 관리자 인증)
    private int pwdFailFreq;  //비밀번호실패횟수
    private String pwdUpdDttm;   //비밀번호 수정일시
    private String pwdStatus;    //비밀번호 상태
    private String custcoId;   //고객사ID
    private String tenantId;   //테넌트ID
    private String encryptKey;   //비밀번호 암호화 키
    private String UserLgnSmsCertYn;   // 2차인증여부
    private String dpcnLgnPmYn;   // 중복 로그인 허용 여부

    /**
     * @param username 사용자ID(for security)
     * @param usrname 사용자명
     */
    public PaletteUserDetailsVO(int userId, String lgnId, String usrname, String nickname, String password, boolean enabled,
        boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
        Collection<? extends GrantedAuthority> authorities, String useYn, String certiYn, int pwdFailFreq, String pwdUpdDttm,
        String pwdStatus, String custcoId, String tenantId, String encryptKey, String UserLgnSmsCertYn, String dpcnLgnPmYn) {
        super(lgnId, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);

        //Custom
        this.userId = userId;
        this.lgnId = lgnId;
        this.usrname = usrname;
        this.nickname = nickname;
        this.useYn = useYn;
        this.certiYn = certiYn;
        this.pwdFailFreq = pwdFailFreq;
        this.pwdUpdDttm = pwdUpdDttm;
        this.pwdStatus = pwdStatus;
        this.custcoId = custcoId;
        this.tenantId = tenantId;
        this.encryptKey = encryptKey;
        this.UserLgnSmsCertYn = UserLgnSmsCertYn;
        this.dpcnLgnPmYn = dpcnLgnPmYn;
    }

    @Override
    public String getName() {
        return super.getUsername(); //loginId임 (for security)
    }
}
