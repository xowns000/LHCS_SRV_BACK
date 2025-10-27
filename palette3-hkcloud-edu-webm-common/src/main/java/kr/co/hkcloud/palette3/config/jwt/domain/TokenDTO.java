package kr.co.hkcloud.palette3.config.jwt.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class TokenDTO {

    @Builder
    @Getter
    @AllArgsConstructor
    public static class TokenInfo {
        private String grantType;
        private String accessToken;
        private String refreshToken;
        private Long accessTokenExpirationTime;
        private Long refreshTokenExpirationTime;
    }
}