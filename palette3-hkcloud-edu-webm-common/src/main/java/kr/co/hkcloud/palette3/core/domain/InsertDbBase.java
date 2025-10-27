package kr.co.hkcloud.palette3.core.domain;


import javax.validation.constraints.NotBlank;

import org.springframework.security.core.context.SecurityContextHolder;

import kr.co.hkcloud.palette3.core.security.authentication.domain.PaletteUserDetailsVO;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;


/**
 * INSERT DB BASE 도메인 추상 클래스 ㄴsetter가 존재하면 안됨
 * 
 * @author RND
 */
@Getter
@SuperBuilder
public abstract class InsertDbBase extends BaseDb
{
    //등록자ID
    //ㄴ기본값: 로그인한 사용자의 ID
    @Builder.Default
    private int rgtrId = ((PaletteUserDetailsVO) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUserId();

    //등록일시
    //ㄴ기본값: 현재 시간(쿼리로 컨트롤)
//	@NotNull private String regDttm; 
}
