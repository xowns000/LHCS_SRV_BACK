package kr.co.hkcloud.palette3.core.palette.asp.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 비즈채널관리
 * 
 * @author Orange
 *
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "PLT_CHN")
public class TwbAspBizChannel
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHN_CLSF_CD")   //비즈_서비스_코드
    private String chnClsfCd;

    @Column(name = "CHN_URI")  //비즈_서비스_URI
    private String chnUri;

    @Column(name = "CHN_NM")   //비즈_서비스_명
    private String chnNm;

    @Column(name = "CHN_CO_NM")       //비즈회사명
    private String chnCoNm;
}
