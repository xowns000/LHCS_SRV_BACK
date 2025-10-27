package kr.co.hkcloud.palette3.config.security;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import kr.co.hkcloud.palette3.core.security.crypto.Crypto;
import lombok.extern.slf4j.Slf4j;


/**
 * PasswordEncoder : 실제 DB에는 비밀번호가 적절한 암호화 알고리즘으로 암호화되 저장되어있다. 폼에서 넘어오는 평문의 사용자 입력정보를 이용해 인증을 하려면 DB에 저장한 암호화 알고리즘 엔코더가 필요하다. 해당 역할을 하는 클래스이다. PasswordEncoder를 구현한 ShaPasswordEncoder를 사용한다.
 *
 */
@Slf4j
@Component
public class ShaPasswordEncoder implements PasswordEncoder
{
    @Override
    public String encode(CharSequence rawPassword)
    {
        log.debug("ShaPasswordEncoder.encode ::::");
        log.trace("ShaPasswordEncoder.encode :::: {}", rawPassword);
        return Crypto.sha256(rawPassword.toString());
    }


    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword)
    {
        log.debug("ShaPasswordEncoder.matches ::::");
        log.trace("ShaPasswordEncoder.matches :::: {} <-> {}", rawPassword, encodedPassword);
        return Crypto.sha256(rawPassword.toString()).equals(encodedPassword);
    }
}
