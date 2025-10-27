package kr.co.hkcloud.palette3.login.app;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.config.security.TeletalkAuthority;
import kr.co.hkcloud.palette3.config.security.properties.PaletteSecurityProperties;
import kr.co.hkcloud.palette3.core.security.authentication.domain.PaletteUserDetailsVO;
import kr.co.hkcloud.palette3.login.dao.domain.PltUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * UserDetailsService : AuthenticationProvider 구현체에서 인증에 사용할 사용자 인증정보를 DB에서 가져오는 역할을 하는 클래스이다.
 * UserDetailsService를 구현한 UserDetailsServiceImpl를 사용한다.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class PaletteUserDetailsServiceImpl implements UserDetailsService {

    private final PaletteSecurityProperties paletteSecurityProperties;
    private final UserService userService;
    private final TwbComDAO mobjDao;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        log.debug("UserDetailsServiceImpl.loadUserByUsername :::: {}", userId);

        PltUser user = userService.findByUserId(userId);

        if (ObjectUtils.isEmpty(user)) {
            throw new BadCredentialsException("Invalid username or password!!!!! :::");
        }

        TelewebJSON jsonParam01 = new TelewebJSON();
        jsonParam01.setInt("USER_ID", user.getUserId());
        TelewebJSON jsonUserData = mobjDao.select("kr.co.hkcloud.palette3.login.dao.LoginMapper", "selectLoginUserCustcoOgzInfo",
            jsonParam01);

        if (jsonUserData.getSize() > 0) {
            user.setCustcoId(jsonUserData.getInt("CUSTCO_ID"));
            user.setDeptId(jsonUserData.getString("DEPT_ID"));
            user.setUserLgnSmsCertYn("Y".equalsIgnoreCase(jsonUserData.getString("USER_LGN_SMS_CERT_YN")) ? "Y" : "N");
            user.setDpcnLgnPmYn(jsonUserData.getString("DPCN_LGN_PM_YN"));
        }

        user.setAuthorities(this.createAuthorities(jsonUserData));

        //사용 여부 and 사용자 인증 모두 확인
        boolean booleanUseYn = ("Y".equalsIgnoreCase(user.getUseYn()) ? true : false);
        boolean booleanCertiYn = ("Y".equalsIgnoreCase(user.getCertiYn()) ? true : false);
        boolean isEnabled = (booleanUseYn && booleanCertiYn ? true : false);

        //accountNonExpired
        boolean isAccountNonExpired = true;

        //비밀번호 안만료여부 2019.01.21 lsm
        // false - 만료됨
        // true - 안만료됨
        boolean isNonExpired = true;
        boolean strPwdUserTermChk = paletteSecurityProperties.getPwdUserTerm().isEnabled();

        if (strPwdUserTermChk) {
            int pwdTerm = paletteSecurityProperties.getPwdUserTerm().getFailCnt();

            TelewebJSON jsonParams = new TelewebJSON();
            jsonParams.setString("USER_ID", user.getUsername());

            try {
                isNonExpired = userService.checkNonExpiredPwd(jsonParams, Integer.toString(pwdTerm));
            } catch (Exception e) {
                throw new BadCredentialsException("Check Credentials NonExpired Failed :::", e);
            }
        }

        //credentialsNonExpired
        boolean isCredentialsNonExpired = isNonExpired;

        //accountNonLocked
        boolean isAccountNonLocked = (!"LOCK".equalsIgnoreCase(user.getPwdStatus()) ? true : false);

        log.info("user.getTwoFactorAuth()user.getTwoFactorAuth()user.getTwoFactorAuth()" + user.getUserLgnSmsCertYn());

        return new PaletteUserDetailsVO(user.getUserId(), user.getUsername(), user.getUsrname(), user.getNickname(), user.getPassword(),
            isEnabled, isAccountNonExpired, isCredentialsNonExpired, isAccountNonLocked, user.getAuthorities(), user.getUseYn(), user
                .getCertiYn(), user.getPwdFailFreq(), user.getPwdUpdDttm(), user.getPwdStatus(), Integer.toString(user.getCustcoId()),
            TenantContext.getCurrentTenant(), user.getEncryptKey(), user.getUserLgnSmsCertYn(), user.getDpcnLgnPmYn());
    }

    public Collection<? extends GrantedAuthority> createAuthorities(TelewebJSON jsonUserData) {

        TelewebJSON authoritiesData = mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemAuthorityByAgentManageMapper",
            "selectRtnAuthByUser", jsonUserData);

        final List<SimpleGrantedAuthority> authorities = new LinkedList<>();
        JSONArray authList = authoritiesData.getDataObject("DATA");

        for (int j = 0; j < authList.size(); j++) {
            JSONObject jsonObj = authList.getJSONObject(j);
            switch (jsonObj.getString("ATRT_GROUP_ID")) {
                case TeletalkAuthority.ROLES.SYSTEM: {
                    authorities.add(new SimpleGrantedAuthority(TeletalkAuthority.ROLES.SYSTEM));
                    break;
                }
                case TeletalkAuthority.ROLES.ADMIN: {
                    authorities.add(new SimpleGrantedAuthority(TeletalkAuthority.ROLES.ADMIN));
                    break;
                }
                case TeletalkAuthority.ROLES.MANAGER: {
                    authorities.add(new SimpleGrantedAuthority(TeletalkAuthority.ROLES.MANAGER));
                    break;
                }
                default: {
                    authorities.add(new SimpleGrantedAuthority(TeletalkAuthority.ROLES.MEMBER));
                    break;
                }
            }
        }

        return authorities;
    }
}
