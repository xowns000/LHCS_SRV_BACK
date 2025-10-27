package kr.co.hkcloud.palette3.core.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


/**
 * SELECT DB BASE 도메인 추상 클래스 ㄴsetter가 존재하면 안됨
 * 
 * @author RND
 */
@NoArgsConstructor
@Getter
@SuperBuilder
public abstract class SelectDbBase extends BaseDb
{

}
