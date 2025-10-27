package kr.co.hkcloud.palette3.v3.auth.api.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import kr.co.hkcloud.palette3.v3.common.dto.V3CommonRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class V3UserAppUpdateTokenRequestDto extends V3CommonRequestDto implements Serializable {
    private static final long serialVersionUID = -1216577195239810893L;
    @NotNull(message = "token은 필수입니다.")
    private String token;
    private long userId;

}
