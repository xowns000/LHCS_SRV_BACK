package kr.co.hkcloud.palette3.v3.common.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class V3CommonFileResponseDto implements Serializable {

    private static final long serialVersionUID = 5989366493555272002L;
    
    private String fileKey;
    private String fileGroupKey;
    private String pathTypeCd;
    private String mimeTypeCd;
    private String actlFileNm;
    private String uri;
}
