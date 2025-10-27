package kr.co.hkcloud.palette3.v3.carrier.api.dto;

import java.io.Serializable;
import java.util.List;

import kr.co.hkcloud.palette3.v3.common.dto.V3CommonFileResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class V3CarrierVstRsvtDetailResponseDto implements Serializable {
    private static final long serialVersionUID = 7233271573044163609L;
    
    private long vstRsvtId;
    private String custNm;
    private String relCd;
    private String custTelno;
    private String zip;
    private String addr;
    private String addrDtl;
    private long rgtrId;
    private String regDt;
    private long mdfrId;
    private String mdfcnDt;
    private String sttsCd;
    private String sttsDtlCd;
    private String cn;
    private String srvcTypeCd;
    private long prdctTypeId;
    private String rsvtBgngDt;
    private String rsvtEndDt;
    private String dmndMttr;
    private String excptnMttr;
    private long altmntRgtrId;
    private String altmntDt;
    private long vstrId;
    private String imgFileGroupKey;
    private String vdoFileGroupKey;
    private String relNm;
    private String sttsNm;
    private String sttsDtlNm;
    private String srvcTypeNm;
    private String prdctTypeNm;
    private String fileDomain;
    private String inqDmndMttr;
    private String inqExcptnMttr;
    
    private List<V3CommonFileResponseDto> imgList;
    private List<V3CommonFileResponseDto> vdoList;
    
}
