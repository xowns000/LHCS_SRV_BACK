package kr.co.hkcloud.palette3.v3.auth.api.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class V3AuthLoginRequestDto implements Serializable {
    private static final long serialVersionUID = 9163801837886049897L;
    @NotNull(message = "id는 필수입니다.")
    private String id;
    @NotNull(message = "pw는 필수입니다.")
    private String pw;
}
