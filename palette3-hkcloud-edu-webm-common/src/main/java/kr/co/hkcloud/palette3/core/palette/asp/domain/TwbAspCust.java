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
 * 고객사관리
 * 
 * @author Orange
 *
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "PLT_CUSTCO")
public class TwbAspCust
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUSTCO_ID")     //ASP_고객사_KEY
    private int custcoId;

    @Column(name = "CO_NM")       //회사명
    private String coNm;

//    @Column(name = "PHN_NO")        //전화번호
//    private String phnNo;

//    @Column(name = "EML")            //이메일
//    private String eml;

//    @Column(name = "HOMEPAGE_ADDR")    //홈페이지주소
//    private String homepageAddr;

//    @Column(name = "SRVC_BGNG_DT")  //서비스시작일시
//    private String srvcBgngDt;
//
//    @Column(name = "SRVC_END_DT")    //서비스종료일시
//    private String srvcEndDt;

    @Column(name = "SRVC_MAINT_YN")     //서비스유지여부
    private String srvcMaintYn;

//    @Column(name = "BZMN_COR")      //사업자등록증
//    private String bzmnCor;
}
