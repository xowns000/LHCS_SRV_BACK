package kr.co.hkcloud.palette3.v3.carrier.api.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import kr.co.hkcloud.palette3.v3.common.dto.V3CommonRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class V3CarrierVstRsvtListRequestDto extends V3CommonRequestDto implements Serializable {
    private static final long serialVersionUID = 8051905899044979559L;
    
    @ApiModelProperty(value="조회기준 시작일")
    @NotNull(message = "dateFrom는 필수입니다.")
    private String dateFrom;
    
    @ApiModelProperty(value="조회기준 종료일")
    @NotBlank(message = "dateTo는 필수입니다.")
    private String dateTo;
    
    @ApiModelProperty(value="상태 코드")
    private String sttsCd;
    
}
