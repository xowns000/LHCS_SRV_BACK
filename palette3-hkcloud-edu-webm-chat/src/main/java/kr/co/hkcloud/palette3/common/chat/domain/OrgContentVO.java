package kr.co.hkcloud.palette3.common.chat.domain;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 원본대화내용 VO
 * @author SJH, LGC(컨버전작업)
 */
@Setter
@Getter
@NoArgsConstructor
public final class OrgContentVO implements Serializable
{
	/**
     * 
     */
    private static final long serialVersionUID = -5599745367747025041L;
    
    @NotEmpty private String custcoId;     //ASP고객사키
    private String orgContId;      //장문ID
    private String orgContent;     //장문
}
