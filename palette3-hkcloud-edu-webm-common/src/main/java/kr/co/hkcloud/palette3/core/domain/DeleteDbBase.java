package kr.co.hkcloud.palette3.core.domain;


import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;


/**
 * DELETE DB BASE 도메인 추상 클래스 ㄴsetter가 존재하면 안됨
 * 
 * @author RND
 */
@Getter
@SuperBuilder
public class DeleteDbBase extends BaseDb
{
    //사용여부
    //ㄴ기본값: N (사용안함 고정)
    @NotBlank
    @Builder.Default
    private String useYn = "N";

    //수정자부서코드
    //ㄴ기본값: 사용안함
    @NotBlank
    @Builder.Default
    private String amdrDeptCd = "x";

    //수정자ID
    //ㄴ기본값: 로그인한 사용자의 ID
    @NotBlank
    @Builder.Default
    private String amdrId = "SYSTEM";

    //수정일시
    //ㄴ기본값: 현재 시간(쿼리로 컨트롤)
//	@NotNull private String updDttm; 
}
