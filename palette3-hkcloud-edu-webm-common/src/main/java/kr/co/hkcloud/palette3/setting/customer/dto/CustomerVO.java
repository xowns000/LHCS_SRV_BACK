package kr.co.hkcloud.palette3.setting.customer.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 고객VO
 * 
 * @author User
 *
 */
@Setter
@Getter
@NoArgsConstructor
public final class CustomerVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5888649331638329947L;
    private String custcoId;    //ASP고객사키
    private String sndrKey;  //ASP고객사키
    private String talkUserKey;   //카카오상담톡 사용자키
    private String customerId;    //고객ID
    private String custChgHstryId;    //고객정보 변경 이력 ID
    private String memberYn;      //회원여부
    private Integer custSeq;      //고객순번
    private String orderNumber;   //주문번호
    private String channel;
    private String sonyUserId;
    private String ssgUserId;
    private String appUserId;
    private String kakaotId;
    private String regrDeptCd;    //등록자부서코드
    private String rgtrId;        //등록자ID
    private String amdrDeptCd;    //수정자부서코드
    private String amdrId;        //수정자ID
    private String userId;        //처리자
    private String customerPhoneNo; //전화번호

    private String chnClsfCd;   //채팅 구분
    
    public String getTalkUserKey() {
		return talkUserKey;
	}
}
