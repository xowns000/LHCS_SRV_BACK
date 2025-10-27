package kr.co.hkcloud.palette3.v3.carrier.api.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiModelProperty;
import kr.co.hkcloud.palette3.v3.common.dto.V3CommonRequestDto;
import kr.co.hkcloud.palette3.v3.common.dto.ValidationGroups;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class V3CarrierVstRsvtRegistRequestDto extends V3CommonRequestDto implements Serializable {
    
    private static final long serialVersionUID = -9092169747589745259L;
    @ApiModelProperty(value="방문 예약 ID")
    private Long vstRsvtId;
    
    @ApiModelProperty(value="고객_명")
    private String custNm;
    @ApiModelProperty(value="관계_코드")
    private String relCd;
    @ApiModelProperty(value="고객_전화번호")
    @NotNull(groups = ValidationGroups.RegistGroup.class, message = "고객 전화번호는 필수입니다.")
    private String custTelno;
    @ApiModelProperty(value="우편번호")
    private String zip;
    @ApiModelProperty(value="주소")
    private String addr;
    @ApiModelProperty(value="주소_상세")
    private String addrDtl;
    @NotNull(groups = {ValidationGroups.RegistGroup.class, ValidationGroups.UpdateGroup.class}, message = "상태 코드는 필수입니다.")
    @ApiModelProperty(value="상태_코드")
    private String sttsCd;
    @ApiModelProperty(value="상태_상세_코드")
    private String sttsDtlCd;
    @ApiModelProperty(value="내용")//기타 사유
    private String cn;
    @ApiModelProperty(value="서비스_유형_코드")
    private String srvcTypeCd;
    @ApiModelProperty(value="제품_유형_ID")
    private String prdctTypeId;
    @ApiModelProperty(value="예약_시작_일시")
    @NotNull(groups = ValidationGroups.RegistGroup.class, message = "예약 시작 일시는 필수입니다.")
    private String rsvtBgngDt;
    @ApiModelProperty(value="예약_종료_일시")
//    @NotNull(message = "예약 종료 일시는 필수입니다.")
    private String rsvtEndDt;
    @ApiModelProperty(value="요청_사항")//문의 내용
    private String dmndMttr;
    @ApiModelProperty(value="특이_사항")
    private String excptnMttr;
    @ApiModelProperty(value="이미지_파일_그룹_키")
    String imgFileGroupKey;
    @ApiModelProperty(value="동영상_파일_그룹_키")
    String vdoFileGroupKey;
    @ApiModelProperty(value="이미지_파일")
    MultipartFile[] imgFiles;
    @ApiModelProperty(value="동영상_파일")
    MultipartFile[] vdoFiles;
    
    
}
