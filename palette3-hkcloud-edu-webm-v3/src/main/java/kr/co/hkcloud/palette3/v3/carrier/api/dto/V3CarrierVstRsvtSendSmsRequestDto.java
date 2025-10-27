package kr.co.hkcloud.palette3.v3.carrier.api.dto;

import java.io.Serializable;
import java.nio.charset.Charset;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.swagger.annotations.ApiModelProperty;
import kr.co.hkcloud.palette3.v3.common.dto.V3CommonRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
@NoArgsConstructor
public class V3CarrierVstRsvtSendSmsRequestDto extends V3CommonRequestDto implements Serializable {
    
    private static final long serialVersionUID = -6392678892529379371L;

    @ApiModelProperty(value="방문 예약 ID")
    private Long vstRsvtId;
    
    @ApiModelProperty(value="고객_전화번호")
    @NotNull(message = "고객 전화번호는 필수입니다.")
    @Pattern(regexp = "\\d+", message = "고객 전화번호는 '-'없이 숫자만 입력해 주세요.")
    private String custTelno;
    
    @NotNull(message = "발송 내용은 필수입니다.")
    @ApiModelProperty(value="발송 내용")
    private String sndngCn;
    
    public void setSndngCn(String sndngCn) {
        this.sndngCn = sndngCn;
        // 문자열이 설정될 때마다 바이트 검증 수행
        this.isByteSizeValid = checkByteSize();
    }
    
    // 문자 byte 검증 메서드를 위한 필드
    private boolean isByteSizeValid;
    
    @AssertTrue(message = "발송 내용(sndngCn)이 2000byte를 초과합니다.")
    private boolean isByteSizeValid() {
        return isByteSizeValid;
    }

    // 실제 바이트 검증 로직
    private boolean checkByteSize() {
        if (sndngCn == null) {
            return true; // @NotNull 검증에 의해 처리됨
        }
        int byteSize = getByteSize();
        return byteSize <= 2000;
    }
    
    public int getByteSize() {
        if (sndngCn == null) {
            return 0; // @NotNull 검증에 의해 처리됨
        }
        byte[] eucKrBytes = sndngCn.getBytes(Charset.forName("EUC-KR"));
        return eucKrBytes.length;
    }
}
