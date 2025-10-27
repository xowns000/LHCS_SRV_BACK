package kr.co.hkcloud.palette3.v3.auth.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class V3AuthLoginResponseDto {
    private String accessToken;
}
