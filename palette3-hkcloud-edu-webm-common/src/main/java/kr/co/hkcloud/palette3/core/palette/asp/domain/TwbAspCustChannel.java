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
 * 고객사채널관리
 * 
 * @author Orange
 *
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "PLT_CUSTCO_CHN")
public class TwbAspCustChannel
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SNDR_KEY")          //ASP_센더_키
    private int sndrKey;

    @Column(name = "CUSTCO_ID")            //ASP_고객사_키
    private int custcoId;

    @Column(name = "CHN_CLSF_CD")         //비즈_서비스_코드
    private String chnClsfCd;

    @Column(name = "DSPTCH_PRF_KEY")         //TALK_센더_키
    private String dsptchPrfKey;

    @Column(name = "DSPTCH_PRF_NM")          //TALK_센더_명
    private String dsptchPrfNm;

//    @Column(name = "TALK_SENDER_ETC_INFO01")  //TALK_센더_추가정보01
//    private String talkSenderEtcInfo01;

    @Column(name = "SRVC_MAINT_YN")            //서비스유지여부
    private String srvcMaintYn;

    @Column(name = "SORT_ORD")                //정렬순서
    private int sortOrd;

    @Column(name = "UUID")               //UUID
    private String uuid;

//    @Column(name = "TALK_ACCESS_TOKEN")       //TALK_ACCESS_TOKEN
//    private String talkAccessToken;

//    @Column(name = "CHANNEL_SECRET")       	  //CHANNEL_SECRET
//    private String channelSecret;
}
