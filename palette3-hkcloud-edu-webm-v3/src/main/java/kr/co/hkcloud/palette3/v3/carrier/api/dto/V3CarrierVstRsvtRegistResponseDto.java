package kr.co.hkcloud.palette3.v3.carrier.api.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiModelProperty;
import kr.co.hkcloud.palette3.v3.common.dto.V3CommonRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class V3CarrierVstRsvtRegistResponseDto implements Serializable {
    
    private static final long serialVersionUID = 705473265744076967L;

    @ApiModelProperty(value="방문 예약 ID")
    private long vstRsvtId;
    
}
