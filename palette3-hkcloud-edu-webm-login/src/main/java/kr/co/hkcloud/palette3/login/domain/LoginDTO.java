package kr.co.hkcloud.palette3.login.domain;


import javax.validation.constraints.NotEmpty;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import lombok.Getter;
import lombok.Setter;


public class LoginDTO
{

    @Getter
    @Setter
    public static class Login
    {

        @NotEmpty(message = "아이디는 필수 입력값입니다.")
        private String username;

        @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
        private String userpassword;


        public UsernamePasswordAuthenticationToken toAuthentication()
        {
            return new UsernamePasswordAuthenticationToken(username, userpassword);
        }
    }


    @Getter
    @Setter
    public static class Reissue
    {
        @NotEmpty(message = "accessToken 을 입력해주세요.")
        private String accessToken;

        @NotEmpty(message = "refreshToken 을 입력해주세요.")
        private String refreshToken;
    }


    @Getter
    @Setter
    public static class Logout
    {
        @NotEmpty(message = "잘못된 요청입니다.")
        private String accessToken;

        @NotEmpty(message = "잘못된 요청입니다.")
        private String jessionid;

        @NotEmpty(message = "잘못된 요청입니다.")
        private int userid;

        //@NotEmpty(message = "잘못된 요청입니다.")
        //private String refreshToken;
    }
}
