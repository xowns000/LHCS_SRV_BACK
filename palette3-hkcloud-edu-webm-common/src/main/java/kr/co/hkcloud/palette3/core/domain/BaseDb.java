package kr.co.hkcloud.palette3.core.domain;


import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


/**
 * BASE DB 도메인 추상 클래스 ㄴsetter가 존재하면 안됨 ㄴ특별한 경우가 아니면 기본값을 사용할 것!
 * 
 * @author RND
 */
@NoArgsConstructor
@Getter
@SuperBuilder
public abstract class BaseDb
{
    //ASP_고객사_키
    //ㄴ기본값: 로그인한 사용자의 고객사 키
    @NotBlank
    @Builder.Default
    private String custcoId = "x";
    //private String custcoId = ((PaletteUserDetailsVO) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getCustcoId();

    //처리자ID
    //ㄴ기본값: 로그인한 사용자의 ID
    @Builder.Default
    private int userId = 1;
    //private String userId = ((PaletteUserDetailsVO) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUserId();

    //처리일시
    //ㄴ기본값: 현재 시간(쿼리로 컨트롤)
//	@NotNull private String itProcessing;
}
