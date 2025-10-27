package kr.co.hkcloud.palette3.v3.carrier.api.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class V3CarrierVstRsvtListResponseDto implements Serializable {
    
    private static final long serialVersionUID = 8589225661669117347L;

    private long vstRsvtId;
    private String zip;
    private String addr;
    private String addrDtl;
    private String sttsCd;
    private String sttsDtlCd;
    private String cn;
    private String srvcTypeCd;
    private String rsvtBgngDt;
    private String rsvtEndDt;
    private String dmndMttr;
    private String excptnMttr;
    private String sttsNm;
    private String sttsDtlNm;
    private String srvcTypeNm;
}
