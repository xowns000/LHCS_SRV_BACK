package kr.co.hkcloud.palette3.login.dao.domain;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * 
 * @author Orange
 *
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "PLT_USER")
public class PltUser {

    @Id
    @GeneratedValue
    @Column(name = "USER_ID")        //사용자ID
    private int userId;

    @Column(name = "LGN_ID")        //로그인ID(기존 사용자 ID)
    private String username;         //for security

    @Column(name = "USER_NM")        //사용자명
    private String usrname;

    @Column(name = "USER_NNM")      //닉네임
    private String nickname;

    @Column(name = "PSWD")            //비밀번호
    private String password;

    @Column(name = "USE_YN")         //사용여부
    private String useYn;

    @Column(name = "USER_CERT_YN")       //인증여부(이메일 인증,관리자 인증)
    private String certiYn;

    @Column(name = "PSWD_FAIL_CNT")  //비밀번호실패횟수
    private int pwdFailFreq;

    @Column(name = "PSWD_MDFCN_DT")   //비밀번호 수정일시
    private String pwdUpdDttm;

    @Column(name = "PSWD_STTS")     //비밀번호 상태
    private String pwdStatus;

    //    @Column(name = "DEPT_ID")     // 조직 ID
    @Transient
    private String deptId;

    @Transient
    private int custcoId;

    @Transient
    private String tenantId;

    //    @Column(name = "CUSTCO_ID")     // custco_id 상태
    //    @Transient
    //    private String custcoId;

    @Column(name = "PSWD_ENCPT_CD")     //비밀번호 암호화 키
    private String encryptKey;

    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    @Transient
    private boolean accountNonExpired = true;  //계정 만료됨 (=false)

    @Transient
    private boolean accountNonLocked = true;  //계정 잠김 (=false)

    @Transient
    private boolean credentialsNonExpired = true;  //암호 만료됨 (=false)

    @Transient
    private boolean enabled = true;  //비활성 (=false)

    @Transient
    private String userLgnSmsCertYn;  // 2차인증여부

    @Transient
    private String dpcnLgnPmYn;  // 중복 로그인 허용 여부
}
