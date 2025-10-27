package kr.co.hkcloud.palette3.v3.common.dto;

import org.springframework.data.domain.Pageable;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class V3CommonRequestListDto<T> {
    
    private T data;
    private Pageable pageable;
}